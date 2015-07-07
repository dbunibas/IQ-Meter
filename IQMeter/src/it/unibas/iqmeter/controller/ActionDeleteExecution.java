/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.iqmeter.view.PanelToolMapping;
import it.unibas.iqmeter.view.View;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Delete Execution")
@DescrizioneSwing("Delete selected execution")
@AcceleratoreSwing("ctrl D")
@MnemonicoSwing("D")
@DisabilitataAllAvvio(true)
public class ActionDeleteExecution extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        View view = (View) this.vista.getSottoVista(View.class.getName());
        MappingExecution mapping = (MappingExecution) this.modello.getBean(Constant.EXECUTION_SELECTED);
        int choice = view.showDialogConfirm(Constant.MESSAGE_DIALOG_DELETE_TASK, "Confirm Delete Execution #" + mapping.getNumberLabel());
        logger.trace("Confirm Choise: " + choice);
        if (choice == 0) {
            PanelToolMapping panel = (PanelToolMapping) this.modello.getBean(Constant.TAB_SELECTED);
            MappingToolPM tool = (MappingToolPM) (MappingTool) this.modello.getBean(MappingTool.class.getName() + "_" + panel.getName());
            JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
            DAOMappingExecution.deleteMappingFile(mapping);
            logger.info("Delete mapping: " + mapping.getDirectory());
            int index = tool.getExecutionsList().indexOf(mapping);
            panel.deleteNodeExecution(index);
            tool.removeMappingExecution(mapping);
            modello.removeBean(mapping.getDirectory() + "_mxGraph");
            PanelChart pChart = (PanelChart) vista.getSottoVista(PanelChart.class.getName());
            PanelChartClick pChartClick = (PanelChartClick) vista.getSottoVista(PanelChartClick.class.getName());
            PanelChartTemporal pChartTemp = (PanelChartTemporal) vista.getSottoVista(PanelChartTemporal.class.getName());
            PanelChartClickTemporal pChartClickTemp = (PanelChartClickTemporal) vista.getSottoVista(PanelChartClickTemporal.class.getName());
            JComponent pContainer = (JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART);
            Scenario scenario = (Scenario) this.modello.getBean(Scenario.class.getName());
            if (tool.getExecutionsList().isEmpty() && hideChart(scenario)) {
                pContainer.setVisible(false);
                controllo.disabilitaAzioneSwing(ActionExportChart.class.getName());
                controllo.disabilitaAzioneSwing(ActionExportImageChart.class.getName());
                controllo.disabilitaAzioneSwing(ActionSwitchChart.class.getName());
                pChart.buildChart();
                pChartClick.buildChart();
                pChartTemp.buildChart();
                pChartClickTemp.buildChart();
            } else {
                pChart.refreshChart();
                pChartClick.refreshChart();
                pChartTemp.refreshChart();
                pChartClickTemp.refreshChart();
            }
            tool.resetExecutionLabel();
            Utils.closeDialog(mapping);
            this.modello.removeBean(Constant.EXECUTION_SELECTED);
            panel.cleanTableDetails();
            this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_DELETE_TASK));
            l.setIcon(Utils.createIcon(Constant.ICON_OK));
        }
    }

    private boolean hideChart(Scenario sc) {
        boolean hide = true;
        for (MappingTool tool : sc.getToolsList()) {
            if (!tool.getExecutionsList().isEmpty()) {
                return false;
            }
        }
        return hide;
    }
}
