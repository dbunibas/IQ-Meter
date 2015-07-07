/*
 Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
 Giansalvatore Mecca - giansalvatore.mecca@unibas.it
 Salvatore Raunich - salrau@gmail.com

 This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool
    
 ++Spicy is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 ++Spicy is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.unibas.spicybenchmark.operators.generators;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicybenchmark.Configuration;
import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import it.unibas.spicybenchmark.model.TuplePair;
import it.unibas.spicybenchmark.model.features.FeatureAncestorChildGlobalId;
import it.unibas.spicybenchmark.model.features.FeatureAncestorChildLocalId;
import it.unibas.spicybenchmark.model.features.FeatureCollection;
import it.unibas.spicybenchmark.model.features.FeatureGlobalId;
import it.unibas.spicybenchmark.model.features.FeatureJoinsGlobalId;
import it.unibas.spicybenchmark.model.features.FeatureJoinsLocalId;
import it.unibas.spicybenchmark.model.features.FeatureLocalId;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNs;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNsBlocking;
import it.unibas.spicybenchmark.model.features.FeatureLocalIdWithLLUNsGreedy;
import it.unibas.spicybenchmark.model.features.FeatureParentChildGlobalId;
import it.unibas.spicybenchmark.model.features.FeatureParentChildLocalId;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeatureCollectionGenerator {

    private Log logger = LogFactory.getLog(FeatureCollectionGenerator.class);

    public FeatureCollection generate(INode expectedInstanceNode, INode translatedInstanceNode,
            List<String> exclusionList, IDataSourceProxy dataSource, Configuration configuration) {
        return generate(expectedInstanceNode, translatedInstanceNode, exclusionList, dataSource, configuration.getFeatures());
    }

    public FeatureCollection generate(INode expectedInstanceNode, INode translatedInstanceNode,
            List<String> exclusionList, IDataSourceProxy dataSource, List<String> features) {
        GenerateTupleNodeBenchmark generator = new GenerateTupleNodeBenchmark();
        List<TupleNodeBenchmark> expectedInstanceTuples = generator.generate(expectedInstanceNode, exclusionList);
        if (logger.isDebugEnabled()) logger.debug("Expected Tuple Nodes: " + expectedInstanceTuples);
        List<TupleNodeBenchmark> translatedInstanceTuples = generator.generate(translatedInstanceNode, exclusionList);
        if (logger.isDebugEnabled()) logger.debug("Translated Tuple Nodes: " + translatedInstanceTuples);
        FeatureCollection featureCollection = new FeatureCollection(expectedInstanceTuples, translatedInstanceTuples);

        for (String feature : features) {
            if (feature.equals(DAOConfiguration.FEATURE_LOCAL_ID)) {
                FeatureLocalId featureLocalId = new FeatureLocalId(expectedInstanceTuples, translatedInstanceTuples);
                featureCollection.addFeature(featureLocalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_GLOBAL_ID)) {
                FeatureGlobalId featureGlobalId = new FeatureGlobalId(expectedInstanceTuples, translatedInstanceTuples);
                featureCollection.addFeature(featureGlobalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_JOINS_LOCAL_ID)) {
                TuplesInJoinGenerator joinGenerator = new TuplesInJoinGenerator(dataSource, translatedInstanceNode, expectedInstanceNode, translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsInJoin = joinGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsInJoin = joinGenerator.getTranslatedTuplePairs();

                FeatureJoinsLocalId tuplesInJoinLocalIdFeature = new FeatureJoinsLocalId(expectedTuplePairsInJoin, translatedTuplePairsInJoin);
                featureCollection.addFeature(tuplesInJoinLocalIdFeature);
            }
            if (feature.equals(DAOConfiguration.FEATURE_JOINS_GLOBAL_ID)) {
                TuplesInJoinGenerator joinGenerator = new TuplesInJoinGenerator(dataSource, translatedInstanceNode, expectedInstanceNode, translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsInJoin = joinGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsInJoin = joinGenerator.getTranslatedTuplePairs();

                FeatureJoinsGlobalId tuplesInJoinGlobalIdFeature = new FeatureJoinsGlobalId(expectedTuplePairsInJoin, translatedTuplePairsInJoin);
                featureCollection.addFeature(tuplesInJoinGlobalIdFeature);
            }
            if (feature.equals(DAOConfiguration.FEATURE_NESTING_PARENT_CHILD_LOCAL_ID)) {
                ParentChildNestingGenerator parentChildGenerator = new ParentChildNestingGenerator(translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsParentChild = parentChildGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsParentChild = parentChildGenerator.getTranslatedTuplePairs();

                FeatureParentChildLocalId featureParentChildLocalId = new FeatureParentChildLocalId(expectedTuplePairsParentChild, translatedTuplePairsParentChild);
                featureCollection.addFeature(featureParentChildLocalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_NESTING_PARENT_CHILD_GLOBAL_ID)) {
                ParentChildNestingGenerator parentChildGenerator = new ParentChildNestingGenerator(translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsParentChild = parentChildGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsParentChild = parentChildGenerator.getTranslatedTuplePairs();

                FeatureParentChildGlobalId featureParentChildGlobalId = new FeatureParentChildGlobalId(expectedTuplePairsParentChild, translatedTuplePairsParentChild);
                featureCollection.addFeature(featureParentChildGlobalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_NESTING_ANCESTOR_CHILD_LOCAL_ID)) {
                AncestorChildNestingGenerator ancestorChildGenerator = new AncestorChildNestingGenerator(translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsAncestorChild = ancestorChildGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsAncestorChild = ancestorChildGenerator.getTranslatedTuplePairs();

                FeatureAncestorChildLocalId featureAncestorChildLocalId = new FeatureAncestorChildLocalId(expectedTuplePairsAncestorChild, translatedTuplePairsAncestorChild);
                featureCollection.addFeature(featureAncestorChildLocalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_NESTING_ANCESTOR_CHILD_GLOBAL_ID)) {
                AncestorChildNestingGenerator ancestorChildGenerator = new AncestorChildNestingGenerator(translatedInstanceTuples, expectedInstanceTuples);
                List<TuplePair> expectedTuplePairsAncestorChild = ancestorChildGenerator.getExpectedTuplePairs();
                List<TuplePair> translatedTuplePairsAncestorChild = ancestorChildGenerator.getTranslatedTuplePairs();

                FeatureAncestorChildGlobalId featureAncestorChildGlobalId = new FeatureAncestorChildGlobalId(expectedTuplePairsAncestorChild, translatedTuplePairsAncestorChild);
                featureCollection.addFeature(featureAncestorChildGlobalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_LOCAL_ID_WITH_LLUNS)) {
                FeatureLocalIdWithLLUNs featureLocalId = new FeatureLocalIdWithLLUNs(expectedInstanceTuples, translatedInstanceTuples);
                featureCollection.addFeature(featureLocalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_LOCAL_ID_WITH_LLUNS_GREEDY)) {
                FeatureLocalIdWithLLUNsGreedy featureLocalId = new FeatureLocalIdWithLLUNsGreedy(expectedInstanceTuples, translatedInstanceTuples);
                featureCollection.addFeature(featureLocalId);
            }
            if (feature.equals(DAOConfiguration.FEATURE_LOCAL_ID_WITH_LLUNS_BLOCKING)) {
                FeatureLocalIdWithLLUNsBlocking featureLocalId = new FeatureLocalIdWithLLUNsBlocking(expectedInstanceTuples, translatedInstanceTuples);
                featureCollection.addFeature(featureLocalId);
            }

        }
        return featureCollection;
    }
}
