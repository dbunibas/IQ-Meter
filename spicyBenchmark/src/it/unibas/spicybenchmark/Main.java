/*
Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
Giansalvatore Mecca - giansalvatore.mecca@unibas.it
Salvatore Raunich - salrau@gmail.com

This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool

++Spicy is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

++Spicy is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.unibas.spicybenchmark;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.operators.CalculateSize;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.xml.DAOXsd;
import it.unibas.spicy.persistence.xml.operators.LoadXMLFile;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.operators.EvaluateSimilarity;
import it.unibas.spicybenchmark.operators.generators.FeatureCollectionGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.TreeEditDistanceGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TreeEditDistanceGeneratorSimPack;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import it.unibas.spicybenchmark.persistence.DAOExclusionList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {

    private static Log logger = LogFactory.getLog(Main.class);

    public static void main(String[] args) {
        try {
            String propertiesFile = args[0];
            StringBuilder executionTimes = new StringBuilder("--- Execution times: ---\n");
            Configuration configuration = DAOConfiguration.loadConfigurationFile(propertiesFile);

            IDataSourceProxy dataSource = new DAOXsd().loadSchema(configuration.getSchemaAbsolutePath());
            LoadXMLFile xmlLoader = new LoadXMLFile();
            Date beforeLoadingExpected = new Date();
            INode expectedInstanceNode = xmlLoader.loadInstance(dataSource, configuration.getExpectedInstanceAbsolutePath());
            Date afterLoadingExpected = new Date();
            long loadingExpected = afterLoadingExpected.getTime() - beforeLoadingExpected.getTime();
            executionTimes.append("Expected Instance loaded: ").append(loadingExpected).append(" ms\n");
            INode translatedInstanceNode = xmlLoader.loadInstance(dataSource, configuration.getTranslatedInstanceAbsolutePath());
            Date afterLoadingTranslated = new Date();
            long loadingTranslated = afterLoadingTranslated.getTime() - afterLoadingExpected.getTime();
            executionTimes.append("Translated Instance loaded: ").append(loadingTranslated).append(" ms\n");
            List<String> exclusionList = new DAOExclusionList().loadExclusionList(dataSource, configuration.getExclusionsFileAbsolutePath());
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
//            similarityResult.setTopDownOrderedMaximumSubtree(new TopDownOrderedMaximumSubtreeGenerator().compute(configuration));
//            similarityResult.setBottomUpMaximumSubtree(new BottomUpMaximumSubtreeGenerator().compute(configuration));
//            similarityResult.setJaccard(new JaccardGenerator().compute(expectedInstanceNode, translatedInstanceNode));
            Utility.printFinalLog(similarityResult, configuration, executionTimes.toString());
        } catch (DAOException ex) {
            logger.error(ex);
        }
    }
}
