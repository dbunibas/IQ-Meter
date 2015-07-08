/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.controller.operator.ThreadMappingComparison;
import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.operator.CalculateEffort;
import it.unibas.iqmeter.controller.operator.ThreadRecording;
import it.unibas.iqmeter.model.EffortRecording;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.persistence.DAOMappingTool;
import it.unibas.iqmeter.persistence.DAOScenario;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.iqmeter.view.PanelToolMapping;
import it.unibas.iqmeter.view.View;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.iqmeter.view.presentationmodel.ScenarioPM;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.contrib.PingThreadWorker;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.IControllo;
import it.unibas.ping.framework.IModello;
import it.unibas.ping.framework.IVista;
import it.unibas.ping.framework.MessaggioPing;
import it.unibas.ping.framework.StatoPing;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Open")
@MnemonicoSwing("O")
@AcceleratoreSwing("ctrl O")
@DescrizioneSwing("Open Project")
public class ActionOpenProject extends AzionePingAstratta {

    private IModello modello;
    private IVista vista;
    private IControllo controllo;
    private Log logger = LogFactory.getLog(this.getClass().getName());
    private boolean reload;
    private Container source = null;

    public ActionOpenProject() {
        super();
    }

    //for test
    public ActionOpenProject(IModello modello, IVista vista, IControllo controllo) {
        this.modello = modello;
        this.vista = vista;
        this.controllo = controllo;

    }

    @Override
    public void esegui(final EventObject eo) {
        source = null;
        if (this.modello == null || this.vista == null || this.controllo == null) {
            this.modello = super.getModello();
            this.vista = super.getVista();
            this.controllo = super.getControllo();
        }

        if (eo == null) {
            reload = true;
        } else {
            source = (Container) eo.getSource();
            if (source.getName() != null && (source.getName().equals(Constant.BUTTON_SAVE_SCRIPT)
                    || source.getName().equals(Constant.BUTTON_DELETE_SCRIPT))) {
                reload = true;
            } else {
                reload = false;
                this.modello.removeBean(Constant.DIRECTORY_LOAD_PROJECT);
                this.controllo.getAzione(ActionChooseFolder.class.getName()).esegui(null);
            }
        }
        if (this.modello.getBean(Constant.DIRECTORY_LOAD_PROJECT) != null) {
            runThread();
        }
    }

    private void runThread() {
        final JProgressBar bar = (JProgressBar) vista.getComponente(Constant.PROGRESS_BAR);
        JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
        l.setIcon(Utils.createIcon(Constant.ICON_INFO));
        modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_LOADING_SCENARIO));
        bar.setIndeterminate(true);
        bar.setVisible(true);

        final PingThreadWorker worker = new PingThreadWorker() {
            int exitCode = -1;
            String error = "";
            ScenarioPM scenario;
            boolean isTask = false;
            View view = (View) vista.getFramePrincipale();

            @Override
            public Object avvia() {
                try {
                    this.cleanModello();
                    if (!reload) {
                        this.loadingForOpen();
                        ((JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART)).setVisible(false);
                        ((JComponent) vista.getComponente(Constant.TAB_PANE)).setVisible(false);
                    } else {
                        this.loadingForReload();
                    }
                    modello.putBean(Scenario.class.getName(), scenario);
                    modello.putBean(Controllo.STATO, new StatoPing(Constant.STATE_SCENARIO_ACTIVE));
                    this.buildPanelTab();
                    exitCode = 0;
                } catch (Exception e) {
                    logger.error(e);
                    error = e.getLocalizedMessage();
                }

                return null;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void concludi() {
                JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
                if (exitCode != 0) {
                    vista.finestraErrore("Error to load Project: \n" + error);
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_PROBLEM_LOAD_SCENARIO));
                    l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
                } else {
                    successfullyConclude(l);

                }
                bar.setIndeterminate(false);
                bar.setVisible(false);
            }

            private void successfullyConclude(JLabel l) {
                PanelChart pChart = (PanelChart) vista.getSottoVista(PanelChart.class.getName());
                PanelChartClick pChartClick = (PanelChartClick) vista.getSottoVista(PanelChartClick.class.getName());
                PanelChartTemporal pChartTemp = (PanelChartTemporal) vista.getSottoVista(PanelChartTemporal.class.getName());
                PanelChartClickTemporal pChartClickTemp = (PanelChartClickTemporal) vista.getSottoVista(PanelChartClickTemporal.class.getName());
                if (ThreadRecording.getInstance().isOsCompatible()) controllo.abilitaAzioneSwing(ActionRecord.class.getName());
                if (!reload) {
                    view.buildTabPane();
                    vista.getComponente(Constant.TAB_PANE).setVisible(true);
                    ((JComponent) vista.getComponente(Constant.PANEL_SOUTH)).setVisible(true);
                    if (isTask) {
                        pChart.buildChart();
                        pChartClick.buildChart();
                        pChartTemp.buildChart();
                        pChartClickTemp.buildChart();
                        ((JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART)).setVisible(true);
                        controllo.abilitaAzioneSwing(ActionSwitchChart.class.getName());
                        controllo.abilitaAzioneSwing(ActionExportImageChart.class.getName());
                        controllo.abilitaAzioneSwing(ActionExportChart.class.getName());
                        // controllo.abilitaAzioneSwing(ActionRecord.class.getName());

                    } else {
                        controllo.disabilitaAzioneSwing(ActionExportChart.class.getName());
                        controllo.disabilitaAzioneSwing(ActionSwitchChart.class.getName());
                        controllo.disabilitaAzioneSwing(ActionExportImageChart.class.getName());
                        // controllo.disabilitaAzioneSwing(ActionRecord.class.getName());

                    }
                    for (MappingTool tool : scenario.getToolsList()) {
                        PanelToolMapping panelToolMapping = (PanelToolMapping) vista.getComponente(tool.getName());
                        panelToolMapping.recreateTree();
                    }
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_LOAD_SCENARIO));

                } else {
                    completeReload(pChart, pChartClick, pChartTemp, pChartClickTemp, l, bar);
                }
                l.setIcon(Utils.createIcon(Constant.ICON_OK));
                if (scenario.getToolsList().size() > 0) {
                    ThreadMappingComparison.startThread();
                }
            }

            private void completeReload(PanelChart pChart, PanelChartClick pChartClick, PanelChartTemporal pChartTemp, PanelChartClickTemporal pChartClickTemp, JLabel l, JProgressBar bar) {
                pChart.buildChart();
                pChartClick.buildChart();
                pChartTemp.buildChart();
                pChartClickTemp.buildChart();
                vista.getComponente(Constant.TAB_PANE).repaint();
                if (source != null) {
                    PanelToolMapping panelToolMapping = (PanelToolMapping) modello.getBean(Constant.TAB_SELECTED);
                    panelToolMapping.recreateTree();
                    JButton button = (JButton) vista.getComponente(source.getName());
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_STATE_OPERATION_SCRIPT_SUCCESS + button.getText().toLowerCase() + "d"));
                } else {
                    for (MappingTool tool : scenario.getToolsList()) {
                        PanelToolMapping panelToolMapping = (PanelToolMapping) vista.getComponente(tool.getName());
                        panelToolMapping.recreateTree();
                    }
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_RELOAD_SCENARIO));
                    if (!isTask) {
                        ((JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART)).setVisible(false);
                    }
                }
                l.setIcon(Utils.createIcon(Constant.ICON_OK));
            }

            private void cleanModello() {
                ScenarioPM sc = (ScenarioPM) modello.getBean(Scenario.class.getName());
                if (sc != null && !reload && !sc.getToolsList().isEmpty()) {
                    for (MappingTool tool : sc.getToolsList()) {
                        modello.removeBean(MappingTool.class.getName() + "_" + tool.getName());
                    }
                    modello.removeBean(Constant.TAB_SELECTED);
                }
//                FramePing dialogQuality = (FramePing) vista.getSottoVista(DialogExplainQuality.class.getName());
//                FramePing dialogEffort = (FramePing) vista.getSottoVista(DialogEffortGraph.class.getName());
//                if(dialogEffort.isVisible()){
//                    dialogEffort.setVisible(false);
//                }
//                if(dialogQuality.isVisible()){
//                    dialogQuality.setVisible(false);
//                }

                Utils.closeAllDialog();
                ThreadMappingComparison.clearMapTool();
                modello.removeBean(Constant.EXECUTION_SELECTED);
                logger.debug("Modello cleaned");
            }

            @SuppressWarnings("unchecked")
            private void buildPanelTab() {
                List<String> listTools = (List<String>) modello.getBean(Constant.TOOLS_LIST);
                List<String> list = new ArrayList<String>();
                for (MappingTool tool : scenario.getToolsList()) {
                    modello.putBean(MappingTool.class.getName() + "_" + tool.getName(), tool);
                    list.add(tool.getName());
                    if (!tool.getExecutionsList().isEmpty()) {
                        isTask = true;
                        String process = ThreadRecording.getProcessFromPanel(tool.getName());
                        for (MappingExecution execution : tool.getExecutionsList()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Processing execution: " + execution);
                            }
                            try {
                                CalculateEffort.calculate(tool, execution);
                                if (execution.getEffortRecording() != null) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Resuming user opeations... " + execution);
                                    }
                                    modello.putBean(Constant.CLICK_NUMBER + process, execution.getEffortRecording().getClickRecorded());
                                    modello.putBean(Constant.KEYBOARD_NUMBER + process, execution.getEffortRecording().getKeyboardRecorded());
                                    modello.putBean(EffortRecording.class.getName() + tool.getName(), execution.getEffortRecording());
                                }
                            } catch (Exception ex) {
                                logger.error(ex);
                                execution.setEffortGraph(null);
                                execution.setEffortRecording(null);
                            }
                        }
                    }

                }
                listTools.removeAll(list);
                logger.debug("Size list tools available: " + listTools.size());
                if (listTools.isEmpty()) {
                    modello.putBean(Controllo.STATO, new StatoPing(Constant.STATE_NO_TOOLS_AVAILABLE));
                }
            }

            private void loadingForOpen() throws DAOException {
                String directory = (String) modello.getBean(Constant.DIRECTORY_LOAD_PROJECT);
                scenario = new ScenarioPM(DAOScenario.load(directory));
                List<String> tools = PropertiesLoader.loadAvailableTools();
                modello.putBean(Constant.TOOLS_LIST, tools);
                DAOMappingTool.loadTools(scenario, tools);
                Collections.sort(scenario.getToolsList(), MappingTool.MappingToolComparatorName);
                List<MappingTool> listToolPM = new ArrayList<MappingTool>();
                for (MappingTool tool : scenario.getToolsList()) {
                    DAOMappingExecution.loadTasks(tool);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Executions for tool: " + tool.getName());
                    }
                    if (logger.isDebugEnabled()) {
                        List<MappingExecution> executionsList = tool.getExecutionsList();
                        for (MappingExecution execution : executionsList) {
                            logger.debug(execution);
                        }
                    }
                    MappingToolPM toolPM = new MappingToolPM(tool);
                    listToolPM.add(toolPM);
                }
                scenario.setToolsList(listToolPM);

            }

            private void loadingForReload() throws DAOException {
                scenario = (ScenarioPM) modello.getBean(Scenario.class.getName());
                for (MappingTool tool : scenario.getToolsList()) {
                    tool.getExecutionsList().clear();
                    DAOMappingExecution.loadTasks(tool);
                }
            }
        };
        worker.start();
    }
}
