/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import com.mxgraph.view.mxGraph;
import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.iqmeter.view.effortgraph.DialogEffortGraph;
import it.unibas.iqmeter.view.effortgraph.layout.DefaultEffortGraphLayout;
import it.unibas.iqmeter.view.effortgraph.layout.IEffortGraphLayout;
import it.unibas.ping.annotazioni.AcceleratoreSwing;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.DisabilitataAllAvvio;
import it.unibas.ping.annotazioni.MnemonicoSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.awt.Frame;
import java.util.EventObject;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
@NomeSwing("Effort Graph")
@DescrizioneSwing("Show Effort Graph")
@DisabilitataAllAvvio(true)
@AcceleratoreSwing("ctrl G")
@MnemonicoSwing("G")
public class ActionShowEffortGraph extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());
    private IEffortGraphLayout layoutGraph;

    @Override
    public void esegui(EventObject eo) {
        MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_SELECTED);
        //Note the name of the tab is the same as the name of the tool
        JPanel panel = (JPanel) modello.getBean(Constant.TAB_SELECTED);
        //MappingTool tool = (MappingTool) this.modello.getBean(MappingTool.class.getName() + "_" + panel.getName());
        logger.info("Panel Selected name: " + panel.getName());
        if (task != null && task.getEffortGraph() != null) {
            mxGraph mxgraph = (mxGraph) modello.getBean(task.getTimeLabel() + "_mxGraph");
            if (mxgraph == null) {
                layoutGraph = (IEffortGraphLayout) modello.getBean(IEffortGraphLayout.class.getName() + panel.getName());
                if (layoutGraph == null) {
                    this.loadLayout(panel.getName());
                    this.modello.putBean(IEffortGraphLayout.class.getName() + panel.getName(), layoutGraph);
                }
                mxgraph = layoutGraph.createmxGraph(task.getEffortGraph());
            }
            modello.putBean(task.getDirectory() + "_mxGraph", mxgraph);
            //instance unique
            DialogEffortGraph dialog = (DialogEffortGraph) vista.getSottoVista(DialogEffortGraph.class.getName());
            //multi-instance
            //DialogEffortGraph dialog = new DialogEffortGraph();
            //dialog.inizializza();
            dialog.buildGraph();
            dialog.setState(Frame.NORMAL);
            
            dialog.setVisible(true);

        } else if (task != null && task.getEffortGraph() == null) {
            this.vista.finestraErrore(Constant.MESSAGE_NO_GRAPH);
        }
    }

    private void loadLayout(String toolName) {
        try {
            String graphLayout = PropertiesLoader.loadGraphLayout(toolName);
            logger.debug("String Operator Effort Class: " + graphLayout);
            Class<?> o = Class.forName(graphLayout);
            logger.debug("Class create: " + o.getName());
            layoutGraph = (IEffortGraphLayout) o.newInstance();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            layoutGraph = new DefaultEffortGraphLayout();
        }
    }
}
