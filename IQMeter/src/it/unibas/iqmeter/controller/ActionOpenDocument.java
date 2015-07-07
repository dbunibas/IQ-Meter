/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Open")
@DescrizioneSwing("Open with system editor")
@MnemonicoSwing("O")
public class ActionOpenDocument extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {

        MappingExecution execution = (MappingExecution) modello.getBean(Constant.EXECUTION_SELECTED);
        File file = new File(execution.getDirectory() + "/similarityResult.txt");
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                //RUNDLL32.EXE SHELL32.DLL,OpenAs_RunDLL <file.ext>
                String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
                Runtime.getRuntime().exec(cmd);
            } else {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            vista.finestraErrore(Constant.MESSAGE_ERROR_LOAD_QUALITY_FILE);
        } catch (UnsupportedOperationException ex) {
            logger.error(ex.getMessage());
            vista.finestraErrore(Constant.MESSAGE_ERROR_ACTION_OPEN_DOCUMENT);
            this.disabilita(0);
        }
    }
}
