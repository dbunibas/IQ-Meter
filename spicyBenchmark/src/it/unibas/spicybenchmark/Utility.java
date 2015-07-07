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

import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.TuplePair;
import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.IFeature;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.model.features.Violations;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import it.unibas.spicybenchmark.persistence.DAOLogFile;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Utility {

    private static Log logger = LogFactory.getLog(Utility.class);

    public static String printTuplePairsWithNoPositions(List<TuplePair> tuplePairs) {
        StringBuilder result = new StringBuilder();
        for (TuplePair tuplePair : tuplePairs) {
            result.append(tuplePair.toStringWithNoPositions()).append("\n");
        }
        return result.toString();
    }

    public static void printFinalLog(SimilarityResult similarityResult, Configuration configuration, String executionTimes) {
        StringBuffer log = new StringBuffer();
        log.append(configuration.toString());
        log.append(printSimilarityResult(similarityResult, configuration));
        log.append(executionTimes);
        if (configuration.printOnStdOut()) {
            logger.info(log);
        }
        if (configuration.printOnFile()) {
            try {
                DAOLogFile.saveLog(log.toString(), configuration.getLogFile());
            } catch (DAOException ex) {
                logger.error(ex);
            }
        }
    }

    private static String printSimilarityResult(SimilarityResult similarityResult, Configuration configuration) {
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
        result.append("-------- Other Measures -------\n");
        if (configuration.isTreeEditDistanceValiente()) {
            result.append("TED (Valiente): ").append(similarityResult.getTreeEditDistanceValiente()).append("\n");
        }
        if (configuration.isTreeEditDistanceShasha()) {
            result.append("TED (Shasha): ").append(similarityResult.getTreeEditDistanceShasha()).append("\n");
        }
//        result.append("Top Down Ordered Max. Subtree (Sim.): " + similarityResult.getTopDownOrderedMaximumSubtree() + "\n");
//        result.append("Bottom Up Max. Subtree (Sim.): " + similarityResult.getBottomUpMaximumSubtree() + "\n");
//        result.append("Jaccard (Sim.): " + similarityResult.getJaccard() + "\n");
        if (!configuration.getLogMessages().equalsIgnoreCase(DAOConfiguration.LOG_MINIMAL)) {
            result.append("------------ Feature Details --------------\n");
            for (FeatureResult featureResult : similarityResult.getFeatureResults()) {
                result.append(printFeatureResult(featureResult, configuration));
            }
            result.append("--------------------------------------");
        }
        return result.toString();
    }

    private static String printFeatureResult(FeatureResult featureResult, Configuration configuration) {
        StringBuilder result = new StringBuilder();
        result.append("\n------------ Feature Result ---------------\n");
        result.append("Feature: ").append(featureResult.getFeature().getName()).append("\n");
        result.append("Precision: ").append(featureResult.getPrecision()).append("\n");
        result.append("Recall: ").append(featureResult.getRecall()).append("\n");
        result.append("F-Measure: ").append(featureResult.getFmeasure()).append("\n");
        result.append("Evaluation Time: ").append(featureResult.getEvaluationTime()).append(" ms\n");
        result.append("XXX: ").append(featureResult.getFeature().getExpectedObjects());
        result.append("XXX: ").append(featureResult.getFeature().getTranslatedObjects());
        if (configuration.getLogMessages().equalsIgnoreCase(DAOConfiguration.LOG_VERBOSE)) {
            result.append(printViolations(featureResult));
        }
        result.append("-------------------------------------------");
        return result.toString();
    }

    public static String printViolations(FeatureResult featureResult) {
        Violations violations = featureResult.getViolations();
        IFeature feature = featureResult.getFeature();
        StringBuffer result = new StringBuffer();
        result.append("-------- Violations ---------\n");
        result.append("Extra elements: \n");
        result.append(generateStringForElements(violations.getExtraElements(), feature));
        result.append("Missing elements: \n");
        result.append(generateStringForElements(violations.getMissingElements(), feature));
        result.append("-----------------------------\n");
        return result.toString();
    }

    private static String generateStringForElements(List elements, IFeature feature) {
        StringBuffer result = new StringBuffer();
        for (Object element : elements) {
            if (element instanceof TupleNodeBenchmark) {
                TupleNodeBenchmark tuple = (TupleNodeBenchmark) element;
                if (feature.getType().equals(SpicyBenchmarkConstants.TYPE_LOCAL_ID)) {
                    result.append(tuple.toStringOnlyLocalId());
                }
                if (feature.getType().equals(SpicyBenchmarkConstants.TYPE_GLOBAL_ID)) {
                    result.append(tuple.toStringOnlyGlobalId());
                }
            }
            if (element instanceof TuplePair) {
                TuplePair tuplePair = (TuplePair) element;
                if (feature.getTuplePairType().equals(SpicyBenchmarkConstants.TYPE_TUPLE_PAIR_JOIN)) {
                    result.append(tuplePair.toString());
                }
                if (feature.getTuplePairType().equals(SpicyBenchmarkConstants.TYPE_TUPLE_PAIR_NESTING)) {
                    result.append(tuplePair.toStringWithNoPositions());
                }
            }
        }
        result.append("\n");
        return result.toString();
    }
}
