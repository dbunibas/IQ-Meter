package it.unibas.iqmeter.test.persistenza;

import it.unibas.iqmeter.persistence.effortgraph.DAOClover;
import it.unibas.spicy.persistence.DAOException;
import junit.framework.TestCase;

public class TestDAOClover extends TestCase {

    public void ttestDao1() {
        try {
            DAOClover dao = new DAOClover();
            String fileName = "Z:/experiments/Horizontal/IQ-Horizontal/IQ-Horizontal/CloverETL/MappingExecution_IQ-Horizontal1.0_2015-06-10_14.03.53/graph1.0.grf";
            String oldFile = "Z:/experiments/Horizontal/risorse/clover/graph/graph.grf";
            dao.parse(fileName, "Z:/experiments/Horizontal/risorse/clover/data-in/partS.xsd", "Z:/experiments/Horizontal/risorse/clover/data-in/partT.xsd");
        } catch (DAOException dAOException) {
            System.out.println(dAOException);
        }
    }

    public void testDao2() {
        try {
            DAOClover dao = new DAOClover();
            dao.parse("Z:/experiments/Horizontal/risorse/clover/graph/grafo1.0.grf", "Z:/experiments/Horizontal/risorse/clover/data-in/partS.xsd", "Z:/experiments/Horizontal/risorse/clover/data-in/partT.xsd");
        } catch (DAOException dAOException) {
            System.out.println(dAOException);
        }
    }

}
