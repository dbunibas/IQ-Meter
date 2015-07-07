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
 
package it.unibas.spicybenchmark.operators.generators;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicybenchmark.Utility;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.TuplePair;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ParentChildNestingGenerator {

    private Log logger = LogFactory.getLog(ParentChildNestingGenerator.class);
    private List<TuplePair> expectedTuplePairs;
    private List<TuplePair> translatedTuplePairs;

    public ParentChildNestingGenerator(List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance) {
        expectedTuplePairs = buildTuplePairs(expectedInstance);
        if (logger.isDebugEnabled()) logger.debug("Expected Tuple Pairs: " + Utility.printTuplePairsWithNoPositions(expectedTuplePairs));
        translatedTuplePairs = buildTuplePairs(translatedInstance);
        if (logger.isDebugEnabled()) logger.debug("Translated Tuple Pairs: " + Utility.printTuplePairsWithNoPositions(translatedTuplePairs));
    }

    private List<TuplePair> buildTuplePairs(List<TupleNodeBenchmark> tupleNodes) {
        List<TuplePair> pairs = new ArrayList<TuplePair>();
        for (TupleNodeBenchmark tupleNodeBenchmark : tupleNodes) {
            INode node = tupleNodeBenchmark.getINode();
            if (!node.isRoot() && node.getFather().getFather() != null) {
                INode fatherTupleNode = node.getFather().getFather();
                TupleNodeBenchmark fatherTupleNodeBenchmark = findTupleNodeBenchmark(fatherTupleNode, tupleNodes);
                TuplePair tuplePair = new TuplePair(tupleNodeBenchmark, fatherTupleNodeBenchmark);
                pairs.add(tuplePair);
            }
        }
        return pairs;
    }

    private TupleNodeBenchmark findTupleNodeBenchmark(INode fatherTupleNode, List<TupleNodeBenchmark> tupleNodes) {
        for (TupleNodeBenchmark currentTupleNodeBenchmark : tupleNodes) {
            if (currentTupleNodeBenchmark.getINode().equals(fatherTupleNode)) {
                return currentTupleNodeBenchmark;
            }
        }
        throw new IllegalArgumentException("Error: father tuple node not found...: " + fatherTupleNode);
    }

    public List<TuplePair> getExpectedTuplePairs() {
        return expectedTuplePairs;
    }

    public List<TuplePair> getTranslatedTuplePairs() {
        return translatedTuplePairs;
    }

}
