/*
 Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
 Giansalvatore Mecca - giansalvatore.mecca@unibas.it
 Salvatore Raunich - salrau@gmail.com

 This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool

 ++Spicy is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 ++Spicy is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.unibas.spicybenchmark;

import it.unibas.spicy.persistence.xml.operators.TransformFilePaths;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import simpack.measure.tree.TreeEditDistance;

public class Configuration {

    private String configurationFilePath;
    private String experimentName;
    private String schemaPath;
    private String expectedInstancePath;
    private String translatedInstancePath;
    private String exclusionsFile;
    private List<String> features = new ArrayList<String>();
    private boolean skipSetElement;
    private boolean treeEditDistanceValiente = false;
    private double treeEditDistanceValienteWeightInsert = TreeEditDistance.DEFAULT_WEIGHT_INSERT;
    private double treeEditDistanceValienteWeightDelete = TreeEditDistance.DEFAULT_WEIGHT_DELETE;
    private double treeEditDistanceValienteWeightSubstitute = TreeEditDistance.DEFAULT_WEIGHT_SUBSTITUE;
    private String treeEditDistanceValienteTreeNodeComparator;
    private String treeEditDistanceValienteDistanceConversion;
    private boolean treeEditDistanceShasha = false;
    private String logMessages;
    private String logAppender;
    private String logFile;
    private TransformFilePaths filePathTransformer = new TransformFilePaths();

    public Configuration(String configurationFilePath) {
        this.configurationFilePath = new File(configurationFilePath).getAbsolutePath();
    }

    public String getExpectedInstanceAbsolutePath() {
        return filePathTransformer.expand(configurationFilePath, expectedInstancePath);
    }

    public String getExpectedInstancePath() {
        return expectedInstancePath;
    }

    public void setExpectedInstancePath(String expectedInstancePath) {
        this.expectedInstancePath = expectedInstancePath;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExclusionsFileAbsolutePath() {
        return filePathTransformer.expand(configurationFilePath, exclusionsFile);
    }

    public String getExclusionsFile() {
        return exclusionsFile;
    }

    public void setExclusionsFile(String exclusionsFile) {
        this.exclusionsFile = exclusionsFile;
    }

    public String getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(String logMessages) {
        this.logMessages = logMessages;
    }

    public String getLogAppender() {
        return logAppender;
    }

    public void setLogAppender(String logAppender) {
        this.logAppender = logAppender;
    }

    public String getLogFile() {
        return filePathTransformer.expand(configurationFilePath, logFile);
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public String getSchemaAbsolutePath() {
        return filePathTransformer.expand(configurationFilePath, schemaPath);
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    public String getTranslatedInstanceAbsolutePath() {
        return filePathTransformer.expand(configurationFilePath, translatedInstancePath);
    }

    public String getTranslatedInstancePath() {
        return translatedInstancePath;
    }

    public void setTranslatedInstancePath(String translatedInstancePath) {
        this.translatedInstancePath = translatedInstancePath;
    }

    public void addFeature(String feature) {
        this.features.add(feature);
    }

    public List<String> getFeatures() {
        return features;
    }

    public boolean isTreeEditDistanceValiente() {
        return treeEditDistanceValiente;
    }

    public void setTreeEditDistanceValiente(boolean ted) {
        this.treeEditDistanceValiente = ted;
    }

    public double getTreeEditDistanceValienteWeightDelete() {
        return treeEditDistanceValienteWeightDelete;
    }

    public void setTreeEditDistanceValienteWeightDelete(double treeEditDistanceValienteWeightDelete) {
        this.treeEditDistanceValienteWeightDelete = treeEditDistanceValienteWeightDelete;
    }

    public double getTreeEditDistanceValienteWeightInsert() {
        return treeEditDistanceValienteWeightInsert;
    }

    public void setTreeEditDistanceValienteWeightInsert(double treeEditDistanceValienteWeightInsert) {
        this.treeEditDistanceValienteWeightInsert = treeEditDistanceValienteWeightInsert;
    }

    public double getTreeEditDistanceValienteWeightSubstitute() {
        return treeEditDistanceValienteWeightSubstitute;
    }

    public void setTreeEditDistanceValienteWeightSubstitute(double treeEditDistanceValienteWeightSubstitute) {
        this.treeEditDistanceValienteWeightSubstitute = treeEditDistanceValienteWeightSubstitute;
    }

    public String getTreeEditDistanceValienteDistanceConversion() {
        return treeEditDistanceValienteDistanceConversion;
    }

    public void setTreeEditDistanceValienteDistanceConversion(String treeEditDistanceValienteDistanceConversion) {
        this.treeEditDistanceValienteDistanceConversion = treeEditDistanceValienteDistanceConversion;
    }

    public String getTreeEditDistanceValienteTreeNodeComparator() {
        return treeEditDistanceValienteTreeNodeComparator;
    }

    public void setTreeEditDistanceValienteTreeNodeComparator(String treeEditDistanceValienteTreeNodeComparator) {
        this.treeEditDistanceValienteTreeNodeComparator = treeEditDistanceValienteTreeNodeComparator;
    }

    public boolean isTreeEditDistanceShasha() {
        return treeEditDistanceShasha;
    }

    public void setTreeEditDistanceShasha(boolean ted) {
        this.treeEditDistanceShasha = ted;
    }

    public boolean printOnFile() {
        return (logAppender.equalsIgnoreCase(DAOConfiguration.LOG_APPENDER_FILE) || logAppender.equalsIgnoreCase(DAOConfiguration.LOG_APPENDER_BOTH));
    }

    public boolean printOnStdOut() {
        return (logAppender.equalsIgnoreCase(DAOConfiguration.LOG_APPENDER_STDOUT) || logAppender.equalsIgnoreCase(DAOConfiguration.LOG_APPENDER_BOTH));
    }

    public boolean isSkipSetElement() {
        return skipSetElement;
    }

    public void setSkipSetElement(boolean skipSetElement) {
        this.skipSetElement = skipSetElement;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("\n------------ Configuration -------------\n");
        result.append("Experiment Name: " + experimentName + "\n");
        result.append("Schema File: " + schemaPath + "\n");
        result.append("Expected Instance: " + expectedInstancePath + "\n");
        result.append("Translated Instance: " + translatedInstancePath + "\n");
        result.append("Exclusions file: " + exclusionsFile + "\n");
        result.append("Skip Set element: " + skipSetElement);
        result.append("Log Messages: " + logMessages + "\n");
        result.append("Log Appender: " + logAppender + "\n");
        result.append("Log File: " + logFile + "\n");
        result.append("Features: " + "\n");
        for (String feature : features) {
            result.append("    " + feature + "\n");
        }
        return result.toString();
    }
}
