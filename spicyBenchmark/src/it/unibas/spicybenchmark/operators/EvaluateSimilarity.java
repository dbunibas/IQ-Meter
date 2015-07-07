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
package it.unibas.spicybenchmark.operators;

import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.IFeature;
import it.unibas.spicybenchmark.model.features.SimilarityResult;

public class EvaluateSimilarity {

    public SimilarityResult getSimilarityResult(FeatureCollection featureCollection) {
        SimilarityResult similarityResult = new SimilarityResult();
        similarityResult.setNumberOfExpectedTuples(featureCollection.getExpectedInstanceTuples().size());
        similarityResult.setNumberOfTranslatedTuples(featureCollection.getTranslatedInstanceTuples().size());
        for (IFeature feature : featureCollection.getFeatures()) {
            FeatureResult featureResult = feature.getFeatureEvaluator().getResult(feature);
            if (featureResult != null) {
                similarityResult.addFeatureResult(featureResult);
            }
        }
        evaluateMetrics(similarityResult);
        return similarityResult;
    }

    private void evaluateMetrics(SimilarityResult similarityResult) {
        similarityResult.setArithmeticMean(evaluateArithmeticMean(similarityResult));
        similarityResult.setFmeasure(evaluateHarmonicMean(similarityResult));
        similarityResult.setMeanOfFMeasures(evaluateMeanOfFMeasures(similarityResult));
    }

    private double evaluateArithmeticMean(SimilarityResult similarityResult) {
        // Arithmetic Mean Formula:
        // Am = (x1 + x2 + ... + xn) / n

        int n = similarityResult.sizeOfFeatures() * 2;
        double numerator = 0;
        for (FeatureResult featureResult : similarityResult.getFeatureResults()) {
            numerator += featureResult.getPrecision() + featureResult.getRecall();
        }
        return (numerator / n);
    }

    private double evaluateHarmonicMean(SimilarityResult similarityResult) {
        // Harmonic Mean Formula:
        // Hm = n / (1/x1 + 1/x2 + ... + 1/xn)
        // Hm is the inverse of Arithmetic Mean of the inverse of data

        int n = similarityResult.sizeOfFeatures() * 2;
        double denominator = 0;
        for (FeatureResult featureResult : similarityResult.getFeatureResults()) {
            double precision = featureResult.getPrecision();
            double recall = featureResult.getRecall();
            denominator += (1 / precision) + (1 / recall);
        }
        return (n / denominator);
    }

    private double evaluateMeanOfFMeasures(SimilarityResult similarityResult) {
        double numerator = 0;
        for (FeatureResult featureResult : similarityResult.getFeatureResults()) {
            numerator += featureResult.getFmeasure();
        }
        return (numerator / similarityResult.sizeOfFeatures());
    }
}
