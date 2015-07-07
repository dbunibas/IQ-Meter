/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.view.wizard.ProjectWizardStep2;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.FinestraDiDialogoPing;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Next >")
@DescrizioneSwing("Continue to the next step")
public class ActionNextStep1 extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        FinestraDiDialogoPing step2 = (FinestraDiDialogoPing) this.vista.getSottoVista(ProjectWizardStep2.class.getName());
        step2.visualizza();
    }
}
