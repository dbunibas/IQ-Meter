/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@DescrizioneSwing("Undo changes")
public class ActionCancel extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {

            ProjectWizardStep3 dialog = (ProjectWizardStep3) this.vista.getSottoVista(ProjectWizardStep3.class.getName());
            dialog.setVisible(false);
            dialog.cleanExclusionElement();

        }


    }
