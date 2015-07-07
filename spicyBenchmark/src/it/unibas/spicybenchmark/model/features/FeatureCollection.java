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

import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import java.util.ArrayList;
import java.util.List;

public class FeatureCollection {

    private List<IFeature> features = new ArrayList<IFeature>();
    private List<TupleNodeBenchmark> expectedInstanceTuples;
    private List<TupleNodeBenchmark> translatedInstanceTuples;

    public FeatureCollection(List<TupleNodeBenchmark> expectedInstanceTuples, List<TupleNodeBenchmark> translatedInstanceTuples) {
        this.expectedInstanceTuples = expectedInstanceTuples;
        this.translatedInstanceTuples = translatedInstanceTuples;
    }

    public void addFeature(IFeature feature) {
        this.features.add(feature);
    }

    public List<IFeature> getFeatures() {
        return features;
    }

    public List<TupleNodeBenchmark> getExpectedInstanceTuples() {
        return expectedInstanceTuples;
    }

    public List<TupleNodeBenchmark> getTranslatedInstanceTuples() {
        return translatedInstanceTuples;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (IFeature iFeature : features) {
            result.append(iFeature.toString());
        }
        return result.toString();
    }
}
