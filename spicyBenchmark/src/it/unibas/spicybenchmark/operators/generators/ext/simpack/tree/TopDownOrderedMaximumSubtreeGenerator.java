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
 
package it.unibas.spicybenchmark.operators.generators.ext.simpack.tree;

import it.unibas.spicybenchmark.Configuration;
import it.unibas.spicybenchmark.operators.generators.ext.simpack.CommonSimPack;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.api.ITreeNodeComparator;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.TopDownOrderedMaximumSubtree;

public class TopDownOrderedMaximumSubtreeGenerator {

    public double compute(Configuration configuration) {
        ITreeNode tree1 = CommonSimPack.generateSample(configuration.getExpectedInstanceAbsolutePath());
        ITreeNode tree2 = CommonSimPack.generateSample(configuration.getTranslatedInstanceAbsolutePath());
        try {
            TopDownOrderedMaximumSubtree calc = new TopDownOrderedMaximumSubtree(new SimpleTreeAccessor(tree1),
                    new SimpleTreeAccessor(tree2));
            calc.calculate();
            return calc.getSimilarity();
//            calc.getSimilarity();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InvalidElementException e) {
            e.printStackTrace();
        }
        return Double.NaN;
    }
}
