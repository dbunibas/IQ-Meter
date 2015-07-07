/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.wizard;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.iqmeter.view.presentationmodel.ScenarioPM;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep1;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;

/**
 *
 * @author antonio
 */
@NomeSwing("New")
@MnemonicoSwing("N")
@AcceleratoreSwing("ctrl N")
@DescrizioneSwing("New Project")
public class ActionNewProject extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        //this.modello.putBean(Constant.SCENARIO_NEW, new Scenario());
        this.modello.putBean(Constant.SCENARIO_NEW, new ScenarioPM());
//        this.modello.putBean(Constant.TOOL_NEW, new MappingTool());
        
        this.modello.putBean(Constant.TOOL_NEW, new MappingToolPM());
        this.modello.putBean(Constant.COMBO_TOOL_SELECTED_INDEX, 0);
        vista.visualizzaSottoVista(ProjectWizardStep1.class.getName());
    }
    
    
}
