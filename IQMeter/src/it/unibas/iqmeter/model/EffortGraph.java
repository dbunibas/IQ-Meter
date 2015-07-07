/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.model;

import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;

/**
 *
 * @author antonio
 */
public class EffortGraph {

    private UndirectedGraph<EffortGraphNode, DefaultEdge> graph;
    private int toolFunctionCost;
    private int toolBoxCost;
    private int numVertex;
    private int numFunction;
    private int numEdges;
    private int numAnnotation;
    private int numBox;
    private int bitNode;
    private int bitEdge;
    private int bitFunction;
    private int bitBox;
    private int bitAnnotation;
    private int bitEffort;
    private Log logger = LogFactory.getLog(this.getClass());
    private int numSource;
    private int numTarget;

    public int getNumSource() {
        return numSource;
    }

    public int getNumTarget() {
        return numTarget;
    }

    public void setNumSource(int numSource) {
        this.numSource = numSource;
    }

    public void setNumTarget(int numTarget) {
        this.numTarget = numTarget;
    }

    public int getToolFunctionCost() {
        return toolFunctionCost;
    }

    public int getNumBox() {
        return numBox;
    }

    public int getBitBox() {
        return bitBox;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public int getNumFunction() {
        return numFunction;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public int getNumAnnotation() {
        return numAnnotation;
    }

    public int getBitNodes() {
        return bitNode;
    }

    public int getBitEdges() {
        return bitEdge;
    }

    public int getBitFunction() {
        return bitFunction;
    }

    public int getBitAnnotation() {
        return bitAnnotation;
    }

    public int getToolBoxCost() {
        return toolBoxCost;
    }

    public int getBitEffort() {
        return bitEffort;
    }

    public EffortGraph(UndirectedGraph<EffortGraphNode, DefaultEdge> g, int toolCost, int boxCost) {
        this.graph = g;
        this.toolFunctionCost = toolCost;
        this.toolBoxCost = boxCost;
        this.calculateEffort();
    }

    public UndirectedGraph<EffortGraphNode, DefaultEdge> getGraph() {
        return this.graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(UndirectedGraph<EffortGraphNode, DefaultEdge> graph) {
        this.graph = graph;
    }

    public Set<EffortGraphNode> getVertexSet() {
        return this.graph.vertexSet();
    }

    public Set<DefaultEdge> getEdgesSet() {
        return this.graph.edgeSet();
    }

    public EffortGraphNode getSourceEdge(DefaultEdge edge) {
        return (EffortGraphNode) this.graph.getEdgeSource(edge);
    }

    public EffortGraphNode getTargetEdge(DefaultEdge edge) {
        return (EffortGraphNode) this.graph.getEdgeTarget(edge);
    }

    private void calculateEffort() {
        Set<EffortGraphNode> vertexSet = this.graph.vertexSet();
        this.numVertex = vertexSet.size();
        this.numFunction = 0;
        this.numAnnotation = 0;
        this.numBox = 0;
        this.numEdges = graph.edgeSet().size();
        double log2 = Math.log(numVertex) / Math.log(2);
        bitNode = (int) Math.ceil(log2);
        bitAnnotation = (bitNode + 8);
        int bitAnnotations = 0;
        for (EffortGraphNode effortGraphNode : vertexSet) {
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                numFunction++;
            }
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION)
                    || effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
                //logger.debug("label: " + effortGraphNode.getLabel() + " lenght " + lenghtLabel);
                //bn + asci code 8bit + label lenght
                bitAnnotations += bitAnnotation * effortGraphNode.getLabel().length();
                numAnnotation++;
            }
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_BOX)) {
                numBox++;
            }
        }
        this.bitFunction = (bitNode + toolFunctionCost);
        this.bitBox = (bitNode + toolBoxCost);
        int bitFunctions = bitFunction * numFunction;
        int bitBoxs = bitBox * numBox;
        bitEdge = (bitNode * 2);
        int bitEdges = numEdges * bitEdge;
        bitEffort = bitFunctions + bitAnnotations + bitEdges + bitBoxs;
        logger.info("Number edges: " + numEdges);
        logger.info("Number Node: " + numVertex);
        logger.info("Number Functions: " + numFunction);
        logger.info("Number Annotations: " + numAnnotation);
        logger.info("Number Boxs: " + numBox);
        logger.info("Cost Tool Function: " + this.toolFunctionCost);
        logger.info("Cost Tool Box: " + this.toolBoxCost);
        logger.info("bit for Nodes: " + bitNode);
        logger.info("bit for Function: " + bitFunction);
        logger.info("bit for Box: " + bitBox);
        logger.info("bit for Annotation: " + bitAnnotation);
        logger.info("bit for Edge: " + bitEdge);
        logger.info("Total bit effort: " + bitEffort);
    }
}
