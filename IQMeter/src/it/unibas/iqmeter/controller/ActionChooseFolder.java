/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller;



import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.persistence.DAOChooser;
import it.unibas.ping.annotazioni.DescrizioneSwing;
import it.unibas.ping.annotazioni.NomeSwing;
import it.unibas.ping.azioni.AzionePingAstratta;
import java.util.EventObject;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
@NomeSwing("...")
@DescrizioneSwing("Select Folder")
public class ActionChooseFolder extends AzionePingAstratta {

    private Log logger = LogFactory.getLog(this.getClass());


    @Override
    public void esegui(EventObject eo) {

//
//         try {
//           // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//           // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//
//           
//        } catch (Exception ex) {
//           System.out.println("Error loading look and feel");
        //} 
        String directoryLast = DAOChooser.getLastDirectory();

        JFileChooser chooser = new JFileChooser(directoryLast);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Project Folder");
        int result = chooser.showOpenDialog(chooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            String directory = chooser.getSelectedFile().getAbsolutePath();
            //JButton source = (JButton) eo.getSource();
            //if (source != null && source.getName().equals(Constant.BUTTON_FOLDER_WIZARD)) {
            if (directory.endsWith("/.")) {
                directory = directory.replace("/.", "");

            } else if (directory.endsWith("/..")) {
                directory = directory.replace("/..", "");
                directory = directory.substring(0, directory.lastIndexOf("/"));

            }
            if (eo != null) {
                JTextField field = (JTextField) vista.getComponente("fieldOutput");
                field.setText(directory);
            } else {
                this.modello.putBean(Constant.DIRECTORY_LOAD_PROJECT, directory);
            }
            DAOChooser.saveLastDirectory(directory);
        }

    }
}
