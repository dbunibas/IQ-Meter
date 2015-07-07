/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.Scenario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jopendocument.util.FileUtils;

/**
 *
 * @author antonio
 */
public class DAOScenario {

    private static Log logger = LogFactory.getLog(DAOScenario.class);

    private DAOScenario() {
    }

    public static void copyFilesToOut(Scenario sc, String[] list, boolean copyTarget) throws DAOException {
        String pathOut = sc.getOutPath() + "/" + sc.getName();
        File directory = new File(sc.getOutPath() + "/" + sc.getName());
        directory.mkdir();
        sc.setOutPath(sc.getOutPath() + "/" + sc.getName());

        if (copyTarget) {
            File instanceTarget = new File(sc.getExpectedInstancePath());
            copyFile(instanceTarget, pathOut);
            sc.setExpectedInstancePath(sc.getOutPath() + "/" + instanceTarget.getName());
        }

        if (sc.getExclusionListPath() != null) {
            File exclusionList = new File(sc.getExclusionListPath());
            copyFile(exclusionList, pathOut);
            exclusionList = new File(pathOut + "/" + exclusionList.getName());
            exclusionList.renameTo(new File(pathOut + "/" + "exclusionsList.txt"));
        } else {
            String exclusion = createTmpFileExclusion(pathOut, list);
            logger.debug("Exclusion list: " + exclusion);
            sc.setExclusionListPath(exclusion);
        }
        File schemaTarget = new File(sc.getSchemaTargetPath());
        File schemaSource = new File(sc.getSchemaSourcePath());
        copyFile(schemaTarget, pathOut);
        copyFile(schemaSource, pathOut);
        saveFileProperties(sc);
        logger.debug("Scenario saved:" + sc);
    }

    private static String createTmpFileExclusion(String pathOut, String[] list) throws DAOException {
        FileWriter filew = null;
        try {
            File file = new File(pathOut + "/exclusionsList.txt");
            if (file.exists()) {
                file.delete();
            }

            filew = new FileWriter(file, true);
            if (list != null) {
                for (int i = 0; i < list.length - 1; i++) {
                    filew.write(list[i] + "\n");
                }
                filew.write(list[list.length - 1]);
            }
            filew.close();
            return file.getAbsolutePath();
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException(ex);
        } finally {
            try {
                filew.close();
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage());
            }
        }
    }

    private static void copyFile(File f1, String pathOut) throws DAOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            String namef2 = f1.getName();
            in = new FileInputStream(f1);
            //For Overwrite the file.
            out = new FileOutputStream(new File(pathOut + "/" + namef2));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            logger.debug("File " + namef2 + " copied.");
        } catch (FileNotFoundException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException(ex);
        } catch (IOException ex) {

            throw new DAOException(ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception ex) {
            }
        }
    }

    private static void saveFileProperties(Scenario sc) throws DAOException {
        Properties prop = new Properties();

        try {
            //set the properties value
            prop.setProperty("project_name", sc.getName());
            prop.setProperty("instance_expected", Utils.toRelativePath(sc.getOutPath(), sc.getExpectedInstancePath()));
            String sourceSchema = sc.getSchemaSourcePath().substring(sc.getSchemaSourcePath().lastIndexOf(File.separator) + 1);
            prop.setProperty("schema_source", sourceSchema);
            String targetSchema = sc.getSchemaTargetPath().substring(sc.getSchemaTargetPath().lastIndexOf(File.separator) + 1);
            prop.setProperty("schema_target", targetSchema);
            sc.setSchemaSourcePath(sc.getOutPath() + "/" + sourceSchema);
            sc.setSchemaTargetPath(sc.getOutPath() + "/" + targetSchema);
            String features = "";
            for (int i = 0; i < sc.getFeatures().length; i++) {
                features += sc.getFeatures()[i] + ";";
            }
            prop.setProperty("features", features);
            //save properties to project root folder
            prop.store(new FileOutputStream(sc.getOutPath() + "/project.properties"), Constant.MESSAGE_FILE_PROPERTIES + "Project");
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to build the project.properties file");
        }
    }

    private static void loadFileProperties(Scenario sc) throws DAOException {
        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream(sc.getOutPath() + "/project.properties"));
            sc.setName(prop.getProperty("project_name"));
            sc.setSchemaSourcePath(sc.getOutPath() + "/" + prop.getProperty("schema_source"));
            sc.setSchemaTargetPath(sc.getOutPath() + "/" + prop.getProperty("schema_target"));
            String features = prop.getProperty("features");
            List<String> array = new ArrayList<String>();
            sc.setFeatures(features.split(";"));
            sc.setExpectedInstancePath(Utils.toAbsolutePath(sc.getOutPath(), prop.getProperty("instance_expected")));
            
        } catch (FileNotFoundException fe) {
            logger.error(fe.getLocalizedMessage());
            throw new DAOException("File project.properties not found");
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the project.properties file");
        }
    }

    public static Scenario load(String directory) throws DAOException {
        Scenario sc = new Scenario();
        sc.setOutPath(directory);
        loadFileProperties(sc);
        sc.setExclusionListPath(sc.getOutPath() + "/exclusionsList.txt");
        logger.debug("Scenario loaded:" + sc);
        return sc;
    }

}
