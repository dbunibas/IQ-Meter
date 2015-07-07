/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence;

import it.unibas.iqmeter.Constant;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class PropertiesLoader {

    private PropertiesLoader() {
    }
    private static Log logger = LogFactory.getLog(PropertiesLoader.class);
    private static InputStream inStream = null;

    public static List<String> loadAvailableTools() throws DAOException {
        List<String> tools = new ArrayList<String>();
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            Set<String> setKey = prop.stringPropertyNames();
            for (String string : setKey) {
                if (string.startsWith("tool.")) {
                    tools.add(string.replace("tool.", ""));
                }
            }
        } catch (Exception ex) {
            logger.error("load Available Tools error: " + ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tools-conf.properties file", ex);
        }
        return tools;
    }

    public static String getToolFileType(String toolName) throws DAOException {
        String fileType = null;
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            fileType = prop.getProperty("toolFile." + toolName);
        } catch (Exception ex) {
            logger.error("get Tool File Type error: " + ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tools-conf.properties file", ex);
        }
        return fileType;
    }

    public static int[] loadPropertiesTemplateChart() throws DAOException {
        int[] cell = new int[3];
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_CHART);
            int column = Integer.parseInt(prop.getProperty("column-number"));
            int row = Integer.parseInt(prop.getProperty("row-number"));
            int step = Integer.parseInt(prop.getProperty("steps"));
            cell[0] = column;
            cell[1] = row;
            cell[2] = step;
            return cell;
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the template-chart.properties file", ex);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load template-chart config", ex);
        }
    }

    public static String loadClassGraphCreator(String nameTool) throws DAOException {
        String name = "";
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            name += prop.getProperty("effortGraphCreator." + nameTool);
            return name;
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tools-conf.properties file", ex);
        }
    }

    public static String loadGraphLayout(String nameTool) throws DAOException {
        String name = "";
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            name += prop.getProperty("effortGraphLayout." + nameTool);
            return name;
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tools-conf.properties file", ex);
        }
    }

    public static int[] toolFunctionBit(String nameTool) throws DAOException {

        int[] bit = new int[2];
        bit[1] = 0;
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            nameTool = nameTool.replace(" ", "_");
            String str = prop.getProperty("tool." + nameTool);
            int i = 0;
            for (String string : str.split(",")) {
                bit[i] = Integer.parseInt(string);
                i++;
            }
            return bit;
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tools-conf file", ex);
        }
    }

    private static Properties loadFileProperties(String pathFile) throws Exception {
        try {
            Properties prop = new Properties();
            inStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(pathFile);
            prop.load(inStream);
            inStream.close();
            return prop;
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public static String loadColorName(String nameTool) {
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            return prop.getProperty("toolColorName." + nameTool);
        } catch (Exception ex) {
            logger.error("load color Name error: " + ex);
            return "";
        }
    }
    
        public static String loadDefaultToolPath(String nameTool) {
            String path = "";
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            path += prop.getProperty("defaultPathScenario." + nameTool) + ",";
            path += prop.getProperty("defaultPathSolution." + nameTool);
        } catch (Exception ex) {
            logger.error("load color Name error: " + ex);
            path +="";
            
        }
        return path;
    }

    public static long loadSleepTime() {
        long time = 0;
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            time = Long.parseLong(prop.getProperty("sleepMillis"));
            return time;
        } catch (Exception ex) {
            logger.error("Problem to load sleep time value from the file tools-conf" + ex.getLocalizedMessage());
            return 0;
        }
    }

    public static File loadTemplateChart(int step) throws DAOException {
        File template = new File("resources/config/template-chart" + step + ".ods");
        if (template.exists() == false) {
            throw new DAOException("Problem to load the template-chart.ods, file not found.");
        }
        return template;
    }

    public static String loadStandardFeatures() {
        String features = "";
        try {
            Properties prop = loadFileProperties(Constant.FILE_PROPERTIES_TOOLS);
            features += prop.getProperty("features");
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return features;
    }
}
