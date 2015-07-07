/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.observer;

import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.ping.binding.eventi.EventoModificaCollezione;
import it.unibas.ping.binding.osservatori.OsservatoreJComponentCollezioneAstratto;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.Modello;
import it.unibas.ping.framework.PingException;
import it.unibas.ping.utilita.riflessione.Ispezionatore;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ObserverTree extends OsservatoreJComponentCollezioneAstratto {

    private DefaultTreeModel model;
    private Log logger = LogFactory.getLog(this.getClass());

    public ObserverTree(JComponent component, String bean, Class classBean, String nameBinding) {
        super(component, bean, classBean, nameBinding);
        JTree tree = (JTree) this.getComponente();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Executions", true);
        root.setParent(null);
        model = new DefaultTreeModel(root, false);
        tree.setModel(model);
        this.registra();
    }

    @Override
    public void registra() {
        Applicazione.getInstance().getModello().addOsservatoreCollezione(this);
    }

    @Override
    protected void setValoreComponente(Object o, EventoModificaCollezione emc) {
        JTree tree = (JTree) this.getComponente();
        model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        logger.trace("Set Valore Component called ");
        String methodName = this.costruisciNomeMetodo();
        Ispezionatore ispezionatore = new Ispezionatore(o);
        Object value = ispezionatore.trovaEdEseguiMetodo(methodName);
        if (value == null) {
            this.ripulisci();
            return;
        }
        List list = (List) value;
        int initialIndex = emc.getIndiceIniziale();
        int finalIndex = emc.getIndiceFinale();
        if (finalIndex == Modello.ULTIMO_INDICE) {
            finalIndex = list.size() - 1;
        }
        try {
            MappingTool tool = (MappingTool) o;
            if (emc.getTipo() == Modello.AGGIUNTA && tool.getNumberExecutions() != model.getChildCount(root)) {
                logger.trace("called: --> Action AGGIUNTA ");
                if (initialIndex == 0 && model.getChildCount(root) > 0) {
                    root.removeAllChildren();
                }
                for (int i = initialIndex; i <= finalIndex; i++) {
                    MappingExecution execution = tool.getMappingTask(i);
                    logger.trace("Add execution: " + execution.getNumberLabel() + " for tool " + tool.getName());
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(execution);
                    model.insertNodeInto(child, (MutableTreeNode) model.getRoot(), model.getChildCount(model.getRoot()));
                }
                ripulisci();
            } else if (emc.getTipo() == Modello.ELIMINAZIONE && tool.getNumberExecutions() != model.getChildCount(root)) {
                logger.trace("called: --> Action ELIMINAZIONE ");
                logger.trace("delete Execution, index: " + initialIndex + " from tool " + tool.getName());
                root.remove(initialIndex);
                ripulisci();
            }
        } catch (PingException pe) {
            logger.error("Exception ping for load project " + emc.getTipo() + " " + pe);
        } catch (NullPointerException e) {
            logger.error(e);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
    }

    @Override
    public void ripulisci() {
        model.reload((TreeNode) model.getRoot());
    }
}
