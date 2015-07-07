/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Reload")
@DescrizioneSwing("Reload Project")
@AcceleratoreSwing("alt F6")
@MnemonicoSwing("L")
@DisabilitataAllAvvio(true)
public class ActionReloadProject extends AzionePingAstratta {

    @Override
    public void esegui(EventObject eo) {
        Scenario scenario = (Scenario) this.modello.getBean(Scenario.class.getName());
        this.modello.putBean(Constant.DIRECTORY_LOAD_PROJECT, scenario.getOutPath());
        this.controllo.getAzione(ActionOpenProject.class.getName()).esegui(null);
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
        if (status == Constant.STATE_NO_SCENARIO) {
            return true;
        }
        return false;
    }
}
