/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.view.DialogAnnotationScript;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Annotation Script")
@DescrizioneSwing("Add or Edit Annotation Script for this Execution")
@DisabilitataAllAvvio(true)
@AcceleratoreSwing("ctrl shift S")
@MnemonicoSwing("S")
public class ActionShowEditScript extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_SELECTED);
        JPanel panel = (JPanel) modello.getBean(Constant.TAB_SELECTED);
        MappingTool tool = (MappingTool) modello.getBean(MappingTool.class.getName() + "_" + panel.getName());
        String text = DAOMappingExecution.loadFileScript(task.getMappingFilePath(), tool);
        this.modello.putBean(task.getNumberLabel() + Constant.ANNOTATION_SCRIPT_TEXT, text);
        DialogAnnotationScript dialog = (DialogAnnotationScript) vista.getSottoVista(DialogAnnotationScript.class.getName());
        logger.trace("script text lenght: " + text.length());
        this.controllo.disabilitaAzioneSwing(ActionDeleteScript.class.getName());
        this.controllo.disabilitaAzioneSwing(ActionOverwriteScript.class.getName());
        dialog.setScriptText();
        dialog.setVisible(true);
    }
}
