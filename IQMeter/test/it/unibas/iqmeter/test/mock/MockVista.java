/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.test.mock;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.ping.configurazione.IConfigurazione;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.FramePing;
import it.unibas.ping.framework.IControllo;
import it.unibas.ping.framework.IModello;
import it.unibas.ping.framework.ISottoVista;
import it.unibas.ping.framework.IVista;
import it.unibas.ping.framework.Modello;
import it.unibas.ping.framework.PingException;
import it.unibas.ping.utilita.IPosizione;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Antonio Genovese
 */
public class MockVista implements IVista {

    public void inizializza() throws PingException {
    }

    public void inizializzaViste() throws PingException {
    }

    public void eseguiSchermo(String string) {
    }

    public void visualizzaSottoVista(String string) {
    }

    public void nascondiVista(String string) {
    }

    public void finestraErrore(String string) {
    }

    public JComponent getComponente(String string) {
        if (string.equals("labelIconStatus")) {
            return new JLabel();
        } else if (string.equals(Constant.PROGRESS_BAR)) {
            return new JProgressBar();
        }
        return new JPanel();

    }

    public JButton getBottone(String string) {
        return new JButton();

    }

    public void assegnaAzione(String string, String string1) {
    }

    public Object getValore(String string) {
        return null;
    }

    public void setValore(String string, Object o) {
    }

    public Integer getIndiceSelezione(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IPosizione getPosizioneCellaSelezione(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putSottoVista(String string, ISottoVista isv) {
    }

    public ISottoVista getSottoVista(String string) {
        if(string.equals(Constant.PANEL_CONTAINER_CHART)){
            return new PanelChart();
        }
        return null;
    }

    public void removeSottoVista(ISottoVista isv) {
    }

    public int getNumeroSottoViste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Collection getSottoViste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Applicazione getApplicazione() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setApplicazione(Applicazione aplczn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IConfigurazione getConfigurazione() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setConfigurazione(IConfigurazione ic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IModello getModello() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setModello(Modello mdl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IControllo getControllo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setControllo(Controllo cntrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FramePing getFramePrincipale() {
        return new MockView();
    }

    public void setFramePrincipale(FramePing fp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
