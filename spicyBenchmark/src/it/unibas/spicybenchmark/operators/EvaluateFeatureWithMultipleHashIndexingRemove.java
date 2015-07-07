package it.unibas.spicybenchmark.operators;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.AttributeNode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.utility.SpicyEngineUtility;
import it.unibas.spicybenchmark.SpicyBenchmarkConstants;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.IFeature;
import it.unibas.spicybenchmark.model.features.Violations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class EvaluateFeatureWithMultipleHashIndexingRemove implements IEvaluateFeature {

    private static Log logger = LogFactory.getLog(EvaluateFeatureWithMultipleHashIndexingRemove.class);
    private double precisionForVariable;
    private List<String> attributesToExclude;

    public EvaluateFeatureWithMultipleHashIndexingRemove(double precisionForVariable, List<String> attributesToExclude) {
        this.precisionForVariable = precisionForVariable;
        this.attributesToExclude = attributesToExclude;
    }

    public FeatureResult getResult(IFeature feature) {
        if (!feature.isEvaluable()) return null;
        Violations violations = new Violations();
        Date beginTime = new Date();
        List<TupleNodeBenchmark> translatedInstance = feature.getTranslatedObjects();
        List<TupleNodeBenchmark> expectedInstance = feature.getExpectedObjects();
        double translatedSize = getSize(translatedInstance);
        double expectedSize = getSize(expectedInstance);
        double numberOfMatches = findGreedyMatches(translatedInstance, expectedInstance, violations);
        double precision = getPrecision(translatedSize, numberOfMatches);
        double recall = getRecall(expectedSize, numberOfMatches);
        double fmeasure = getFMeasure(precision, recall);
        Date endTime = new Date();
        long evaluationTime = endTime.getTime() - beginTime.getTime();
        FeatureResult result = new FeatureResult(feature, precision, recall, fmeasure, evaluationTime, violations);
        return result;
    }

    private double findGreedyMatches(List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance, Violations violations) {
        Map<String, List<TupleNodeBenchmark>> tableMapTranslated = getTableMap(translatedInstance);
     //   Map<String, List<TupleNodeBenchmark>> tableMapExpected = getTableMap(expectedInstance);

        double matches = 0;
        // every tupleNodeBenchmark has the same attributes
        // hp single table
        Iterator<String> tableNames = tableMapTranslated.keySet().iterator();
        while (tableNames.hasNext()) {
            String tableName = tableNames.next();
            logger.debug("Find matches for table: " + tableName);
            List<TupleNodeBenchmark> tuplesTranslated = tableMapTranslated.get(tableName);
       //     List<TupleNodeBenchmark> tuplesExpected = tableMapExpected.get(tableName);
            List<String> attributesNames = findAttributes(tuplesTranslated); // cost O(n)
            ICombinatoricsVector<String> initialSet = Factory.createVector(attributesNames);
            Generator<String> gen = Factory.createSubSetGenerator(initialSet);
            List<List<String>> attributeSubsets = new ArrayList<List<String>>();
            Map<List<String>, Integer> statistics = new HashMap<List<String>, Integer>();
            for (ICombinatoricsVector<String> subset : gen) {
                attributeSubsets.add(subset.getVector());
                statistics.put(subset.getVector(), 0);
            }
            logger.debug(printStats(statistics));
            List<String> allAttributes = findMaxAndRemove(attributeSubsets, null);
//            matches += findMatch(tuplesTranslated, tuplesExpected, allAttributes, statistics, violations);
            matches += findMatch(tuplesTranslated, expectedInstance, allAttributes, statistics, violations);
            allAttributes = findMaxAndRemove(attributeSubsets, statistics);
            while (allAttributes != null && !allAttributes.isEmpty()) {
                clearStatistics(statistics);
//                matches += findMatch(tuplesTranslated, tuplesExpected, allAttributes, statistics, violations);
                matches += findMatch(tuplesTranslated, expectedInstance, allAttributes, statistics, violations);
                allAttributes = findMaxAndRemove(attributeSubsets, statistics);
                logger.debug(printStats(statistics));
            }
//            violations.getMissingElements().addAll(tuplesExpected);
        }
        violations.getMissingElements().addAll(expectedInstance);
        if (logger.isDebugEnabled()) logger.debug("Violations:\n" + violations.printViolations());

        return matches;
    }

    private Map<String, List<TupleNodeBenchmark>> getTableMap(List<TupleNodeBenchmark> translatedInstance) {
        // TODO: find all tables
        Map<String, List<TupleNodeBenchmark>> tableMapTranslated = new HashMap<String, List<TupleNodeBenchmark>>();
        for (TupleNodeBenchmark tuple : translatedInstance) {
            String tableName = tuple.getINode().getLabel();
            List<TupleNodeBenchmark> tuples = tableMapTranslated.get(tableName);
            if (tuples == null) {
                tuples = new ArrayList<TupleNodeBenchmark>();
                tableMapTranslated.put(tableName, tuples);
            }
            tuples.add(tuple);
        }
        return tableMapTranslated;
    }

    private double findMatch(List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance, List<String> attributesToCheck, Map<List<String>, Integer> statistics, Violations violations) {
        // populate statistics
        // create index
        List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
        List<TupleNodeBenchmark> searched = new ArrayList<TupleNodeBenchmark>();
        List<TupleNodeBenchmark> allLLuns = new ArrayList<TupleNodeBenchmark>();
        for (TupleNodeBenchmark tuple : translatedInstance) {
            List<String> key = getAttributesWithoutVariables(tuple);
            if (!key.isEmpty()) {
                statistics.put(key, statistics.get(key) + 1);
            }
            if (sameAttributes(key, attributesToCheck)) {
                List<String> attributesWithoutVariables = getAttributesWithoutVariables(tuple);
                String tableName = tuple.getINode().getLabel();
                String tupleId = getTupleId(tuple, attributesWithoutVariables);
                AttributeIndex index = getIndexForAttributes(tableName, attributesWithoutVariables, indexes);
                index.addTupleInBucket(tupleId, tuple);
                searched.add(tuple);
            } else {
                if (key.isEmpty()) {
                    allLLuns.add(tuple);
                }
            }
        }
        double matches = 0;
        List<TupleNodeBenchmark> matched = new ArrayList<TupleNodeBenchmark>();
        List<TupleNodeBenchmark> toDeleteFromExpected = new ArrayList<TupleNodeBenchmark>();
        List<TupleMatch> tupleMatches = new ArrayList<TupleMatch>();

        for (TupleNodeBenchmark expectedTuple : expectedInstance) {
            TupleMatch tupleMatch = findMatch(expectedTuple, indexes);
            if (tupleMatch != null) {
                matched.add(tupleMatch.getGenerated());
                toDeleteFromExpected.add(expectedTuple);
                tupleMatches.add(tupleMatch);
            }
        }
        for (TupleMatch tupleMatch : tupleMatches) {
            matches += tupleMatch.getNumberOfMatchingAttributes();
            matches += tupleMatch.getNumberOfNonMatchingAttributes() * precisionForVariable;
        }
        translatedInstance.removeAll(searched);
        expectedInstance.removeAll(toDeleteFromExpected);
        searched.removeAll(matched);
        violations.getExtraElements().addAll(searched);
        if (!allLLuns.isEmpty() && allLLuns.size() == 1) {
            TupleNodeBenchmark tuple = allLLuns.get(0);
            matches += findLLunsMatch(tuple, expectedInstance);
            allLLuns.remove(tuple);
        } else {
            logger.error("Instance contains multiple tuples with lluns");
        }
        if (logger.isDebugEnabled()) logger.debug("Matches:\n" + SpicyEngineUtility.printCollection(tupleMatches));
        if (logger.isDebugEnabled()) logger.debug("Violations:\n" + violations.printViolations());
        if (logger.isDebugEnabled()) logger.debug("Matching cells: " + matches);

        return matches;
    }

    private double findLLunsMatch(TupleNodeBenchmark tuple, List<TupleNodeBenchmark> expectedInstance) {
        double match = 0;
        if (!expectedInstance.isEmpty()) {
            TupleMatch tupleMatch = new TupleMatch(tuple, expectedInstance.get(0), attributesToExclude, attributesToExclude);
            match += tupleMatch.getNumberOfMatchingAttributes();
            match += tupleMatch.getNumberOfNonMatchingAttributes() * precisionForVariable;
            expectedInstance.remove(expectedInstance.get(0));
            List<TupleMatch> matches = new ArrayList<TupleMatch>();
            matches.add(tupleMatch);
            if (logger.isDebugEnabled()) logger.debug("Matches:\n" + SpicyEngineUtility.printCollection(matches));
        }
        return match;
    }

    private TupleMatch findMatch(TupleNodeBenchmark expectedTuple, List<AttributeIndex> indexesForTranslated) {
        String tableName = expectedTuple.getINode().getLabel();
        for (AttributeIndex attributeIndex : indexesForTranslated) {
            if (!attributeIndex.getTableName().equals(tableName)) {
                continue;
            }
            String tupleId = getTupleId(expectedTuple, attributeIndex.getAttributes());
            List<TupleNodeBenchmark> bucket = attributeIndex.getIndex().get(tupleId);
            if (bucket != null && !bucket.isEmpty()) {
                TupleNodeBenchmark generatedTuple = bucket.remove(0);
                return new TupleMatch(expectedTuple, generatedTuple, attributeIndex.getAttributes(), attributesToExclude);
            }
        }
        return null;
    }

    private String getTupleId(TupleNodeBenchmark tupleNodeBenchmark, List<String> attributes) {
        INode node = tupleNodeBenchmark.getINode();
        List<INode> children = node.getChildren();
        StringBuilder sb = new StringBuilder();
        for (INode child : children) {
            if (!(child instanceof AttributeNode)) {
                continue;
            }
            String attributeName = child.getLabel();
            if (attributesToExclude.contains(attributeName)) {
                continue;
            }
            if (!attributes.contains(attributeName)) {
                continue;
            }
            LeafNode leafNode = (LeafNode) child.getChild(0);
            String value = leafNode.getValue().toString();
            sb.append(value).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private AttributeIndex getIndexForAttributes(String tableName, List<String> attributesWithoutLLUN, List<AttributeIndex> indexes) {
        for (AttributeIndex attributeIndex : indexes) {
            if (attributeIndex.getTableName().equals(tableName) && attributeIndex.getAttributes().equals(attributesWithoutLLUN)) {
                return attributeIndex;
            }
        }
        AttributeIndex attributeIndex = new AttributeIndex(tableName, attributesWithoutLLUN);
        indexes.add(attributeIndex);
        return attributeIndex;
    }

    private List<String> findAttributes(List<TupleNodeBenchmark> instance) {
        List<String> attributes = new ArrayList<String>();
        if (instance != null && !instance.isEmpty()) {
            for (TupleNodeBenchmark tuple : instance) {
                List<String> atts = getAttributes(tuple);
                if (!attributes.containsAll(atts)) {
                    attributes.addAll(atts);
                }
            }
        }
        return attributes;
    }

    private List<String> getAttributes(TupleNodeBenchmark tuple) {
        List<String> attributes = new ArrayList<String>();
        INode node = tuple.getINode();
        List<INode> children = node.getChildren();
        for (INode child : children) {
            if (!(child instanceof AttributeNode)) {
                continue;
            }
            String attributeName = child.getLabel();
            if (attributesToExclude.contains(attributeName)) {
                continue;
            }
            LeafNode leafNode = (LeafNode) child.getChild(0);
            String value = leafNode.getValue().toString();
            if (value != null) attributes.add(attributeName);
        }
        return attributes;
    }

    private List<String> getAttributesWithoutVariables(TupleNodeBenchmark tupleNodeBenchmark) {
        List<String> attributesWithoutLLUN = new ArrayList<String>();
        INode node = tupleNodeBenchmark.getINode();
        List<INode> children = node.getChildren();
        for (INode child : children) {
            if (!(child instanceof AttributeNode)) {
                continue;
            }
            String attributeName = child.getLabel();
            if (attributesToExclude.contains(attributeName)) {
                continue;
            }
            LeafNode leafNode = (LeafNode) child.getChild(0);
            String value = leafNode.getValue().toString();
            if (value.startsWith(SpicyBenchmarkConstants.LLUN_PREFIX) || value.startsWith(SpicyBenchmarkConstants.SKOLEM_PREFIX)) {
                continue;
            } else {
                attributesWithoutLLUN.add(attributeName);
            }
        }
        return attributesWithoutLLUN;
    }

    private List<String> findMaxAndRemove(List<List<String>> attributeSubsets, Map<List<String>, Integer> statistics) {
        int max = 0;
        for (List<String> subset : attributeSubsets) {
            if (subset.size() >= max) {
                max = subset.size();
            }
        }
        List<List<String>> attributesMax = findAttributesWithSize(attributeSubsets, max);
        if (attributesMax.size() == 1) {
            attributeSubsets.remove(attributesMax.get(0));
            return attributesMax.get(0);
        }
        if (statistics != null) {
            List<String> best = pickBest(attributeSubsets, statistics, max);
            attributeSubsets.remove(best);
            return best;
        }
        return null;
    }

    private List<String> pickBest(List<List<String>> attributeSubsets, Map<List<String>, Integer> statistics, int max) {
        for (int i = max; i > 0; i--) {
            List<List<String>> attributesMax = findAttributesWithSize(attributeSubsets, i);
            int countMax = 0;
            List<String> best = null;
            for (List<String> attributes : attributesMax) {
                int value = statistics.get(attributes);
                if (value > countMax) {
                    countMax = value;
                    best = attributes;
                }
            }
            if (best != null) return best;

        }
        return null;
    }

    private List<List<String>> findAttributesWithSize(List<List<String>> attributeSubsets, int max) {
        List<List<String>> maxs = new ArrayList<List<String>>();
        for (List<String> subsets : attributeSubsets) {
            if (subsets.size() == max) maxs.add(subsets);
        }
        return maxs;
    }

    private double getSize(List<TupleNodeBenchmark> instance) {
        double size = 0;
        for (TupleNodeBenchmark tuple : instance) {
            size += tuple.getNumberOfTotalAttributes(attributesToExclude);
        }
        return size;
    }

    private double getPrecision(double translatedSize, double numberOfMatches) {
        return numberOfMatches / translatedSize;
    }

    private double getRecall(double expectedSize, double numberOfMatches) {
        return numberOfMatches / expectedSize;
    }

    private double getFMeasure(double precision, double recall) {
        if (precision == 0.0 && recall == 0.0) {
            return 0.0;
        } else {
            return (2 * precision * recall) / (precision + recall);
        }
    }

    private boolean sameAttributes(List<String> collection1, List<String> collection2) {
        if (collection1 == null && collection2 == null) return true;
        if (collection1 == null || collection2 == null) return false;
        List<String> c1 = new ArrayList<String>();
        List<String> c2 = new ArrayList<String>();
        c1.addAll(collection1);
        c2.addAll(collection2);
        Collections.sort(c1);
        Collections.sort(c2);
        if (c1.size() == c2.size()) {
            for (int i = 0; i < c1.size(); i++) {
                if (!c1.get(i).equalsIgnoreCase(c2.get(i))) return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private String printStats(Map<List<String>, Integer> statistics) {
        StringBuilder sb = new StringBuilder();
        Set<List<String>> keySet = statistics.keySet();
        Iterator<List<String>> iterator = keySet.iterator();
        sb.append("\n").append("----- STATISTICS -----").append("\n");
        while (iterator.hasNext()) {
            List<String> next = iterator.next();
            sb.append(next).append(":").append(statistics.get(next)).append("\n");
        }
        return sb.toString();
    }

    private void clearStatistics(Map<List<String>, Integer> statistics) {
        Set<List<String>> keySet = statistics.keySet();
        Iterator<List<String>> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            statistics.put(iterator.next(), 0);
        }
    }

}
