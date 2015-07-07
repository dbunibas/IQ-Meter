package it.unibas.spicybenchmark.model.features;

import it.unibas.spicybenchmark.SpicyBenchmarkConstants;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.operators.EvaluateFeatureWithMultipleHashIndexingRemove;
import it.unibas.spicybenchmark.operators.IEvaluateFeature;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeatureLocalIdWithLLUNsGreedy extends AbstractFeature {

    private double precisionForVariable = 0.5;
    private List<String> attributesToExclude = new ArrayList<String>();

    public FeatureLocalIdWithLLUNsGreedy(List<TupleNodeBenchmark> expectedTuples, List<TupleNodeBenchmark> translatedTuples) {
        this.expectedObjects = expectedTuples;
        this.translatedObjects = translatedTuples;
    }

    @Override
    //RETURN DOUBLE
    public boolean match(Object expectedObject, Object translatedObject) {
        throw new UnsupportedOperationException();
//        TupleNodeBenchmark expectedTuple = (TupleNodeBenchmark) expectedObject;
//        TupleNodeBenchmark translatedTuple = (TupleNodeBenchmark) translatedObject;
//        return expectedTuple.getLocalId().equals(translatedTuple.getLocalId());
    }

    public String getType() {
        return SpicyBenchmarkConstants.TYPE_LOCAL_ID;
    }

    public String getTuplePairType() {
        throw new UnsupportedOperationException();
//        return SpicyBenchmarkConstants.TYPE_TUPLE_PAIR_DUMMY;
    }

    public Comparator getComparator() {
        throw new UnsupportedOperationException();
//        return new SingleLocalIdComparator();
    }

    public double getPrecisionForVariable() {
        return precisionForVariable;
    }

    public void setPrecisionForVariable(double precisionForVariable) {
        this.precisionForVariable = precisionForVariable;
    }

    public List<String> getAttributesToExclude() {
        return attributesToExclude;
    }

    public void setAttributesToExclude(List<String> attributesToExclude) {
        this.attributesToExclude = attributesToExclude;
    }

    @Override
    public IEvaluateFeature getFeatureEvaluator() {
        return new EvaluateFeatureWithMultipleHashIndexingRemove(precisionForVariable, attributesToExclude);
    }
}
