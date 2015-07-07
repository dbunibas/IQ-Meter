/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.model.EffortRecording;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.model.SolutionQuality;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author antonio
 */
public class DAOMappingExecution {

    private static Log logger = LogFactory.getLog(DAOMappingExecution.class);
    private static FileOutputStream outStream = null;
    private static FileInputStream inStream = null;

    private static void createFileProperties(MappingExecution mapping) throws DAOException {
        Properties prop = new Properties();
        try {
            prop.setProperty("time", mapping.getMappingTime().getTime() + "");
            prop.setProperty("quality_precision", mapping.getQuality().getPrecision() + "");
            prop.setProperty("quality_Fmeasure", mapping.getQuality().getFmeasure() + "");
            prop.setProperty("quality_recall", mapping.getQuality().getRecall() + "");
            prop.setProperty("quality_expNodes", mapping.getQuality().getNodesExpected() + "");
            prop.setProperty("quality_transNodes", mapping.getQuality().getNodesTranslated() + "");
            prop.setProperty("quality_expTumples", mapping.getQuality().getTuplesExpected() + "");
            prop.setProperty("quality_transTumples", mapping.getQuality().getTuplesTranslated() + "");
            prop.setProperty("recorded_clicks", mapping.getEffortRecording() == null ? "0" : mapping.getEffortRecording().getClickRecorded()+"");
            prop.setProperty("recorded_keys", mapping.getEffortRecording() == null ? "0" : mapping.getEffortRecording().getKeyboardRecorded()+"");
            //save properties to project root folder
            outStream = new FileOutputStream(mapping.getDirectory() + "/mapping.properties");
            prop.store(outStream, Constant.MESSAGE_FILE_PROPERTIES + "Mapping Execution");
            //outStream.close();
        } catch (IOException ex) {
            throw new DAOException("Problem to build the mapping.properties file", ex);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception ex) {
                }
            }

        }
    }

    public static StringBuilder loadSimilarityResultText(String dir) throws DAOException {
        StringBuilder similarityText = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dir + "/similarityResult.txt"));
            similarityText = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                similarityText.append(line);
                similarityText.append('\n');
                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load similarityResult.txt file", ex);

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }

        }

        return similarityText;
    }

    public static void loadTasks(MappingTool tool) throws DAOException {
        File directory = new File(tool.getDirectory());
        FileFilter filter = new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory() && pathname.getName().startsWith("MappingExecution_")) {
                    return true;
                }
                return false;
            }
        };
        File[] subDir = directory.listFiles(filter);
        List<MappingExecution> list = new ArrayList<MappingExecution>();
        for (int i = 0; i < subDir.length; i++) {
            File file = subDir[i];
            MappingExecution execution = load(file.getAbsolutePath(), tool);
            if (execution != null) {
                list.add(execution);
            }
        }
        Collections.sort(list, MappingExecution.MappingDateComparator);
        for (MappingExecution exc : list) {
            exc.setNumberLabel(tool.getNumberExecutions() + 1);
            tool.addMappingExecution(exc);
        }
    }

    private static MappingExecution load(String filePath, MappingTool tool) throws DAOException {
        Properties prop = new Properties();
        MappingExecution mapping = new MappingExecution();
        SolutionQuality quality = new SolutionQuality();
        try {
            //load a properties file
            inStream = new FileInputStream(filePath + "/mapping.properties");
            prop.load(inStream);
            quality.setFmeasure(Double.parseDouble(prop.getProperty("quality_Fmeasure")));
            quality.setPrecision(Double.parseDouble(prop.getProperty("quality_precision")));
            quality.setRecall(Double.parseDouble(prop.getProperty("quality_recall")));

            quality.setNodesExpected(Integer.parseInt(prop.getProperty("quality_expNodes")));
            quality.setNodesTranslated(Integer.parseInt(prop.getProperty("quality_transNodes")));
            quality.setTuplesExpected(Integer.parseInt(prop.getProperty("quality_expTumples")));
            quality.setTuplesTranslated(Integer.parseInt(prop.getProperty("quality_transTumples")));
            
            int clicks = getOptionalValue(prop, "recorded_clicks");
            int keys = getOptionalValue(prop, "recorded_keys");
           // int clicks = getOptionalValue(Integer.parseInt(prop.getProperty("recorded_clicks")));
           // int keys = Integer.parseInt(prop.getProperty("recorded_keys"));
            inStream.close();
            mapping.setQuality(quality);
            mapping.setEffortRecording(new EffortRecording(clicks, keys));
            Long longTime = Long.parseLong(prop.getProperty("time"));
            Date date = new Date(longTime);
            logger.debug("MappingDate: " + date);

            File instanceTranslated = new File(filePath + tool.getTranslatedInstancePath().substring(tool.getTranslatedInstancePath().lastIndexOf(File.separator)));
            logger.debug("Mapping instance translated: " + instanceTranslated.getAbsolutePath());
            String fileMappingName = tool.getMappingFilePath().substring(tool.getMappingFilePath().lastIndexOf(File.separator));
            File fileMapping = new File(filePath + replaceLast(fileMappingName, ".", mapping.getQuality().getFmeasure() + "."));
            logger.debug("Mapping file: " + fileMapping.getAbsolutePath());

            if (!instanceTranslated.isFile() || !fileMapping.isFile()) {
                throw new DAOException("InstanceTranslated or Mapping file not found.");
            } else {
                mapping.setMappingTime(date);
                mapping.setDirectory(filePath);
                mapping.setGenerateIstancePath(instanceTranslated.getAbsolutePath());
                mapping.setMappingFilePath(fileMapping.getAbsolutePath());

                return mapping;
            }
        } catch (IOException ex) {
            File folder = new File(filePath);
            if (folder.listFiles().length == 0) {
                logger.error("Task ignored because mapping.properties not found at:" + ex.getLocalizedMessage());
                folder.delete();
                return null;
            } else {
                logger.error("Task ignored because mapping.properties not found at:" + ex.getLocalizedMessage());
                //throw new DAOException("Problem to load the mapping.properties file", ex);
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw new DAOException("Problem to load mapping task", ex);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void createDirectory(MappingExecution mapping, MappingTool tool, Scenario sc) throws DAOException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'_'HH.mm.ss");
        File directoryMapping = new File(tool.getDirectory() + "/MappingExecution_" + sc.getName() + (mapping.getQuality().getFmeasure()) + "_" + format.format(mapping.getMappingTime()));
        try {
            int i = 1;
            while (directoryMapping.exists()) {
                directoryMapping = new File(directoryMapping + "(" + i + ")");
                i++;
            }
            directoryMapping.mkdir();
            File instanceTranslated = new File(directoryMapping + tool.getTranslatedInstancePath().substring(tool.getTranslatedInstancePath().lastIndexOf(File.separator)));
            String fileMappingName = tool.getMappingFilePath().substring(tool.getMappingFilePath().lastIndexOf(File.separator));
            File fileMapping = new File(directoryMapping + replaceLast(fileMappingName, ".", mapping.getQuality().getFmeasure() + "."));

            outStream = new FileOutputStream(instanceTranslated);
            FileUtils.copyFile(new File(tool.getTranslatedInstancePath()), outStream);
            outStream.close();
            outStream = new FileOutputStream(fileMapping);
            FileUtils.copyFile(new File(tool.getMappingFilePath()), outStream);
            outStream.close();
            if (tool.getFileScript() != null && !tool.getFileScript().equals("")) {
                File script = new File(tool.getFileScript());
                if (script.length() != 0) {
                    outStream = new FileOutputStream(directoryMapping + "/" + tool.getName() + Constant.SCRIPT_FILE_NAME);
                    logger.info("Script file: " + script.getName() + ", path " + script.getAbsolutePath());
                    FileUtils.copyFile(script, outStream);
                    outStream.close();
                }
            }
            mapping.setDirectory(directoryMapping.getAbsolutePath());
            mapping.setGenerateIstancePath(instanceTranslated.getAbsolutePath());
            mapping.setMappingFilePath(fileMapping.getAbsolutePath());
            createFileProperties(mapping);
            if (tool.getName().equalsIgnoreCase("Spicy") || tool.getName().contains("Spicy")) {
                modifyFileMappingSpicy(fileMapping, sc);
            }

        } catch (Exception ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException ex) {
                }
            }
        }

    }

    private static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    public static boolean deleteMappingFile(MappingExecution task) {
        File directory = new File(task.getDirectory());
        try {
            //deleteDirectory(directory);
            FileUtils.deleteDirectory(directory);
            directory.delete();
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return directory.exists();
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    logger.debug("Deleting file: " + files[i].getName());
                    files[i].delete();
                }
            }
        }
    }

    public static void modifyFileMappingSpicy(File fileMapping, Scenario sc) throws DAOException {
        try {
            inStream = new FileInputStream(fileMapping);
            logger.trace("Scenario: " + sc);
            Document document = buildDOM(inStream);
            Element elementRoot = document.getRootElement();
            Element source = elementRoot.getChild("source");
            Element xml = source.getChild("xml");
            Element schemaSource = xml.getChild("xml-schema");
            Element instaces = xml.getChild("xml-instances");
            instaces.removeChildren("xml-instance");
            logger.debug("Element schema source: " + schemaSource.getName());
            String schemaName = sc.getSchemaSourcePath().substring(sc.getSchemaSourcePath().lastIndexOf("/") + 1);
            logger.debug("Scema modify source: " + schemaName);
            schemaSource.setText("../../" + schemaName);
            Element target = elementRoot.getChild("target");
            xml = target.getChild("xml");
            Element schemaTarget = xml.getChild("xml-schema");
            schemaName = sc.getSchemaTargetPath().substring(sc.getSchemaTargetPath().lastIndexOf("/") + 1);
            schemaTarget.setText("../../" + schemaName);
            inStream.close();
            outStream = new FileOutputStream(fileMapping);
            XMLOutputter xmlOutput = new XMLOutputter();

            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, outStream);
            outStream.close();
        } catch (NullPointerException npe) {
            logger.error(npe);
            throw new DAOException("Problem to load schema file");
        } catch (IOException ex) {
            logger.error(ex);
            throw new DAOException("Problem to write mapping file");
        } finally {
            if (inStream != null || outStream != null) {
                try {
                    inStream.close();
                    outStream.close();
                } catch (IOException ex) {
                }
            }

        }
    }

    private static Document buildDOM(FileInputStream mappingFileStream) throws DAOException {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(mappingFileStream);
            logger.debug("Schema document:" + document.toString());
            return document;
        } catch (JDOMException jde) {
            logger.error(jde);
            throw new DAOException(jde);
        } catch (java.io.IOException ioe) {
            logger.error(ioe);
            throw new DAOException(ioe);
        }
    }

    public static void saveFileScript(String text, MappingExecution execution, MappingTool tool) throws DAOException {
        FileWriter writer = null;
        try {
            String path = execution.getDirectory();
            path = path + File.separator + tool.getName() + Constant.SCRIPT_FILE_NAME;
            writer = new FileWriter(new File(path));
            writer.append(text);
            writer.close();
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Error to save the annotation script file");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static boolean deleteFileScript(MappingExecution execution, MappingTool tool) {
        String path = execution.getDirectory();
        path = path + File.separator + tool.getName() + Constant.SCRIPT_FILE_NAME;
        logger.debug("Path File Script " + path);
        File script = new File(path);
        return script.delete();
    }

    public static String loadFileScript(String pathFile, MappingTool tool) {
        String path = pathFile.substring(0, pathFile.lastIndexOf(File.separator) + 1);
        path = path + tool.getName() + Constant.SCRIPT_FILE_NAME;
        logger.debug("File Script path " + path);
        File script = new File(path);
        if (script.exists()) {
            return scanScriptFile(path);
        } else {
            return "";
        }
    }

    private static String scanScriptFile(String path) {
        StringBuilder annotationText = null;
        BufferedReader br = null;
        try {
            logger.info("Path File Script loaded" + path);
            br = new BufferedReader(new FileReader(path));
            annotationText = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                annotationText.append(line);
                annotationText.append("\n");
                line = br.readLine();
            }
            br.close();
            return annotationText.toString();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return "";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private static int getOptionalValue(Properties prop, String propertyName) {
        String value = prop.getProperty(propertyName);
        if (value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return 0;
    }

}
