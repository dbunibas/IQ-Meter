/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.persistence.DAOChooser;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
@NomeSwing("...")
@DescrizioneSwing("Select File")
public class ActionChooseFile extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());
    private FileDialog chooser;

    @Override
    public void esegui(EventObject eo) {
        FilenameFilter filter = new FilterXML();
        JButton source = (JButton) eo.getSource();
        logger.debug("button Name: " + source.getName());
        String directoryLast = DAOChooser.getLastDirectory();
        chooser = new FileDialog(this.vista.getFramePrincipale(), "Open ", FileDialog.LOAD);
        chooser.setMultipleMode(false);
        chooser.setDirectory(directoryLast);
        chooser.setLocation(this.vista.getFramePrincipale().getLocation());
        if (source.getName().equals(Constant.BUTTON_FILE_EXPECTED)) {
            chooser.setTitle("Select Expected Instance File");
        } else if (source.getName().equals(Constant.BUTTON_FILE_EXCLUSIONS)) {
            chooser.setTitle("Select Exclusions List File");
            filter = new FilterTXT();
        } else if (source.getName().equals(Constant.BUTTON_FILE_MAPPING) || source.getName().equals(Constant.BUTTON_FILE_MAPPING_STEP)) {
            chooser.setTitle("Select Mapping Task File");
            filter = new FilterMappingFile();
        } else if (source.getName().equals(Constant.BUTTON_FILE_SCHEMA_SOURCE)) {
            chooser.setTitle("Select Schema Source File");
            filter = new FilterXSD();
        } else if (source.getName().equals(Constant.BUTTON_FILE_SCHEMA_TARGET)) {
            chooser.setTitle("Select Schema Target File");
            filter = new FilterXSD();
        } else if (source.getName().equals(Constant.BUTTON_FILE_TRANSLATED) || source.getName().equals(Constant.BUTTON_FILE_TRANSLATED_STEP)) {
            chooser.setTitle("Select Generate Instance File");
        } else if (source.getName().equals(Constant.BUTTON_FILE_SCRIPT_STEP) || source.getName().equals(Constant.BUTTON_FILE_SCRIPT)) {
            chooser.setTitle("Select Script File");
            filter = new FilterTXT();
        }

        String fieldName = source.getName().replace("buttonFile", "field");
        setPathFile(filter, fieldName);
    }

    private void setPathFile(FilenameFilter filter, String fieldName) {

        chooser.setFilenameFilter(filter);
        chooser.setVisible(true);

        File[] file = chooser.getFiles();

        if (file.length > 0) {
            JTextField field = (JTextField) vista.getComponente(fieldName);
            logger.info("set Field: " + field.getName());
            String filePath = file[0].getAbsolutePath();
            field.setText(filePath);
            String directory = filePath.substring(0, filePath.lastIndexOf('/') + 1);
            logger.info("set Field: " + field.getText());
            DAOChooser.saveLastDirectory(directory);
        }


    }

    private class FilterXML implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return name.endsWith(".xml");
        }
    }

    private class FilterTXT implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return name.endsWith(".txt");
        }
    }

    private class FilterALL implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return true;
        }
    }

    private class FilterXSD implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return name.endsWith(".xsd");
        }
    }

    private class FilterMappingFile implements FilenameFilter {

        public boolean accept(File dir, String name) {
            String tool = (String) modello.getBean(Constant.COMBO_TOOL_SELECTED);
            return Utils.convalidateFormatFile(tool, name);

        }
    }
}
