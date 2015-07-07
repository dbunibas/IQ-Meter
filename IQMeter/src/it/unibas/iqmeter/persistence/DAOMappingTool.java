/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class DAOMappingTool {

    private static Log logger = LogFactory.getLog(DAOMappingTool.class);

    public static void createDirectory(MappingTool tool, Scenario sc) throws DAOException {
        try {
            File directoryTool = new File(sc.getOutPath() + File.separator + tool.getName());
            directoryTool.mkdirs();
//            if (tool.getFileScript() != null && !tool.getFileScript().equals("")) {
//                logger.debug("There is File script");
//                String script = tool.getFileScript().substring(tool.getFileScript().lastIndexOf(File.separator) + 1);
//                copyFile(directoryTool, script);
//            }
            createFileProperties(tool, directoryTool);
            tool.setDirectory(directoryTool.getAbsolutePath());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DAOException(ex);
        }

    }

    private static void createFileProperties(MappingTool tool, File directory) throws DAOException {
        Properties prop = new Properties();
        FileOutputStream out = null;
        try {
            prop.setProperty(tool.getName() + ".mapping_file", Utils.toRelativePath(directory.getAbsolutePath(), tool.getMappingFilePath()));
            prop.setProperty(tool.getName() + ".instance_translated_file", Utils.toRelativePath(directory.getAbsolutePath(), tool.getTranslatedInstancePath()));
            if (tool.getFileScript() != null && !tool.getFileScript().equals("")) {
                prop.setProperty(tool.getName() + ".script_file", Utils.toRelativePath(directory.getAbsolutePath(), tool.getFileScript()));
            }
            //save properties to project root folder
            logger.trace("Path for File properties: " + directory.getAbsolutePath() + File.separator + "tool.properties");
            File file = new File(directory.getAbsolutePath() + File.separator + "tool.properties");
            out = new FileOutputStream(file.getAbsolutePath(), true);
            prop.store(out, Constant.MESSAGE_FILE_PROPERTIES + "Mapping Tool");
            //out.close();
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to build the tool.properties file", ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private static Properties loadToolProperties(String directory) throws DAOException {
        FileInputStream inStream = null;
        try {
            Properties prop = new Properties();
            inStream = new FileInputStream(directory + "/" + "tool.properties");
            prop.load(inStream);
            inStream.close();
            return prop;
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException(ex);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void saveLastModifiedTime(long lastModified, MappingTool tool) throws DAOException {
        FileOutputStream out = null;
        try {
            Properties prop = loadToolProperties(tool.getDirectory());
            logger.info(tool.getName() + ".last_modified " + lastModified + "");
            prop.setProperty(tool.getName() + ".last_modified", lastModified + "");
            out = new FileOutputStream(tool.getDirectory() + "/tool.properties");
            prop.store(out, null);
            out.close();
        } catch (Exception ex) {
            logger.error("saveLastModifiedTime error: " + ex.getLocalizedMessage());
            throw new DAOException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
        }

    }

    public static Long loadLastModifiedTime(MappingTool tool) throws DAOException {
        Long last;
        try {
            logger.debug("Tool properties loaded");
            Properties prop = loadToolProperties(tool.getDirectory());
            last = Long.parseLong(prop.getProperty(tool.getName() + ".last_modified"));
        } catch (Exception ex) {
            logger.error("loadLastModifiedTime error: " + ex.getLocalizedMessage());
            return new Long(0);
        }
        return last;
    }

    public static MappingTool loadTool(String toolDirectory) throws DAOException {
        MappingTool tool = new MappingTool();
        Properties prop = new Properties();
        FileInputStream in = null;
        try {
            //load a properties file
            in = new FileInputStream(toolDirectory + "/" + "tool.properties");
            prop.load(in);
            tool.setName(toolDirectory.substring(toolDirectory.lastIndexOf(File.separator) + 1));
            tool.setMappingFilePath(Utils.toAbsolutePath(toolDirectory, prop.getProperty(tool.getName() + ".mapping_file")));
            tool.setTranslatedInstancePath(Utils.toAbsolutePath(toolDirectory, prop.getProperty(tool.getName() + ".instance_translated_file")));
            String script = prop.getProperty(tool.getName() + ".script_file");
            if (script != null) {
                tool.setFileScript(Utils.toAbsolutePath(toolDirectory, script));
            }
            tool.setDirectory(toolDirectory);
            in.close();
            return tool;
        } catch (FileNotFoundException fe) {
            logger.error("File tool.properties not found: " + fe.getLocalizedMessage());
            return null;
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new DAOException("Problem to load the tool.properties file");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }

    }

//    private static void copyFile(File f1, String pathOut) throws DAOException {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            String namef2 = f1.getName();
//            in = new FileInputStream(f1);
//            out = new FileOutputStream(new File(pathOut + "/" + namef2));
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            in.close();
//            out.close();
//            logger.debug("File " + namef2 + " copied.");
//        } catch (FileNotFoundException ex) {
//            logger.error(ex.getLocalizedMessage());
//            throw new DAOException(ex);
//        } catch (IOException ex) {
//
//            throw new DAOException(ex);
//        } finally {
//            try {
//                in.close();
//                out.close();
//            } catch (Exception ex) {
//            }
//        }
//    }
    public static void loadTools(Scenario scenario, List<String> tools) throws DAOException {
        File directory = new File(scenario.getOutPath());
        for (final String name : tools) {
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.isDirectory() && pathname.getName().equals(name)) {
                        return true;
                    }
                    return false;
                }
            };
            File[] subDir = directory.listFiles(filter);
            if (subDir.length != 0) {
                File file = subDir[0];
                MappingTool tool = loadTool(file.getAbsolutePath());
                if (tool != null) {
                    scenario.addTool(tool);
                }
            }
        }
    }
}
