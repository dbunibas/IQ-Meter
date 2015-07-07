/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.test.mock;

import it.unibas.ping.azioni.ActionFactory;
import it.unibas.ping.azioni.AzionePingAstratta;
import it.unibas.ping.azioni.AzioneSwingAvanti;
import it.unibas.ping.azioni.AzioneSwingCommit;
import it.unibas.ping.azioni.AzioneSwingIndietro;
import it.unibas.ping.azioni.AzioneSwingRollback;
import it.unibas.ping.azioni.IAzione;
import it.unibas.ping.azioni.ListenerFactory;
import it.unibas.ping.configurazione.IConfigurazione;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.IControllo;
import it.unibas.ping.framework.IModello;
import it.unibas.ping.framework.IVista;
import it.unibas.ping.framework.Modello;
import it.unibas.ping.framework.PingException;
import it.unibas.ping.framework.Vista;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.util.EventObject;
import javax.swing.Action;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Antonio Genovese
 */
public class MockControllo implements IControllo{

    public void eseguiAzioneIniziale() throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IAzione getAzione(String string) {
        return new AzionePingAstratta() {

            @Override
            public void esegui(EventObject eo) {
                
            }
        };
    }

    public void eseguiAzione(String string, EventObject eo) throws PingException {
        
    }

    public Action getAzioneSwing(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AzioneSwingCommit getAzioneSwingCommit(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AzioneSwingRollback getAzioneSwingRollback(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AzioneSwingAvanti getAzioneSwingAvanti(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AzioneSwingIndietro getAzioneSwingIndietro(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void abilitaAzioneSwing(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabilitaAzioneSwing(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void abilitaAzioneSwingCommit(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabilitaAzioneSwingCommit(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void abilitaAzioneSwingAvanti(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabilitaAzioneSwingAvanti(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AdjustmentListener getAdjustmentListener(String string) throws PingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ChangeListener getChangeListener(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DocumentListener getDocumentListener(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ItemListener getItemListener(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MouseListener getMouseListener(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ListSelectionListener getListSelectionListener(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IConfigurazione getConfigurazione() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setConfigurazione(IConfigurazione ic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Applicazione getApplicazione() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setApplicazione(Applicazione aplczn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IModello getModello() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setModello(Modello mdl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IVista getVista() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setVista(Vista vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ActionFactory getActionFactory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setActionFactory(ActionFactory af) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ListenerFactory getListenerFactory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setListenerFactory(ListenerFactory lf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
