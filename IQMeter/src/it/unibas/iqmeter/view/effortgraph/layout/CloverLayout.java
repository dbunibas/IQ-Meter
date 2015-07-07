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
public class CloverLayout extends AbstractCommonLayout {

    private Log logger = LogFactory.getLog(this.getClass());
    private final String META_COLOR = "#CFE7E2;";
    private final String BOX_COLOR = "#e1bfff;";

    public CloverLayout() {
    }

    @Override
    public mxGraph createmxGraph(EffortGraph graphEffort) {
        Graph graph = graphEffort.getGraph();
        mapVertex = new HashMap<String, Object>();
        Set<EffortGraphNode> vertexSet = graph.vertexSet();
        MXEffortGraph mxgraph = new MXEffortGraph();
        double posSource = 250;
        double posMetay = 250;
        double posMetax = 350;
        double posTargety = 250;
        double posTargetx = 80;
        double posOy = 250;
        double posOx = 200;
        double posBx = 5;
        double posBy = 10;
        double posAy = 5;
        double posAx = 0;
        String metaId = "";
        mxgraph.getModel().beginUpdate();
        for (EffortGraphNode effortGraphNode : vertexSet) {

            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(), 5, posSource, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + SOURCE_COLOR);//parent, id, value, x,y,w,h
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                posSource += 70;

            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION)
                    || effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
                String label = super.cutLabel(effortGraphNode, graphEffort, "");
//                int factScale = (int) (label.length() * ANNOTATION_LABEL_SCALE);
                //Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx + posAx, posOy, 110 + factScale, 35 + factScale, DEFAULT_VERTEX_STYLE + ";fillColor=" + ANNOTATION_COLOR);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx + posAx, posOy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + ANNOTATION_COLOR);
                posOy += 80;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                //posAx = tooglePosition(posAx, 0, 50);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                String label = cutLabel(effortGraphNode, graphEffort, "");
                //int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                //Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx + posAx, posOy, 110 + factScale, 35 + factScale, DEFAULT_VERTEX_STYLE + ";fillColor=" + FUNCTON_COLOR);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx + posAx, posOy, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + FUNCTON_COLOR);
                posOy += 80;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                //posAx = tooglePosition(posAx, 0, 50);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_BOX)) {
                String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitBox();
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posBx, (posBy + posAy), 90, 50, DEFAULT_VERTEX_STYLE + ";fillColor=" + BOX_COLOR);
                logger.trace("vertical pos box " + effortGraphNode.getLabel() + " :" + (posBy + posAy));
                //Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, effortGraphNode.getPosition().getX(), effortGraphNode.getPosition().getY(), 100, 60, "StyleBox");
                posBx += 155;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                posAy = tooglePosition(posAy, 5, 80);

            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA)) {
                if (metaId.equals("")) {
                    metaId = effortGraphNode.getNodeId();
                    posOx += 400;
                } else if (!effortGraphNode.getNodeId().contains(metaId)) {
                    metaId = effortGraphNode.getNodeId();
                    posMetax = posMetax + 400;
                    posOx += 400;
                    posOy = posMetay;
                }
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(), posMetax, posMetay, 110, 50, DEFAULT_VERTEX_STYLE + ";fillColor=" + META_COLOR);//parent, id, value, x,y,w,h
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                posMetay += 70;

            }

        }

        for (EffortGraphNode effortGraphNode : vertexSet) {
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                double posX = 0;

                if (posBx > posOx) {
                    posX = posBx;
                } else {
                    posX = posMetax + 450;
                }

                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(), posTargetx + posX, posTargety, VERTEX_WIDTH, VERTEX_HEIGHT, DEFAULT_VERTEX_STYLE + ";fillColor=" + TARGET_COLOR);//parent, id, value, x,y,w,h
                posTargety += 70;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            }
        }
        mxgraph.getModel().endUpdate();
        super.setVertexStyleDefault(mxgraph);
        createEdges(mxgraph, mapVertex, graphEffort);
        return mxgraph;
    }

    private void createEdges(mxGraph mxgraph, Map mapVertex, EffortGraph graphEffort) {
        Graph grap = graphEffort.getGraph();
        mxgraph.getModel().beginUpdate();
        for (Object object : grap.edgeSet()) {
            DefaultEdge edge = (DefaultEdge) object;
            EffortGraphNode source = (EffortGraphNode) grap.getEdgeSource(edge);
            EffortGraphNode target = (EffortGraphNode) grap.getEdgeTarget(edge);
            int cost = graphEffort.getBitEdges();
            if (!(source.getType().equals("S") && target.getType().equals("T")) && !(source.getType().equals("T") && target.getType().equals("S"))) {
                for (DefaultEdge object1 : (Set<DefaultEdge>) grap.edgeSet()) {
                    if (!(object == object1) && grap.getEdgeSource(object1).equals(source) && grap.getEdgeTarget(object1).equals(target)) {
                        logger.info("there is a double edge");
                        cost += cost;
                    }
                }
            }
            String style = selectEdgeStyle(source, target);
            logger.trace("Style clover edges: " + source.getNodeId() + " " + style + " target " + target.getNodeId());
            mxgraph.insertEdge(null, null, "Cost:" + cost, mapVertex.get(((EffortGraphNode) grap.getEdgeSource(edge)).getNodeId()), mapVertex.get(((EffortGraphNode) grap.getEdgeTarget(edge)).getNodeId()), style);
        }
        mxgraph.getModel().endUpdate();
        setCloverStyleEdges(mxgraph);
    }

    private String selectEdgeStyle(EffortGraphNode source, EffortGraphNode target) {
        String style = "StyleEdgesClover";
        if (source.getNodeId().toUpperCase().contains("WRITER")
                || (source.getNodeId().toUpperCase().contains("EXTRACT") && !target.getType().equals(EffortGraphNode.TYPE_NODE_BOX))) {
            //|| (source.getType().equals(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA) && target.getType().equals(EffortGraphNode.TYPE_NODE_BOX)) ){
            style = "StyleEdgesWriter";
        } else if (target.getType().equals(EffortGraphNode.TYPE_NODE_BOX)
                && (source.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA) || source.getType().equals(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA))) {
            style = "StyleEdgesBox";
        } else if ((source.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA) || source.getType().equals(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA))
                && target.getType().equals(EffortGraphNode.TYPE_NODE_BOX)) {
            style = "StyleEdgesBoxTarget";
        } else if (source.getType().equals(target.getType()) && source.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA) || source.getType().equals(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA)) {
            style = "StyleSchema";
        } else if (source.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION) && target.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
            style = "StyleEdgesAnnotationFunction";

        } else if (source.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA) && (target.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION) || target.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION))) {
            style = "StyleEdgesAnnotationTarget";
        }
        return style;

    }

    private void setCloverStyleEdges(mxGraph mxgraph) {

        //create Style Edges
        mxgraph.getModel().beginUpdate();
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        Map<String, Object> styleEdges = new HashMap<String, Object>();
        styleEdges.putAll(super.getStyleEdgesMap());

        styleEdges.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges.put(mxConstants.STYLE_EXIT_Y, 0);
        styleEdges.put(mxConstants.STYLE_ENTRY_X, -25);
        styleEdges.put(mxConstants.STYLE_ENTRY_Y, 0);
        stylesheet.putCellStyle("StyleEdgesClover", styleEdges);

        mxStylesheet stylesheet2 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges2 = new HashMap<String, Object>();
        styleEdges2.putAll(super.getStyleEdgesMap());
        styleEdges2.put(mxConstants.ELBOW_VERTICAL, "vertical");
        styleEdges2.put(mxConstants.STYLE_DASHED, "1");
        styleEdges2.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.DIRECTION_NORTH);
        styleEdges2.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        styleEdges2.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);

        styleEdges2.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges2.put(mxConstants.STYLE_EXIT_Y, 10);
        styleEdges2.put(mxConstants.STYLE_ENTRY_X, 0.5);
        styleEdges2.put(mxConstants.STYLE_ENTRY_Y, 1);
        stylesheet2.putCellStyle("StyleEdgesBox", styleEdges2);


        mxStylesheet stylesheet3 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges3 = new HashMap<String, Object>();
        styleEdges3.putAll(super.getStyleEdgesMap());
        //define position if edges, start and end point
        styleEdges3.put(mxConstants.STYLE_EXIT_X, 0.5);
        styleEdges3.put(mxConstants.STYLE_EXIT_Y, 1);
        styleEdges3.put(mxConstants.STYLE_ENTRY_X, 0.5);
        styleEdges3.put(mxConstants.STYLE_ENTRY_Y, 0);
        styleEdges3.put(mxConstants.STYLE_DASHED, "1");
        stylesheet3.putCellStyle("StyleEdgesWriter", styleEdges3);

        mxStylesheet stylesheet4 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges4 = new HashMap<String, Object>();
        styleEdges4.putAll(super.getStyleEdgesMap());
        styleEdges4.put(mxConstants.STYLE_EXIT_X, -0.5);
        styleEdges4.put(mxConstants.STYLE_EXIT_Y, 0.5);
        styleEdges4.put(mxConstants.STYLE_ENTRY_X, 0.5);
        styleEdges4.put(mxConstants.STYLE_ENTRY_Y, 1);
        styleEdges4.put(mxConstants.STYLE_DASHED, "1");
        stylesheet4.putCellStyle("StyleEdgesBoxTarget", styleEdges4);

        mxStylesheet stylesheet5 = mxgraph.getStylesheet();
        Map<String, Object> styleSchema = new HashMap<String, Object>();
        styleSchema.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
        styleSchema.putAll(super.getStyleEdgesMap());
        styleSchema.put(mxConstants.STYLE_DASHED, "0");
        stylesheet5.putCellStyle("StyleSchema", styleSchema);

        mxStylesheet stylesheet6 = mxgraph.getStylesheet();
        Map<String, Object> style5 = new HashMap<String, Object>();
        style5.putAll(super.getStyleEdgesMap());
        style5.put(mxConstants.STYLE_DASHED, "0");
        stylesheet6.putCellStyle("StyleEdgesAnnotationFunction", style5);

        mxStylesheet stylesheet7 = mxgraph.getStylesheet();
        Map<String, Object> styleEdges6 = new HashMap<String, Object>();
        styleEdges6.putAll(super.getStyleEdgesMap());
        styleEdges6.put(mxConstants.STYLE_EXIT_X, 0);
        styleEdges6.put(mxConstants.STYLE_EXIT_Y, 0.5);
        styleEdges6.put(mxConstants.STYLE_ENTRY_X, 1);
        styleEdges6.put(mxConstants.STYLE_ENTRY_Y, 0.5);
        stylesheet7.putCellStyle("StyleEdgesAnnotationTarget", styleEdges6);

        mxgraph.getModel().endUpdate();
    }
}
