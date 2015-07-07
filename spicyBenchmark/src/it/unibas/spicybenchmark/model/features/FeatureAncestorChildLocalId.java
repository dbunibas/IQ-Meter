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
import it.unibas.spicybenchmark.model.TuplePair;
import it.unibas.spicybenchmark.model.features.comparators.PairLocalIdComparator;
import java.util.Comparator;
import java.util.List;

public class FeatureAncestorChildLocalId extends AbstractFeatureNesting {

    public FeatureAncestorChildLocalId(List<TuplePair> expectedTuplePairs, List<TuplePair> translatedTuplePairs) {
        this.expectedObjects = expectedTuplePairs;
        this.translatedObjects = translatedTuplePairs;
    }

    protected boolean check(TupleNodeBenchmark translatedTuple, TupleNodeBenchmark expectedTuple) {
        return translatedTuple.getLocalId().equals(expectedTuple.getLocalId());
    }

    public String getType() {
        return SpicyBenchmarkConstants.TYPE_LOCAL_ID;
    }

    public Comparator getComparator() {
        return new PairLocalIdComparator();
    }
}
