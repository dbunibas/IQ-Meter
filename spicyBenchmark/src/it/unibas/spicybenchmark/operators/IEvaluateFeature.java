package it.unibas.spicybenchmark.operators;

import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.IFeature;

public interface IEvaluateFeature {

    public FeatureResult getResult(IFeature feature);
}
