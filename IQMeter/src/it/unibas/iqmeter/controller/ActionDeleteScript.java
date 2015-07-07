/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.iqmeter.view.DialogAnnotationScript;
import it.unibas.iqmeter.view.View;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Delete")
@DescrizioneSwing("Delete Annotation Script")
@MnemonicoSwing("D")
@DisabilitataAllAvvio(true)
public class ActionDeleteScript extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        View view = (View) this.vista.getSottoVista(View.class.getName());
        DialogAnnotationScript dialog = (DialogAnnotationScript) vista.getSottoVista(DialogAnnotationScript.class.getName());
        MappingExecution execution = (MappingExecution) modello.getBean(Constant.EXECUTION_SELECTED);
        dialog.setVisible(false);
        int choice = view.showDialogConfirm(Constant.MESSAGE_DIALOG_DELETE_SCRIPT,
                "Confirm to delete Annotation Script for Execution #" + execution.getNumberLabel());
        logger.trace("Confirm Choise: " + choice);
        if (choice == 0) {
            JPanel panel = (JPanel) modello.getBean(Constant.TAB_SELECTED);
            MappingTool tool = (MappingTool) modello.getBean(MappingTool.class.getName() + "_" + panel.getName());
            boolean isDeleted = DAOMappingExecution.deleteFileScript(execution, tool);
            if (isDeleted) {
                controllo.eseguiAzione(ActionOpenProject.class.getName(), eo);
            } else {
                JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
                vista.finestraErrore("Sorry, it was not possible to delete the file " + Constant.SCRIPT_FILE_NAME);
                modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_STATE_ERROR_DELETE_SCRIPT));
                l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
            }
            dialog.setVisible(false);
        } else {
            dialog.setVisible(true);
        }
    }
}
