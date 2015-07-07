/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.io.File;
import java.util.EventObject;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ActionSelectTool extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @SuppressWarnings("unchecked")
    @Override
    public void esegui(EventObject eo) {
        JComboBox combo = (JComboBox) eo.getSource();
        logger.info("Tool Selected index: " + combo.getSelectedIndex());
        if (combo.getSelectedIndex() != -1) {
            List<String> list;
            JTextField fieldScenario;
            JTextField fieldTranslated;
            if (combo.getName().contains("Step")) {
                list = (List<String>) this.modello.getBean(Constant.TOOLS_LIST_NEW);
                fieldScenario = (JTextField) this.vista.getComponente(Constant.FIELD_FILE_MAPPING_STEP);
                fieldTranslated = (JTextField) this.vista.getComponente(Constant.FIELD_FILE_TRANSLATED_STEP);
            } else {
                list = (List<String>) this.modello.getBean(Constant.TOOLS_LIST);
                fieldScenario = (JTextField) this.vista.getComponente(Constant.FIELD_FILE_MAPPING);
                fieldTranslated = (JTextField) this.vista.getComponente(Constant.FIELD_FILE_TRANSLATED);
            }
            this.modello.putBean(Constant.COMBO_TOOL_SELECTED, list.get(combo.getSelectedIndex()));
            initFieldsPath(list.get(combo.getSelectedIndex()), fieldScenario, fieldTranslated);
        }
    }

    private void initFieldsPath(String selectedTool, JTextField fieldScenario, JTextField fieldTranslated) {

        String defaultPath = PropertiesLoader.loadDefaultToolPath(selectedTool);
        Scenario scenario = (Scenario) modello.getBean(Constant.SCENARIO_NEW);
        if (defaultPath.contains(",") && scenario != null) {
            logger.error("Path scenario: " + scenario.getOutPath());
            defaultPath = defaultPath.replaceAll("\\.+\\/", "/");
            if (File.separatorChar == '\\') {
                File file = new File(scenario.getOutPath() + defaultPath.substring(0, defaultPath.indexOf(",")));
                fieldScenario.setText(file.getAbsolutePath());
                file = new File(scenario.getOutPath() + defaultPath.substring(defaultPath.indexOf(",") + 1));
                fieldTranslated.setText(file.getAbsolutePath());
                logger.debug("path default: " + defaultPath);
                logger.debug("path file: " + file.getAbsolutePath());
            } else {
                fieldScenario.setText(scenario.getOutPath() + defaultPath.substring(0, defaultPath.indexOf(",")));
                fieldTranslated.setText(scenario.getOutPath() + defaultPath.substring(defaultPath.indexOf(",") + 1));
            }
        }
    }
}
