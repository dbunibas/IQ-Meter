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
public class DefaultEffortGraphLayout implements IEffortGraphLayout {

    private Map<String, Object> mapVertex;
    private Log logger = LogFactory.getLog(this.getClass());
    private final double ANNOTATION_LABEL_SCALE = 0.7;
    private final double FUNCTION_LABEL_SCALE = 0.7;

    public mxGraph createmxGraph(EffortGraph graphEffort) {
        mapVertex = new HashMap<String, Object>();
        Graph graph = graphEffort.getGraph();
        Set<EffortGraphNode> vertexSet = graph.vertexSet();
        MXEffortGraph mxgraph = new MXEffortGraph();
        double posS = 40;
        double posT = 40;
        double posO = 40;
        double posOx = 250;

        mxgraph.getModel().beginUpdate();

        for (EffortGraphNode effortGraphNode : vertexSet) {
            if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(),
                        5, posS, 120, 50, "StyleSource;fillColor=#ff9103;");//parent, id, value, x,y,w,h
                mapVertex.put(effortGraphNode.getNodeId(), ob);
                posS += 70;
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_TARGET_SCHEMA)) {
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), effortGraphNode.toString(),
                        700, posT, 120, 50, "StyleTarget");//parent, id, value, x,y,w,h
                posT += 70;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION)
                    || effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
                String label = effortGraphNode.toString();
                if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT) && label.length() > 20) {
                    label = label.substring(0, 15) + " ..." + "\n\n"
                            + "cost: " + (graphEffort.getBitAnnotation() * effortGraphNode.getLabel().length());
                } else {
                    label = label + "\n\n"
                            + "cost: " + (graphEffort.getBitAnnotation() * effortGraphNode.getLabel().length());
                }
                int factScale = (int) (label.length() * ANNOTATION_LABEL_SCALE);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx, posO, 120 + factScale, 35 + factScale, "StyleAnnotation");
                posO += 150;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            } else if (effortGraphNode.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
                String label = effortGraphNode.toString() + "\n\n" + "cost: " + graphEffort.getBitFunction();
                int factScale = (int) (label.length() * FUNCTION_LABEL_SCALE);
                Object ob = mxgraph.insertVertex(null, effortGraphNode.getNodeId(), label, posOx, posO, 120 + factScale, 35 + factScale, "StyleFunction");
                posO += 150;
                mapVertex.put(effortGraphNode.getNodeId(), ob);
            }
        }
        mxgraph.getModel().endUpdate();
        setVertexStyle(mxgraph);

        createEdges(mxgraph, graphEffort);

        return mxgraph;
    }

    private void createEdges(MXEffortGraph mxgraph, EffortGraph graphEffort) {
        Graph grap = graphEffort.getGraph();
        mxgraph.getModel().beginUpdate();
        for (Object object : grap.edgeSet()) {
            DefaultEdge edge = (DefaultEdge) object;
            mxgraph.insertEdge(null, null, "cost: " + graphEffort.getBitEdges(), mapVertex.get(((EffortGraphNode) grap.getEdgeSource(edge)).getNodeId()),
                    mapVertex.get(((EffortGraphNode) grap.getEdgeTarget(edge)).getNodeId()), "StyleEdges", "center");
        }

        mxgraph.getModel().endUpdate();

        //create Style Edges
        mxgraph.getModel().beginUpdate();
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        Map<String, Object> styleEdges = new HashMap<String, Object>();
        styleEdges.put(mxConstants.STYLE_ENDARROW, "oval");
        styleEdges.put(mxConstants.STYLE_STARTARROW, "oval");
        styleEdges.put(mxConstants.STYLE_STROKECOLOR, "#003366");
        styleEdges.put(mxConstants.STYLE_STROKEWIDTH, 1);
        styleEdges.put(mxConstants.STYLE_OPACITY, 60);
        styleEdges.put(mxConstants.EDGESTYLE_ELBOW, "horizontal");
        styleEdges.put(mxConstants.STYLE_ROUNDED, 40);
        styleEdges.put(mxConstants.STYLE_FONTSIZE, 12);
        styleEdges.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        styleEdges.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        //edges position
        styleEdges.put(mxConstants.STYLE_EXIT_X, 50);
        styleEdges.put(mxConstants.STYLE_EXIT_Y, 0);
        styleEdges.put(mxConstants.STYLE_ENTRY_X, -25);
        styleEdges.put(mxConstants.STYLE_ENTRY_Y, 0);


        stylesheet.putCellStyle("StyleEdges", styleEdges);

        mxgraph.getModel().endUpdate();

    }

    private void setVertexStyle(mxGraph mxgraph) {
        //create Style
        mxgraph.getModel().beginUpdate();

        //Style Vertex Source
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        Map<String, Object> style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_FONTCOLOR, "#ffffff");
        style.put(mxConstants.STYLE_GRADIENTCOLOR, "#ffffff");
        style.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
        style.put(mxConstants.STYLE_FONTSIZE, 12);
        style.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        style.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style.put(mxConstants.STYLE_RESIZABLE, 0);
        stylesheet.putCellStyle("StyleSource", style);


        //Style vertex Target
        Map<String, Object> style2 = new HashMap<String, Object>();
        style2.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style2.put(mxConstants.STYLE_FONTCOLOR, "#ffffff");
        style2.put(mxConstants.STYLE_FILLCOLOR, "#66b366");
        style2.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
        style2.put(mxConstants.STYLE_GRADIENTCOLOR, "#ffffff");
        style2.put(mxConstants.STYLE_FONTSIZE, 12);
        style2.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        style2.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style2.put(mxConstants.STYLE_RESIZABLE, 0);
        stylesheet.putCellStyle("StyleTarget", style2);

        //Style vertex Annotation
        Map<String, Object> style3 = new HashMap<String, Object>();
        style3.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style3.put(mxConstants.STYLE_FONTCOLOR, "#686868");
        style3.put(mxConstants.STYLE_FILLCOLOR, "#d0d8e3");
        style3.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
        style3.put(mxConstants.STYLE_GRADIENTCOLOR, "#ffffff");
        style3.put(mxConstants.STYLE_FONTSIZE, 11);
        style3.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style3.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        style3.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style3.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
        stylesheet.putCellStyle("StyleAnnotation", style3);
        mxgraph.getModel().endUpdate();

        //Style vertex Function
        Map<String, Object> style4 = new HashMap<String, Object>();
        style4.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style4.put(mxConstants.STYLE_FONTCOLOR, "#686868");
        style4.put(mxConstants.STYLE_FILLCOLOR, "#ffffa1");
        style4.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
        style4.put(mxConstants.STYLE_GRADIENTCOLOR, "#ffffff");
        style4.put(mxConstants.STYLE_FONTSIZE, 11);
        style4.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style4.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        style4.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style4.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
        stylesheet.putCellStyle("StyleFunction", style4);
        mxgraph.getModel().endUpdate();
    }
}
