/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator.effort;

import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.persistence.effortgraph.DAOMapforce;
import it.unibas.ping.framework.Applicazione;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.xml.DAOXsd;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author francescodefino
 */
@SuppressWarnings("unchecked")
public class CreatorMapforceEffortGraph implements ICreatorEffortGraph {

    private DAOXsd daoXsd = new DAOXsd();
    private Log logger = LogFactory.getLog(this.getClass());
    private GeneratePathExpression pathGenerator;
    private int countSource;
    private int countTarget;
    private List<EffortGraphNode> nodeListFunction;
    private List<EffortGraphNode> nodeListSource;
    private List<EffortGraphNode> nodeListTarget;
    private List<EffortGraphNode> nodeListConstant;
    private DAOMapforce daoMapforce = new DAOMapforce();

    @Override
    public UndirectedGraph<EffortGraphNode, DefaultEdge> getGraph(String scenarioPath, MappingTool tool) {
        clearCollection();
        UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph = new SimpleGraph<EffortGraphNode, DefaultEdge>(DefaultEdge.class);

        try {
            Scenario sc = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
            logger.debug(sc.getSchemaSourcePath());
            daoMapforce.parse(scenarioPath, sc.getSchemaSourcePath().substring(sc.getSchemaSourcePath().lastIndexOf("/") + 1).replaceAll(".xsd", ""), sc.getSchemaTargetPath().substring(sc.getSchemaTargetPath().lastIndexOf("/") + 1).replaceAll(".xsd", ""));
            logger.debug("countSource: " + daoMapforce.getCountSource() + " - target: " + daoMapforce.getCountTarget());
            IDataSourceProxy sourceSchema = daoXsd.loadSchema(sc.getSchemaSourcePath());
            IDataSourceProxy targetSchema = daoXsd.loadSchema(sc.getSchemaTargetPath());

            countSource = daoMapforce.getCountSource();
            countTarget = daoMapforce.getCountTarget();
            addNodes(sourceSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA, effortGraph,
                    countSource, nodeListSource, daoMapforce);
            addNodes(targetSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_TARGET_SCHEMA, effortGraph,
                    countTarget, nodeListTarget, daoMapforce);
            String scriptAnnotation = DAOMappingExecution.loadFileScript(scenarioPath, tool).replaceAll("\n|\\s+", "");
            if (!scriptAnnotation.equals("")) {
                effortGraph.addVertex(new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT, scriptAnnotation, scriptAnnotation));
            }
            addFunctionNodes(daoMapforce, effortGraph);
            addAnnotation(daoMapforce, effortGraph);
            addEdgesToGraph(effortGraph, daoMapforce);
            setPositionSchemas(effortGraph, daoMapforce);
        } catch (DAOException daoe) {
            logger.error(daoe);
        }
        return effortGraph;
    }

    private void clearCollection() {
        pathGenerator = new GeneratePathExpression();
        nodeListFunction = new ArrayList<EffortGraphNode>();
        nodeListSource = new ArrayList<EffortGraphNode>();
        nodeListTarget = new ArrayList<EffortGraphNode>();
        nodeListConstant = new ArrayList<EffortGraphNode>();
        countSource = 0;
        countTarget = 0;
    }

    private void addNodes(INode schemaNode, String type, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph, int count,
            List<EffortGraphNode> nodeList, DAOMapforce daoMapforce) {
        for (int i = 1; i < count; i++) {
            addSchemaNodes(schemaNode, type, effortGraph, i, nodeList, daoMapforce);
        }
    }

    private void addSchemaNodes(INode schemaNode, String type, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph, int position,
            List<EffortGraphNode> nodeList, DAOMapforce daoMapforce) {
        if (!(schemaNode instanceof LeafNode)) {
            PathExpression path = pathGenerator.generatePathFromRoot(schemaNode);
            String nodeId = path.toString();
            List<String> list = daoMapforce.getListPosSchema(type);
            logger.debug("## Adding value for schema node " + nodeId + position);
            EffortGraphNode effortNode = new EffortGraphNode(type, schemaNode.getLabel(), nodeId, list.get(position - 1));
            effortGraph.addVertex(effortNode);
            nodeList.add(effortNode);
        }
        if (schemaNode.getChildren() != null) {
            for (INode child : schemaNode.getChildren()) {
                addSchemaNodes(child, type, effortGraph, position, nodeList, daoMapforce);
            }
        }
    }

    private void addFunctionNodes(DAOMapforce daoMapforce, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (String uid : daoMapforce.getFunctionUidList()) {
            String functionName = daoMapforce.getFunctionName(uid);
            logger.debug(functionName);
            EffortGraphNode effortFunctionNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, functionName, uid);

            if (functionName.equals("constant")) {
                nodeListConstant.add(effortFunctionNode);
            } else {
                effortGraph.addVertex(effortFunctionNode);
                nodeListFunction.add(effortFunctionNode);
            }
        }
    }

    private void addAnnotation(DAOMapforce daoMapforce, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode nodeConstant : nodeListConstant) {
            addAnnotationNodes(daoMapforce, effortGraph, nodeConstant);
        }
    }

    private void addAnnotationNodes(DAOMapforce daoMapforce, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph,
            EffortGraphNode constantNode) {

        String uid = constantNode.getNodeId();
        String annotationValue = daoMapforce.getAnnotationValue(uid);
        if (annotationValue != null) {
            EffortGraphNode effortAnnotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotationValue, uid);
            effortGraph.addVertex(effortAnnotationNode);
//            logger.debug("Adding annotation" + annotationValue + ":" + uid);
//            for (String key[] : daoMapforce.getVertexLinkList()) {
//                for (EffortGraphNode nodeFunction : nodeListFunction) {
//                    String uidSource = daoMapforce.getVertexName(key[0]);
//                    String uidTarget = daoMapforce.getVertexName(key[1]);
//                    String nodeName = nodeFunction.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
//                    if (uid.equals(uidSource) && nodeName.equals(uidTarget)) {
//                        effortGraph.addEdge(effortAnnotationNode, nodeFunction);
//                    }
//                }
//            }
        }
    }

    private void addEdgesToGraph(UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph, DAOMapforce daoMapforce) {

        for (String[] link : daoMapforce.getVertexLinkList()) {
            EffortGraphNode nodeSource;
            EffortGraphNode nodeTarget;
            String vertexSource = daoMapforce.getVertexName(link[0]);
            String vertexTarget = daoMapforce.getVertexName(link[1]);

            if (!isFunction(vertexSource, effortGraph) && !isFunction(vertexTarget, effortGraph)
                    && !isAnnotation(vertexSource, effortGraph) && !isAnnotation(vertexTarget, effortGraph)) {
                String[] source = daoMapforce.getVertexInList(link[0]);
                String[] target = daoMapforce.getVertexInList(link[1]);
                nodeSource = searchNode(source[1], source[4], effortGraph);
                nodeTarget = searchNode(target[1], target[4], effortGraph);
                effortGraph.addEdge(nodeSource, nodeTarget);
            } else if (isFunction(vertexSource, effortGraph) && !isFunction(vertexTarget, effortGraph)
                    && !isAnnotation(vertexTarget, effortGraph)) {
                String[] target = daoMapforce.getVertexInList(link[1]);
                nodeSource = searchNode(vertexSource, effortGraph);
                nodeTarget = searchNode(target[1], target[4], effortGraph);
                effortGraph.addEdge(nodeSource, nodeTarget);
            } else if (!isFunction(vertexSource, effortGraph) && isFunction(vertexTarget, effortGraph)
                    && !isAnnotation(vertexSource, effortGraph)) {
                String[] source = daoMapforce.getVertexInList(link[0]);
                if (source != null) {
                    nodeSource = searchNode(source[1], source[4], effortGraph);
                    nodeTarget = searchNode(vertexTarget, effortGraph);
                    effortGraph.addEdge(nodeSource, nodeTarget);
                }
            } else if (isFunction(vertexSource, effortGraph) && isFunction(vertexTarget, effortGraph)) {
                nodeSource = searchNode(vertexSource, effortGraph);
                nodeTarget = searchNode(vertexTarget, effortGraph);
                effortGraph.addEdge(nodeSource, nodeTarget);
            } else if (isAnnotation(vertexSource, effortGraph) && !isAnnotation(vertexTarget, effortGraph)
                    && !isFunction(vertexTarget, effortGraph)) {
                String[] target = daoMapforce.getVertexInList(link[1]);
                nodeSource = searchNode(vertexSource, effortGraph);
                nodeTarget = searchNode(target[1], target[4], effortGraph);
                effortGraph.addEdge(nodeSource, nodeTarget);
            } else if (isAnnotation(vertexSource, effortGraph) && isFunction(vertexTarget, effortGraph)) {
                nodeSource = searchNode(vertexSource, effortGraph);
                nodeTarget = searchNode(vertexTarget, effortGraph);
                logger.trace(nodeTarget.getLabel());
                effortGraph.addEdge(nodeSource, nodeTarget);
            }
        }
    }

    private EffortGraphNode searchNode(String name, String position, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String nodeName = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            if (node.getPosition() != null) {
                if (name.equals(nodeName) && position.equals(node.getPosition())) {
                    logger.trace(node.getNodeId() + node.getPosition());
                    return node;
                }
            }
        }
        return null;
    }

    private EffortGraphNode searchNode(String name, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            logger.trace(name);
            String nodeName = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            if (name.equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    private Boolean isFunction(String name, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String nodeName = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            logger.trace(nodeName);
            if (name.equals(nodeName)) {
                if ((node.getType()).equals("F")) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean isAnnotation(String name, UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String nodeName = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            if (name.equals(nodeName)) {
                logger.trace(name + "=" + nodeName);
                if ((node.getType()).equals("A")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setPositionSchemas(UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph, DAOMapforce daoMapforce) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            if (!isFunction(node.getNodeId(), effortGraph)) {
                if (node.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                    int position = daoMapforce.getPosition(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA, node.getPosition());
                    if (position != -1) {
                        node.setPosition(Integer.toString(position + 1));
                    }
                } else if (node.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                    int position = daoMapforce.getPosition(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA, node.getPosition());
                    if (position != -1) {
                        node.setPosition(Integer.toString(position + 1));
                    }
                }
            }
        }
    }

    public int getNumSchemaSource() {
        return this.countSource;
    }

    public int getNumSchemaTarget() {
        return this.countTarget;
    }

    public void setNumSchemaSource(int i) {
        this.countSource = i;
    }

    public void setNumSchemaTarget(int i) {
        this.countTarget = i;
    }
}
