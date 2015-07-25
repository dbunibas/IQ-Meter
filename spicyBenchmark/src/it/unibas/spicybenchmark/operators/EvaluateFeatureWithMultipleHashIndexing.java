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
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EvaluateFeatureWithMultipleHashIndexing implements IEvaluateFeature {

    private static Log logger = LogFactory.getLog(EvaluateFeatureWithMultipleHashIndexing.class.getName());
    private double precisionForVariable;
    private List<String> attributesToExclude;

    public EvaluateFeatureWithMultipleHashIndexing(double precisionForVariable, List<String> attributesToExclude) {
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
        List<AttributeIndex> indexesForTranslated = generateIndex(translatedInstance);
        double numberOfMatches = findMatches(expectedInstance, violations, indexesForTranslated);
        double precision = getPrecision(translatedInstance, numberOfMatches);
        double recall = getRecall(expectedInstance, numberOfMatches);
        double fmeasure = getFMeasure(precision, recall);
        Date endTime = new Date();
        long evaluationTime = endTime.getTime() - beginTime.getTime();
        FeatureResult result = new FeatureResult(feature, precision, recall, fmeasure, evaluationTime, violations);
        return result;
    }

    private List<AttributeIndex> generateIndex(List<TupleNodeBenchmark> translatedInstance) {
        List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
        long start = System.currentTimeMillis();
        generateIndex(translatedInstance, indexes);
        long end1 = System.currentTimeMillis();

        if (logger.isInfoEnabled()) logger.info("Creating index:" + (end1 - start));
        Collections.sort(indexes);
        if (logger.isDebugEnabled()) logger.debug("Indexes for translated instance\n" + indexes);
        long end2 = System.currentTimeMillis();

        if (logger.isInfoEnabled()) logger.info("After sorting: " + (end2 - start));
        return indexes;
    }

    @SuppressWarnings("unchecked")
    private double findMatches(List<TupleNodeBenchmark> expectedInstance, Violations violations, List<AttributeIndex> indexesForTranslated) {
        List<TupleMatch> tupleMatches = new ArrayList<TupleMatch>();
        long start = System.currentTimeMillis();
        for (TupleNodeBenchmark expectedTuple : expectedInstance) {
            TupleMatch tupleMatch = findMatch(expectedTuple, indexesForTranslated);
            if (tupleMatch == null) {
                violations.addMissingElement(expectedTuple);
            } else {
                tupleMatches.add(tupleMatch);
            }
        }
        addExtraTuples(indexesForTranslated, violations);
        if (logger.isDebugEnabled()) logger.debug("Matches:\n" + SpicyEngineUtility.printCollection(tupleMatches));
        if (logger.isDebugEnabled()) logger.debug("Violations:\n" + violations.printViolations());
        double cellMatching = 0.0;
        for (TupleMatch tupleMatch : tupleMatches) {
            cellMatching += tupleMatch.getNumberOfMatchingAttributes();
            for (String nonMatchingAttribute : tupleMatch.getNonMatchingAttributes()) {
                String expectedValue = tupleMatch.getExpected().getAttributeValue(nonMatchingAttribute);
                String generatedValue = tupleMatch.getGenerated().getAttributeValue(nonMatchingAttribute);
                if (logger.isDebugEnabled()) logger.debug("**** ExpectedValue: " + expectedValue);
                if (logger.isDebugEnabled()) logger.debug("**** GeneratedValue: " + generatedValue);
                if (isVariable(expectedValue) && isVariable(generatedValue)) {
                    cellMatching += 1.0;
                } else {
                    cellMatching += precisionForVariable;
                }
            }
        }
        if (logger.isDebugEnabled()) logger.debug("Matching cells: " + cellMatching);
        long end = System.currentTimeMillis();
        if (logger.isInfoEnabled()) logger.info("Time for matching: " + (end - start));
        return cellMatching;
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
    @SuppressWarnings("unchecked")
    private void generateIndex(List<TupleNodeBenchmark> instance, List<AttributeIndex> indexes) {
        for (TupleNodeBenchmark translatedTuple : instance) {
            List<String> attributesWithoutVariables = getAttributesWithoutVariables(translatedTuple);
            String tableName = translatedTuple.getINode().getLabel();
            String tupleId = getTupleId(translatedTuple, attributesWithoutVariables);
            AttributeIndex index = getIndexForAttributes(tableName, attributesWithoutVariables, indexes);
            index.addTupleInBucket(tupleId, translatedTuple);
        }
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
            if (isVariable(value)) {
                continue;
            } else {
                attributesWithoutLLUN.add(attributeName);
            }
        }
        return attributesWithoutLLUN;
    }

    private boolean isVariable(String value) {
        return (value.startsWith(SpicyBenchmarkConstants.LLUN_PREFIX) || value.startsWith(SpicyBenchmarkConstants.SKOLEM_PREFIX));
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

    private double getPrecision(List<TupleNodeBenchmark> instance, double numberOfMatches) {
        int translatedSize = 0;
        for (TupleNodeBenchmark tuple : instance) {
            translatedSize += tuple.getNumberOfTotalAttributes(attributesToExclude);
        }
        return numberOfMatches / (double) translatedSize;
    }

    private double getRecall(List<TupleNodeBenchmark> instance, double numberOfMatches) {
        int expectedSize = 0;
        for (TupleNodeBenchmark tuple : instance) {
            expectedSize += tuple.getNumberOfTotalAttributes(attributesToExclude);
        }
        return numberOfMatches / (double) expectedSize;
    }

    private double getFMeasure(double precision, double recall) {
        if (precision == 0.0 && recall == 0.0) {
            return 0.0;
        } else {
            return (2 * precision * recall) / (precision + recall);
        }
    }
}

class AttributeIndex implements Comparable<AttributeIndex> {

    private String tableName;
    private List<String> attributes;
    private Map<String, List<TupleNodeBenchmark>> index = new HashMap<String, List<TupleNodeBenchmark>>();

    public AttributeIndex(String tableName, List<String> attributes) {
        this.tableName = tableName;
        this.attributes = attributes;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public Map<String, List<TupleNodeBenchmark>> getIndex() {
        return index;
    }

    public void addTupleInBucket(String tuple, TupleNodeBenchmark tupleNodeBenchmark) {
        List<TupleNodeBenchmark> bucket = index.get(tuple);
        if (bucket == null) {
            bucket = new ArrayList<TupleNodeBenchmark>();
            index.put(tuple, bucket);
        }
        bucket.add(tupleNodeBenchmark);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n------------------------------------").append("\n");
        result.append(" Table: ").append(tableName).append(" Attributes: ").append(attributes.isEmpty() ? "EMPTY ATTRIBUTES" : attributes).append("\n");
        result.append("------------------------------------").append("\n");
        for (String key : index.keySet()) {
            result.append(" ").append((key.isEmpty() ? "[empty]" : key)).append("\n");
            for (TupleNodeBenchmark tupleNodeBenchmark : index.get(key)) {
                result.append("     ").append(tupleNodeBenchmark.getLocalId()).append("\n");
            }
        }
        result.append("------------------------------------").append("\n");
        return result.toString();
    }

    public int compareTo(AttributeIndex o) {
        return o.attributes.size() - this.attributes.size();
    }
}

class TupleMatch {

    private TupleNodeBenchmark expected;
    private TupleNodeBenchmark generated;
    private List<String> matchingAttributes;
    private List<String> attributesToExclude;

    public TupleMatch(TupleNodeBenchmark expected, TupleNodeBenchmark generated, List<String> matchingAttributes, List<String> attributesToExclude) {
        this.expected = expected;
        this.generated = generated;
        this.matchingAttributes = matchingAttributes;
        this.attributesToExclude = attributesToExclude;
//        if (!expected.getAttributes().equals(generated.getAttributes())) {
        if (expected.getNumberOfTotalAttributes(attributesToExclude) != generated.getNumberOfTotalAttributes(attributesToExclude)) {
            System.out.println(expected);
            System.out.println(generated);

            throw new IllegalArgumentException("Matches must be between tuples with same attributes");
        }
    }

    public TupleNodeBenchmark getExpected() {
        return expected;
    }

    public TupleNodeBenchmark getGenerated() {
        return generated;
    }

    public int getNumberOfTotalAttributes() {
        return expected.getNumberOfTotalAttributes(attributesToExclude);
    }

    public int getNumberOfMatchingAttributes() {
        return matchingAttributes.size();
    }

    public int getNumberOfNonMatchingAttributes() {
        return getNumberOfTotalAttributes() - getNumberOfMatchingAttributes();
    }

    public List<String> getNonMatchingAttributes() {
        List<String> result = new ArrayList<String>();
        for (String attribute : expected.getAttributes()) {
            if (matchingAttributes.contains(attribute)) {
                continue;
            }
            result.add(attribute);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Match: " + expected.getLocalId() + " <-> " + generated.getLocalId();
    }
}
