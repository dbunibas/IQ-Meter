/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.effortgraph.layout;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.shape.mxLabelShape;
import com.mxgraph.view.mxGraph;

/**
 *
 * @author Antonio Genovese
 */
public class MXEffortGraph extends mxGraph {

    @Override
    public boolean isCellDisconnectable(Object o, Object o1, boolean bln) {
        return false;
    }

    @Override
    public boolean isCellEditable(Object cell) {

        return false;
    }

    @Override
    public boolean isCellMovable(Object cell) {
        if (cell != null) {
            if (cell instanceof mxCell) {
                mxCell myCell = (mxCell) cell;
                if (myCell.isEdge()) {
                    return false;
                }
            }
        }
        return super.isCellMovable(cell);
    }

    @Override
    public boolean isConstrainChild(Object o) {
        return true;
    }

    @Override
    public boolean isDropEnabled() {
        return false;
    }

    @Override
    public boolean isCellSelectable(Object cell) {
        if (cell != null) {
            if (cell instanceof mxCell) {
                mxCell myCell = (mxCell) cell;
                if (myCell.isEdge()) {
                    return false;
                }
            } else if (cell instanceof mxLabelShape) {
                return false;
            }
        }

        return super.isCellSelectable(cell); //To change body of generated methods, choose Tools | Templates.
    }

    public Object createEdge(Object parent, String id, Object value,
            Object source, Object target, String style, double labelX, double labelY) {
        mxCell edge = new mxCell(value, new mxGeometry(), style);

        edge.setId(id);
        edge.setEdge(true);
        edge.getGeometry().setRelative(true);
        edge.getGeometry().setX(labelX);
        edge.getGeometry().setY(labelY);

        return edge;
    }

    public Object insertEdge(Object parent, String id, Object value,
            Object source, Object target, String style, String labelPositioning) {
        double labelX, labelY;

        if (labelPositioning.equals("left") || labelPositioning.equals("top")) {
            labelX = -0.7;
            labelY = 9;
        } else if (labelPositioning.equalsIgnoreCase("center")) {
            labelX = 0;
            labelY = 9;
        } else {
            labelX = 0.7;
            labelY = 9;
        }

        Object edge = createEdge(parent, id, value, source, target, style, labelX, labelY);

        return addEdge(edge, parent, source, target, null);
    }
}
