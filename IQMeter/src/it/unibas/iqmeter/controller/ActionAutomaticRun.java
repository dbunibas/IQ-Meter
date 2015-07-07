/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.operator.CalculateEffort;
import it.unibas.iqmeter.controller.operator.CalculateQuality;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.iqmeter.view.PanelToolMapping;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.contrib.PingThreadWorker;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import it.unibas.ping.framework.Modello;
import it.unibas.ping.framework.Vista;
import java.io.File;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ActionAutomaticRun extends AzionePingAstratta implements Runnable {

    private Log logger = LogFactory.getLog(this.getClass());
    private Vista vista = Applicazione.getInstance().getVista();
    private Modello modello = Applicazione.getInstance().getModello();
    private Controllo controllo = Applicazione.getInstance().getControllo();

    public void esegui(EventObject eo) {

        final JProgressBar bar = (JProgressBar) vista.getComponente(Constant.PROGRESS_BAR);
        JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
        controllo.disabilitaAzioneSwing(ActionManualRun.class.getName());
        l.setIcon(Utils.createIcon(Constant.ICON_INFO));
        modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_QUALITY));
        bar.setIndeterminate(true);
        bar.setVisible(true);

        final MappingToolPM tool = (MappingToolPM) modello.getBean(Constant.TOOL_ANALYZED);

        final PingThreadWorker worker = new PingThreadWorker() {

            int exitCode = -1;
            String error = "";

            @Override
            public Object avvia() {
                try {
                    Scenario sc = (Scenario) modello.getBean(Scenario.class.getName());
                    if (new File(tool.getTranslatedInstancePath()).exists() && new File(tool.getMappingFilePath()).exists()) {
                        CalculateQuality.calculate(sc, tool, modello);
                        MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_ANALYZED);
                        logger.debug("Execution ANALYZED: " + task.getTimeLabel());
                        CalculateEffort.calculate(tool, task);
                        CalculateEffort.calculateClick(tool, task, true);
                        MappingExecution last = tool.lastMappingExecution();
                        if (last != null && task.getQuality().getFmeasure() == last.getQuality().getFmeasure()
                                && task.getEffortGraph().getBitEffort() == last.getEffortGraph().getBitEffort()) {
                            logger.info("Mapping Execution skipped because is the same execution");
                            DAOMappingExecution.deleteMappingFile(task);
                            exitCode = 2;
                        } else {
                            tool.addMappingExecution(task);
                            logger.debug("Task Add: " + tool.getExecutionsList().size() + " for tool " + tool.getName());

                            PanelToolMapping panel = (PanelToolMapping) vista.getComponente(tool.getName());
                            panel.addNodeExecution();
                            modello.removeBean(Constant.EXECUTION_ANALYZED);
                            PanelChart pChart = (PanelChart) vista.getSottoVista(PanelChart.class.getName());
                            PanelChartClick pChartClick = (PanelChartClick) vista.getSottoVista(PanelChartClick.class.getName());
                            PanelChartTemporal pChartTemp = (PanelChartTemporal) vista.getSottoVista(PanelChartTemporal.class.getName());
                            PanelChartClickTemporal pChartClickTemp = (PanelChartClickTemporal) vista.getSottoVista(PanelChartClickTemporal.class.getName());
                            JComponent panelContainer = (JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART);
                            if (!panelContainer.isVisible()) {
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
                            exitCode = 0;
                        }
                    } else {
                        exitCode = 1;
                        logger.error("File Instances Translated or File mapping not exist");
                    }
                } catch (Exception e) {
                    logger.error(e);
                    error += e.getLocalizedMessage();
                }
                return null;
            }

            @Override
            public void concludi() {
                JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
                if (exitCode != 0) {
                    if (exitCode != 1 && exitCode != 2) {
                        vista.finestraErrore("Error to run task: \n" + error);
                        modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_QUALITY_ERROR));
                        l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
                    } else if (exitCode == 2) {
                        l.setIcon(Utils.createIcon(Constant.ICON_OK));
                        modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_QUALITY_SUCCESS));
                    } else {
                        modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(tool.getName() + " " + Constant.MESSAGE_QUALITY_SKIP));
                        l.setIcon(Utils.createIcon(Constant.ICON_INFO));
                    }
                } else {
                    l.setIcon(Utils.createIcon(Constant.ICON_OK));
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_QUALITY_SUCCESS));
                    JComponent panelContainer = (JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART);
                    if (!panelContainer.isVisible()) {
                        controllo.abilitaAzioneSwing(ActionExportChart.class.getName());
                        controllo.abilitaAzioneSwing(ActionSwitchChart.class.getName());
                        controllo.abilitaAzioneSwing(ActionExportImageChart.class.getName());
                        panelContainer.setVisible(true);
                    }
                }
                bar.setVisible(false);
                bar.setIndeterminate(false);
                controllo.abilitaAzioneSwing(ActionManualRun.class.getName());
            }
        };
        worker.start();
    }

    public void run() {
        this.esegui(null);
    }
}
