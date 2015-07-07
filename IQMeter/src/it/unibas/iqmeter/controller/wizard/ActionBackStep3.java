/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.view.wizard.ProjectWizardStep2;
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
public class ActionBackStep3 extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        ProjectWizardStep2 step2 =  (ProjectWizardStep2) this.vista.getSottoVista(ProjectWizardStep2.class.getName());
        step2.visualizzaBack();
    }
    
}
