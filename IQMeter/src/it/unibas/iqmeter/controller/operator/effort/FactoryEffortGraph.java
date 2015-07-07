/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.controller.operator.effort;

import it.unibas.iqmeter.persistence.DAOException;
import it.unibas.iqmeter.persistence.PropertiesLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class FactoryEffortGraph {

    private static Log logger = LogFactory.getLog(FactoryEffortGraph.class);

    private FactoryEffortGraph() {
    }

    public static ICreatorEffortGraph getOperatorEffort(String name) throws DAOException, ClassNotFoundException {
        ICreatorEffortGraph effort = null;
        logger.debug("String Tool: " + name);
        String effortClass = PropertiesLoader.loadClassGraphCreator(name);
        logger.info("String Operator Effort Class: " + effortClass);
        Class<?> o = Class.forName(effortClass);
        logger.info("Class create: " + o.getName());
        try {
            effort = (ICreatorEffortGraph) o.newInstance();
        } catch (InstantiationException ex) {
            logger.error(ex.getLocalizedMessage());
            return null;

        } catch (IllegalAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return null;
        }
        return effort;
    }
}
