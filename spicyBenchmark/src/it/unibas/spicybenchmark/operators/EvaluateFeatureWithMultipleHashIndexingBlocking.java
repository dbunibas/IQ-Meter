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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.paukov.combinatorics.ICombinatoricsVector;

public class EvaluateFeatureWithMultipleHashIndexingBlocking implements IEvaluateFeature {

    private static Log logger = LogFactory.getLog(EvaluateFeatureWithMultipleHashIndexingBlocking.class.getName());
    private double precisionForVariable;
    private List<String> attributesToExclude;
    private int counter = 0;
    private Map<String, String> tableNameString = new HashMap<String, String>();

    public EvaluateFeatureWithMultipleHashIndexingBlocking(double precisionForVariable, List<String> attributesToExclude) {
        this.precisionForVariable = precisionForVariable;
        this.attributesToExclude = attributesToExclude;
    }

    @SuppressWarnings("unchecked")
    public FeatureResult getResult(IFeature feature) {
        if (!feature.isEvaluable()) {
            return null;
        }
        Violations violations = new Violations();
        Date beginTime = new Date();
        List<TupleNodeBenchmark> translatedInstance = feature.getTranslatedObjects();
        List<TupleNodeBenchmark> expectedInstance = feature.getExpectedObjects();
        int translatedSize = cellsNumber(translatedInstance);
        int expectedSize = cellsNumber(expectedInstance);
        logger.debug("Size: Translated -->" + translatedSize + "  Expected -->" + expectedSize);
        double numberOfMatches = findMatches(expectedInstance, translatedInstance, violations);
        double precision = getPrecision(translatedSize, numberOfMatches);
        double recall = getRecall(expectedSize, numberOfMatches);
        double fmeasure = getFMeasure(precision, recall);
        Date endTime = new Date();
        long evaluationTime = endTime.getTime() - beginTime.getTime();
        FeatureResult result = new FeatureResult(feature, precision, recall, fmeasure, evaluationTime, violations);
        return result;
    }

    private double findMatches(List<TupleNodeBenchmark> expectedInstance, List<TupleNodeBenchmark> translatedInstance, Violations violations) {
        double matches = 0;
     //   Map<String, List<TupleNodeBenchmark>> tableMapTranslated = getTableMap(translatedInstance);
        //   Map<String, List<TupleNodeBenchmark>> tableMapExpected = getTableMap(expectedInstance);

        //    Set<String> tables = tableMapExpected.keySet();
        //    for (String table : tables) {
        //        List<TupleNodeBenchmark> expectedTuples = tableMapExpected.get(table);
        //        List<TupleNodeBenchmark> translatedTuples = tableMapTranslated.get(table);
        //        logger.debug("Find matches for table: " + table);
        //        matches += matchesInTable(expectedTuples, translatedTuples, violations);
        matches += matchesInTable(expectedInstance, translatedInstance, violations);

        //        expectedTuples.clear();
        //        translatedTuples.clear();
        //    }
        return matches;
    }

    private double matchesInTable(List<TupleNodeBenchmark> expectedInstance, List<TupleNodeBenchmark> translatedInstance, Violations violations) {
        double cellMatching = 0.0;
        if (!expectedInstance.isEmpty() && !translatedInstance.isEmpty()) {
            List<TupleMatch> tupleMatches = new ArrayList<TupleMatch>();
            long startMap = System.currentTimeMillis();

            // blocking find only equals
//            Map<String, Map<String, TupleNodeBenchmark>> mapTranslated = generateMap(translatedInstance);
            Map<String, TupleNodeBenchmark> mapTranslated = generateMap(translatedInstance);
            if (logger.isInfoEnabled()) logger.info("Generating Map: " + (System.currentTimeMillis() - startMap));

            Iterator<TupleNodeBenchmark> iteratorExpected = expectedInstance.iterator();
            while (iteratorExpected.hasNext()) {
                TupleNodeBenchmark expected = iteratorExpected.next();
                List<String> attributesWithoutNull = new ArrayList<String>();
                String key = getAttributesWithoutVariablesAndKey(expected, attributesWithoutNull);
//                List<String> attributesWithoutNull = getAttributesWithoutVariables(expected);
//                String key = getTupleIdConstants(expected, attributesWithoutNull);
//                logger.info("Ask key: "+key);
//                Map<String, TupleNodeBenchmark> internalMap = mapTranslated.get(attributesWithoutNull.toString());
//                if (internalMap != null) {
//                    TupleNodeBenchmark translated = internalMap.get(key);
                TupleNodeBenchmark translated = mapTranslated.get(key);
                if (translated != null) {
                    try {
                        TupleMatch tupleMatch = new TupleMatch(expected, translated, attributesWithoutNull, attributesToExclude);
                        tupleMatches.add(tupleMatch);
                        expected.setMatched(true);
                        translated.setMatched(true);
//                            iteratorExpected.remove();
//                            translatedInstance.remove(translated);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Key: " + key);
                        throw new IllegalArgumentException();
                    }
//                    }
                }
            }
            // removeMatched(translatedInstance,expectedInstance);
            long endMap = System.currentTimeMillis();
            if (logger.isInfoEnabled()) logger.info("Blocking time ms: " + (endMap - startMap));
            if (logger.isDebugEnabled()) logger.debug("Matches after blocking:\n" + SpicyEngineUtility.printCollection(tupleMatches));
            if (logger.isInfoEnabled()) logger.info("Matches after blocking: " + tupleMatches.size());
            // end blocking

            // start donatello code
            List<AttributeIndex> indexesForTranslated = generateIndex(translatedInstance);
            tupleMatches.addAll(findMatches(translatedInstance, expectedInstance, indexesForTranslated, violations));
            // end donatello code

            for (TupleMatch tupleMatch : tupleMatches) {
                cellMatching += tupleMatch.getNumberOfMatchingAttributes();
                cellMatching += tupleMatch.getNumberOfNonMatchingAttributes() * precisionForVariable;
            }

            if (logger.isDebugEnabled()) logger.debug("Matches:\n" + SpicyEngineUtility.printCollection(tupleMatches));
            if (logger.isDebugEnabled()) logger.debug("Violations:\n" + violations.printViolations());
            if (logger.isInfoEnabled()) logger.info("Final Matches tuples: " + tupleMatches.size());
            if (logger.isInfoEnabled()) logger.info("Matching cells: " + cellMatching);
            long endAll = System.currentTimeMillis();
            if (logger.isInfoEnabled()) logger.info("All Match time ms: " + (endAll - startMap));

        }
        return cellMatching;
    }

    private List<AttributeIndex> generateIndex(List<TupleNodeBenchmark> translatedInstance) {
        List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
        generateIndex(translatedInstance, indexes);
        Collections.sort(indexes);
        if (logger.isDebugEnabled()) logger.debug("Indexes for translated instance\n" + indexes);
        return indexes;
    }

    private void generateIndex(List<TupleNodeBenchmark> instance, List<AttributeIndex> indexes) {
        for (TupleNodeBenchmark translatedTuple : instance) {
            if (!translatedTuple.isMatched()) {
                List<String> attributesWithoutVariables = getAttributesWithoutVariables(translatedTuple);
                String tableName = translatedTuple.getINode().getLabel();
                String tupleId = getTupleId(translatedTuple, attributesWithoutVariables);
                AttributeIndex index = getIndexForAttributes(tableName, attributesWithoutVariables, indexes);
                index.addTupleInBucket(tupleId, translatedTuple);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<TupleMatch> findMatches(List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance, List<AttributeIndex> indexesForTranslated, Violations violations) {
        List<TupleMatch> tupleMatches = new ArrayList<TupleMatch>();
        Iterator<TupleNodeBenchmark> iterator = expectedInstance.iterator();
        while (iterator.hasNext()) {
            TupleNodeBenchmark expectedTuple = iterator.next();
            if (!expectedTuple.isMatched()) {
                TupleMatch tupleMatch = findMatch(expectedTuple, indexesForTranslated);
                if (tupleMatch != null) {
                    tupleMatches.add(tupleMatch);
                    expectedTuple.setMatched(true);
                    tupleMatch.getGenerated().setMatched(true);
//                    iterator.remove();
//                    translatedInstance.remove(tupleMatch.getGenerated());
                } else {
                    violations.addMissingElement(expectedTuple);
                }
            }
        }
        addExtraTuples(indexesForTranslated, violations);

        return tupleMatches;
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

    private Map<String, List<TupleNodeBenchmark>> getTableMap(List<TupleNodeBenchmark> instance) {
        Map<String, List<TupleNodeBenchmark>> tableMapTranslated = new HashMap<String, List<TupleNodeBenchmark>>();
        for (TupleNodeBenchmark tuple : instance) {
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

    private void addExtraTuples(List<AttributeIndex> indexesForTranslated, Violations violations) {
        for (AttributeIndex attributeIndex : indexesForTranslated) {
            for (List<TupleNodeBenchmark> bucket : attributeIndex.getIndex().values()) {
                for (TupleNodeBenchmark translatedTuple : bucket) {
                    violations.addExtraElement(translatedTuple);
                }
            }
        }
    }

    ///////////////////////////////////////////////
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

    private String getAttributesWithoutVariablesAndKey(TupleNodeBenchmark tupleNodeBenchmark, List<String> attributesWithoutLLUN) {
        INode node = tupleNodeBenchmark.getINode();
        List<INode> children = node.getChildren();
        StringBuilder sb = new StringBuilder();
        boolean changed = false;
        String valueTable = tableNameString.get(node.getLabel());
        if (valueTable == null) {
            valueTable = counter + "";
            tableNameString.put(node.getLabel(), valueTable);
            counter++;
        }
        sb.append(valueTable).append("[");
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
                sb.append(attributeName).append(":").append(value).append("|");
                attributesWithoutLLUN.add(node.getLabel() + "." + attributeName);
                changed = true;
            }
        }
        sb.append("]");
        if (!changed) return "";
        return sb.toString();
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

    private Map<String, TupleNodeBenchmark> generateMap(List<TupleNodeBenchmark> instance) {
//        Map<String, Map<String, TupleNodeBenchmark>> mapAttributes = new HashMap<String, Map<String, TupleNodeBenchmark>>();
        Map<String, TupleNodeBenchmark> map = new HashMap<String, TupleNodeBenchmark>(instance.size());
        for (TupleNodeBenchmark tuple : instance) {
            List<String> attributesWithoutNull = new ArrayList<String>();
            String key = getAttributesWithoutVariablesAndKey(tuple, attributesWithoutNull);
//            Map<String, TupleNodeBenchmark> internalMap = mapAttributes.get(attributesWithoutNull.toString());
//            if (internalMap == null && !attributesWithoutNull.isEmpty()) {
//                internalMap = new HashMap<String, TupleNodeBenchmark>();
//                mapAttributes.put(attributesWithoutNull.toString(), internalMap);
//            }
            //List<String> attributesWithoutNull = getAttributesWithoutVariables(tuple);
            //String key = getTupleIdConstants(tuple, attributesWithoutNull);
            if (!key.isEmpty()) {
//                logger.info("Put key: "+ key);
//                internalMap.put(key, tuple);
                map.put(key, tuple);
            }
        }
        return map;
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

    private String getTupleIdConstants(TupleNodeBenchmark tupleNodeBenchmark, List<String> attributes) {
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
            sb.append(node.getLabel()).append(".").append(attributeName).append(":").append(value).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private List<TupleNodeBenchmark> findTuplesWithAttributes(List<TupleNodeBenchmark> instance, List<String> attributes, List<AttributeIndex> transtlatedIndex) {
        List<TupleNodeBenchmark> tuples = new ArrayList<TupleNodeBenchmark>();
        for (TupleNodeBenchmark t : instance) {
            List<String> attributesWithoutVariables = getAttributesWithoutVariables(t);
            if (sameAttributes(attributesWithoutVariables, attributes)) {
                tuples.add(t);
                if (transtlatedIndex != null) {
                    String tableName = t.getINode().getLabel();
                    String tupleId = getTupleId(t, attributesWithoutVariables);
                    AttributeIndex index = getIndexForAttributes(tableName, attributesWithoutVariables, transtlatedIndex);
                    index.addTupleInBucket(tupleId, t);
                }
            }
        }
        return tuples;
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

    // STATISTISC
    private double getPrecision(int translatedSize, double numberOfMatches) {
        return numberOfMatches / (double) translatedSize;
    }

    private int cellsNumber(List<TupleNodeBenchmark> instance) {
        int translatedSize = 0;
        for (TupleNodeBenchmark tuple : instance) {
            translatedSize += tuple.getNumberOfTotalAttributes(attributesToExclude);
        }
        return translatedSize;
    }

    private double getRecall(int expectedSize, double numberOfMatches) {
        return numberOfMatches / (double) expectedSize;
    }

    private double getFMeasure(double precision, double recall) {
        if (precision == 0.0 && recall == 0.0) {
            return 0.0;
        } else {
            return (2 * precision * recall) / (precision + recall);
        }
    }

    private String printTuples(List<TupleNodeBenchmark> list) {
        StringBuilder sb = new StringBuilder();
        for (TupleNodeBenchmark tuple : list) {
            sb.append(tuple.getLocalId()).append("\n");
        }
        return sb.toString();
    }

    private void removeMatched(List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance) {
        Iterator<TupleNodeBenchmark> iteratorTranslated = translatedInstance.iterator();
        while (iteratorTranslated.hasNext()) {
            if (iteratorTranslated.next().isMatched()) iteratorTranslated.remove();
        }
        Iterator<TupleNodeBenchmark> iteratorExpected = expectedInstance.iterator();
        while (iteratorExpected.hasNext()) {
            if (iteratorExpected.next().isMatched()) iteratorExpected.remove();
        }
    }

    private List<TupleNodeBenchmark> notMached(List<TupleNodeBenchmark> instance) {
        List<TupleNodeBenchmark> notMatched = new ArrayList<TupleNodeBenchmark>();
        Iterator<TupleNodeBenchmark> iterator = instance.iterator();
        while (iterator.hasNext()) {
            TupleNodeBenchmark next = iterator.next();
            if (!next.isMatched()) notMatched.add(next);
        }
        return notMatched;
    }

    private class ComparatorSubset implements Comparator<ICombinatoricsVector<String>> {

        public int compare(ICombinatoricsVector<String> o1, ICombinatoricsVector<String> o2) {
            int size1 = o1.getSize();
            int size2 = o2.getSize();
            return -(Integer.compare(size1, size2));
        }

    }

}
