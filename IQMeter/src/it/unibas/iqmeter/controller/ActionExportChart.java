/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.contrib.PingThreadWorker;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.io.File;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Antonio Genovese
 */
@DescrizioneSwing("Export Effort/Quality Graph to the .ods file")
@AcceleratoreSwing("ctrl shift E")
@MnemonicoSwing("E")
@DisabilitataAllAvvio(true)
public class ActionExportChart extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        final PingThreadWorker worker = new PingThreadWorker() {
            int exitCode = -1;
            String error = "";

            @Override
            public Object avvia() {
                try {
                    Scenario scenario = (Scenario) modello.getBean(Scenario.class.getName());
                    List<MappingTool> toolsList = scenario.getToolsList();
                    int[] cellProperties = PropertiesLoader.loadPropertiesTemplateChart();
                    File template = PropertiesLoader.loadTemplateChart(cellProperties[2]);
                    File file = new File(scenario.getOutPath() + "/" + scenario.getName() + "_" + "qualityGraph");
                    final TableModel model = createTableModel(toolsList, cellProperties[2], false);
                    final Sheet sheet = SpreadSheet.createFromFile(template).getSheet(0);
                    sheet.merge(model, cellProperties[0], cellProperties[1], false);
                    SpreadSheet ss = sheet.getSpreadSheet();

                    if (file.exists()) {
                        file.delete();
                    }
                    ss.saveAs(file);

                    File templateClick = PropertiesLoader.loadTemplateChart(cellProperties[2]);
                    File fileClick = new File(scenario.getOutPath() + "/" + scenario.getName() + "_" + "qualityGraphRecorderd");
                    final TableModel modelRecorded = createTableModel(toolsList, cellProperties[2], true);
                    final Sheet sheetRecorded = SpreadSheet.createFromFile(templateClick).getSheet(0);
                    sheetRecorded.merge(modelRecorded, cellProperties[0], cellProperties[1], false);
                    SpreadSheet ssRecorded = sheetRecorded.getSpreadSheet();

                    if (fileClick.exists()) {
                        fileClick.delete();
                    }
                    ssRecorded.saveAs(fileClick);

                    exitCode = 0;
                } catch (Exception daoe) {
                    logger.error(daoe);
                    error = daoe.getLocalizedMessage();
                }

                return null;
            }

            @Override
            public void concludi() {
                JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);

                if (exitCode != 0) {
                    l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
                    vista.finestraErrore("Error to export the .ods file: \n" + error);
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_EXPORT_ERROR));
                } else {
                    l.setIcon(Utils.createIcon(Constant.ICON_OK));
                    modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_EXPORT_SUCCESS));
                }
            }

            private TableModel createTableModel(List<MappingTool> toolsList, int steps, boolean recorded) {
                DefaultTableModel model = new DefaultTableModel();
                double divisor = 0.1;
                int lenght = 0;

                if (steps == 5) {
                    model.addColumn("Quality", new Object[]{"Quality", .0, .05, .10, .15, .20, .25, .30, .35, .40, .45, .50,
                        .55, .60, .65, .70, .75, .80, .85, .90, .95, 1.0});
                    divisor = 0.05;
                    lenght = 22;
                } else if (steps == 10) {
                    model.addColumn("Quality", new Object[]{"Quality", .0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0});
                    lenght = 12;
                }
                for (MappingTool tool : toolsList) {
                    createObjectTable(tool, model, divisor, lenght, recorded);
                }
                return model;
            }

            private void createObjectTable(MappingTool tool, DefaultTableModel data, double divisor, int lenght, boolean recorded) {
                Object[] toolColumn = new Object[lenght];
                Arrays.fill(toolColumn, 0);
                toolColumn[0] = tool.getName();
                toolColumn[1] = 0;
                for (int i = 0; i < tool.getExecutionsList().size(); i++) {
                    MappingExecution task = tool.getMappingTask(i);
                    double bitEffort = 0;
                    double quality = (task.getQuality().getFmeasure());
                    int round = (int) Math.round(quality / divisor);
                    if (!recorded) {
                        if (task.getEffortGraph() != null) {
                            bitEffort = task.getEffortGraph().getBitEffort();
                        }
                    } else {
                        if (task.getEffortRecording() != null) {
                            bitEffort = task.getEffortRecording().getTotalInteraction();
                        }
                    }
                    logger.debug("round: " + round);
                    Arrays.fill(toolColumn, round + 1, lenght, bitEffort);
                }
                data.addColumn(tool.getName(), toolColumn);
            }
        };
        worker.start();
    }

    @Override
    public boolean disabilita(Integer status) {
        if (status == Constant.STATE_NO_SCENARIO) {
            return true;
        }
        return false;
    }
}
