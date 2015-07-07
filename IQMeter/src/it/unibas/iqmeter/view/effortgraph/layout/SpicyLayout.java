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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;

/**
 *
 * @author Antonio Genovese
 */
@SuppressWarnings("unchecked")
public class SpicyLayout extends AbstractCommonLayout {

    public SpicyLayout() {
        super();
    }
    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public mxGraph createmxGraph(EffortGraph graphEffort) {
        mapVertex = new HashMap<String, Object>();
        Graph graph = graphEffort.getGraph();
        Set<EffortGraphNode> vertexSet = graph.vertexSet();
        MXEffortGraph mxgraph = new MXEffortGraph();
        double posSy = 40;
        double posTy = 40;
        double posO = 40;
        double posOx = 250;

        mxgraph.getModel().beginUpdate();

        for (EffortGraphNode effortGraphNode : vertexSet) {
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(),
                        5, posSy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + SOURCE_COLOR);//parent, id, value, x,y,w,h
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                posSy += 70;
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(),
                        700, posTy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + TARGET_COLOR);//parent, id, value, x,y,w,h
                posTy += 70;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION)
                    || effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
//                Set edgesNode = graph.edgesOf(effortGraphNode);
//                
//                System.out.println("Edges del nodo: " + effortGraphNode.getNodeId() + " = " + edgesNode.size());
//                DefaultEdge edge = null;
//                for (Object object : graph.edgeSet()) {
//                  
//                   edge = (DefaultEdge) object;
//                }
//                 mxRectangle rec = mxgraph.getCellBounds(mapVertex.get(((EffortGraphNode) graph.getEdgeSource(edge)).getNodeId()));
//                System.out.println("pos: " + rec.getX() +"-"+rec.getY());   
                /* if (edgesNode.size() == 1) {
                 posOx = -60;
                 }*/
                //Replace the path of vertex with dots 
                String label = "";
                label = super.cutLabel(effortGraphNode, graphEffort, label);
//                int factScale = (int) (label.length() * ANNOTATION_LABEL_SCALE);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx, posO, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + ANNOTATION_COLOR);
                posO += 150;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                Set edgesNode = graph.edgesOf(effortGraphNode);
                logger.debug("Edges del nodo: " + effortGraphNode.getNodeId() + " = " + edgesNode.size());
                String label = effortGraphNode.toString().replaceAll("[a-zA-Z_]+\\.{1}", "");
                label = label.replaceAll("\\w+Set_\\d+", "");
                //String label = effortGraphNode.toString();
                label = super.cutLabel(effortGraphNode, graphEffort, label);
                //label = label + "\n\n" + "cost: " + graphEffort.getBitFunction();

                // String label =  effortGraphNode.toString()+ "\n\n" + "cost: " + graphEffort.getBitFunction();
//                int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx, posO, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE_LABEL + ";fillColor=" + FUNCTON_COLOR);
                posO += 150;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            }
        }
        mxgraph.getModel().endUpdate();


        super.setVertexStyleDefault(mxgraph);
        super.setVertexStyleLabel(mxgraph);

        createEdges(mxgraph, graphEffort);

        return mxgraph;
    }

    private void setEdgesStyle(mxGraph mxgraph) {

        mxgraph.getModel().beginUpdate();
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        Map<String, Object> styleEdges = new HashMap<String, Object>();
        styleEdges.putAll(super.getStyleEdgesMap());
        //edges position relative to the vertex target and source
        styleEdges.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges.put(mxConstants.STYLE_EXIT_Y, 0);
        styleEdges.put(mxConstants.STYLE_ENTRY_X, -25);
        styleEdges.put(mxConstants.STYLE_ENTRY_Y, 0);
        stylesheet.putCellStyle(DEFAULT_EDGES_STYLE, styleEdges);
        mxgraph.getModel().endUpdate();


    }

    private void createEdges(MXEffortGraph mxgraph, EffortGraph graphEffort) {
        Graph grap = graphEffort.getGraph();
        mxgraph.getModel().beginUpdate();
        for (Object object : grap.edgeSet()) {
            DefaultEdge edge = (DefaultEdge) object;
            mxgraph.insertEdge(null, null, "cost: " + graphEffort.getBitEdges(), mapVertex.get(((EffortGraphNode) grap.getEdgeSource(edge)).getNodeId()),
                    mapVertex.get(((EffortGraphNode) grap.getEdgeTarget(edge)).getNodeId()), DEFAULT_EDGES_STYLE, "center");

        }
        mxgraph.getModel().endUpdate();

        setEdgesStyle(mxgraph);
    }
}