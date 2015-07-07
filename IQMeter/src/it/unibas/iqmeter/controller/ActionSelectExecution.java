/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.view.PanelToolMapping;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import javax.swing.JTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@DisabilitataAllAvvio(true)
public class ActionSelectExecution extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        PanelToolMapping panel = (PanelToolMapping) modello.getBean(Constant.TAB_SELECTED);
        JTree treeExecutions = (JTree) vista.getComponente(panel.getName() + Constant.TREE_EXECUTIONS);
        logger.debug("Tree component: " + treeExecutions.getName());
        int index = treeExecutions.getMaxSelectionRow();
        MappingTool tool = (MappingTool) this.modello.getBean(MappingTool.class.getName() + "_" + panel.getName());
        logger.info("Tool Selected: " + tool.getName());
        if (index > 0) {
            logger.info("Selected Index: " + index);
            MappingExecution mapping = tool.getMappingTask(index - 1);
            this.modello.putBean(Constant.EXECUTION_SELECTED, mapping);
            panel.buildTableDetails();
            this.controllo.getAzioneSwing(ActionShowEffortGraph.class.getName()).setEnabled(true);
            this.controllo.getAzioneSwing(ActionDeleteExecution.class.getName()).setEnabled(true);
            this.controllo.getAzioneSwing(ActionExplainQuality.class.getName()).setEnabled(true);
            this.controllo.getAzioneSwing(ActionShowEditScript.class.getName()).setEnabled(true);
        } else {
            panel.cleanTableDetails();
            this.modello.removeBean(Constant.EXECUTION_SELECTED);
            this.controllo.getAzioneSwing(ActionShowEffortGraph.class.getName()).setEnabled(false);
            this.controllo.getAzioneSwing(ActionDeleteExecution.class.getName()).setEnabled(false);
            this.controllo.getAzioneSwing(ActionExplainQuality.class.getName()).setEnabled(false);
            this.controllo.getAzioneSwing(ActionShowEditScript.class.getName()).setEnabled(false);
        }
    }
}
