/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.view.View;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ActionChangeTab extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        View view = (View) vista.getSottoVista(View.class.getName());
        JTabbedPane tabPane = (JTabbedPane) vista.getComponente(Constant.TAB_PANE);
        Component panel = tabPane.getSelectedComponent();
        if (panel != null) {
            this.modello.putBean(Constant.TAB_SELECTED, panel);
            Component[] tabs = tabPane.getComponents();
            for (Component component : tabs) {
                if (!component.equals(panel)) {
                    JTree tree = (JTree) vista.getComponente(component.getName() + Constant.TREE_EXECUTIONS);
                    tree.clearSelection();
                }
            }
        }
    }
}
