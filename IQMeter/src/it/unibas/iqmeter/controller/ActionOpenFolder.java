/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.contrib.PingThreadWorker;
import java.awt.Desktop;
import java.io.File;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
@NomeSwing("...")
@AcceleratoreSwing("ctrl shift O")
@MnemonicoSwing("O")
@DescrizioneSwing("Open Output Folder")
@DisabilitataAllAvvio(true)
public class ActionOpenFolder extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        final PingThreadWorker worker = new PingThreadWorker() {

            @Override
            public Object avvia() {
                try {
                    runAction();
                } catch (Exception ex) {
                    logger.error("Problem opening folder: " + ex);
                    vista.finestraErrore("Problem opening folder");
                    controllo.disabilitaAzioneSwing(this.getClass().getName());
                }
                return null;
            }
        };
        worker.avvia();
    }

    private void runAction() throws Exception {
        Scenario scenario = (Scenario) this.modello.getBean(Scenario.class.getName());
        File folder = new File(scenario.getOutPath());

        if (!Desktop.isDesktopSupported()) {
            logger.error("Desktop not supported, problem opening the output folder");
            vista.finestraErrore("Error opening folder, OS not supported for this function.");
            controllo.disabilitaAzioneSwing(this.getClass().getName());
        } else {
            String osName = System.getProperties().getProperty("os.name");
            logger.info("Os name: " + osName);
            //Desktop.getDesktop().browse(folder.toURI());
            if (osName.equalsIgnoreCase("Linux")) {
                Runtime.getRuntime().exec("nautilus " + folder.getPath());
            } else if (osName.contains("Windows")){
                Runtime.getRuntime().exec("explorer "+ folder.getPath());
                // Desktop.getDesktop().open(folder);
                //Desktop.getDesktop().open(new File("c:"));
            }
        }
    }

    @Override
    public boolean abilita(Integer status) {
        if (status == Constant.STATE_SCENARIO_ACTIVE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean disabilita(Integer status) {
        if (status == Constant.STATE_NO_SCENARIO) {
            return true;
        }
        return false;
    }
}
