/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.ActionAddTool;
import it.unibas.iqmeter.controller.ActionRecord;
import it.unibas.iqmeter.controller.operator.ThreadMappingComparison;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOScenario;
import it.unibas.iqmeter.view.View;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.IModello;
import it.unibas.ping.framework.IVista;
import it.unibas.ping.framework.MessaggioPing;
import it.unibas.ping.framework.StatoPing;
import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
@DescrizioneSwing("Create new Project")
public class ActionFinishStep4 extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());
    private IModello modello;
    private IVista vista;

    public ActionFinishStep4() {
        super();
    }
//for test
    public ActionFinishStep4(IModello modello, IVista vista) {
        this.modello = modello;
        this.vista = vista;
    }

    @Override
    public void esegui(EventObject eo) {
        if (this.modello == null || this.vista == null) {
            this.modello = super.getModello();
            this.vista = super.getVista();
        }

        try {
            Scenario sc = (Scenario) this.modello.getBean(Constant.SCENARIO_NEW);
            ProjectWizardStep3 dialog = (ProjectWizardStep3) this.vista.getSottoVista(ProjectWizardStep3.class.getName());
            String[] listExclusions = null;
            if (dialog != null) {
                JTextArea textFunction = (JTextArea) super.vista.getComponente(Constant.TEXT_AREA_FEATURES);
                sc.setFeatures(textFunction.getText().split("\n"));
                JRadioButton button = (JRadioButton) super.vista.getComponente(Constant.RADIO_BUTTON_EXCLUSIONS);
                if (button.isSelected()) {
                    JTextField field = (JTextField) super.vista.getComponente(Constant.FIELD_FILE_EXCLUSIONS);
                    sc.setExclusionListPath(field.getText());
                } else {
                    JTextArea textExclusionList = (JTextArea) super.vista.getComponente(Constant.TEXT_AREA_EXCLUSION);
                    listExclusions = textExclusionList.getText().split("\n");
                }
            }
            JCheckBox checkCopy = (JCheckBox) vista.getComponente(Constant.CHECK_COPY);
            DAOScenario.copyFilesToOut(sc, listExclusions, checkCopy.isSelected());
            if (modello.getBean(Scenario.class.getName()) != null) {
                View view = (View) this.vista.getFramePrincipale();
                view.removeAllTab();
                this.modello.removeBean(Constant.TOOLS_LIST);
                cleanModello();
            }
            this.modello.putBean(Scenario.class.getName(), sc);
            this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_NEW_SCENARIO));
            this.modello.removeBean(Constant.SCENARIO_NEW);
            JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
            l.setIcon(Utils.createIcon(Constant.ICON_OK));
            if (dialog != null) {
                dialog.cleanExclusionElement();
            }
            vista.getComponente(Constant.PANEL_CONTAINER_CHART).setVisible(false);
            this.controllo.eseguiAzione(ActionAddTool.class.getName(), null);
            this.controllo.abilitaAzioneSwing(ActionRecord.class.getName());
        } catch (DAOException ex) {
            vista.finestraErrore("Problem to create Project, because file Schema or file instances Target is not found.\nSorry, recreate Project");
            JLabel l = (JLabel) vista.getComponente(Constant.LABEL_ICON_STATUS);
            l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
            this.modello.putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_ERROR_SCENARIO));
        }
    }

    private void cleanModello() {
        Scenario sc = (Scenario) modello.getBean(Scenario.class.getName());
        if (!sc.getToolsList().isEmpty()) {
            for (MappingTool tool : sc.getToolsList()) {
                modello.removeBean(MappingTool.class.getName() + "_" + tool.getName());
            }
            modello.removeBean(Constant.TAB_SELECTED);
        }
        Utils.closeAllDialog();
        ThreadMappingComparison.clearMapTool();
        logger.debug("Modello cleaned");
        modello.removeBean(Constant.EXECUTION_SELECTED);
    }
}
