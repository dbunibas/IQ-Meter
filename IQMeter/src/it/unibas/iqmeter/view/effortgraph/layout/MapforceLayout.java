/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.effortgraph.layout;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import it.unibas.iqmeter.model.EffortGraph;
import it.unibas.iqmeter.model.EffortGraphNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;

/**
 *
 * @author francescodefino
 */
@SuppressWarnings("unchecked")
public class MapforceLayout extends AbstractCommonLayout {

    public MapforceLayout() {
        super();
    }
    private Log logger = LogFactory.getLog(this.getClass());
    private List<String[]> listVertexPosition;
    private List<String[]> listFunctionPosition;
    private final double SCHEMA_DISTANCE = 500;
    private final double FUNCTION_DISTANCE = 320;
    private final double ANNOTATION_DISTANCE = 180;

    @Override
    public mxGraph createmxGraph(EffortGraph graphEffort) {
        mapVertex = new HashMap<String, Object>();
        listVertexPosition = new ArrayList<String[]>();
        listFunctionPosition = new ArrayList<String[]>();
        Graph graph = graphEffort.getGraph();
        Set<EffortGraphNode> vertexSet = graph.vertexSet();
        MXEffortGraph mxgraph = new MXEffortGraph();

        double posSy = 40;
        double posTy = 40;
        double posX = 5;

        mxgraph.getModel().beginUpdate();

        //Inserimento nodi source

        for (int i = 1; i < graphEffort.getNumSource(); i++) {
            String[] infoVertex;
            posSy = 40;
            for (EffortGraphNode effortGraphNode : vertexSet) {
                infoVertex = new String[4];
                if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA) && effortGraphNode.getPosition().equals(Integer.toString(i))) {
                    Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId() + effortGraphNode.getPosition(), effortGraphNode.toString(),
                            posX, posSy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + SOURCE_COLOR);//parent, id, value, x,y,w,h
                    infoVertex[0] = effortGraphNode.getNodeId();
                    infoVertex[1] = effortGraphNode.getPosition();
                    infoVertex[2] = Double.toString(posX);
                    infoVertex[3] = Double.toString(posSy);
                    listVertexPosition.add(infoVertex);
                    mapVertex.put(effortGraphNode.getNodeId() + effortGraphNode.getPosition(), ob);
                    posSy += 70;
                }
            }
            posX += SCHEMA_DISTANCE;
        }

        //Inserimento nodi target

        for (int i = 1; i < graphEffort.getNumTarget(); i++) {
            String[] infoVertex;
            posTy = 40;
            for (EffortGraphNode effortGraphNode : vertexSet) {
                infoVertex = new String[4];
                if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA) && effortGraphNode.getPosition().equals(Integer.toString(i))) {
                    Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId() + effortGraphNode.getPosition(), effortGraphNode.toString(),
                            posX, posTy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + TARGET_COLOR);//parent, id, value, x,y,w,h
                    infoVertex[0] = effortGraphNode.getNodeId();
                    infoVertex[1] = effortGraphNode.getPosition();
                    infoVertex[2] = Double.toString(posX);
                    infoVertex[3] = Double.toString(posTy);
                    //logger.info("Adding target "+ effortGraphNode.getNodeId() + " n." + effortGraphNode.getPosition() + "\nPosition: " + posX);
                    listVertexPosition.add(infoVertex);
                    mapVertex.put(effortGraphNode.getNodeId() + effortGraphNode.getPosition(), ob);
                    posTy += 70;
                }
            }
            posX += SCHEMA_DISTANCE;
        }

        //Inserimento nodi funzioni separando source e target

        for (EffortGraphNode effortGraphNode : vertexSet) {
            String[] infoFunction;
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                Set<DefaultEdge> edgesNode = graph.edgesOf(effortGraphNode);
                for (DefaultEdge edge : edgesNode) {
                    infoFunction = new String[3];
                    EffortGraphNode nodeSource = graphEffort.getSourceEdge(edge);
                    if (nodeSource.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                        String[] infoVertex = searchVertex(nodeSource.getNodeId(), nodeSource.getPosition());
                        if (infoVertex != null) {
                            Double posSourceX = Double.valueOf(infoVertex[2]);
                            Double posSourceY = Double.valueOf(infoVertex[3]);
                            posSourceX = posSourceX + FUNCTION_DISTANCE;
                            //nuovo
                            Boolean bool = verifyPositionFunction(posSourceX, posSourceY);
                            while (bool) {
                                posSourceY += 70;
                                bool = verifyPositionFunction(posSourceX, posSourceY);
                            }
                            //
//                            String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitFunction();
                            String label = "";
                            //String label = effortGraphNode.toString();
                            label = super.cutLabel(effortGraphNode, graphEffort, label);
//                            int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                            if (!isInMap(effortGraphNode.getNodeId())) {
                                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY,
                                        VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + FUNCTON_COLOR);
                                infoFunction[0] = effortGraphNode.getNodeId();
                                infoFunction[1] = Double.toString(posSourceX);
                                infoFunction[2] = Double.toString(posSourceY);
                                listFunctionPosition.add(infoFunction);
                                mapVertex.put(effortGraphNode.getNodeId(), ob);
                            }
                        }
                    } else if (nodeSource.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                        String[] infoVertex = searchVertex(nodeSource.getNodeId(), nodeSource.getPosition());
                        if (infoVertex != null) {
                            Double posSourceX = Double.valueOf(infoVertex[2]);
                            Double posSourceY = Double.valueOf(infoVertex[3]);
                            //nuovo
                            posSourceX = posSourceX + FUNCTION_DISTANCE;
                            Boolean bool = verifyPositionFunction(posSourceX, posSourceY);
                            while (bool) {
                                posSourceY += 70;
                                bool = verifyPositionFunction(posSourceX, posSourceY);
                            }
                            //
//                            String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitFunction();
                            String label = "";
                            //String label = effortGraphNode.toString();
                            label = super.cutLabel(effortGraphNode, graphEffort, label);
//                            int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                            if (!isInMap(effortGraphNode.getNodeId())) {
                                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY,
                                        VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + FUNCTON_COLOR);
                                infoFunction[0] = effortGraphNode.getNodeId();
                                infoFunction[1] = Double.toString(posSourceX);
                                infoFunction[2] = Double.toString(posSourceY);
                                listFunctionPosition.add(infoFunction);
                                mapVertex.put(effortGraphNode.getNodeId(), ob);
                            }
                        }
                    }
                }
            }
        }

        //Inserimento funzioni collegate a costante e a target

        for (EffortGraphNode effortGraphNode : vertexSet) {
            String[] infoFunction;
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                Set<DefaultEdge> edgesNode = graph.edgesOf(effortGraphNode);
                for (DefaultEdge edge : edgesNode) {
                    infoFunction = new String[3];
                    EffortGraphNode nodeSource = graphEffort.getTargetEdge(edge);
                    String[] infoVertex = searchVertex(nodeSource.getNodeId(), nodeSource.getPosition());
                    if (infoVertex != null) {
                        Double posSourceX = Double.valueOf(infoVertex[2]);
                        Double posSourceY = Double.valueOf(infoVertex[3]);
                        //nuovo
                        posSourceX = posSourceX - ANNOTATION_DISTANCE;
                        Boolean bool = verifyPositionFunction(posSourceX, posSourceY);
                        while (bool) {
                            posSourceY += 70;
                            bool = verifyPositionFunction(posSourceX, posSourceY);
                        }
                        //
//                        String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitFunction();
                        String label = "";
                        //String label = effortGraphNode.toString();
                        label = super.cutLabel(effortGraphNode, graphEffort, label);
//                        int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                        if (!isInMap(effortGraphNode.getNodeId())) {
                            Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY,
                                    VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + FUNCTON_COLOR);
                            infoFunction[0] = effortGraphNode.getNodeId();
                            infoFunction[1] = Double.toString(posSourceX);
                            infoFunction[2] = Double.toString(posSourceY);
                            listFunctionPosition.add(infoFunction);
                            mapVertex.put(effortGraphNode.getNodeId(), ob);
                        }
                    }
                }
            }
        }

        //Inserimento funzioni collegate a funzioni

        for (EffortGraphNode effortGraphNode : vertexSet) {
            String[] infoFunction;
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                Set<DefaultEdge> edgesNode = graph.edgesOf(effortGraphNode);
                for (DefaultEdge edge : edgesNode) {
                    infoFunction = new String[3];
                    EffortGraphNode nodeSource = graphEffort.getSourceEdge(edge);
                    if (nodeSource.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                        String[] infoVertex = searchFunction(nodeSource.getNodeId());
                        if (infoVertex != null) {
                            Double posSourceX = Double.valueOf(infoVertex[1]);
                            Double posSourceY = Double.valueOf(infoVertex[2]);
                            Boolean bool = verifyPositionFunction(posSourceX, posSourceY);
                            while (bool) {
                                posSourceY += 70;
                                bool = verifyPositionFunction(posSourceX, posSourceY);
                            }
//                            String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitFunction();
//                            int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                            String label = "";
                            //String label = effortGraphNode.toString();
                            label = super.cutLabel(effortGraphNode, graphEffort, label);
                            if (!isInMap(effortGraphNode.getNodeId())) {
                                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY,
                                        VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + FUNCTON_COLOR);
                                infoFunction[0] = effortGraphNode.getNodeId();
                                infoFunction[1] = Double.toString(posSourceX);
                                infoFunction[2] = Double.toString(posSourceY);
                                listFunctionPosition.add(infoFunction);
                                mapVertex.put(effortGraphNode.getNodeId(), ob);
                            }
                        }
                    }
                }
            }
        }

        //Inserimento annotazioni

        for (EffortGraphNode effortGraphNode : vertexSet) {
            String[] infoAnnotation;
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION)) {
                Set<DefaultEdge> edgesNode = graph.edgesOf(effortGraphNode);
                for (DefaultEdge edge : edgesNode) {
                    infoAnnotation = new String[3];
                    EffortGraphNode nodeTarget = graphEffort.getTargetEdge(edge);
//                    logger.info(nodeTarget.getLabel());
                    if (nodeTarget.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                        String[] infoFunction = searchFunction(nodeTarget.getNodeId());
                        if (infoFunction != null) {
//                            logger.info(infoFunction[0]);
                            Double posSourceX = Double.valueOf(infoFunction[1]);
                            Double posSourceY = Double.valueOf(infoFunction[2]);
                            posSourceX = posSourceX - ANNOTATION_DISTANCE;
                            //nuovo
                            Boolean bool = verifyPositionFunction(posSourceX, posSourceY);
                            while (bool) {
                                posSourceY += 70;
                                bool = verifyPositionFunction(posSourceX, posSourceY);
                            }
                            //
//                            String label = effortGraphNode.toString() + "\n\n"
//                                    + "cost: " + (graphEffort.getBitAnnotation() * effortGraphNode.getLabel().length());
//                            int factScale = (int) (label.length() * ANNOTATION_LABEL_SCALE);
                            String label = "";
                            //String label = effortGraphNode.toString();
                            label = super.cutLabel(effortGraphNode, graphEffort, label);
                            if (!isInMap(effortGraphNode.getNodeId())) {
                                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY + 35,
                                        VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + ANNOTATION_COLOR);
                                infoAnnotation[0] = effortGraphNode.getNodeId();
                                infoAnnotation[1] = Double.toString(posSourceX);
                                infoAnnotation[2] = Double.toString(posSourceY + 35);
                                listFunctionPosition.add(infoAnnotation);
                                mapVertex.put(effortGraphNode.getNodeId(), ob);
                            }
                        }
                    } else if (nodeTarget.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                        String[] infoVertex = searchVertex(nodeTarget.getNodeId(), nodeTarget.getPosition());
                        if (infoVertex != null) {
                            Double posSourceX = Double.valueOf(infoVertex[2]);
                            Double posSourceY = Double.valueOf(infoVertex[3]);
                            //nuovo
                            posSourceX = posSourceX - 2 * ANNOTATION_DISTANCE;
//                            String label = effortGraphNode.toString() + "\n\n"
//                                    + "cost: " + (graphEffort.getBitAnnotation() * effortGraphNode.getLabel().length());
//                            int factScale = (int) (label.length() * ANNOTATION_LABEL_SCALE);
                            String label = "";
                            //String label = effortGraphNode.toString();
                            label = super.cutLabel(effortGraphNode, graphEffort, label);
                            if (!isInMap(effortGraphNode.getNodeId())) {
                                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posSourceX, posSourceY + 35,
                                        VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + ANNOTATION_COLOR);
                                infoAnnotation[0] = effortGraphNode.getNodeId();
                                infoAnnotation[1] = Double.toString(posSourceX);
                                infoAnnotation[2] = Double.toString(posSourceY + 35);
                                listFunctionPosition.add(infoAnnotation);
                                mapVertex.put(effortGraphNode.getNodeId(), ob);
                            }
                        }
                    }
                }
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
                String label = "";
                label = super.cutLabel(effortGraphNode, graphEffort, label);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, ANNOTATION_DISTANCE, 40, VERTEX_WIDTH, VERTEX_HEIGHT,
                        DEFAULT_VERTEX_STYLE + ";fillColor=" + ANNOTATION_COLOR);
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            }
        }

        mxgraph.getModel().endUpdate();


        super.setVertexStyleDefault(mxgraph);
        super.setVertexStyleLabel(mxgraph);

        createEdges(mxgraph, graphEffort);
        return mxgraph;
    }

    

    //Modificare stile edge tra funzioni --> vedi clover
    
    @SuppressWarnings("unchecked")
    private void createEdges(MXEffortGraph mxgraph, EffortGraph graphEffort) {
        Graph graph = graphEffort.getGraph();
        mxgraph.getModel().beginUpdate();
        for (Object object : graph.edgeSet()) {
            DefaultEdge edge = (DefaultEdge) object;
            EffortGraphNode source = (EffortGraphNode) graph.getEdgeSource(edge);
            EffortGraphNode target = (EffortGraphNode) graph.getEdgeTarget(edge);
            int cost = graphEffort.getBitEdges();
            if (source.getPosition() != null && target.getPosition() != null) {
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId() + source.getPosition()), mapVertex.get(target.getNodeId() + target.getPosition()), "StyleEdgeMapforce", "center");
            } else if (source.getPosition() == null && target.getPosition() != null) {
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId()), mapVertex.get(target.getNodeId() + target.getPosition()), "StyleEdgeMapforce", "center");
            } else if (source.getPosition() != null && target.getPosition() == null) {
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId() + source.getPosition()), mapVertex.get(target.getNodeId()), "StyleEdgeMapforce", "center");
            } else if(source.getType().equals("F") && target.getType().equals("F")) {
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId()), mapVertex.get(target.getNodeId()),"StyleFunctionFunction" , "center");
            } else if (source.getType().equals("A") || target.getType().equals("A")){
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId()), mapVertex.get(target.getNodeId()), "StyleConstant", "center");
            } else {
                mxgraph.insertEdge(null, null, "cost: " + cost, mapVertex.get(source.getNodeId()), mapVertex.get(target.getNodeId()), "StyleEdgeMapforce", "center");
            }
        }
        mxgraph.getModel().endUpdate();
        setEdgesStyle(mxgraph);
    }
    
    private void setEdgesStyle(mxGraph mxgraph) {

        mxgraph.getModel().beginUpdate();
        
        Map<String, Object> styleEdges = new HashMap<String, Object>();
        styleEdges.putAll(super.getStyleEdgesMap());
        
        mxStylesheet stylesheet1 = mxgraph.getStylesheet();
        styleEdges.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges.put(mxConstants.STYLE_EXIT_Y, 0);
        styleEdges.put(mxConstants.STYLE_ENTRY_X, -25);
        styleEdges.put(mxConstants.STYLE_ENTRY_Y, 0);
        stylesheet1.putCellStyle("StyleEdgeMapforce", styleEdges);
        
        mxStylesheet stylesheet2 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges2 = new HashMap<String, Object>();
        styleEdges2.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
        styleEdges2.putAll(super.getStyleEdgesMap());
        styleEdges2.put(mxConstants.STYLE_DASHED, "2");
        stylesheet2.putCellStyle("StyleFunctionFunction", styleEdges2);
        
        mxStylesheet stylesheet3 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges3 = new HashMap<String, Object>();
        styleEdges3.putAll(super.getStyleEdgesMap());
        styleEdges3.put(mxConstants.ELBOW_VERTICAL, "vertical");
        styleEdges3.put(mxConstants.STYLE_DASHED, "1");
        styleEdges3.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.DIRECTION_NORTH);
        styleEdges3.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        styleEdges3.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);
        styleEdges3.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges3.put(mxConstants.STYLE_EXIT_Y, 10);
        styleEdges3.put(mxConstants.STYLE_ENTRY_X, 0.5);
        styleEdges3.put(mxConstants.STYLE_ENTRY_Y, 1);
        stylesheet3.putCellStyle("StyleConstant", styleEdges3);
        
        
        mxgraph.getModel().endUpdate();
    }

    private String[] searchVertex(String nodeId, String position) {
        for (String[] infoVertex : listVertexPosition) {
            if (position != null) {
                if (nodeId.equals(infoVertex[0]) && position.equals(infoVertex[1])) {
                    return infoVertex;
                }
            }
        }
        return null;
    }

    private String[] searchFunction(String nodeId) {
        for (String[] infoFunction : listFunctionPosition) {
            if (nodeId.equals(infoFunction[0])) {
                return infoFunction;
            }
        }
        return null;
    }

    private Boolean isInMap(String name) {
        if (mapVertex.get(name) != null) {
            return true;
        }
        return false;
    }

    private Boolean verifyPositionFunction(double positionX, double positionY) {
        for (String[] function : listFunctionPosition) {
            if (Double.toString(positionX).equals(function[1])) {
                if (Double.toString(positionY).equals(function[2])) {
                    return true;
                }
            }
        }
        return false;
    }
}