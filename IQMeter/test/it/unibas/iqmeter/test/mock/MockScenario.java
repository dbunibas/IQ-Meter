/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.test.mock;

import it.unibas.iqmeter.model.Scenario;
import java.io.File;

/**
 *
 * @author Antonio Genovese
 */
public class MockScenario {

    public static Scenario load() {
        try {
            String expected = MockScenario.class.getResource("/dati/copyTargetIstance_small.xml").getPath();
            File fileExpected = new File(expected);
            System.out.println("Expected: " + fileExpected.getAbsolutePath());
            String sourceSchema = MockScenario.class.getResource("/dati/copySource.xsd").getPath();
            File fileSourceSchema = new File(sourceSchema);
            String targetSchema = MockScenario.class.getResource("/dati/copyTarget.xsd").getPath();
            File fileTargetSchema = new File(targetSchema);

            File fileOut = new File(System.getProperty("java.io.tmpdir") + "/directoryTest");
            fileOut.mkdirs();
            System.out.println("File Out: " + fileOut.getAbsolutePath());
            String[] features = {"FEATURE_GLOBAL_ID", "FEATURE_JOINS_GLOBAL_ID"};
            
            Scenario scenario = new Scenario();
            scenario.setName("Test Scenario");
            scenario.setExpectedInstancePath(fileExpected.getAbsolutePath());
            scenario.setOutPath(System.getProperty("java.io.tmpdir") + "/directoryTest");
            scenario.setSchemaSourcePath(fileSourceSchema.getAbsolutePath());
            scenario.setSchemaTargetPath(fileTargetSchema.getAbsolutePath());
            scenario.setFeatures(features);
            return scenario;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }


    }
}
