/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.view.FxEffortQualityChart;
import it.unibas.iqmeter.view.FxEffortQualityChartTemporal;
import it.unibas.iqmeter.view.FxRecordedQualityChart;
import it.unibas.iqmeter.view.FxRecordedQualityChartTemporal;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Switch chart")
@AcceleratoreSwing("ctrl W")
@MnemonicoSwing("W")
@DisabilitataAllAvvio(true)
@DescrizioneSwing("Switch the type of chart")
public class ActionSwitchChart extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        PanelChart pChart =  (PanelChart) this.vista.getSottoVista(PanelChart.class.getName());
        FxEffortQualityChart chartFx = pChart.getFxChart();
        if(chartFx.getChartType().equals(Constant.AREA_CHART)){
            chartFx.setChartType(Constant.LINE_CHART);
        }else{
            chartFx.setChartType(Constant.AREA_CHART);
        }
        pChart.buildChart();
        PanelChartTemporal pChartTemp =  (PanelChartTemporal) this.vista.getSottoVista(PanelChartTemporal.class.getName());
        FxEffortQualityChartTemporal chartFxTemp = pChartTemp.getFxChart();
        if(chartFxTemp.getChartType().equals(Constant.AREA_CHART)){
            chartFxTemp.setChartType(Constant.LINE_CHART);
        }else{
            chartFxTemp.setChartType(Constant.AREA_CHART);
        }
        pChartTemp.buildChart();
        PanelChartClick pChartClick = (PanelChartClick) this.vista.getSottoVista(PanelChartClick.class.getName());
        FxRecordedQualityChart fxChart = pChartClick.getFxChart();
        if (fxChart.getChartType().equals(Constant.AREA_CHART)) {
            fxChart.setChartType(Constant.LINE_CHART);
        } else {
            fxChart.setChartType(Constant.AREA_CHART);
        }
        pChartClick.buildChart();
        PanelChartClickTemporal pChartClickTemporal = (PanelChartClickTemporal) this.vista.getSottoVista(PanelChartClickTemporal.class.getName());
        FxRecordedQualityChartTemporal fxChartTemporal = pChartClickTemporal.getFxChart();
        if (fxChartTemporal.getChartType().equals(Constant.AREA_CHART)) {
            fxChartTemporal.setChartType(Constant.LINE_CHART);
        } else {
            fxChartTemporal.setChartType(Constant.AREA_CHART);
        }
        pChartClickTemporal.buildChart();
    }
    
}
