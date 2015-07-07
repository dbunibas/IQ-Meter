/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.test.controllo;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.controller.ActionOpenProject;
import it.unibas.iqmeter.controller.operator.CalculateQuality;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.test.mock.MockControllo;
import it.unibas.iqmeter.test.mock.MockMappingTool;
import it.unibas.iqmeter.test.mock.MockScenario;
import it.unibas.iqmeter.test.mock.MockVista;
import it.unibas.ping.framework.IControllo;
import it.unibas.ping.framework.IModello;
import it.unibas.ping.framework.IVista;
import it.unibas.ping.framework.Modello;
import java.io.File;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author Antonio Genovese
 */
public class TestController extends TestCase {

    private Scenario scenario;
    private IModello modello;
    private IVista vista;
    private IControllo controllo;
    private File directory;
    private MappingTool tool;

    @Override
    public void setUp() {

        this.scenario = MockScenario.load();
        this.modello = new Modello();
        this.vista = new MockVista();
        this.controllo = new MockControllo();
        this.tool = null;
        this.directory = new File(System.getProperty("java.io.tmpdir") + File.pathSeparator + "directoryTest");

    }

//    public void testActionFinish() {
//        this.modello.putBean(Constant.SCENARIO_NEW, this.scenario);
//        ActionFinish action = new ActionFinish(modello, vista);
//        action.esegui(null);
//        assertTrue(directory.exists());
//        //assertTrue(new File(directory.getAbsoluteFile()+"").exists());
//        //  assertEquals("/tmp/directoryTest/exclusionsList.txt", scenario.getExclusionListPath());
//
//    }
    public void testActionOpenProject() {
        this.modello.putBean(Constant.DIRECTORY_LOAD_PROJECT, directory.getAbsolutePath());
        ActionOpenProject action = new ActionOpenProject(modello, vista, controllo);
        action.esegui(null);
        assertEquals("FEATURE_GLOBAL_ID", scenario.getFeatures()[0]);
        assertEquals("FEATURE_JOINS_GLOBAL_ID", scenario.getFeatures()[1]);
    }

    public void testCalculateQuality() {
        try {
            this.tool = MockMappingTool.load();
            File file = new File(directory.getAbsoluteFile() + File.pathSeparator + "exclusionsList.txt");
            file.createNewFile();
            this.scenario.setExclusionListPath(directory.getAbsoluteFile() + File.pathSeparator + "exclusionsList.txt");
            this.modello.putBean(Scenario.class.getName(), this.scenario);

            CalculateQuality.calculate(this.scenario, this.tool, this.modello);
            assertNotNull(modello.getBean(Constant.EXECUTION_ANALYZED));
            MappingExecution task = (MappingExecution) modello.getBean(Constant.EXECUTION_ANALYZED);
            assertEquals(1.0, task.getQuality().getFmeasure());
            File filedir = new File(task.getDirectory());
            cleanFile(filedir);
                    cleanFile(this.directory);
        } catch (Exception ex) {
            System.out.println(ex);
        }


    }

    private void cleanFile(File file) {
        File[] subDir = file.listFiles();
        for (File file1 : subDir) {
            file1.delete();
        }
        file.delete();
    }
}
