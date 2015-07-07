/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class Scenario {

    private String name;
    private String schemaTargetPath;
    private String schemaSourcePath;
    private String expectedInstancePath;
    private String exclusionListPath;
    private String outPath;
    private List<MappingTool> toolsList = new ArrayList<MappingTool>();
    private String[] features;

    public Scenario() {
    }

    public Scenario(Scenario sc) {
        this.name = sc.getName();
        this.schemaTargetPath = sc.getSchemaTargetPath();
        this.schemaSourcePath = sc.getSchemaSourcePath();
        this.expectedInstancePath = sc.getExpectedInstancePath();
        this.exclusionListPath = sc.getExclusionListPath();
        this.outPath = sc.getOutPath();
        this.features = sc.getFeatures();
    }
    
    
    

    public String[] getFeatures() {
        return features;
    }

    public String getSchemaSourcePath() {
        return schemaSourcePath;
    }

    public void setSchemaSourcePath(String schemaSourcePath) {
        this.schemaSourcePath = schemaSourcePath;
    }

    public void setFeatures(String[] features) {
        this.features = features;
    }

    public void setToolsList(List<MappingTool> toolsList) {
        this.toolsList = toolsList;
    }
    
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getLabelName() {
        return "Name: " + name;
    }

//    public String getLabelNameFolder() {
//        return "Name: " + name + "            " + this.getLabelOutPath();
//    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public void addTool(MappingTool tool) {
        this.toolsList.add(tool);
    }

    public List<MappingTool> getToolsList() {
        return this.toolsList;
    }

    public MappingTool getTool(int i) {
        return this.toolsList.get(i);
    }

    public String getExclusionListPath() {
        return exclusionListPath;
    }

    public void setExclusionListPath(String exclusionListPath) {
        this.exclusionListPath = exclusionListPath;
    }

    /**
     * @return the schemaTargetPath
     */
    public String getSchemaTargetPath() {
        return schemaTargetPath;
    }

    /**
     * @param schemaTargetPath the schemaTargetPath to set
     */
    public void setSchemaTargetPath(String schemaTargetPath) {
        this.schemaTargetPath = schemaTargetPath;
    }

    /**
     * @return the expectedTargetPath
     */
    public String getExpectedInstancePath() {
        return expectedInstancePath;
    }

//        public String getLabelExpectedInstancePath() {
//         String out = "..";
//        out += outPath.substring(outPath.lastIndexOf("/"));
//        return "Output Folder: " + out;
//    }
    public void setExpectedInstancePath(String instanceTargetPath) {
        this.expectedInstancePath = instanceTargetPath;
    }

    /**
     * @return the outPath
     */
    public String getOutPath() {
        return outPath;
    }

//    public String getLabelOutPath() {
//        String out = "..";
//        out += outPath.substring(outPath.lastIndexOf(File.separator));
//        return "Output Folder: " + out;
//    }

    @Override
    public String toString() {
        String string = "\n Scenario name: " + this.name + "\n" + "directory: " + outPath + "\n schema: <S> " + schemaSourcePath + " <T> " + schemaTargetPath
                + "\ninstances target: " + expectedInstancePath;
        return string;
    }
}
