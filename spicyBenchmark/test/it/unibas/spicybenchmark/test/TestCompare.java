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
package it.unibas.spicybenchmark.test;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.xml.DAOXsd;
import it.unibas.spicy.persistence.xml.operators.LoadXMLFile;
import it.unibas.spicybenchmark.Configuration;
import it.unibas.spicybenchmark.Utility;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.operators.EvaluateSimilarity;
import it.unibas.spicybenchmark.operators.generators.FeatureCollectionGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.TreeEditDistanceGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.sets.JaccardGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.BottomUpMaximumSubtreeGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TopDownOrderedMaximumSubtreeGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TreeEditDistanceGeneratorSimPack;
import it.unibas.spicybenchmark.persistence.DAOExclusionList;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestCompare extends TestCase {

    protected Log logger = LogFactory.getLog(this.getClass());
    protected Configuration configuration;
    protected IDataSourceProxy dataSource;
    protected DAOXsd daoXsd;
    protected LoadXMLFile xmlLoader = new LoadXMLFile();
    protected DAOExclusionList exclusionLoader = new DAOExclusionList();

    public TestCompare(String testName) {
        super(testName);
    }

    public void testDummy() {
    }

    public String printResult(SimilarityResult similarityResult) {
        StringBuilder result = new StringBuilder();
        result.append("\n------------ Similarity Result ------------\n");
        result.append("Number of Tuples in Expected Instance: ").append(similarityResult.getNumberOfExpectedTuples()).append("\n");
        result.append("Number of Tuples in Translated Instance: ").append(similarityResult.getNumberOfTranslatedTuples()).append("\n");
        result.append("Number of Nodes in Translated Instance: ").append(similarityResult.getNumberOfNodesInTranslatedInstance()).append("\n");
        result.append("Number of Nodes in Expected Instance: ").append(similarityResult.getNumberOfNodesInExpectedInstance()).append("\n");
        result.append("Number of Analyzed Features: ").append(similarityResult.sizeOfFeatures()).append("\n");
        result.append("F-Measure: ").append(similarityResult.getFmeasure()).append("\n");
        result.append("Arithmetic Mean: ").append(similarityResult.getArithmeticMean()).append("\n");
        result.append("Mean of F-Measures: ").append(similarityResult.getMeanOfFMeasures()).append("\n");
        return result.toString();
    }

    protected SimilarityResult evaluateAndPrint(Configuration configuration) throws DAOException {
        Date beforeLoadingSchemaAndInstances = new Date();
        dataSource = daoXsd.loadSchema(configuration.getSchemaAbsolutePath());
        if (logger.isDebugEnabled()) logger.debug(dataSource);
        INode expectedInstanceNode = xmlLoader.loadInstance(dataSource, configuration.isSkipSetElement(), configuration.getExpectedInstanceAbsolutePath());
        INode translatedInstanceNode = xmlLoader.loadInstance(dataSource, configuration.isSkipSetElement(), configuration.getTranslatedInstanceAbsolutePath());
        System.out.println("## Expected tuples: " + expectedInstanceNode);
        System.out.println("## Translated tuples: " + translatedInstanceNode);
        Date afterLoadingSchemaAndInstances = new Date();
        long loadingTime = (afterLoadingSchemaAndInstances.getTime() - beforeLoadingSchemaAndInstances.getTime());
        logger.info("Loading Time: " + loadingTime + " ms");
        List<String> exclusionList = exclusionLoader.loadExclusionList(dataSource, configuration.getExclusionsFileAbsolutePath());
        if (logger.isDebugEnabled()) logger.debug("Exclusion List Loaded: " + exclusionList);
        SimilarityResult similarityResult = generateAndEvaluateFeatureCollection(expectedInstanceNode, translatedInstanceNode, exclusionList);
        Date afterEvaluation = new Date();
        long evaluationTime = (afterEvaluation.getTime() - afterLoadingSchemaAndInstances.getTime());
        logger.info("Evaluation Time: " + evaluationTime + " ms");
        Utility.printFinalLog(similarityResult, configuration, "");
        return similarityResult;
    }

    protected SimilarityResult generateAndEvaluateFeatureCollection(INode expectedInstanceNode, INode translatedInstanceNode, List<String> exclusionList) {
        FeatureCollectionGenerator featureCollectionGenerator = new FeatureCollectionGenerator();
        FeatureCollection featureCollection = featureCollectionGenerator.generate(expectedInstanceNode, translatedInstanceNode, exclusionList, dataSource, configuration);
        EvaluateSimilarity evaluator = new EvaluateSimilarity();
        SimilarityResult similarityResult = evaluator.getSimilarityResult(featureCollection);
        if (configuration.isTreeEditDistanceValiente()) {
            similarityResult.setTreeEditDistanceValiente(new TreeEditDistanceGeneratorSimPack().compute(configuration));
        }
        if (configuration.isTreeEditDistanceShasha()) {
            similarityResult.setTreeEditDistanceShasha(new TreeEditDistanceGenerator().computeTreeEditDistance(featureCollection));
        }
        similarityResult.setTopDownOrderedMaximumSubtree(new TopDownOrderedMaximumSubtreeGenerator().compute(configuration));
        similarityResult.setBottomUpMaximumSubtree(new BottomUpMaximumSubtreeGenerator().compute(configuration));
        similarityResult.setJaccard(new JaccardGenerator().compute(expectedInstanceNode, translatedInstanceNode));
//        double ted = new TreeEditDistanceGenerator().computeTreeEditDistance(featureCollection);
//        similarityResult.setTreeEditDistance(ted);
        return similarityResult;
    }
}
