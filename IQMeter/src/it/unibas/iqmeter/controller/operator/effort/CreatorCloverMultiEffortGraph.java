/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator.effort;

import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.persistence.effortgraph.DAOClover;
import it.unibas.ping.framework.Applicazione;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.xml.DAOXsd;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.Multigraph;

/**
 *
 * @author Antonio Genovese
 */
public class CreatorCloverMultiEffortGraph implements ICreatorEffortGraph {

    private DAOClover daoClover = new DAOClover();
    private DAOXsd daoXsd = new DAOXsd();
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    private Log logger = LogFactory.getLog(this.getClass());
    private boolean existMetadir = false;

    public UndirectedGraph<EffortGraphNode, DefaultEdge> getGraph(String scenarioPath, MappingTool tool) {
        Multigraph<EffortGraphNode, DefaultEdge> effortGraph = new Multigraph<EffortGraphNode, DefaultEdge>(DefaultEdge.class);
        try {
            Scenario sc = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
            //MappingTask mappingTask = daoMappingTask.loadMappingTask(scenarioPath); 
            IDataSourceProxy sourceSchema = daoXsd.loadSchema(sc.getSchemaSourcePath());
            addSchemaNodes(sourceSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA, effortGraph);
            IDataSourceProxy targetSchema = daoXsd.loadSchema(sc.getSchemaTargetPath());
            addSchemaNodes(targetSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_TARGET_SCHEMA, effortGraph);
            String pathMeta = createDirMeta(scenarioPath, tool);
            logger.debug("path Meta: " + pathMeta);
            daoClover.setMetaDir(pathMeta);
            daoClover.parse(scenarioPath, sc.getSchemaSourcePath(), sc.getSchemaTargetPath());
            if (daoClover.getMetaNodeMap() != null) {
                addSchemaMetaNode(effortGraph);
                if (!existMetadir) {
                    DAOClover.createDirectoryMetaClover(scenarioPath, pathMeta);
                }
            }
            String scriptAnnotation = DAOMappingExecution.loadFileScript(scenarioPath, tool).replaceAll("\n|\\s+", "");
            if (!scriptAnnotation.equals("")) {
                effortGraph.addVertex(new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT, scriptAnnotation, scriptAnnotation));
            }
            addBoxReader(effortGraph);
            addOtherBox(effortGraph);
            addBoxWriter(effortGraph);

            //add edge between schema node and metadata on the basis of the transformations specified in the box
            if (!this.daoClover.getEdgesList().isEmpty()) {
                for (String[] edge : this.daoClover.getEdgesList()) {
                    addEdgesBox(edge, effortGraph);
                }
            }
        } catch (DAOException ex) {
            logger.error("Unable to load scenario: " + ex.getLocalizedMessage());
        }
        return effortGraph;
    }

    //load and save meta file
    private String createDirMeta(String scenarioPath, MappingTool tool) {
        File dir = new File(scenarioPath.substring(0, scenarioPath.lastIndexOf(File.separator) + 1) + "meta");
        if (!dir.exists()) {
            String dirMeta = tool.getMappingFilePath().substring(0, tool.getMappingFilePath().lastIndexOf(File.separator));
            dirMeta = dirMeta.substring(0, dirMeta.lastIndexOf(File.separator) + 1) + "meta" + File.separator;
            logger.trace("directory Meta not exist: " + dirMeta);
            dir.mkdir();
            existMetadir = false;
            return dirMeta;
        } else {
            existMetadir = true;
            return dir.getAbsolutePath() + File.separator;
        }
    }

    // add vertex for schema nodes
    private void addSchemaNodes(INode schemaNode, String type, Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        if (!(schemaNode instanceof LeafNode)) {
            PathExpression path = pathGenerator.generatePathFromRoot(schemaNode);
            String nodeId = path.toString();
            logger.trace("## Adding vertex for schema node " + nodeId);
            EffortGraphNode effortNode = new EffortGraphNode(type, schemaNode.getLabel(), nodeId);
            effortGraph.addVertex(effortNode);
        }
        if (schemaNode.getChildren() != null) {
            for (INode child : schemaNode.getChildren()) {
                addSchemaNodes(child, type, effortGraph);
            }
        }
    }

    //add metadata node
    private void addSchemaMetaNode(Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        for (EffortGraphNode node : this.daoClover.getMetaNodeMap().values()) {
            effortGraph.addVertex(node);
        }
    }
    //add box XML_Extract

    private void addBoxReader(Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        if (this.daoClover.getBoxReader() != null) {
            effortGraph.addVertex(this.daoClover.getBoxReader());
            String[] array = daoClover.getBoxMapping(this.daoClover.getBoxReader().getNodeId());
            logger.trace("Array size boxExtractor: " + array.length);
            for (int i = 0; i < array.length; i++) {
                String string = array[i];
                addEdgesMappingBoxIO(string, effortGraph, this.daoClover.getBoxReader());
            }
            if (!daoClover.getReaderMappingMap().isEmpty()) {
                for (String string : daoClover.getReaderMappingMap().keySet()) {
                    addEdgesMappingReader(string, effortGraph, daoClover.getReaderMappingMap().get(string));
                }
            }
        }
    }

    private void addEdgesMappingReader(String stringS, Multigraph<EffortGraphNode, DefaultEdge> effortGraph, String stringT) {
        EffortGraphNode target = null;
        EffortGraphNode source = null;
        logger.debug("Node Source : " + stringS);
        logger.debug("Node Target Box: " + stringT);
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String id = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            logger.debug("Compare " + node.getNodeId() + " to " + stringS + " or " + stringT);
            if (id.equals(stringS)) {
                source = node;
            } else if (id.contains(stringT)) {
                target = node;
            }
        }
        if (target != null && source != null) {
            logger.debug("addEdge " + source.getNodeId() + " to " + target.getNodeId());
            effortGraph.addEdge(source, target);
        }
    }

    //add box XML_Writer
    private void addBoxWriter(Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        if (this.daoClover.getBoxWriter() != null) {
            effortGraph.addVertex(this.daoClover.getBoxWriter());
            String[] array = daoClover.getBoxMapping(this.daoClover.getBoxWriter().getNodeId());
            for (String string : array) {
                addEdgesMappingBoxIO(string, effortGraph, this.daoClover.getBoxWriter());
            }
        }
    }

    //add egdes for mapping of box XML_Extract and XML_Writer
    private void addEdgesMappingBoxIO(String string, Multigraph<EffortGraphNode, DefaultEdge> effortGraph, EffortGraphNode target) {
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            if (node.getNodeId().equals(string)) {
                logger.debug("#Clover GraphAdd edges for node " + node.getNodeId());
                effortGraph.addEdge(target, node);

            }
        }
    }

    //add others box
    private void addOtherBox(Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        logger.trace("Map box size: " + this.daoClover.getBoxList().size());
        for (EffortGraphNode node : this.daoClover.getBoxList()) {
            effortGraph.addVertex(node);
            if (this.daoClover.getBoxMapping(node.getNodeId()) != null) {
                for (String string : this.daoClover.getBoxMapping(node.getNodeId())) {
                    addEdgesBoxMapping(string, effortGraph, node);
                }
            }
        }
    }

    ///add edges between node for specific mapping of other box 
    private void addEdgesBoxMapping(String string, Multigraph<EffortGraphNode, DefaultEdge> effortGraph, EffortGraphNode target) {
        logger.trace("String for addEdgesBoxMapping: " + string);
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String id = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            if (id.equals(string)) {
                logger.debug("Add edges for node " + node.getNodeId());
                effortGraph.addEdge(node, target);
            }
        }
    }

    //add edges between box
    private void addEdgesBox(String[] edge, Multigraph<EffortGraphNode, DefaultEdge> effortGraph) {
        EffortGraphNode target = null;
        EffortGraphNode source = null;
        logger.trace("Node Source Box: " + edge[0]);
        logger.trace("Node Target Box: " + edge[1]);
        for (EffortGraphNode node : effortGraph.vertexSet()) {
            String id = node.getNodeId().replaceAll("\\.+\\w+Set\\.", ".");
            logger.debug("Compare " + node.getNodeId() + " to " + edge[0] + " or " + edge[1]);
            if (id.equals(edge[0])) {
                source = node;
            } else if (id.equals(edge[1])) {
                target = node;
            }
        }
        if (target != null && source != null) {
            effortGraph.addEdge(source, target);
        }
    }

    public int getNumSchemaSource() {
        return 0;
    }

    public int getNumSchemaTarget() {
        return 0;
    }

    public void setNumSchemaSource(int i) {
    }

    public void setNumSchemaTarget(int i) {
    }
}
