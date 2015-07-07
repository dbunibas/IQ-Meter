/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.view.wizard.ProjectWizardStep1;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("< Back")
@DescrizioneSwing("Return to the back step")
public class ActionBackStep2 extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        ProjectWizardStep1 step1 =  (ProjectWizardStep1) this.vista.getSottoVista(ProjectWizardStep1.class.getName());
        step1.visualizzaBack();
    }
    
}
