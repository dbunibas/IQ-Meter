/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.test.mock;

import it.unibas.iqmeter.model.MappingTool;
import java.io.File;

/**
 *
 * @author Antonio Genovese
 */
public class MockMappingTool {
     public static MappingTool load() {
        try {
        
            String translated = MockMappingTool.class.getResource("/dati/Target-translatedInstance0.xml").getPath();
            File fileT= new File(translated);
            System.out.println("Translated: " + fileT.getAbsolutePath());
            String mappingName = MockScenario.class.getResource("/dati/copyScenario.xml").getPath();
            File mappingFile = new File(mappingName);


            File directory = new File(System.getProperty("java.io.tmpdir") + "/directoryTest/Spicy");
            directory.mkdirs();
            System.out.println("Directory Tool: " + directory.getAbsolutePath());
   
            MappingTool tool = new MappingTool();
            tool.setName("Spicy");
            tool.setDirectory(directory.getAbsolutePath());
            tool.setTranslatedInstancePath(fileT.getAbsolutePath());
            tool.setMappingFilePath(mappingFile.getAbsolutePath());
            return tool;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }


    }
    
}
