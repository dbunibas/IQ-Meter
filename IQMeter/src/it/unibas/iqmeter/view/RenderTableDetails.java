/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Antonio Genovese
 */
public class RenderTableDetails extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, false, isSelected,
                row, column);
        c.setBackground(table.getBackground());
        c.setForeground(table.getForeground());
        return c;
//    if (table.getValueAt(row, column) == null && isSelected) {
//        table.clearSelection();
//
//        return super.getTableCellRendererComponent(table, value, false, false,
//                row, column);
//    } else {
//          table.clearSelection();
//        return  super.getTableCellRendererComponent(table, value, isSelected,
//                hasFocus, row, column);
//    }

    }
    
    
    
}