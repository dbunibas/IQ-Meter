/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator;

import it.unibas.iqmeter.controller.operator.effort.FactoryEffortGraph;
import it.unibas.iqmeter.controller.operator.effort.ICreatorEffortGraph;
import it.unibas.iqmeter.model.EffortGraph;
import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.iqmeter.model.EffortRecording;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.ping.framework.Applicazione;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;

/**
 *
 * @author Antonio Genovese
 */
public class CalculateEffort {

    private static Log logger = LogFactory.getLog(CalculateEffort.class);

    private CalculateEffort() {
    }

    public static void calculate(MappingTool tool, MappingExecution task) throws DAOException {
        ICreatorEffortGraph creatorGraph = (ICreatorEffortGraph) Applicazione.getInstance().getModello().getBean(ICreatorEffortGraph.class.getName() + tool.getName());
        try {
            if (creatorGraph == null) {
                creatorGraph = FactoryEffortGraph.getOperatorEffort(tool.getName());
                Applicazione.getInstance().getModello().putBean(ICreatorEffortGraph.class.getName() + tool.getName(), creatorGraph);
            }
            UndirectedGraph<EffortGraphNode, DefaultEdge> graph = creatorGraph.getGraph(task.getMappingFilePath(), tool);
            int[] cost = PropertiesLoader.toolFunctionBit(tool.getName());
            EffortGraph effortGraph = new EffortGraph(graph, cost[0], cost[1]);
            effortGraph.setNumSource(creatorGraph.getNumSchemaSource());
            effortGraph.setNumTarget(creatorGraph.getNumSchemaTarget());
            task.setEffortGraph(effortGraph);
        } catch (ClassNotFoundException ex) {
            logger.error(ex.getLocalizedMessage());
            task.setEffortGraph(null);
        }
    }

    public static void calculateClick(MappingTool tool, MappingExecution task, boolean automatic) {
        ThreadRecording recording = ThreadRecording.getInstance();
        if (recording == null) {
            return;
        }
        if (automatic) {
            String process = recording.getProcessFromPanel(tool.getName());
            if (recording.isStarted()) {
                int clickCounter = recording.getClickCounter(process);
                int keyboardCounter = recording.getKeyboardCounter(process);
                EffortRecording effortRecording = new EffortRecording(clickCounter, keyboardCounter);
                task.setEffortRecording(effortRecording);
            }
        } else {
            calculateClick(tool, task);
        }
    }

    private static void calculateClick(MappingTool tool, MappingExecution task) {
        if (logger.isDebugEnabled()) {
            logger.debug("Ask for key: " + EffortRecording.class.getName() + tool.getName());
        }
        EffortRecording recording = (EffortRecording) Applicazione.getInstance().getModello().getBean(EffortRecording.class.getName() + tool.getName());
        if (logger.isDebugEnabled()) {
            logger.debug("Effort recording: " + recording);
        }
        task.setEffortRecording(recording);
    }
}
