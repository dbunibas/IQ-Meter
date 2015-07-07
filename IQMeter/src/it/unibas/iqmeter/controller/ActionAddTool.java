/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.controller.operator.ThreadMappingComparison;
import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOMappingTool;
import it.unibas.iqmeter.view.View;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import it.unibas.ping.framework.StatoPing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Add")
@DescrizioneSwing("Add the Mapping Tool to the Project")
public class ActionAddTool extends AzionePingAstratta {
    
    private Log logger = LogFactory.getLog(this.getClass());
    
    @SuppressWarnings("unchecked")
    @Override
    public void esegui(EventObject eo) {
        MappingTool tool;
        List<String> listTools;
        if (eo == null) {
            tool = (MappingTool) this.modello.getBean(Constant.TOOL_NEW);
            listTools = (List<String>) this.modello.getBean(Constant.TOOLS_LIST_NEW);
            this.modello.removeBean(Constant.TOOL_NEW);
            this.modello.removeBean(Constant.TOOLS_LIST_NEW);
        } else {
            tool = (MappingTool) this.modello.getBean(MappingTool.class.getName());
            this.modello.removeBean(MappingTool.class.getName());
            listTools = (List<String>) this.modello.getBean(Constant.TOOLS_LIST);
        }
        
        
        Scenario sc = (Scenario) this.modello.getBean(Scenario.class.getName());
        sc.addTool(tool);
        Collections.sort(sc.getToolsList(), MappingTool.MappingToolComparatorName);
        
        List<String> newList = new ArrayList<String>();
        for (String string : listTools) {
            logger.debug("StringList: " + string);
            if (!string.equals(tool.getName())) {
                newList.add(string);
            }
        }
        
        if (newList.isEmpty()) {
            this.modello.putBean(Controllo.STATO, new StatoPing(Constant.STATE_NO_TOOLS_AVAILABLE));
        }
        
        try {
            DAOMappingTool.createDirectory(tool, sc);
            this.modello.putBean(Constant.TOOLS_LIST, newList);
            View view = (View) this.vista.getFramePrincipale();
            view.addTab(tool.getName());
            JTabbedPane tabPane = (JTabbedPane) vista.getComponente(Constant.TAB_PANE);
            if (!tabPane.isVisible()) {
                tabPane.setVisible(true);
                vista.getComponente(Constant.PANEL_SOUTH).setVisible(true);
            }
            if (eo != null) {
                this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_NEW_TOOL));
                JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
                l.setIcon(Utils.createIcon(Constant.ICON_OK));
                this.modello.putBean(Controllo.STATO, new StatoPing(Constant.STATE_SCENARIO_ACTIVE));
            } else {
                modello.putBean(Controllo.STATO, new StatoPing(Constant.STATE_SCENARIO_ACTIVE));
            }
            modello.putBean(MappingTool.class.getName() + "_" + tool.getName(), tool);
            ThreadMappingComparison.startThread();
        } catch (DAOException ex) {
            logger.error(ex);
            vista.finestraErrore("Problem to add Tool. \n" + ex.getLocalizedMessage());
            JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
            l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
            if (eo == null) {
                this.modello.removeBean(Scenario.class.getName());
                this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_ERROR_SCENARIO));
                //TODO Clean DIRECTORY
            } else {
                this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_ERROR_ADD_TOOL));
            }
        }
    }
}
