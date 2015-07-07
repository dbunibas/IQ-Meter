package it.unibas.spicybenchmark.persistence;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.AttributeNode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.datasource.nodes.SetNode;
import it.unibas.spicy.model.datasource.nodes.TupleNode;
import it.unibas.spicy.model.datasource.values.IOIDGeneratorStrategy;
import it.unibas.spicy.model.datasource.values.IntegerOIDGenerator;
import it.unibas.spicy.model.datasource.values.NullValueFactory;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.Types;
import it.unibas.spicy.utility.SpicyEngineConstants;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoadCSVFile {

    private static Log logger = LogFactory.getLog(LoadCSVFile.class.getName());
    private String CSV_SEPARATOR = ",";
    private String tablePrefix = "#";

    public INode load(String instancePath) throws DAOException {
        IOIDGeneratorStrategy oidGenerator = new IntegerOIDGenerator();
        INode instanceNode = new TupleNode(SpicyEngineConstants.DATASOURCE_ROOT_LABEL, oidGenerator.getNextOID());
        instanceNode.setRoot(true);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(instancePath)));
            String line;
            String tableName = null;
            SetNode tableNode = null;
            List<String> attributes = null;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (line.startsWith(tablePrefix)) {
                    tableName = readTableName(line);
                    tableNode = new SetNode(tableName, oidGenerator.getNextOID());
                    instanceNode.addChild(tableNode);
                    line = reader.readLine();
                    attributes = readAttributes(line);
                    continue;
                }
                if (tableName == null) {
                    throw new DAOException("Instance must start with a table");
                }
                TupleNode tupleNode = loadTuple(line, tableName, attributes, oidGenerator);
                tableNode.addChild(tupleNode);
            }
        } catch (Exception ex) {
            throw new DAOException("Unable to load file " + instancePath + "\n" + ex.getLocalizedMessage());
        }
        return instanceNode;
    }

    private String readTableName(String line) {
        return line.substring(tablePrefix.length());
    }

    private List<String> readAttributes(String line) {
        List<String> attributes = new ArrayList<String>();
        attributes.addAll(Arrays.asList(line.split(CSV_SEPARATOR)));
        return attributes;
    }

    private TupleNode loadTuple(String line, String tableName, List<String> attributes, IOIDGeneratorStrategy oidGenerator) throws DAOException {
        TupleNode tupleNode = new TupleNode(tableName + "Tuple", oidGenerator.getNextOID());
        String[] values = line.split(CSV_SEPARATOR);
        if (values.length != attributes.size()) {
            throw new DAOException("Tuple " + line + " has a wrong number of attributes. Expected attributes: " + attributes);
        }
        for (int i = 0; i < values.length; i++) {
            String attribute = attributes.get(i);
            String value = values[i];
            AttributeNode attributeNodeInstance = new AttributeNode(attribute, oidGenerator.getNextOID());
            LeafNode leafNodeInstance;
            if (value.equals("NULL")) {
                leafNodeInstance = new LeafNode(Types.STRING, NullValueFactory.getNullValue());
            } else {
                String valueType = findType(value);
                leafNodeInstance = new LeafNode(valueType, value);
            }
            attributeNodeInstance.addChild(leafNodeInstance);
            tupleNode.addChild(attributeNodeInstance);
        }
        return tupleNode;
    }

    private String findType(Object value) {
        try {
            Integer.parseInt(value.toString());
            return Types.INTEGER;
        } catch (NumberFormatException e) {
        }
        try {
            Double.parseDouble(value.toString());
            return Types.DOUBLE;
        } catch (NumberFormatException e) {
        }
        return Types.STRING;
    }
}
