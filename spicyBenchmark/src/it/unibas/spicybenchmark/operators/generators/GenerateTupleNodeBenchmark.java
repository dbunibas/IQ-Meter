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
import it.unibas.spicy.model.datasource.nodes.AttributeNode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.datasource.nodes.MetadataNode;
import it.unibas.spicy.model.datasource.nodes.SequenceNode;
import it.unibas.spicy.model.datasource.nodes.SetNode;
import it.unibas.spicy.model.datasource.nodes.TupleNode;
import it.unibas.spicy.model.datasource.nodes.UnionNode;
import it.unibas.spicy.model.datasource.operators.INodeVisitor;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenerateTupleNodeBenchmark {

    public List<TupleNodeBenchmark> generate(INode instance, List<String> exclusionList) {
        GenerateTupleNodeBenchmarkVisitor visitor = new GenerateTupleNodeBenchmarkVisitor(exclusionList);
        instance.accept(visitor);
        return visitor.getResult();
    }
}

class GenerateTupleNodeBenchmarkVisitor implements INodeVisitor {

    private Log logger = LogFactory.getLog(GenerateTupleNodeBenchmark.class);
    private static final String SEPARATOR = ",";
    private List<String> exclusionList;
    private List<TupleNodeBenchmark> tupleNodes = new ArrayList<TupleNodeBenchmark>();
    private Map<INode, TupleNodeBenchmark> cache = new HashMap<INode, TupleNodeBenchmark>();
    GeneratePathExpression pathGenerator = new GeneratePathExpression();

    public GenerateTupleNodeBenchmarkVisitor(List<String> exclusionList) {
        this.exclusionList = exclusionList;
    }

    public List<TupleNodeBenchmark> getResult() {
        return tupleNodes;
    }

    public void visitSetNode(SetNode node) {
        visitChildren(node);
    }

    public void visitTupleNode(TupleNode node) {
        if (node.getFather() instanceof TupleNode || node.getFather() instanceof SequenceNode) {
            visitChildren(node);
        } else {
            visitGenericNode(node);
        }
    }

    public void visitSequenceNode(SequenceNode node) {
        if (node.getFather() instanceof TupleNode || node.getFather() instanceof SequenceNode) {
            visitChildren(node);
        } else {
            visitGenericNode(node);
        }
    }

    public void visitUnionNode(UnionNode node) {
        visitGenericNode(node);
    }

    public void visitAttributeNode(AttributeNode node) {
        return;
    }

    public void visitMetadataNode(MetadataNode node) {
        return;
    }

    public void visitLeafNode(LeafNode node) {
        return;
    }

    private void visitGenericNode(INode node) {
        if (!node.isVirtual()) {
            createTupleNodeBenchmark(node);
        }
        visitChildren(node);
    }

    private TupleNodeBenchmark createTupleNodeBenchmark(INode node) {
        TupleNodeBenchmark tupleNodeBenchmark = new TupleNodeBenchmark();
        tupleNodeBenchmark.setINode(node);
        generateLocalId(node, tupleNodeBenchmark);
        generateGlobalId(node, tupleNodeBenchmark);
        if (!cache.containsKey(node)) {
            cache.put(node, tupleNodeBenchmark);
        }
        tupleNodes.add(tupleNodeBenchmark);
        return tupleNodeBenchmark;
    }

    private void generateLocalId(INode node, TupleNodeBenchmark tupleNodeBenchmark) {
        List<String> listOfChildren = generateListOfChildren(node);
        StringBuilder result = new StringBuilder();
        result.append(node.getLabel()).append("(");
        for (int i = 0; i < listOfChildren.size(); i++) {
            String child = listOfChildren.get(i);
            result.append(child);
            if (i < listOfChildren.size() - 1) {
                result.append(SEPARATOR);
            }
        }
        result.append(")");
        String localId = result.toString();
        tupleNodeBenchmark.setLocalId(localId);
    }

    private List<String> generateListOfChildren(INode node) {
        List<String> listOfChildren = new ArrayList<String>();
        List<INode> children = node.getChildren();
        for (INode child : children) {
            if (child instanceof AttributeNode || child instanceof MetadataNode) {
                PathExpression childPathExpression = pathGenerator.generatePathFromRoot(child);
                String childPath = childPathExpression.toString();
                if (exclusionList.contains(childPath)) {
                    continue;
                }
                LeafNode leafNode = (LeafNode) child.getChild(0);
                if (logger.isTraceEnabled()) logger.trace("Generating list of attributes. Leaf Node: " + leafNode.getLabel());
                String value = leafNode.getValue().toString();
                String valueToAdd = "";
                if (node.getFather() instanceof TupleNode || node.getFather() instanceof SequenceNode) {
                    valueToAdd += node.getLabel() + ".";
                }
                valueToAdd += childPathExpression.getLastStep() + ":" + value;
                listOfChildren.add(valueToAdd);
            }
            if (child instanceof SequenceNode || child instanceof TupleNode) {
                listOfChildren.addAll(generateListOfChildren(child));
            }
        }
        Collections.sort(listOfChildren);
        return listOfChildren;
    }

    private void generateGlobalId(INode node, TupleNodeBenchmark tupleNodeBenchmark) {
        StringBuffer globalId = new StringBuffer();
        INode fatherNode = node.getFather();
        while (fatherNode != null) {
            if (fatherNode instanceof TupleNode && !fatherNode.isVirtual()) {
                TupleNodeBenchmark fatherNodeInCache = cache.get(fatherNode);
                globalId.insert(0, SEPARATOR).insert(0, fatherNodeInCache.getLocalId());//.insert(0, fatherNode.getLabel());
            } else {
                globalId.insert(0, SEPARATOR).insert(0, fatherNode.getLabel());
            }
            fatherNode = fatherNode.getFather();
        }
        String localId = tupleNodeBenchmark.getLocalId();
        globalId.append(localId);
        tupleNodeBenchmark.setGlobalId(globalId.toString());
    }

    private void visitChildren(INode node) {
        List<INode> children = node.getChildren();
        if (children != null) {
            for (INode child : children) {
                child.accept(this);
            }
        }
    }
}
