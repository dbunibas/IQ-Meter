/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter;

import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.iqmeter.view.DialogExplainQuality;
import it.unibas.iqmeter.view.effortgraph.DialogEffortGraph;
import it.unibas.ping.utilita.ResourceManager;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jopendocument.util.FileUtils;

/**
 *
 * @author antonio
 */
public class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);
//public static int mappingInstanceID = 1;

    private Utils() {
    }

    public static ImageIcon createIcon(String iconName) {
        URL url = ResourceManager.getURL(Constant.ICONS + iconName);
        ImageIcon img = null;
        try {
            img = new ImageIcon(url);
            logger.trace("Url icona " + url);
        } catch (Exception e) {
            logger.info("Problem loading icon" + e);
            return null;
        }
        return img;
    }

    public static ImageIcon createImage(String imgName) {
        URL url = ResourceManager.getURL(Constant.IMAGES + imgName);
        ImageIcon img = null;
        try {
            img = new ImageIcon(url);
        } catch (Exception e) {
            logger.info("Problem loading image" + e);
        }
        return img;
    }

    public static String getLabelPath(String path) {
        String out = "..";
        out += path.substring(path.lastIndexOf(File.separator));
        return out;
    }

    public static String toRelativePath(String pathRoot, String pathTarget) throws IOException {
        logger.trace("String pathRoot: " + pathRoot + " - pathTarget: " + pathTarget);
        
        String pathRel = FileUtils.relative(new File(pathRoot), new File(pathTarget));
        File fileRel = new File(pathRoot + "/" + pathRel.replace("\\.{1}/", ""));
        logger.trace("*****Relative Path*****" + pathRel);
        logger.debug("*****File exists: " + fileRel.exists() + " - file path absolute: " + fileRel.getAbsolutePath() + " - path canonical: " + fileRel.getCanonicalPath());
        return pathRel;
    }

    public static String toAbsolutePath(String pathRoot, String pathTarget) throws IOException {
        logger.trace("String pathRoot: " + pathRoot + " - pathTarget: " + pathTarget);
        if(! System.getProperties().getProperty("os.name").contains("Windows")){
          pathTarget =   pathTarget.replaceAll("\\\\", "/");
        }
        File fileRel = new File(pathRoot + "/" + pathTarget.replace("\\.{1}/", ""));
        logger.debug("*****File exists: " + fileRel.exists() + " - file path absolute: " + fileRel.getAbsolutePath() + " - path canonical: " + fileRel.getCanonicalPath());
        return fileRel.getCanonicalPath();
    }

    public static void closeDialog(MappingExecution mapping) {
        Window[] listWin = Window.getOwnerlessWindows();
        for (Window window : listWin) {
            if (window instanceof DialogEffortGraph && window.getName().endsWith(mapping.getDirectory())) {
                ((JFrame) window).setVisible(false);
            }
            if (window instanceof DialogExplainQuality && window.getName().equals(mapping.getNumberLabel() + Constant.QUALITY_TEXT)) {
                ((JFrame) window).setVisible(false);
            }
        }
    }

    public static boolean convalidateFormatFile(String toolName, String fileName) {
        try {
            String toolFile = PropertiesLoader.getToolFileType(toolName);
            if (toolFile == null) {
                return true;
            }
            logger.info("Tool File filter: " + toolFile);
            String[] files = toolFile.split(",");
            for (String string : files) {
                logger.trace("Tool File extension: " + string);
                if (fileName.endsWith("." + string)) {
                    return true;
                }
            }
            return false;
        } catch (DAOException ex) {
            logger.error(ex);
            return true;
        }

    }

    public static void closeAllDialog() {
        Window[] listWin = Window.getOwnerlessWindows();
        for (Window window : listWin) {
            if ((window instanceof DialogEffortGraph || window instanceof DialogExplainQuality) && window.isVisible()) {
                //((JFrame) window).setVisible(false);
                ((JFrame) window).dispose();
            }
        }
    }
}
