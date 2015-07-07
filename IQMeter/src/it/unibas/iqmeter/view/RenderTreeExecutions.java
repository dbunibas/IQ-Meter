/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.Utils;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class RenderTreeExecutions extends DefaultTreeCellRenderer {

    private Icon icon;
    private Log logger = LogFactory.getLog(this.getClass());

    public RenderTreeExecutions(String name) {
        super();
        this.icon = Utils.createIcon(name.toLowerCase() + ".png");
    }

    @Override
    public JComponent getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (leaf && !node.isRoot()) {
            setIcon(this.icon);
        }
        c.setPreferredSize(new Dimension(370, 27));
        return c;
    }
}
