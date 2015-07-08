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

import it.unibas.spicybenchmark.SpicyBenchmarkConstants;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.features.comparators.SingleLocalIdComparator;
import it.unibas.spicybenchmark.operators.EvaluateFeatureWithMultipleHashIndexing;
import it.unibas.spicybenchmark.operators.IEvaluateFeature;
import it.unibas.spicybenchmark.operators.StandardEvaluateFeature;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeatureLocalIdWithLLUNs extends AbstractFeature {

    private double precisionForVariable = 0.5;
    private List<String> attributesToExclude = new ArrayList<String>();

    public FeatureLocalIdWithLLUNs(List<TupleNodeBenchmark> expectedTuples, List<TupleNodeBenchmark> translatedTuples) {
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
        return new EvaluateFeatureWithMultipleHashIndexing(precisionForVariable,attributesToExclude);
    }
}