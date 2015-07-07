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
package it.unibas.spicybenchmark.persistence;

import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicybenchmark.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DAOConfiguration {

    private static Log logger = LogFactory.getLog(DAOConfiguration.class);
    private static Properties configurationProperties;
    private static final String YES = "yes";
    // File Paths
    private static final String EXPERIMENT_NAME = "EXPERIMENT_NAME";
    private static final String EXPERIMENT_FOLDER = "EXPERIMENT_FOLDER";
    private static final String SCHEMA_FILE = "SCHEMA_FILE";
    private static final String EXPECTED_INSTANCE_FILE = "EXPECTED_INSTANCE_FILE";
    private static final String TRANSLATED_INSTANCE_FILE = "TRANSLATED_INSTANCE_FILE";
    private static final String EXCLUSIONS_FILE = "EXCLUSIONS_FILE";
    // Features
    public static final String FEATURE_LOCAL_ID = "FEATURE_LOCAL_ID";
    public static final String FEATURE_LOCAL_ID_WITH_LLUNS = "FEATURE_LOCAL_ID_WITH_LLUNS";
    public static final String FEATURE_LOCAL_ID_WITH_LLUNS_GREEDY = "FEATURE_LOCAL_ID_WITH_LLUNS_GREEDY";
    public static final String FEATURE_LOCAL_ID_WITH_LLUNS_BLOCKING = "FEATURE_LOCAL_ID_WITH_LLUNS_BLOCKING";
    public static final String FEATURE_GLOBAL_ID = "FEATURE_GLOBAL_ID";
    public static final String FEATURE_JOINS_LOCAL_ID = "FEATURE_JOINS_LOCAL_ID";
    public static final String FEATURE_JOINS_GLOBAL_ID = "FEATURE_JOINS_GLOBAL_ID";
    public static final String FEATURE_NESTING_PARENT_CHILD_LOCAL_ID = "FEATURE_NESTING_PARENT_CHILD_LOCAL_ID";
    public static final String FEATURE_NESTING_PARENT_CHILD_GLOBAL_ID = "FEATURE_NESTING_PARENT_CHILD_GLOBAL_ID";
    public static final String FEATURE_NESTING_ANCESTOR_CHILD_LOCAL_ID = "FEATURE_NESTING_ANCESTOR_CHILD_LOCAL_ID";
    public static final String FEATURE_NESTING_ANCESTOR_CHILD_GLOBAL_ID = "FEATURE_NESTING_ANCESTOR_CHILD_GLOBAL_ID";
    // Other metrics
    public static final String TREE_EDIT_DISTANCE_VALIENTE = "TREE_EDIT_DISTANCE_VALIENTE";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_INSERT = "TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_INSERT";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_DELETE = "TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_DELETE";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_SUBSTITUE = "TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_SUBSTITUE";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR = "TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_ALWAYS_TRUE = "ALWAYS_TRUE_NC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_NAMED_TREE = "NAMED_TREE_NC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_TYPED_TREE = "TYPED_TREE_NC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_DEBUG = "DEBUG_NC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION = "TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_COMMON = "COMMON_DC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_LOGARITHMIC = "LOGARITHMIC_DC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_WORST_CASE = "WORST_CASE_DC";
    public static final String TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_DEBUG = "DEBUG_DC";
    public static final String TREE_EDIT_DISTANCE_SHASHA = "TREE_EDIT_DISTANCE_SHASHA";
    // Logging
    private static final String LOG_MESSAGES = "LOG_MESSAGES";
    public static final String LOG_MINIMAL = "MINIMAL";
    public static final String LOG_NORMAL = "NORMAL";
    public static final String LOG_VERBOSE = "VERBOSE";
    private static final String LOG_APPENDER = "LOG_APPENDER";
    public static final String LOG_APPENDER_STDOUT = "STDOUT";
    public static final String LOG_APPENDER_FILE = "FILE";
    public static final String LOG_APPENDER_BOTH = "BOTH";
    private static final String LOG_FILE = "LOG_FILE";

    public static Configuration loadConfigurationFile(String configurationFile) throws DAOException {
        try {
            String absolutePath = new File(DAOConfiguration.class.getResource(configurationFile).toURI()).getAbsolutePath();
            Configuration configuration = new Configuration(absolutePath);
            loadProperties(absolutePath, configuration);
            return configuration;
        } catch (URISyntaxException ex) {
            throw new DAOException(ex);
        }
    }

    private static void loadProperties(String configurationFile, Configuration configuration) {
        configurationProperties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(configurationFile);
            configurationProperties.load(inputStream);
            String experimentName = configurationProperties.getProperty(EXPERIMENT_NAME);
            configuration.setExperimentName(experimentName);
            String schemaFile = configurationProperties.getProperty(SCHEMA_FILE);
            configuration.setSchemaPath(schemaFile);
            String expectedInstanceFile = configurationProperties.getProperty(EXPECTED_INSTANCE_FILE);
            configuration.setExpectedInstancePath(expectedInstanceFile);
            String translatedInstanceFile = configurationProperties.getProperty(TRANSLATED_INSTANCE_FILE);
            configuration.setTranslatedInstancePath(translatedInstanceFile);
            String exclusionsFile = configurationProperties.getProperty(EXCLUSIONS_FILE);
            configuration.setExclusionsFile(exclusionsFile);
            checkFilePaths(configuration);

            if (configurationProperties.getProperty(FEATURE_LOCAL_ID, null) != null) {
                configuration.addFeature(FEATURE_LOCAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_GLOBAL_ID, null) != null) {
                configuration.addFeature(FEATURE_GLOBAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_JOINS_LOCAL_ID, null) != null) {
                configuration.addFeature(FEATURE_JOINS_LOCAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_JOINS_GLOBAL_ID, null) != null) {
                configuration.addFeature(FEATURE_JOINS_GLOBAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_NESTING_PARENT_CHILD_LOCAL_ID, null) != null) {
                configuration.addFeature(FEATURE_NESTING_PARENT_CHILD_LOCAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_NESTING_PARENT_CHILD_GLOBAL_ID, null) != null) {
                configuration.addFeature(FEATURE_NESTING_PARENT_CHILD_GLOBAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_NESTING_ANCESTOR_CHILD_LOCAL_ID, null) != null) {
                configuration.addFeature(FEATURE_NESTING_ANCESTOR_CHILD_LOCAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_NESTING_ANCESTOR_CHILD_GLOBAL_ID, null) != null) {
                configuration.addFeature(FEATURE_NESTING_ANCESTOR_CHILD_GLOBAL_ID);
            }
            if (configurationProperties.getProperty(FEATURE_LOCAL_ID_WITH_LLUNS, null) != null) {
                configuration.addFeature(FEATURE_LOCAL_ID_WITH_LLUNS);
            }
            if (configurationProperties.getProperty(FEATURE_LOCAL_ID_WITH_LLUNS_GREEDY, null) != null) {
                configuration.addFeature(FEATURE_LOCAL_ID_WITH_LLUNS_GREEDY);
            }
            if (configurationProperties.getProperty(FEATURE_LOCAL_ID_WITH_LLUNS_BLOCKING, null) != null) {
                configuration.addFeature(FEATURE_LOCAL_ID_WITH_LLUNS_BLOCKING);
            }

            // Other metrics
            //TREE EDIT DISTANCE
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE, null) != null && configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE, null).equals(YES)) {
                configuration.setTreeEditDistanceValiente(true);
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_SHASHA, null) != null && configurationProperties.getProperty(TREE_EDIT_DISTANCE_SHASHA, null).equals(YES)) {
                configuration.setTreeEditDistanceShasha(true);
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_INSERT, null) != null) {
                try {
                    double weight = Double.valueOf(configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_INSERT, null));
                    configuration.setTreeEditDistanceValienteWeightInsert(weight);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_INSERT + " property");
                }
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_DELETE, null) != null) {
                try {
                    double weight = Double.valueOf(configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_DELETE, null));
                    configuration.setTreeEditDistanceValienteWeightDelete(weight);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_DELETE + " property");
                }
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_SUBSTITUE, null) != null) {
                try {
                    double weight = Double.valueOf(configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_SUBSTITUE, null));
                    configuration.setTreeEditDistanceValienteWeightSubstitute(weight);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + TREE_EDIT_DISTANCE_VALIENTE_WEIGHT_SUBSTITUE + " property");
                }
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR, null) != null) {
                String nodeComparator = configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR, null);
                if (!nodeComparator.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_ALWAYS_TRUE)
                        && !nodeComparator.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_NAMED_TREE)
                        && !nodeComparator.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_TYPED_TREE)
                        && !nodeComparator.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR_DEBUG)) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + TREE_EDIT_DISTANCE_VALIENTE_TREE_NODE_COMPARATOR + " property");
                }else{
                    configuration.setTreeEditDistanceValienteTreeNodeComparator(nodeComparator);
                }
            }
            if (configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION, null) != null) {
                String distanceConversion = configurationProperties.getProperty(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION, null);
                if (!distanceConversion.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_COMMON)
                        && !distanceConversion.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_LOGARITHMIC)
                        && !distanceConversion.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_WORST_CASE)
                        && !distanceConversion.equalsIgnoreCase(TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION_DEBUG)) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + TREE_EDIT_DISTANCE_VALIENTE_DISTANCE_CONVERSION + " property");
                }else{
                    configuration.setTreeEditDistanceValienteDistanceConversion(distanceConversion);
                }
            }



            String logMessages = configurationProperties.getProperty(LOG_MESSAGES);
            if (!logMessages.equalsIgnoreCase(LOG_MINIMAL) && !logMessages.equalsIgnoreCase(LOG_NORMAL) && !logMessages.equalsIgnoreCase(LOG_VERBOSE)) {
                throw new IllegalArgumentException("Error in configuration file. Wrong value for " + LOG_MESSAGES + " property");
            }
            configuration.setLogMessages(logMessages);

            String logAppender = configurationProperties.getProperty(LOG_APPENDER);
            if (!logAppender.equalsIgnoreCase(LOG_APPENDER_STDOUT) && !logAppender.equalsIgnoreCase(LOG_APPENDER_FILE) && !logAppender.equalsIgnoreCase(LOG_APPENDER_BOTH)) {
                throw new IllegalArgumentException("Error in configuration file. Wrong value for " + LOG_APPENDER + " property");
            }
            configuration.setLogAppender(logAppender);
            if (configuration.printOnFile()) {
                String logFile = configurationProperties.getProperty(LOG_FILE, null);
                if (logFile == null || logFile.equals("")) {
                    throw new IllegalArgumentException("Error in configuration file. Wrong value for " + LOG_FILE + " property");
                }
                configuration.setLogFile(logFile);
            }
        } catch (Exception e) {
            logger.error("Error: Impossible to read properties file: " + "\n" + e.getMessage());
        }
    }

    private static void checkFilePaths(Configuration configuration) throws DAOException {
        checkFile(configuration.getSchemaAbsolutePath());
        checkFile(configuration.getExpectedInstanceAbsolutePath());
        checkFile(configuration.getTranslatedInstanceAbsolutePath());
        checkFile(configuration.getExclusionsFileAbsolutePath());
    }

    private static void checkFile(String fileName) throws DAOException {
        File file = new File(fileName);
        if (!file.isFile() || !file.exists()) {
            throw new DAOException("File not found: " + file);
        }

    }
}
