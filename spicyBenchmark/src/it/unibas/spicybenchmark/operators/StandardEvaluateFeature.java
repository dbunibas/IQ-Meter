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

import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.IFeature;
import it.unibas.spicybenchmark.model.features.Violations;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StandardEvaluateFeature implements IEvaluateFeature{

    private Log logger = LogFactory.getLog(StandardEvaluateFeature.class);

    public FeatureResult getResult(IFeature feature) {
        if (!feature.isEvaluable()) {
            return null;
        }
        Violations violations = new Violations();
        Date beginTime = new Date();
        int numberOfMatches = countMatches(feature, violations);
        double precision = getPrecision(feature, numberOfMatches);
        double recall = getRecall(feature, numberOfMatches);
        double fmeasure = getFMeasure(precision, recall);
        Date endTime = new Date();
        long evaluationTime = endTime.getTime() - beginTime.getTime();
        FeatureResult result = new FeatureResult(feature, precision, recall, fmeasure, evaluationTime, violations);
        return result;
    }

    private double getPrecision(IFeature feature, int numberOfMatches) {
        int translatedSize = feature.getTranslatedObjects().size();
        return numberOfMatches / (double) translatedSize;
    }

    private double getRecall(IFeature feature, int numberOfMatches) {
        int expectedSize = feature.getExpectedObjects().size();
        return numberOfMatches / (double) expectedSize;
    }

    private double getFMeasure(double precision, double recall) {
        if (precision == 0.0 && recall == 0.0) {
            return 0.0;
        } else {
            return (2 * precision * recall) / (precision + recall);
        }
    }

    @SuppressWarnings("unchecked")
    private int countMatches(IFeature feature, Violations violations) {
        Collections.sort(feature.getTranslatedObjects(), feature.getComparator());
        Collections.sort(feature.getExpectedObjects(), feature.getComparator());

//        int minSize = Math.min(feature.getTranslatedObjects().size(), feature.getExpectedObjects().size());
        int expectedIndex = 0;
        int translatedIndex = 0;
        int count = 0;
        while (translatedIndex < feature.getTranslatedObjects().size() && expectedIndex < feature.getExpectedObjects().size()) {
            Object expectedObject = feature.getExpectedObjects().get(expectedIndex);
            Object translatedObject = feature.getTranslatedObjects().get(translatedIndex);
            if (logger.isTraceEnabled()) logger.trace("Comparing Objects: " + expectedObject + ", " + translatedObject);
            if (feature.match(expectedObject, translatedObject)) {
                if (logger.isTraceEnabled()) logger.trace("MATCH...");
                expectedIndex++;
                translatedIndex++;
                count++;
            } else {
                if (feature.getComparator().compare(expectedObject, translatedObject) < 0) {
                    if (logger.isTraceEnabled()) logger.trace("expected object less than translated object");
                    violations.addMissingElement(expectedObject);
                    expectedIndex++;
                }
                if (feature.getComparator().compare(expectedObject, translatedObject) > 0) {
                    if (logger.isTraceEnabled()) logger.trace("expected object greater than translated object");
                    violations.addExtraElement(translatedObject);
                    translatedIndex++;
                }
            }
        }

        for (int i = expectedIndex; i < feature.getExpectedObjects().size(); i++) {
            Object object = feature.getExpectedObjects().get(i);
            violations.addMissingElement(object);
        }

        for (int i = translatedIndex; i < feature.getTranslatedObjects().size(); i++) {
            Object object = feature.getTranslatedObjects().get(i);
            violations.addExtraElement(object);
        }

        return count;
    }
}
