/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.view.DialogExplainQuality;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.awt.Frame;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Explain Quality")
@DescrizioneSwing("Show Quality Details")
@DisabilitataAllAvvio(true)
@AcceleratoreSwing("ctrl P")
@MnemonicoSwing("P")
public class ActionExplainQuality extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        try {
            MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_SELECTED);
            String text = DAOMappingExecution.loadSimilarityResultText(task.getDirectory()).toString();
            this.modello.putBean(task.getNumberLabel() + Constant.QUALITY_TEXT, text);
            DialogExplainQuality dialog = (DialogExplainQuality) vista.getSottoVista(DialogExplainQuality.class.getName());
            logger.trace("quality text lenght: " + text.length());
            dialog.setTextQuality();

            dialog.setState(Frame.NORMAL);
            //dialog.toFront();
            dialog.setVisible(true);
            
        } catch (DAOException ex) {
            vista.finestraErrore("Error to read quality details: \n" + ex.getLocalizedMessage());
        }
    }
}
