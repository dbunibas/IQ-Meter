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
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.api.ITreeNodeComparator;
import simpack.api.impl.AbstractDistanceConversion;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.TreeEditDistance;
import simpack.util.conversion.CommonDistanceConversion;
import simpack.util.conversion.LogarithmicDistanceConversion;
import simpack.util.conversion.WorstCaseDistanceConversion;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;
import simpack.util.tree.comparator.NamedTreeNodeComparator;
import simpack.util.tree.comparator.TypedTreeNodeComparator;

public class TreeEditDistanceGeneratorSimPack {

    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(TreeEditDistanceGeneratorSimPack.class.getName());

    public double compute(Configuration configuration) {
        ITreeNode tree1 = CommonSimPack.generateSample(configuration.getExpectedInstanceAbsolutePath());
        ITreeNode tree2 = CommonSimPack.generateSample(configuration.getTranslatedInstanceAbsolutePath());
        try {
            ITreeNodeComparator comparator = loadTreeNodeComparator(configuration);
            AbstractDistanceConversion conversion = loadDistanceConversion(configuration);
            double weightInsert = configuration.getTreeEditDistanceValienteWeightInsert();
            double weightDelete = configuration.getTreeEditDistanceValienteWeightDelete();
            double weightSubstitute = configuration.getTreeEditDistanceValienteWeightSubstitute();
            TreeEditDistance calc = new TreeEditDistance(new SimpleTreeAccessor(tree1), new SimpleTreeAccessor(tree2), comparator, conversion);
            calc.setWeightInsert(weightInsert);
            calc.setWeightDelete(weightDelete);
            calc.setWeightSubstitute(weightSubstitute);
            calc.calculate();
            double treeEditDistance = calc.getTreeEditDistance();
            logger.debug("TED " + treeEditDistance);
            logger.debug("TreeNodeComparator " + calc.getComparator());
            logger.debug("Similarity " + calc.getSimilarity());
            logger.debug("ShortestPath " + calc.getShortestPath());
            logger.debug("Weight Delete " + calc.getWeightDelete());
            logger.debug("Weight Insert " + calc.getWeightInsert());
            logger.debug("Weight Substitute " + calc.getWeightSubstitute());
            logger.debug("getWorstCaseRetainStructure " + calc.getWorstCaseRetainStructure());
            logger.debug("getWorstCaseSubstituteAll " + calc.getWorstCaseSubstituteAll());
            logger.debug("getWorstCaseSumOfNodes " + calc.getWorstCaseSumOfNodes());
            logger.debug("----------------------");
            return treeEditDistance;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InvalidElementException e) {
            e.printStackTrace();
        }
        return Double.NaN;
    }

    private AbstractDistanceConversion loadDistanceConversion(Configuration configuration) {
        AbstractDistanceConversion conversion = new CommonDistanceConversion();
        if (configuration.getTreeEditDistanceValienteDistanceConversion() == null) {
            return conversion;
        }
        if (configuration.getTreeEditDistanceValienteDistanceConversion().equalsIgnoreCase(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_COMMON)) {
            conversion = new CommonDistanceConversion();
        }
        if (configuration.getTreeEditDistanceValienteDistanceConversion().equalsIgnoreCase(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_LOGARITHMIC)) {
            conversion = new LogarithmicDistanceConversion();
        }
        if (configuration.getTreeEditDistanceValienteDistanceConversion().equalsIgnoreCase(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_WORST_CASE)) {
            conversion = new WorstCaseDistanceConversion();
        }
        if (configuration.getTreeEditDistanceValienteDistanceConversion().equalsIgnoreCase(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_DEBUG)) {
            conversion = new DebugDistanceConversion();
        }
        return conversion;
    }

    private ITreeNodeComparator loadTreeNodeComparator(Configuration configuration) {
        ITreeNodeComparator comparator = new AlwaysTrueTreeNodeComparator();
        if (configuration.getTreeEditDistanceValienteTreeNodeComparator() == null) {
            return comparator;
        }
        if (configuration.getTreeEditDistanceValienteTreeNodeComparator().equals(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_ALWAYS_TRUE)) {
            comparator = new AlwaysTrueTreeNodeComparator();
        }
        if (configuration.getTreeEditDistanceValienteTreeNodeComparator().equals(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_NAMED_TREE)) {
            comparator = new NamedTreeNodeComparator();
        }
        if (configuration.getTreeEditDistanceValienteTreeNodeComparator().equals(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_TYPED_TREE)) {
            comparator = new TypedTreeNodeComparator();
        }
        if (configuration.getTreeEditDistanceValienteTreeNodeComparator().equals(DAOConfiguration.TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_DEBUG)) {
            comparator = new DebugTreeNodeComparator();
        }
        return comparator;
    }
}
