package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.operator.CalculateEffort;
import it.unibas.iqmeter.controller.operator.CalculateQuality;
import it.unibas.iqmeter.controller.operator.ThreadRecording;
import it.unibas.iqmeter.model.EffortRecording;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.iqmeter.view.PanelToolMapping;
import it.unibas.iqmeter.view.View;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@NomeSwing("Record")
@DescrizioneSwing("Recording left click on external tools")
@DisabilitataAllAvvio(true)
public class ActionRecord extends AzionePingAstratta {

//    private ThreadRecording recording;

    private static Log logger = LogFactory.getLog(ActionRecord.class);
    
    public ActionRecord(){
//        try{
//            recording = ThreadRecording.getInstance();
//        }catch(Error ex){
//            logger.error("Unable to record mouse and keyboard clicks. Current system is not supported. " + ex.getLocalizedMessage());
//        }
    }

    @Override
    public void esegui(EventObject eo) {
//        if(recording == null) return;
//        final JProgressBar bar = (JProgressBar) vista.getComponente(Constant.PROGRESS_BAR);
//        AbstractButton toggleButton = (JToggleButton) eo.getSource();
//        ButtonModel buttonModel = toggleButton.getModel();
//        boolean isSelected = buttonModel.isSelected();
//        if (!recording.isStarted()) {
//            recording.startThread();
//        }
//        Component panel = (Component) modello.getBean(Constant.TAB_SELECTED);
//        String panelName = panel.getName();
//        logger.debug("Panel name: " + panelName);
//        String process = ThreadRecording.getProcessFromPanel(panelName);
//        logger.debug("Process name: " + process);
//        if (isSelected) {
//            logger.debug("Start recording thread");
//            boolean askToResume = isRecorded(process);
//            logger.debug("AskToResume = " + askToResume);
//            if (askToResume) {
//                View view = (View) this.vista.getSottoVista(View.class.getName());
//                int clicks = (Integer) modello.getBean(Constant.CLICK_NUMBER + process);
//                int keys = (Integer) modello.getBean(Constant.KEYBOARD_NUMBER + process);
//                int choise = view.showDialogConfirm(Constant.MESSAGE_RECORD_RESET + "\nClicks: " + clicks + "   Keys: " + keys, "Warning");
//                if (choise != 0) {
//                    recording.resetForTool(process);
//                } else {
//                    recording.setClickCounter(process, clicks);
//                    recording.setKeyboardCounter(process, keys);
//                }
//            }
//            JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
//            l.setIcon(Utils.createIcon(Constant.ICON_INFO));
//            modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_RECORD));
//            bar.setIndeterminate(true);
//            bar.setVisible(true);
//            recording.setRecord(true);
//            recording.setProcessToRecord(process);
//        } else {
//            logger.debug("Stop recording thread");
//            recording.concludi();
//            modello.putBean(Constant.CLICK_NUMBER + process, recording.getClickCounter(process));
//            modello.putBean(Constant.KEYBOARD_NUMBER + process, recording.getKeyboardCounter(process));
//            modello.putBean(EffortRecording.class.getName() + panelName, new EffortRecording(recording.getClickCounter(process), recording.getKeyboardCounter(process)));
//            logger.debug("Put key: " + EffortRecording.class.getName() + panelName);
//            modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_RECORD_STOP + Constant.MESSAGE_RECORD_MOUSE + recording.getClickCounter(process) + " " + Constant.MESSAGE_RECORD_KEYBOARD + recording.getKeyboardCounter(process)));
//            bar.setVisible(false);
//            bar.setIndeterminate(false);
//            try {
//                logger.debug("Update quality...");
//                final MappingToolPM tool = (MappingToolPM) modello.getBean(Constant.TOOL_ANALYZED);
//                Scenario sc = (Scenario) modello.getBean(Scenario.class.getName());
//                CalculateQuality.calculate(sc, tool, modello);
//                MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_ANALYZED);
//                CalculateEffort.calculate(tool, task);
//                CalculateEffort.calculateClick(tool, task, false);
//                tool.addMappingExecution(task);
//                PanelToolMapping panelToolMapping = (PanelToolMapping) vista.getComponente(tool.getName());
//                panelToolMapping.addNodeExecution();
//                modello.removeBean(Constant.EXECUTION_ANALYZED);
//                PanelChart pChart = (PanelChart) vista.getSottoVista(PanelChart.class.getName());
//                PanelChartClick pChartClick = (PanelChartClick) vista.getSottoVista(PanelChartClick.class.getName());
//                PanelChartTemporal pChartTemp = (PanelChartTemporal) vista.getSottoVista(PanelChartTemporal.class.getName());
//                PanelChartClickTemporal pChartClickTemp = (PanelChartClickTemporal) vista.getSottoVista(PanelChartClickTemporal.class.getName());
//                JComponent panelContainer = (JComponent) vista.getComponente(Constant.PANEL_CONTAINER_CHART);
//                logger.debug("Update graphics");
//                if (!panelContainer.isVisible()) {
//                    pChart.buildChart();
//                    pChartClick.buildChart();
//                    pChartTemp.buildChart();
//                    pChartClickTemp.buildChart();
//                    panelContainer.setVisible(true);
//                } else {
//                    pChart.refreshChart();
//                    pChartClick.refreshChart();
//                    pChartTemp.refreshChart();
//                    pChartClickTemp.refreshChart();
//                }
//
//            } catch (Exception exception) {
//                logger.error("Error for calculating quality and effort:" + exception);
//                if (logger.isDebugEnabled()) {
//                    exception.printStackTrace();
//                }
//
//            }
//        }
    }

    private boolean isRecorded(String process) {
        if (modello.getBean(Constant.CLICK_NUMBER + process) == null && modello.getBean(Constant.KEYBOARD_NUMBER + process) == null) {
            return false;
        }
        return true;
    }

}
