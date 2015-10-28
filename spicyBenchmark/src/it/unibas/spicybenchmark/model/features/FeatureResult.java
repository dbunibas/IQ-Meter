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

public class FeatureResult {

    private IFeature feature;
    private double precision;
    private double recall;
    private double fmeasure;
    private long evaluationTime;
    private Violations violations;

    public FeatureResult(IFeature feature, double precision, double recall, double fmeasure, long evalualtionTime, Violations violations) {
        assert (precision <= 1.0) : "Precision cannot be higher than 1.0: " + precision;
        assert (recall <= 1.0): "Recall cannot be higher than 1.0: " + recall;
        assert (fmeasure <= 1.0): "FMeasure cannot be higher than 1.0: " + fmeasure;
        this.feature = feature;
        this.precision = precision;
        this.recall = recall;
        this.fmeasure = fmeasure;
        this.evaluationTime = evalualtionTime;
        this.violations = violations;
    }

    public IFeature getFeature() {
        return feature;
    }

    public double getFmeasure() {
        return fmeasure;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public long getEvaluationTime() {
        return evaluationTime;
    }

    public Violations getViolations() {
        return violations;
    }
}
