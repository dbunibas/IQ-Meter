/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep4;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Next >")
@DescrizioneSwing("Continue to the next step")
public class ActionNextStep3 extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        ProjectWizardStep4 step4 = (ProjectWizardStep4) this.vista.getSottoVista(ProjectWizardStep4.class.getName());
        if (this.modello.getBean(Constant.TOOLS_LIST_NEW) == null) {
            try {
                this.modello.putBean(Constant.TOOLS_LIST_NEW, PropertiesLoader.loadAvailableTools());
            } catch (DAOException ex) {
                logger.error(ex.getLocalizedMessage());
                vista.finestraErrore(Constant.MESSAGE_ERROR_LOAD_TOOLS_LIST);
            }
        }
        step4.buildCombo();
        step4.visualizza();
    }
}
