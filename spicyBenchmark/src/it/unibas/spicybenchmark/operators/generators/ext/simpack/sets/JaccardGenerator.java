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
 
package it.unibas.spicybenchmark.operators.generators.ext.simpack.sets;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.operators.generators.GenerateTupleNodeBenchmark;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import simpack.measure.set.Jaccard;

public class JaccardGenerator {

    public double compute(INode expectedInstanceNode, INode translatedInstanceNode) {
        List<TupleNodeBenchmark> expectedTuples = new GenerateTupleNodeBenchmark().generate(expectedInstanceNode, new ArrayList<String>());
        List<TupleNodeBenchmark> translatedTuples = new GenerateTupleNodeBenchmark().generate(translatedInstanceNode, new ArrayList<String>());
        try {
            Set set1 = convertListInSet(expectedTuples);
            Set set2 = convertListInSet(translatedTuples);
            Jaccard calc = new Jaccard(set1, set2);

            calc.calculate();
            return calc.getSimilarity();
//            calc.getSimilarity();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return Double.NaN;
    }
    
    private Set<Object> convertListInSet(List<TupleNodeBenchmark> inputList) {
        Set<Object> result = new HashSet<Object>();
        for (TupleNodeBenchmark tupleNodeBenchmark : inputList) {
            result.add(tupleNodeBenchmark.getGlobalId());
        }
        return result;
    }

}
