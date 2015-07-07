package it.unibas.spicybenchmark.operators.generators.ext.simpack.tree;

import simpack.api.impl.AbstractDistanceConversion;
import simpack.util.conversion.CommonDistanceConversion;
import simpack.util.conversion.LogarithmicDistanceConversion;
import simpack.util.conversion.WorstCaseDistanceConversion;

public class DebugDistanceConversion extends AbstractDistanceConversion {

    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(DebugDistanceConversion.class.getName());

    public double convert(double d) {
        logger.debug("-------------");
        logger.debug("Converting value " + d);
        CommonDistanceConversion c1 = new CommonDistanceConversion();
        logger.debug("CommonDistanceConversion: " + c1.convert(d));
        LogarithmicDistanceConversion c2 = new LogarithmicDistanceConversion();
        logger.debug("LogarithmicDistanceConversion: " + c2.convert(d));
        WorstCaseDistanceConversion c3 = new WorstCaseDistanceConversion();
        logger.debug("WorstCaseDistanceConversion: " + c3.convert(d));
        return c1.convert(d);
    }

}
