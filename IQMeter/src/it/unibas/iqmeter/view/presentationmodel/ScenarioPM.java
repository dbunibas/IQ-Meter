/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.presentationmodel;

import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.ping.annotazioni.BindingPing;
import java.util.List;

/**
 *
 * @author Antonio Genovese
 */
public class ScenarioPM extends Scenario {

    public ScenarioPM() {
        super();

    }

    public ScenarioPM(Scenario sc) {
        super(sc);
    }

    @Override
    public String[] getFeatures() {
        return super.getFeatures();
    }

    @BindingPing("schemaSource")
    @Override
    public String getSchemaSourcePath() {
        return super.getSchemaSourcePath();
    }

    @Override
    public void setSchemaSourcePath(String schemaSourcePath) {
        super.setSchemaSourcePath(schemaSourcePath);
    }

    @Override
    public void setFeatures(String[] features) {
        super.setFeatures(features);
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return super.getName();
    }

    @BindingPing("name")
    @Override
    public String getLabelName() {
        return super.getLabelName();
    }

//    @BindingPing("nameFolder")
//    public String getLabelNameFolder() {
//        return super.getLabelNameFolder();
//    }
    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setOutPath(String outPath) {
        super.setOutPath(outPath);
    }

    @Override
    public void addTool(MappingTool tool) {
        super.addTool(tool);
    }

    @Override
    public List<MappingTool> getToolsList() {
        return super.getToolsList();
    }

    @Override
    public MappingTool getTool(int i) {
        return super.getTool(i);
    }

    @BindingPing("exclusionListPath")
    @Override
    public String getExclusionListPath() {
        return super.getExclusionListPath();
    }

    @Override
    public void setExclusionListPath(String exclusionListPath) {
        super.setExclusionListPath(exclusionListPath);
    }

    /**
     * @return the schemaTargetPath
     */
    @BindingPing("schemaTarget")
    @Override
    public String getSchemaTargetPath() {
        return super.getSchemaTargetPath();
    }

    /**
     * @param schemaTargetPath the schemaTargetPath to set
     */
    @Override
    public void setSchemaTargetPath(String schemaTargetPath) {
        super.setSchemaTargetPath(schemaTargetPath);
    }

    /**
     * @return the expectedTargetPath
     */
    @BindingPing("expectedInstance")
    @Override
    public String getExpectedInstancePath() {
        return super.getExpectedInstancePath();
    }

//        public String getLabelExpectedInstancePath() {
//         String out = "..";
//        out += outPath.substring(outPath.lastIndexOf("/"));
//        return "Output Folder: " + out;
//    }
    @Override
    public void setExpectedInstancePath(String instanceTargetPath) {
        super.setExpectedInstancePath(instanceTargetPath);
    }

    /**
     * @return the outPath
     */
    @BindingPing("outputFolder")
    @Override
    public String getOutPath() {
        return super.getOutPath();
    }

//    @BindingPing("outputFolderLabel")
//    public String getLabelOutPath() {
//        return super.getLabelOutPath();
//    }
    @Override
    public String toString() {
        return super.toString();
    }
}
