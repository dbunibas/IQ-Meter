/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import javax.swing.JComboBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("< Back")
@DescrizioneSwing("Return to the back step")
public class ActionBackStep4 extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        ProjectWizardStep3 step3 = (ProjectWizardStep3) this.vista.getSottoVista(ProjectWizardStep3.class.getName());

        JComboBox combo = (JComboBox) vista.getComponente(Constant.COMBO_TOOL_STEP);
        logger.trace("Combo Action step4 back " + combo.getSelectedIndex());
        modello.putBean(Constant.COMBO_TOOL_SELECTED_INDEX, combo.getSelectedIndex());
        step3.visualizzaBack();
    }
}
