package it.unibas.spicybenchmark.test;

import it.unibas.spicybenchmark.CompareInstances;
import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TCompareInstances extends TestCase {

    private static Log logger = LogFactory.getLog(TCompareInstances.class.getName());

    public void test() throws Exception {
        String expectedFile = "/Users/donatello/Projects/Llunatic-Ex/lunaticExperiments/misc/experiments/ICDE2014/doctors//data_scenario_quality/5k/core/core.csv";
        String generatedFile = "/Users/donatello/Temp/lunatic_tmp/TSQLDoctors_FR_1-2015-10-20_124356/Solution01.csv";
        List<String> exclude = Arrays.asList(new String[]{"oid", "tid", "dconf", "rconf"});
        double precisionForVariable = 0.5;
        CompareInstances comparator = new CompareInstances();
        SimilarityResult similarityResult = comparator.evaluate(expectedFile, generatedFile, Arrays.asList(new String[]{DAOConfiguration.FEATURE_LOCAL_ID_WITH_LLUNS}), exclude, precisionForVariable);
        FeatureResult feature = similarityResult.getFeatureResultByFeatureName("FeatureLocalIdWithLLUNs");
        logger.info("Precision: " + feature.getPrecision());
        logger.info("Recall: " + feature.getRecall());
        logger.info("FMeasure: " + feature.getFmeasure());
        if (logger.isDebugEnabled()) logger.debug("## Similarity result\n\tExpected: " + similarityResult.getExpectedInstance() + "\n\tTranslated: " + similarityResult.getTranslatedInstance());
        if (logger.isDebugEnabled()) logger.debug("## VIOLATIONS\n" + feature.getViolations().printViolations());
    }

}
