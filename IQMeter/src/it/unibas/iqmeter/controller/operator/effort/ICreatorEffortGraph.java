package it.unibas.iqmeter.controller.operator.effort;

import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.iqmeter.model.MappingTool;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;

public interface ICreatorEffortGraph {

    public UndirectedGraph<EffortGraphNode, DefaultEdge> getGraph(String scenarioPath, MappingTool tool);

    public int getNumSchemaSource();

    public int getNumSchemaTarget();

    public void setNumSchemaSource(int i);

    public void setNumSchemaTarget(int i);
}
