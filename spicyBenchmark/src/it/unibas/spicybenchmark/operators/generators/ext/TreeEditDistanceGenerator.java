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
 
package it.unibas.spicybenchmark.operators.generators.ext;

import headliner.treedistance.ComparisonZhangShasha;
import headliner.treedistance.CreateTreeHelper;
import headliner.treedistance.OpsZhangShasha;
import headliner.treedistance.Transformation;
import headliner.treedistance.TreeDefinition;
import it.unibas.spicybenchmark.model.TuplePair;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.operators.generators.ParentChildNestingGenerator;
import java.util.List;

public class TreeEditDistanceGenerator {

    public double computeTreeEditDistance(FeatureCollection featureCollection) {
        ParentChildNestingGenerator generator = new ParentChildNestingGenerator(featureCollection.getTranslatedInstanceTuples(), featureCollection.getExpectedInstanceTuples());
        String translatedTree = buildStringForTreeEditDistance(generator.getTranslatedTuplePairs());
        String expectedTree = buildStringForTreeEditDistance(generator.getExpectedTuplePairs());

	System.out.println("The string-tree is: \n"+translatedTree);
        TreeDefinition aTree = CreateTreeHelper.makeTree(translatedTree);
	System.out.println("The tree is: \n"+aTree);
	TreeDefinition bTree = CreateTreeHelper.makeTree(expectedTree);
	System.out.println("The tree is: \n"+bTree);

	ComparisonZhangShasha treeCorrector = new ComparisonZhangShasha();
	OpsZhangShasha costs = new OpsZhangShasha();
	Transformation transform = treeCorrector.findDistance(aTree, bTree, costs);
//	System.out.println("Distance: "+transform.getCost());
        return transform.getCost();
    }
    
    private String buildStringForTreeEditDistance(List<TuplePair> tuplePairs) {
        StringBuilder result = new StringBuilder();
        for (TuplePair tuplePair : tuplePairs) {
            result.append(Math.abs(tuplePair.getSecondTuple().getGlobalId().hashCode()));
            result.append("-");
            result.append(Math.abs(tuplePair.getFirstTuple().getGlobalId().hashCode()));
            result.append(";");
        }
        return result.toString().replaceAll(":", "..");
    }
}
