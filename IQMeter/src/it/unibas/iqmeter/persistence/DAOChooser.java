/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
public class DAOChooser {

    private static Log logger = LogFactory.getLog(DAOChooser.class);
    private static final String fileName = System.getProperty("java.io.tmpdir") + "/directoryChooser.txt";

    private DAOChooser() {
    }

    

    public static String getLastDirectory() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String fileContents = reader.readLine();
            return fileContents;
        } catch (FileNotFoundException ex) {
            logger.info("JFileChooser problem reading directory file: " + ex);
            return System.getProperty("user.home");
        } catch (IOException ex) {
            logger.info("JFileChooser problem reading directory file: " + ex);
            return System.getProperty("user.home");
        }
    }

    public static void saveLastDirectory(String lastDirectory) {
        PrintWriter writer = null;
        try {
            FileWriter fileWriter = new java.io.FileWriter(fileName);
            writer = new PrintWriter(fileWriter);
            writer.println(lastDirectory);
        } catch (IOException ioe) {
            logger.error("JFileChooser problem writing directory file: " + ioe);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
