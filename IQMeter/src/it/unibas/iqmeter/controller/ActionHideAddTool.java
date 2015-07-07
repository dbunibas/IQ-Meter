/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import javax.swing.JPanel;

/**
 *
 * @author Antonio Genovese
 */
@DescrizioneSwing("Undo changes")
@MnemonicoSwing("C")
public class ActionHideAddTool extends AzionePingAstratta{

    @Override
    public void esegui(EventObject eo) {
        JPanel panelTools = (JPanel) vista.getComponente("panelTools");
        panelTools.setVisible(false);
    }
    
}
