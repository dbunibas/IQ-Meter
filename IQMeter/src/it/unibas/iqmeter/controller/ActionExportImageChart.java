/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.Scenario;
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
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventObject;
import javafx.embed.swing.JFXPanel;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Export chart to .png")
@DescrizioneSwing("Save chart as image .png")
@MnemonicoSwing("T")
@AcceleratoreSwing("ctrl T")
@DisabilitataAllAvvio(true)
public class ActionExportImageChart extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
        FileOutputStream stream = null;
        try {
            Scenario scenario = (Scenario) modello.getBean(Scenario.class.getName());
            PanelChart panelChart = (PanelChart) vista.getComponente(PanelChart.class.getName());
            JFXPanel fxPanel = panelChart.getFxPanel();
            String type = panelChart.getFxChart().getChartType();
            stream = new FileOutputStream(new File(scenario.getOutPath() + "/" + scenario.getName() + "-QE_" + type + ".png"));
            BufferedImage bufImage = new BufferedImage(fxPanel.getSize().width, fxPanel.getSize().height, BufferedImage.TYPE_INT_RGB);
            fxPanel.paint(bufImage.createGraphics());
            ImageIO.write(bufImage, "png", stream);
            stream.close();

            PanelChartClick panelChartClick = (PanelChartClick) vista.getComponente(PanelChartClick.class.getName());
            JFXPanel fxPanelClick = panelChartClick.getFxPanel();
            String typeClick = panelChartClick.getFxChart().getChartType();
            stream = new FileOutputStream(new File(scenario.getOutPath() + "/" + scenario.getName() + "-QE_" + typeClick + "_CLICK.png"));
            BufferedImage bufImageClick = new BufferedImage(fxPanelClick.getSize().width, fxPanelClick.getSize().height, BufferedImage.TYPE_INT_RGB);
            fxPanelClick.paint(bufImageClick.createGraphics());
            ImageIO.write(bufImageClick, "png", stream);
            stream.close();

            PanelChartTemporal panelChartTemporal = (PanelChartTemporal) vista.getComponente(PanelChartTemporal.class.getName());
            JFXPanel fxPanelTemporal = panelChartTemporal.getFxPanel();
            String typeTemporal = panelChartTemporal.getFxChart().getChartType();
            stream = new FileOutputStream(new File(scenario.getOutPath() + "/" + scenario.getName() + "-QE_" + typeTemporal + "_TEMPORAL.png"));
            BufferedImage bufImageTemporal = new BufferedImage(fxPanelTemporal.getSize().width, fxPanelTemporal.getSize().height, BufferedImage.TYPE_INT_RGB);
            fxPanelTemporal.paint(bufImageTemporal.createGraphics());
            ImageIO.write(bufImageTemporal, "png", stream);
            stream.close();
            
            PanelChartClickTemporal panelChartTemporalClick = (PanelChartClickTemporal) vista.getComponente(PanelChartClickTemporal.class.getName());
            JFXPanel fxPanelTemporalClick = panelChartTemporalClick.getFxPanel();
            String typeTemporalClick = panelChartTemporalClick.getFxChart().getChartType();
            stream = new FileOutputStream(new File(scenario.getOutPath() + "/" + scenario.getName() + "-QE_" + typeTemporalClick + "_TEMPORAL_CLICK.png"));
            BufferedImage bufImageTemporalClick = new BufferedImage(fxPanelTemporalClick.getSize().width, fxPanelTemporalClick.getSize().height, BufferedImage.TYPE_INT_RGB);
            fxPanelTemporalClick.paint(bufImageTemporalClick.createGraphics());
            ImageIO.write(bufImageTemporalClick, "png", stream);
            stream.close();

            modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_STATE_CHART_EXPORT_SUCCESS));
            l.setIcon(Utils.createIcon(Constant.ICON_OK));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            vista.finestraErrore("Error saving image \n" + ex.getLocalizedMessage());
            modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_STATE_CHART_EXPORT_ERROR));
            l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
