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

/**
 *
 * @author Antonio Genovese
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCommonLayout implements IEffortGraphLayout {

    //Vertex Constants
    protected Map<String, Object> mapVertex;
    //protected final double ANNOTATION_LABEL_SCALE = 0.7;
    protected final double ANNOTATION_LABEL_SCALE = 0.4;
    //protected final double FUNCTION_LABEL_SCALE = 0.7;
    protected final double FUNCTION_LABEL_SCALE = 0.4;
    protected final double VERTEX_WIDTH = 110;
    protected final double VERTEX_HEIGHT = 50;
    //Style Constants
    protected final String FONT = "Trebuchet MS";
    protected final String FONT_COLOR_VERTEX = "#686868";
    protected final String STROKE_COLOR_VERTEX = "#686868";
    protected final String GRADIENT_COLOR_VERTEX = "#ffffff";
    protected final String FONT_COLOR_EDGES = "#000000";
    protected final String STROKE_COLOR_EDGES = "#000000";
    protected final int FONT_SIZE_VERTEX = 11;
    protected final int FONT_SIZE_EDGES = 11;
    protected final String DEFAULT_EDGES_STYLE = "StyleEdgesDefault";
    protected final String DEFAULT_VERTEX_STYLE = "StyleVertexDefault";
    private Map<String, Object> styleVertexMap;
    private Map<String, Object> styleEdgesMap;
    //Style Vertex Color
    protected final String SOURCE_COLOR = "#eeef92;";
    protected final String TARGET_COLOR = "#98ff98;";
    protected final String ANNOTATION_COLOR = "#b2dbf0;";
    protected final String FUNCTON_COLOR = "#ffc0ce;";
    protected final int maxLabelLenght = 20;
    protected final String DEFAULT_VERTEX_STYLE_LABEL = "StyleVertexLabelDefault";

    public AbstractCommonLayout() {
        initMap();
    }

    @Override
    public abstract mxGraph createmxGraph(EffortGraph graphEffort);

    protected Map<String, Object> getStyleVertexMap() {
        return styleVertexMap;
    }

    protected Map<String, Object> getStyleEdgesMap() {
        return styleEdgesMap;
    }

    protected void setStyleEdgesDefault(mxGraph mxgraph) {
        mxgraph.getModel().endUpdate();
        //create Style Edges
        mxgraph.getModel().beginUpdate();
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        stylesheet.putCellStyle(DEFAULT_EDGES_STYLE, styleEdgesMap);
        mxgraph.getModel().endUpdate();

    }

    protected void setVertexStyleDefault(mxGraph mxgraph) {
        //create Style
        mxgraph.getModel().beginUpdate();
        mxStylesheet stylesheet = mxgraph.getStylesheet();
        stylesheet.putCellStyle(DEFAULT_VERTEX_STYLE, styleVertexMap);
        mxgraph.getModel().endUpdate();

    }

    protected void setVertexStyleLabel(mxGraph mxgraph) {
        //create Style
        mxgraph.getModel().beginUpdate();
        Map<String, Object> styleVertexMapLabel = new HashMap<String, Object>();
        mxStylesheet stylesheet1 = mxgraph.getStylesheet();
        styleVertexMapLabel.putAll(styleVertexMap);
//        styleVertexMapLabel.put(mxConstants.STYLE_OVERFLOW,"hidden");
//        styleVertexMapLabel.put(mxConstants.STYLE_OVERFLOW, "fill");
        styleVertexMapLabel.put(mxConstants.STYLE_OVERFLOW, "with");
        stylesheet1.putCellStyle(DEFAULT_VERTEX_STYLE_LABEL, styleVertexMapLabel);
        mxgraph.getModel().endUpdate();

    }

    private void initMap() {
        //Vertex Style
        styleVertexMap = new HashMap<String, Object>();
        styleVertexMap.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        styleVertexMap.put(mxConstants.STYLE_STROKECOLOR, STROKE_COLOR_VERTEX);
        styleVertexMap.put(mxConstants.STYLE_FONTCOLOR, FONT_COLOR_VERTEX);
        styleVertexMap.put(mxConstants.STYLE_GRADIENTCOLOR, GRADIENT_COLOR_VERTEX);
        styleVertexMap.put(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
        styleVertexMap.put(mxConstants.STYLE_FONTSIZE, FONT_SIZE_VERTEX);
        styleVertexMap.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        styleVertexMap.put(mxConstants.STYLE_FONTFAMILY, FONT);
        styleVertexMap.put(mxConstants.STYLE_RESIZABLE, false);

        //EdgesStyle
        styleEdgesMap = new HashMap<String, Object>();
        styleEdgesMap.put(mxConstants.STYLE_ENDARROW, "oval");
        styleEdgesMap.put(mxConstants.STYLE_STARTARROW, "oval");
        styleEdgesMap.put(mxConstants.STYLE_STROKECOLOR, STROKE_COLOR_EDGES);
        styleEdgesMap.put(mxConstants.STYLE_STROKEWIDTH, 1);
        styleEdgesMap.put(mxConstants.STYLE_OPACITY, 60);
        styleEdgesMap.put(mxConstants.EDGESTYLE_ELBOW, "horizontal");
        styleEdgesMap.put(mxConstants.STYLE_ROUNDED, 40);
        styleEdgesMap.put(mxConstants.STYLE_FONTSIZE, FONT_SIZE_EDGES);
        //styleEdgesMap.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        styleEdgesMap.put(mxConstants.STYLE_FONTFAMILY, FONT);
        styleEdgesMap.put(mxConstants.STYLE_FONTCOLOR, FONT_COLOR_EDGES);

    }

    //Truncates the Annotation Label that are too long
    public String cutLabel(EffortGraphNode node, EffortGraph graphEffort, String label) {
        if (label == null || label.isEmpty()) {
            label = node.toString();
        }
        if (label.length() > maxLabelLenght) {
            label = label.substring(0, maxLabelLenght - 5) + " ..." + "\n\n";
        } else {
            label = label + "\n\n";
        }
        if (node.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION) || node.getType().equals(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT)) {
            label = label + "cost: " + (graphEffort.getBitAnnotation() * node.getLabel().length());
        } else if (node.getType().equals(EffortGraphNode.TYPE_NODE_FUNCTION)) {
            label = label + "cost: " + (graphEffort.getBitFunction());
        }
        return label;
    }

    //Utility method for toggling the node position (actual position, min position, max position)
    public double tooglePosition(double i, double min, double max) {
        if (i == max) {
            return min;
        } else {
            return max;
        }

    }
}
