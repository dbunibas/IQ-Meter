package it.unibas.spicybenchmark.model.features;

import it.unibas.spicybenchmark.SpicyBenchmarkConstants;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.operators.EvaluateFeatureWithMultipleHashIndexingBlocking;
import it.unibas.spicybenchmark.operators.IEvaluateFeature;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeatureLocalIdWithLLUNsBlocking extends AbstractFeature {

    private double precisionForVariable = 0.5;
    private List<String> attributesToExclude = new ArrayList<String>();

    public FeatureLocalIdWithLLUNsBlocking(List<TupleNodeBenchmark> expectedTuples, List<TupleNodeBenchmark> translatedTuples) {
        this.expectedObjects = expectedTuples;
        this.translatedObjects = translatedTuples;
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
        return new EvaluateFeatureWithMultipleHashIndexingBlocking(precisionForVariable, attributesToExclude);
    }

    public boolean match(Object expectedObject, Object translatedObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
