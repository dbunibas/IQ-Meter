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

import it.unibas.spicy.model.datasource.ForeignKeyConstraint;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.KeyConstraint;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.exceptions.PathSyntaxException;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.TuplePair;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TuplesInJoinGenerator {

    private Log logger = LogFactory.getLog(TuplesInJoinGenerator.class);
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    private FindNode nodeFinder = new FindNode();
    private List<TuplePair> expectedTuplePairs;
    private List<TuplePair> translatedTuplePairs;

    public TuplesInJoinGenerator(IDataSourceProxy dataSource, INode translatedInstanceNode, INode expectedInstanceNode, List<TupleNodeBenchmark> translatedInstance, List<TupleNodeBenchmark> expectedInstance) {
        Date beginTime = new Date();
        expectedTuplePairs = buildTuplePairs(dataSource, expectedInstance, expectedInstanceNode);
        if (logger.isDebugEnabled()) {
            logger.debug("Number of Expected Tuple Pairs: " + expectedTuplePairs.size());
        }
        translatedTuplePairs = buildTuplePairs(dataSource, translatedInstance, translatedInstanceNode);
        if (logger.isDebugEnabled()) {
            logger.debug("Number of Translated Tuple Pairs: " + translatedTuplePairs.size());
        }
        Date endTime = new Date();
        long evaluationTime = endTime.getTime() - beginTime.getTime();
        logger.info("Evaluation Time for generating Tuples in Join: " + evaluationTime + " ms");
    }

    private List<TuplePair> buildTuplePairs(IDataSourceProxy dataSource, List<TupleNodeBenchmark> instance, INode instanceNode) {
        List<TuplePair> pairs = new ArrayList<TuplePair>();

        Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> tuplesWithFKs = extractTuplesWithFKs(dataSource, instance, instanceNode);
        Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> tuplesWithPKs = extractTuplesWithPKs(dataSource, instance, instanceNode);

        for (ForeignKeyConstraint foreignKeyConstraint : tuplesWithFKs.keySet()) {
            List<TupleNodeBenchmark> fkNodes = tuplesWithFKs.get(foreignKeyConstraint);
            List<TupleNodeBenchmark> pkNodes = tuplesWithPKs.get(foreignKeyConstraint);
            if (pkNodes == null) {
                continue;
            }
            Map<String, List<TupleNodeBenchmark>> hashMapForeignKeys = generateHashMap(foreignKeyConstraint, fkNodes);
            generatePairs(foreignKeyConstraint, hashMapForeignKeys, pkNodes, pairs);
        }

        return pairs;
    }

    private Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> extractTuplesWithFKs(IDataSourceProxy dataSource, List<TupleNodeBenchmark> instance, INode instanceNode) {
        Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> result = new HashMap<ForeignKeyConstraint, List<TupleNodeBenchmark>>();
        for (ForeignKeyConstraint foreignKeyConstraint : dataSource.getForeignKeyConstraints()) {
            PathExpression attributePath = foreignKeyConstraint.getForeignKeyPaths().get(0);
            INode tupleNode = attributePath.getLastNode(instanceNode).getFather();
            PathExpression tupleNodePath = pathGenerator.generatePathFromRoot(tupleNode);
            List<INode> nodesInInstance = nodeFinder.findNodesInInstance(tupleNodePath, instanceNode);
            List<TupleNodeBenchmark> tupleBenchmarks = convertTuple(nodesInInstance, instance);
            result.put(foreignKeyConstraint, tupleBenchmarks);
        }
        return result;
    }

    private Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> extractTuplesWithPKs(IDataSourceProxy dataSource, List<TupleNodeBenchmark> instance, INode instanceNode) {
        Map<ForeignKeyConstraint, List<TupleNodeBenchmark>> result = new HashMap<ForeignKeyConstraint, List<TupleNodeBenchmark>>();
        for (ForeignKeyConstraint foreignKeyConstraint : dataSource.getForeignKeyConstraints()) {
            PathExpression attributePath = foreignKeyConstraint.getKeyConstraint().getKeyPaths().get(0);
            try {
                INode tupleNode = attributePath.getLastNode(instanceNode).getFather();
                PathExpression tupleNodePath = pathGenerator.generatePathFromRoot(tupleNode);
                List<INode> nodesInInstance = nodeFinder.findNodesInInstance(tupleNodePath, instanceNode);
                List<TupleNodeBenchmark> tupleBenchmarks = convertTuple(nodesInInstance, instance);
                result.put(foreignKeyConstraint, tupleBenchmarks);
            } catch (PathSyntaxException pse) {
                if (logger.isDebugEnabled()) logger.debug("Skipping join: primary key not found: " + foreignKeyConstraint);
            }
        }
        return result;
    }

    private List<TupleNodeBenchmark> convertTuple(List<INode> nodesInInstance, List<TupleNodeBenchmark> instance) {
        List<TupleNodeBenchmark> result = new ArrayList<TupleNodeBenchmark>();
        for (INode node : nodesInInstance) {
            TupleNodeBenchmark tuple = findCorrespondingTuple(node, instance);
            result.add(tuple);
        }
        return result;
    }

    private TupleNodeBenchmark findCorrespondingTuple(INode node, List<TupleNodeBenchmark> instance) {
        for (TupleNodeBenchmark tupleNodeBenchmark : instance) {
            if (tupleNodeBenchmark.getINode().equals(node)) {
                return tupleNodeBenchmark;
            }
        }
        throw new IllegalArgumentException("Tuple Node Benchmark not found!!!");
    }

    private Map<String, List<TupleNodeBenchmark>> generateHashMap(ForeignKeyConstraint foreignKeyConstraint, List<TupleNodeBenchmark> fkNodes) {
        Map<String, List<TupleNodeBenchmark>> result = new HashMap<String, List<TupleNodeBenchmark>>();
        for (TupleNodeBenchmark tupleNodeBenchmark : fkNodes) {
            String fkValue = generateFKValue(foreignKeyConstraint, tupleNodeBenchmark);
            List<TupleNodeBenchmark> tupleNodes = result.get(fkValue);
            if (tupleNodes == null) {
                tupleNodes = new ArrayList<TupleNodeBenchmark>();
                result.put(fkValue, tupleNodes);
            }
            tupleNodes.add(tupleNodeBenchmark);
        }
        return result;
    }

    private void generatePairs(ForeignKeyConstraint foreignKeyConstraint, Map<String, List<TupleNodeBenchmark>> hashMapForeignKeys, List<TupleNodeBenchmark> pkNodes, List<TuplePair> pairs) {
        for (TupleNodeBenchmark pkTupleNodeBenchmark : pkNodes) {
            String pkValue = generatePKValue(foreignKeyConstraint.getKeyConstraint(), pkTupleNodeBenchmark);
            List<TupleNodeBenchmark> foreignKeyTuples = hashMapForeignKeys.get(pkValue);
            if (foreignKeyTuples == null) {
                continue;
            }
            for (TupleNodeBenchmark fkTupleNode : foreignKeyTuples) {
                String fkValue = generateFKValue(foreignKeyConstraint, fkTupleNode);
                if (fkValue.equals(pkValue)) {
                    TuplePair tuplePair = new TuplePair(fkTupleNode, pkTupleNodeBenchmark);
                    pairs.add(tuplePair);
                }
            }
        }
    }

    private String generateFKValue(ForeignKeyConstraint foreignKeyConstraint, TupleNodeBenchmark fkTupleNode) {
        INode fkNode = fkTupleNode.getINode();
        String fkValue = "";
        for (PathExpression foreignKeyPath : foreignKeyConstraint.getForeignKeyPaths()) {
            String attributeNodeLabel = fkNode.getChild(foreignKeyPath.getLastStep()).getLabel();
            fkValue += fkNode.getChild(attributeNodeLabel).getChild(0).getValue() + "-";
        }
        return fkValue;
    }

    private String generatePKValue(KeyConstraint keyConstraint, TupleNodeBenchmark pkTupleNode) {
        INode pkNode = pkTupleNode.getINode();
        String pkValue = "";
        for (PathExpression keyPath : keyConstraint.getKeyPaths()) {
            String attributeNodeLabel = pkNode.getChild(keyPath.getLastStep()).getLabel();
            pkValue += pkNode.getChild(attributeNodeLabel).getChild(0).getValue() + "-";
        }
        return pkValue;
    }

    public List<TuplePair> getExpectedTuplePairs() {
        return expectedTuplePairs;
    }

    public List<TuplePair> getTranslatedTuplePairs() {
        return translatedTuplePairs;
    }
}
