/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Antonio Genovese
 */
public class MappingTool {

    private String name;
    private String translatedInstancePath;
    private String mappingFilePath;
    private String fileScript;
    private List<MappingExecution> executionsList = new ArrayList<MappingExecution>();
    private String directory;

    public MappingTool() {
    }

    public MappingTool(MappingTool tool) {
        this.name = tool.getName();
        this.translatedInstancePath = tool.getTranslatedInstancePath();
        this.mappingFilePath = tool.getMappingFilePath();
        this.fileScript = tool.getFileScript();
        this.executionsList = tool.getExecutionsList();
        this.directory = tool.getDirectory();
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void addMappingExecution(MappingExecution m) {
        this.executionsList.add(m);

    }

    public void setFileScript(String fileScript) {
        this.fileScript = fileScript;
    }

    public MappingExecution lastMappingExecution() {
        if (this.executionsList.isEmpty()) {
            return null;
        }
        return this.executionsList.get(this.executionsList.size() - 1);
    }

    public void resetExecutionLabel() {
        for (int i = 0; i < this.executionsList.size(); i++) {
            MappingExecution task = executionsList.get(i);
            task.setNumberLabel(i + 1);

        }

    }

    public void removeMappingExecution(MappingExecution m) {
        int index = this.executionsList.indexOf(m);
        this.executionsList.remove(index);

    }

    public MappingExecution getMappingTask(int i) {
        return this.executionsList.get(i);

    }

    public List<MappingExecution> getExecutionsList() {
        return executionsList;
    }

    public void setExecutionsList(List<MappingExecution> tasksList) {
        this.executionsList = tasksList;
    }

    public String getMappingFilePath() {
        return mappingFilePath;
    }

    public void setMappingFilePath(String mappingFilePath) {
        this.mappingFilePath = mappingFilePath;
    }

    public String getName() {
        return name;
    }

    public String getFileScript() {
        return fileScript;
    }

    public int getNumberExecutions() {
        return this.executionsList.size();
    }

    public void setName(String toolName) {

        this.name = toolName.replace(" ", "_");
    }

    public String getTranslatedInstancePath() {
        return translatedInstancePath;
    }

    public void setTranslatedInstancePath(String translatedInstancesPath) {
        this.translatedInstancePath = translatedInstancesPath;
    }
    public static Comparator<MappingTool> MappingToolComparatorName = new Comparator<MappingTool>() {

        public int compare(MappingTool tool1, MappingTool tool2) {
            String name1 = tool1.getName();
            String name2 = tool2.getName();
            return name1.compareTo(name2);
        }
    };
}
