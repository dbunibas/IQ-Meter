/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.presentationmodel;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.ping.annotazioni.BindingPing;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.Modello;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class MappingToolPM extends MappingTool {

    private Log logger = LogFactory.getLog(this.getClass());

    public MappingToolPM() {
        super();
    }

    public MappingToolPM(MappingTool tool) {
        super(tool);

    }

    @Override
    public void addMappingExecution(MappingExecution m) {
        super.addMappingExecution(m);
        Applicazione.getInstance().getModello().notificaModificaCollezione(this, Constant.EXECUTIONS_LIST, this.getNumberExecutions() - 1, Modello.AGGIUNTA);
        logger.trace("ADD Mapping Execution " + m.getNumberLabel());
    }

    @Override
    public void removeMappingExecution(MappingExecution m) {
        super.removeMappingExecution(m);
        Applicazione.getInstance().getModello().notificaModificaCollezione(this, Constant.EXECUTIONS_LIST, m.getNumberLabel() - 1, Modello.ELIMINAZIONE);
        logger.trace("REMOVE Mapping Execution " + m.getNumberLabel());
    }

    @Override
    public String getDirectory() {
        return super.getDirectory();
    }

    @Override
    public void setDirectory(String directory) {
        super.setDirectory(directory);
    }

    @Override
    public void setFileScript(String fileScript) {
        super.setFileScript(fileScript);
    }

    @Override
    public void resetExecutionLabel() {
        super.resetExecutionLabel();
    }

    @Override
    public MappingExecution getMappingTask(int i) {
        return super.getMappingTask(i);

    }

    @BindingPing(Constant.EXECUTIONS_LIST)
    @Override
    public List<MappingExecution> getExecutionsList() {
        return super.getExecutionsList();
    }

    @Override
    public void setExecutionsList(List<MappingExecution> tasksList) {
        super.setExecutionsList(tasksList);
    }

    @Override
    public MappingExecution lastMappingExecution() {
        return super.lastMappingExecution();
    }

    @BindingPing("mappingPath")
    @Override
    public String getMappingFilePath() {
        return super.getMappingFilePath();
    }

    @Override
    public void setMappingFilePath(String mappingFilePath) {
        super.setMappingFilePath(mappingFilePath);
    }

    @BindingPing("toolName")
    @Override
    public String getName() {
        return super.getName();
    }

    @BindingPing("scriptFile")
    @Override
    public String getFileScript() {
        return super.getFileScript();
    }

    @Override
    public int getNumberExecutions() {
        return super.getNumberExecutions();
    }

    @Override
    public void setName(String toolName) {
        super.setName(toolName);
    }

    @BindingPing("translatedInstance")
    @Override
    public String getTranslatedInstancePath() {
        return super.getTranslatedInstancePath();
    }

    @Override
    public void setTranslatedInstancePath(String translatedInstancesPath) {
        super.setTranslatedInstancePath(translatedInstancesPath);
    }
}
