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

public abstract class AbstractFeatureJoins extends AbstractFeature {

    protected abstract boolean check(TupleNodeBenchmark translatedTuple, TupleNodeBenchmark expectedTuple);

    @Override
    public boolean match(Object expectedObject, Object translatedObject) {
        TuplePair expectedTuplePair = (TuplePair) expectedObject;
        TuplePair translatedTuplePair = (TuplePair) translatedObject;
        TupleNodeBenchmark firstTranslatedTupleNode = translatedTuplePair.getFirstTuple();
        TupleNodeBenchmark secondTranslatedTupleNode = translatedTuplePair.getSecondTuple();
        TupleNodeBenchmark firstExpectedTuple = expectedTuplePair.getFirstTuple();
        TupleNodeBenchmark secondExpectedTuple = expectedTuplePair.getSecondTuple();
        if ((check(firstTranslatedTupleNode, firstExpectedTuple) && check(secondTranslatedTupleNode, secondExpectedTuple))
                || (check(secondTranslatedTupleNode, firstExpectedTuple) && check(firstTranslatedTupleNode, secondExpectedTuple))) {
            return true;
        }
        return false;
    }

    public String getTuplePairType() {
        return SpicyBenchmarkConstants.TYPE_TUPLE_PAIR_JOIN;
    }
}
