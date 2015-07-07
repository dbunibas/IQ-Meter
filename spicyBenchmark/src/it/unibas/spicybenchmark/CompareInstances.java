package it.unibas.spicybenchmark;

import it.unibas.spicybenchmark.persistence.LoadCSVFile;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNs;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNsBlocking;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNsGreedy;
import it.unibas.spicybenchmark.model.features.IFeature;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.operators.EvaluateSimilarity;
import it.unibas.spicybenchmark.operators.generators.FeatureCollectionGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.TreeEditDistanceGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.sets.JaccardGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.BottomUpMaximumSubtreeGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TopDownOrderedMaximumSubtreeGenerator;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.tree.TreeEditDistanceGeneratorSimPack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompareInstances {

    protected LoadCSVFile csvLoader = new LoadCSVFile();

    public List<SimilarityResult> evaluate(String expectedInstances, List<String> translatedInstances, List<String> features, List<String> attributesToExclude, double precisionForVariable) throws Exception {
        return evaluate(Arrays.asList(new String[]{expectedInstances}), translatedInstances, features, attributesToExclude, precisionForVariable);
    }

    public List<SimilarityResult> evaluate(List<String> expectedInstances, List<String> translatedInstances, List<String> features, List<String> attributesToExclude, double precisionForVariable) throws Exception {
        List<SimilarityResult> result = new ArrayList<SimilarityResult>();
        for (String translatedInstance : translatedInstances) {
            for (String expectedInstance : expectedInstances) {
                result.add(evaluate(expectedInstance, translatedInstance, features, attributesToExclude, precisionForVariable));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public SimilarityResult evaluate(String expectedInstance, String translatedInstance, List<String> features, List<String> attributesToExclude, double precisionForVariable) throws Exception {
        INode expectedInstanceNode = csvLoader.load(expectedInstance);
        INode translatedInstanceNode = csvLoader.load(translatedInstance);
        FeatureCollectionGenerator featureCollectionGenerator = new FeatureCollectionGenerator();
        FeatureCollection featureCollection = featureCollectionGenerator.generate(expectedInstanceNode, translatedInstanceNode, Collections.EMPTY_LIST, null, features);
        for (IFeature feature : featureCollection.getFeatures()) {
            if (feature instanceof FeatureLocalIdWithLLUNs) {
                ((FeatureLocalIdWithLLUNs) feature).setPrecisionForVariable(precisionForVariable);
                ((FeatureLocalIdWithLLUNs) feature).setAttributesToExclude(attributesToExclude);
            }
            if (feature instanceof FeatureLocalIdWithLLUNsGreedy) {
                ((FeatureLocalIdWithLLUNsGreedy) feature).setPrecisionForVariable(precisionForVariable);
                ((FeatureLocalIdWithLLUNsGreedy) feature).setAttributesToExclude(attributesToExclude);
            }
            if (feature instanceof FeatureLocalIdWithLLUNsBlocking) {
                ((FeatureLocalIdWithLLUNsBlocking) feature).setPrecisionForVariable(precisionForVariable);
                ((FeatureLocalIdWithLLUNsBlocking) feature).setAttributesToExclude(attributesToExclude);
            }

        }
        EvaluateSimilarity evaluator = new EvaluateSimilarity();
        SimilarityResult similarityResult = evaluator.getSimilarityResult(featureCollection);
        similarityResult.setExpectedInstance(expectedInstance);
        similarityResult.setTranslatedInstance(translatedInstance);
        return similarityResult;
    }
}
