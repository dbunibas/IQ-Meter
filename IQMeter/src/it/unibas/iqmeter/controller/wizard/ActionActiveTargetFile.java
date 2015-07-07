/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;


import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;

/**
 *
 * @author antonio
 */
@DescrizioneSwing("Active Exclusions List File")

public class ActionActiveTargetFile extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        ProjectWizardStep3 dialog = (ProjectWizardStep3) this.vista.getSottoVista(ProjectWizardStep3.class.getName());
        dialog.redisplayExclusionElement();
    }
    
}
