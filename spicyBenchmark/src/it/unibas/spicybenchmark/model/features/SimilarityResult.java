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
package it.unibas.spicybenchmark.model.features;

import java.util.ArrayList;
import java.util.List;

public class SimilarityResult {

    private String expectedInstance;
    private String translatedInstance;
    private int numberOfExpectedTuples;
    private int numberOfTranslatedTuples;
    private int numberOfNodesInExpectedInstance;
    private int numberOfNodesInTranslatedInstance;
    private double fmeasure;
    private double arithmeticMean;
    private double meanOfFMeasures;
    private double treeEditDistanceValiente;
    private double treeEditDistanceShasha;
    private double topDownOrderedMaximumSubtree;
    private double bottomUpMaximumSubtree;
    private double jaccard;
    private List<FeatureResult> featureResults = new ArrayList<FeatureResult>();

    public void addFeatureResult(FeatureResult featureResult) {
        this.featureResults.add(featureResult);
    }

    public int sizeOfFeatures() {
        return this.featureResults.size();
    }

    public List<FeatureResult> getFeatureResults() {
        return featureResults;
    }

    public FeatureResult getFeatureResultByFeatureName(String featureName) {
        for (FeatureResult featureResult : featureResults) {
            if (featureResult.getFeature().getName().equals(featureName)) {
                return featureResult;
            }
        }
        return null;
    }

    public void setArithmeticMean(double arithmeticMean) {
        this.arithmeticMean = arithmeticMean;
    }

    public void setFmeasure(double fmeasure) {
        this.fmeasure = fmeasure;
    }

    public void setMeanOfFMeasures(double meanOfFMeasures) {
        this.meanOfFMeasures = meanOfFMeasures;
    }

    public double getArithmeticMean() {
        return arithmeticMean;
    }

    public double getFmeasure() {
        return fmeasure;
    }

    public double getMeanOfFMeasures() {
        return meanOfFMeasures;
    }

    public int getNumberOfExpectedTuples() {
        return numberOfExpectedTuples;
    }

    public void setNumberOfExpectedTuples(int numberOfExpectedTuples) {
        this.numberOfExpectedTuples = numberOfExpectedTuples;
    }

    public int getNumberOfTranslatedTuples() {
        return numberOfTranslatedTuples;
    }

    public void setNumberOfTranslatedTuples(int numberOfTranslatedTuples) {
        this.numberOfTranslatedTuples = numberOfTranslatedTuples;
    }

    public int getNumberOfNodesInExpectedInstance() {
        return numberOfNodesInExpectedInstance;
    }

    public void setNumberOfNodesInExpectedInstance(int numberOfNodesInExpectedInstance) {
        this.numberOfNodesInExpectedInstance = numberOfNodesInExpectedInstance;
    }

    public int getNumberOfNodesInTranslatedInstance() {
        return numberOfNodesInTranslatedInstance;
    }

    public void setNumberOfNodesInTranslatedInstance(int numberOfNodesInTranslatedInstance) {
        this.numberOfNodesInTranslatedInstance = numberOfNodesInTranslatedInstance;
    }

    public double getTreeEditDistanceValiente() {
        return treeEditDistanceValiente;
    }

    public void setTreeEditDistanceValiente(double treeEditDistance) {
        this.treeEditDistanceValiente = treeEditDistance;
    }

    public double getTreeEditDistanceShasha() {
        return treeEditDistanceShasha;
    }

    public void setTreeEditDistanceShasha(double treeEditDistance) {
        this.treeEditDistanceShasha = treeEditDistance;
    }

    public double getTopDownOrderedMaximumSubtree() {
        return topDownOrderedMaximumSubtree;
    }

    public void setTopDownOrderedMaximumSubtree(double topDownOrderedMaximumSubtree) {
        this.topDownOrderedMaximumSubtree = topDownOrderedMaximumSubtree;
    }

    public double getBottomUpMaximumSubtree() {
        return bottomUpMaximumSubtree;
    }

    public void setBottomUpMaximumSubtree(double bottomUpMaximumSubtree) {
        this.bottomUpMaximumSubtree = bottomUpMaximumSubtree;
    }

    public double getJaccard() {
        return jaccard;
    }

    public void setJaccard(double jaccard) {
        this.jaccard = jaccard;
    }

    public String getExpectedInstance() {
        return expectedInstance;
    }

    public void setExpectedInstance(String expectedInstance) {
        this.expectedInstance = expectedInstance;
    }

    public String getTranslatedInstance() {
        return translatedInstance;
    }

    public void setTranslatedInstance(String translatedInstance) {
        this.translatedInstance = translatedInstance;
    }
}
