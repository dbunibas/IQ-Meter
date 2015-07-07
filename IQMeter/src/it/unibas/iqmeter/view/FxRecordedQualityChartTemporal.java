/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.ping.framework.Applicazione;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@SuppressWarnings("unchecked")
public class FxRecordedQualityChartTemporal {

    private JFXPanel fxPanel;
    private XYChart chart;
    private Log logger = LogFactory.getLog(this.getClass());
    private String chartType = Constant.AREA_CHART;
    private Scene scene;
//    private ObservableList<XYChart.Series<Number, Number>> seriesOs;
    private Map<String, String> mapColor = new HashMap<String, String>();

    FxRecordedQualityChartTemporal(JFXPanel fxPanel) {
        this.fxPanel = fxPanel;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    public void initFX() {
        // This method is invoked on the JavaFX thread
        fxPanel.removeAll();
        createScene();
        chart.setAnimated(true);
        fxPanel.setScene(scene);
    }

    private void createScene() {
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis(0, 105, 10);
        xAxis.setLabel("User Interactions");
        yAxis.setLabel("Quality %");
        xAxis.forceZeroInRangeProperty();
//        xAxis.setMinorTickCount(0);
//        yAxis.setMinorTickCount(0);
        //creating the chart
        if (chartType == null) {
            chartType = Constant.AREA_CHART;
            //overlapping area colors
            this.chart = new AreaChart<Number, Number>(xAxis, yAxis);
//            this.chart = new StackedAreaChart<Number, Number>(xAxis, yAxis);
        } else if (chartType.equals(Constant.LINE_CHART)) {
            this.chart = new LineChart<Number, Number>(xAxis, yAxis);
        } else {
            //overlapping area colors
            this.chart = new AreaChart<Number, Number>(xAxis, yAxis);
//            this.chart = new StackedAreaChart<Number, Number>(xAxis, yAxis);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Chart created: " + this.chart);
        }
        chart.setAnimated(true);
        scene = new Scene(chart, 600, 400);
        chart.getStylesheets().add(this.getClass().getResource(Constant.FOLDER_CONF + "styleChart.css").toExternalForm());
        createSeries(chart);
//        seriesOs = FXCollections.observableArrayList();
//        createObservableSeries();
//        chart.setData(seriesOs);

        chart.setLegendSide(Side.TOP);
        logger.debug("Thread chart active: " + Thread.activeCount());

    }

    private void createSeries(final XYChart chart) {
        //populating the series with data
        XYChart.Series series;
        Scenario scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
        for (MappingTool tool : scenario.getToolsList()) {
            if (logger.isTraceEnabled()) {
                logger.trace("Tool Scan : " + tool.getName());
            }
            if (tool.getNumberExecutions() > 0) {
                series = new XYChart.Series();
                series.setName(tool.getName());
                if (!mapColor.containsKey(tool.getName())) {
                    mapColor.put(tool.getName(), PropertiesLoader.loadColorName(tool.getName()));
                    if (logger.isTraceEnabled()) {
                        logger.trace("map color put" + tool.getName() + " value " + mapColor.get(tool.getName()));
                    }
                }
                series.getData().add(new XYChart.Data(0, 0));
                List<MappingExecution> listExecution = new ArrayList<MappingExecution>();
                listExecution.addAll(tool.getExecutionsList());
                Collections.sort(listExecution,MappingExecution.MappingRecordedComparator);
                if (logger.isTraceEnabled()) {
                    logger.trace("Mapping executions: " + listExecution.size());
                }
                for (MappingExecution mappingTask : listExecution) {
                    int effort = 0;
                    if (logger.isTraceEnabled()) {
                        logger.trace("Mapping: " + mappingTask);
                    }
                    if (mappingTask.getEffortRecording() != null) {
                        if (logger.isTraceEnabled()) {
                            logger.trace("Updating effort:" + mappingTask.getEffortRecording());
                        }
                        effort = mappingTask.getEffortRecording().getTotalInteraction();
                    }
                    series.getData().add(new XYChart.Data(effort, mappingTask.getQuality().getFmeasure() * 100));
                }
                chart.getData().add(series);
            }
        }
        setColors();
//        printNodes(chart, 0);
    }

    private void setColors() {
        Scenario scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
        for (int i = 0; i < chart.getData().size(); i++) {
            for (Node node : chart.lookupAll(".series" + i)) {
                node.getStyleClass().remove("default-color" + i);
                node.getStyleClass().add("default-color" + mapColor.get(scenario.getToolsList().get(i).getName()));
            }
            int item = 0;
            for (Node node : chart.lookupAll(".chart-legend-item")) {
                if (node instanceof Label && ((Label) node).getGraphic() != null) {
                    String color = mapColor.get(scenario.getToolsList().get(item).getName());
                    if (color != null && !color.isEmpty()) {
                        ((Label) node).getGraphic().getStyleClass().remove("default-color" + item);
                        ((Label) node).getGraphic().getStyleClass().add("default-color" + color);
                    }
                }
                item++;
            }
        }
    }

    public void refreshSeries() {
//        if (chart != null && !chart.getData().isEmpty()) {
//            chart.getData().removeAll(seriesOs);
//            seriesOs.removeAll();
//            this.createObservableSeries();
        if (chart == null) {
            initFX();
        }
        chart.getData().removeAll();
        chart.getData().clear();
        this.createSeries(chart);
//        } else {           
//            initFX();
//        }
    }
//    private void createObservableSeries() {
//        //populating the series with data
//        Scenario scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
//        if (scenario != null) {
//            for (int i = 0; i < scenario.getToolsList().size(); i++) {
//                MappingTool tool = scenario.getTool(i);
//                createSeriesforTool(tool);
//            }
//        }
//
//    }
//    private void createSeriesforTool(MappingTool tool) {
//        ObservableList<XYChart.Data<Number, Number>> seriesData = FXCollections.observableArrayList();
//        if (!tool.getExecutionsList().isEmpty()) {
//            seriesData.add(new XYChart.Data<Number, Number>(0, 0));
//            List<MappingExecution> listExecution = new ArrayList<MappingExecution>();
//            listExecution.addAll(tool.getExecutionsList());
//            Collections.sort(listExecution);
//            for (MappingExecution mappingTask : listExecution) {
//                double bitEffort = 0;
//                if (mappingTask.getEffortGraph() != null) {
//                    bitEffort = mappingTask.getEffortGraph().getBitEffort();
//                }
//                seriesData.add(new XYChart.Data<Number, Number>(mappingTask.getQuality().getFmeasure() * 100, bitEffort));
//            }
//            seriesOs.add(new XYChart.Series(tool.getName(), seriesData));
//        }
//    }
//    public void printNodes(Node node, int depth) {
//        for (int i = 0; i < depth; i++) {
//            System.out.print(" ");
//        }
//        System.out.println(node);
//        if (node instanceof Parent) {
//            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
//                printNodes(child, depth + 1);
//            }
//        }
////        if(node.toString().contains("styleClass=chart-series-area-fill series1 default-color1")){
////            node.setStyle("*.series-spicy.chart-series-area-fill");
////            System.out.println("New STyle --> " + node); 
////        
////        }
//    }
}
