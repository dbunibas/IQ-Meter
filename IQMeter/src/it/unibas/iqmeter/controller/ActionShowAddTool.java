/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.iqmeter.view.DialogAddTool;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Add Mapping Tool")
@DescrizioneSwing("Add a Mapping Tool to the  Project")
@MnemonicoSwing("A")
@AcceleratoreSwing("ctrl A")
@DisabilitataAllAvvio(true)
public class ActionShowAddTool extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void esegui(EventObject eo) {
        Scenario sc = (Scenario) this.modello.getBean(Scenario.class.getName());
        try {
            if (this.modello.getBean(Constant.TOOLS_LIST) == null) {
                this.modello.putBean(Constant.TOOLS_LIST, PropertiesLoader.loadAvailableTools());
            }
            this.modello.putBean(MappingTool.class.getName(), new MappingToolPM());
            DialogAddTool tool = (DialogAddTool) this.vista.getSottoVista(DialogAddTool.class.getName());
            tool.buildCombo();
            vista.visualizzaSottoVista(DialogAddTool.class.getName());
        } catch (DAOException ex) {
            logger.error(ex);
            this.vista.finestraErrore(ex.toString());
        }
    }

    @Override
    public boolean abilita(Integer status) {
        if (status == Constant.STATE_SCENARIO_ACTIVE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean disabilita(Integer status) {
        if (status == Constant.STATE_NO_SCENARIO || status == Constant.STATE_NO_TOOLS_AVAILABLE) {
            return true;
        }
        return false;
    }
}
