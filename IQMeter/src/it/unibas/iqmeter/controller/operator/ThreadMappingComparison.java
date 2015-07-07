/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.ActionAutomaticRun;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.DAOMappingTool;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import it.unibas.ping.contrib.PingThreadWorker;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.Controllo;
import it.unibas.ping.framework.MessaggioPing;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ThreadMappingComparison extends Thread {

    private Log logger = LogFactory.getLog(this.getClass());
    private Runnable actionRun = (Runnable) new ActionAutomaticRun();
    private static ThreadMappingComparison singleton = new ThreadMappingComparison();
    private Map<String, Long> mapToolFile = new HashMap<String, Long>();
    private boolean started = false;
    private long sleepTime;

    private ThreadMappingComparison() {
    }

    @Override
    public void start() {
        sleepTime = PropertiesLoader.loadSleepTime();
        if (sleepTime == 0) {
            sleepTime = 3000;
        }
        this.started = true;
        Scenario scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
        while (scenario.getToolsList().size() > 0) {
            try {
                logger.debug("Thread AutomaticRun go");
                for (MappingTool tool : scenario.getToolsList()) {
                    this.compareContent(tool);
                    
                }
                ThreadMappingComparison.sleep(sleepTime);
                scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
        this.started = false;
    }

    private void compareContent(MappingTool tool) {
        try {
            Applicazione.getInstance().getModello().putBean(Constant.TOOL_ANALYZED, tool);
            File fileMappingTarget = new File(tool.getMappingFilePath());
            Long lastModified;
            if (this.mapToolFile.containsKey(tool.getName())) {
                lastModified = this.mapToolFile.get(tool.getName());
            } else {
                lastModified = DAOMappingTool.loadLastModifiedTime(tool);
                this.mapToolFile.put(tool.getName(), lastModified);
            }
            logger.debug("last execution: " + lastModified);
            logger.trace("File Target: " + fileMappingTarget.getAbsolutePath());
            if (!fileMappingTarget.exists()) {
                logger.info("file mapping Target not exist ");
                this.executeActionRun(tool, lastModified);
            } else if (fileMappingTarget.lastModified() > lastModified) {
                logger.info("File last and file target not equal, last execution: " + lastModified + ", last modified " + fileMappingTarget.lastModified());
                //Applicazione.getInstance().getModello().putBean(Constant.TOOL_ANALYZED, tool);
                this.executeActionRun(tool, fileMappingTarget.lastModified());
            }
        } catch (DAOException ex) {
            logger.error(ex);
            //Notification only once the same error
            MessaggioPing mess = (MessaggioPing) Applicazione.getInstance().getModello().getBean(Controllo.MESSAGGIO_STATO);
            if (! mess.getValore().equals(Constant.MESSAGE_ERROR_COMPARE)) {
                JLabel l = (JLabel) Applicazione.getInstance().getVista().getComponente(Constant.LABEL_ICON_STATUS);
                Applicazione.getInstance().getVista().finestraErrore(Constant.MESSAGE_ERROR_COMPARE + "\n" + ex.getLocalizedMessage());
                Applicazione.getInstance().getModello().putBean(Controllo.MESSAGGIO_STATO, new MessaggioPing(Constant.MESSAGE_ERROR_COMPARE));
                l.setIcon(Utils.createIcon(Constant.ICON_ERROR));
            }
        }
    }

    private void executeActionRun(MappingTool tool, long last) {
        try {
            Thread action = new Thread(actionRun);
            action.start();
            action.join();
            logger.trace("Last Modified Time: " + last);
            //if there are exceptions in the actionRUN he did not try again, in this case must be done manually run or modified the scenario file
            DAOMappingTool.saveLastModifiedTime(last, tool);
            this.mapToolFile.put(tool.getName(), last);
            ThreadMappingComparison.sleep(1000);
            ThreadMappingComparison.yield();
            logger.debug("Thread AutomaticRun go Restart");
        } catch (InterruptedException ex) {
            logger.error(ex);
        } catch (DAOException ex) {
            logger.error(ex.getMessage());
        }

    }

    public static void clearMapTool() {
        singleton.mapToolFile.clear();
    }

    public static void startThread() {
        if (!singleton.started) {
            final PingThreadWorker workerAutomaticRun = new PingThreadWorker() {
                @Override
                public Object avvia() {
                    singleton.start();
                    return null;
                }
            };
            workerAutomaticRun.start();
        }
    }
}
