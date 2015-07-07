/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.EffortRecording;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.model.SolutionQuality;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.ping.framework.IModello;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.operators.CalculateSize;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.persistence.xml.DAOXsd;
import it.unibas.spicy.persistence.xml.operators.LoadXMLFile;
import it.unibas.spicybenchmark.Configuration;
import it.unibas.spicybenchmark.Utility;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.operators.EvaluateSimilarity;
import it.unibas.spicybenchmark.operators.generators.FeatureCollectionGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.TreeEditDistanceGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TreeEditDistanceGeneratorSimPack;
import it.unibas.spicybenchmark.persistence.DAOExclusionList;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class CalculateQuality {

    private static Log logger = LogFactory.getLog(CalculateQuality.class);
    private static boolean skipSetNode = true;

    private CalculateQuality() {
    }

    public static void calculate(Scenario sc, MappingTool tool, IModello modello) throws it.unibas.spicy.persistence.DAOException, DAOException, IOException, Exception {
        try {
            Configuration configuration = new Configuration("");
            configuration.setExperimentName(sc.getName());
            configuration.setSchemaPath(sc.getSchemaTargetPath());
            configuration.setExpectedInstancePath(sc.getExpectedInstancePath());
            configuration.setTranslatedInstancePath(tool.getTranslatedInstancePath());
            configuration.setExclusionsFile(sc.getExclusionListPath());
            configuration.setSkipSetElement(skipSetNode);

            for (int i = 0; i < sc.getFeatures().length; i++) {
                configuration.addFeature(sc.getFeatures()[i]);

            }
            configuration.setLogMessages("NORMAL");
            configuration.setLogAppender("BOTH");
            configuration.setLogFile("similarityResult.txt");
            logger.trace(configuration);

            StringBuilder executionTimes = new StringBuilder("--- Execution times: ---\n");

            IDataSourceProxy dataSource = new DAOXsd().loadSchema(configuration.getSchemaPath());

            LoadXMLFile xmlLoader = new LoadXMLFile();
            Date beforeLoadingExpected = new Date();
            logger.debug("\n" + dataSource);
            INode expectedInstanceNode = xmlLoader.loadInstance(dataSource, configuration.getExpectedInstancePath());
            logger.debug("Expected instance:\n" + expectedInstanceNode);
            Date afterLoadingExpected = new Date();
            long loadingExpected = afterLoadingExpected.getTime() - beforeLoadingExpected.getTime();
            executionTimes.append("Expected Instance loaded: ").append(loadingExpected).append(" ms\n");
            INode translatedInstanceNode = xmlLoader.loadInstance(dataSource, skipSetNode, configuration.getTranslatedInstancePath());
            logger.debug("translated Instance:\n" + translatedInstanceNode);
            Date afterLoadingTranslated = new Date();
            long loadingTranslated = afterLoadingTranslated.getTime() - afterLoadingExpected.getTime();
            executionTimes.append("Translated Instance loaded: ").append(loadingTranslated).append(" ms\n");
            List<String> exclusionList = new DAOExclusionList().loadExclusionList(dataSource, configuration.getExclusionsFile());
            Date startSpicyTime = new Date();
            FeatureCollectionGenerator featureCollectionGenerator = new FeatureCollectionGenerator();
            FeatureCollection featureCollection = featureCollectionGenerator.generate(expectedInstanceNode, translatedInstanceNode, exclusionList, dataSource, configuration);
            EvaluateSimilarity evaluator = new EvaluateSimilarity();
            SimilarityResult similarityResult = evaluator.getSimilarityResult(featureCollection);
            similarityResult.setNumberOfNodesInExpectedInstance(new CalculateSize().getNumberOfNodes(expectedInstanceNode));
            similarityResult.setNumberOfNodesInTranslatedInstance(new CalculateSize().getNumberOfNodes(translatedInstanceNode));
            Date endSpicyTime = new Date();
            long spicyExecTime = endSpicyTime.getTime() - startSpicyTime.getTime();
            executionTimes.append("Spicy execution time: ").append(spicyExecTime).append(" ms\n");

            if (configuration.isTreeEditDistanceValiente()) {
                Date startValiente = new Date();
                similarityResult.setTreeEditDistanceValiente(new TreeEditDistanceGeneratorSimPack().compute(configuration));
                Date stopValiente = new Date();
                long valienteExecTime = stopValiente.getTime() - startValiente.getTime();
                executionTimes.append("Valiente execution time: ").append(valienteExecTime).append(" ms\n");
            }
            if (configuration.isTreeEditDistanceShasha()) {
                Date startShasha = new Date();
                similarityResult.setTreeEditDistanceShasha(new TreeEditDistanceGenerator().computeTreeEditDistance(featureCollection));
                Date stopShasha = new Date();
                long shashaExecTime = stopShasha.getTime() - startShasha.getTime();
                executionTimes.append("Shasha execution time: ").append(shashaExecTime).append(" ms\n");
            }
            logger.debug("Simularity result F-Measure: " + similarityResult.getFmeasure());
            MappingExecution mapping = new MappingExecution();
            SolutionQuality quality = new SolutionQuality();
            quality.setFmeasure(round(similarityResult.getFmeasure()));
            quality.setPrecision(round(similarityResult.getArithmeticMean()));
            quality.setRecall(round(similarityResult.getMeanOfFMeasures()));
            logger.debug("FMEASURE: " + quality.getFmeasure());
            quality.setNodesExpected(similarityResult.getNumberOfNodesInExpectedInstance());
            quality.setNodesTranslated(similarityResult.getNumberOfNodesInTranslatedInstance());
            quality.setTuplesExpected(similarityResult.getNumberOfExpectedTuples());
            quality.setTuplesTranslated(similarityResult.getNumberOfTranslatedTuples());
            mapping.setQuality(quality);
            mapping.setMappingTime(endSpicyTime);
            mapping.setNumberLabel(tool.getNumberExecutions() + 1);

            // recorded operations
            Component panel = (Component) modello.getBean(Constant.TAB_SELECTED);
            String panelName = panel.getName();
            ThreadRecording recording = ThreadRecording.getInstance();
            if (recording != null) {
                String process = recording.getProcessFromPanel(panelName);
                mapping.setEffortRecording(new EffortRecording(recording.getClickCounter(process), recording.getKeyboardCounter(process)));
            }
            // modello.putBean(EffortRecording.class.getName() + panelName, new EffortRecording(recording.getClickCounter(process), recording.getKeyboardCounter(process)));
            // logger.debug("Put key: " + EffortRecording.class.getName() + panelName);

            Utility.printFinalLog(similarityResult, configuration, executionTimes.toString());

            DAOMappingExecution.createDirectory(mapping, tool, sc);
            logger.debug("Mapping Execution created: " + mapping.getNumberLabel());
            modello.putBean(Constant.EXECUTION_ANALYZED, mapping);
            FileUtils.moveFile(new File(configuration.getLogFile()), new File(mapping.getDirectory() + "/similarityResult.txt"));

        } catch (AssertionError err) {
            logger.error(err);
            throw new Exception(Constant.MESSAGE_EXCEPTION_CALCULATE_QUALITY + err);
        }
    }

    private static double round(double number) {
        return (double) Math.round((number * 1000)) / 1000;
    }
}
