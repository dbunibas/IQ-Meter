/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence.effortgraph;

import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.utility.SpicyEngineUtility;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Antonio Genovese
 */
@SuppressWarnings("unchecked")
public class DAOClover {

    private static Log logger = LogFactory.getLog(DAOClover.class);
    private EffortGraphNode boxReader = null;
    private EffortGraphNode boxWriter = null;
    private String metaSource = "";
    private String metaTarget = "";
    private List<EffortGraphNode> boxList = new ArrayList<EffortGraphNode>();
    //edges from box to schema e meta
    private Map<String, String[]> boxMappingMap = new HashMap<String, String[]>();
    private Map<String, String> readerMappingMap = new HashMap<String, String>();
    private Map<String, EffortGraphNode> metaNodeMap = new LinkedHashMap<String, EffortGraphNode>();
    private Map<String, String> metadataSchema = new HashMap<String, String>();
    //edges for box, schema or meta
    private List<String[]> edgesList = new ArrayList<String[]>();
    private String metaDir;
    private Element elementRootDocument;
    private final String[] OPERATORS_FUNCTION = {"&&", "||", "==", "!=", "<=", ">=", "=>", "=<", "+=", "-=", "/=", "*=", "=", "+", "-", ":", "/", "*", "<", ">", " and ", " or "};
    private int progressiveId = 0;

    private void clearCollection() {
        boxReader = null;
        boxWriter = null;
        metaSource = "";
        metaTarget = "";
        boxList = new ArrayList<EffortGraphNode>();
        //edges from box to schema e meta
        boxMappingMap = new HashMap<String, String[]>();
        readerMappingMap = new HashMap<String, String>();
        metaNodeMap = new LinkedHashMap<String, EffortGraphNode>();
        metadataSchema = new HashMap<String, String>();
        //edges for box, schema or meta
        edgesList = new ArrayList<String[]>();
        elementRootDocument = null;
        progressiveId = 0;
    }

    private int getProgressiveNumber() {
        return progressiveId++;
    }

    public Map<String, String> getReaderMappingMap() {
        return readerMappingMap;
    }

    public void setMetaDir(String metaDir) {
        this.metaDir = metaDir;
    }

    public String[] getBoxMapping(String idBox) {
        return (String[]) this.boxMappingMap.get(idBox);
    }

    public Map<String, EffortGraphNode> getMetaNodeMap() {
        if (this.metaNodeMap.isEmpty()) {
            return null;
        }
        return metaNodeMap;
    }

    public List<EffortGraphNode> getBoxList() {
        return boxList;
    }

    public List<String[]> getEdgesList() {
        return edgesList;
    }

    public EffortGraphNode getBoxWriter() {
        return boxWriter;
    }

    public EffortGraphNode getBoxReader() {
        return boxReader;
    }

    public void parse(String mappingFile, String schemaSourcePath, String schemaTargetPath) throws DAOException {
        clearCollection();
        loadSchema(schemaSourcePath);
        loadSchema(schemaTargetPath);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(mappingFile);
            Document document = buildDOM(inStream);
            this.elementRootDocument = document.getRootElement();
            for (Element elem : (List<Element>) elementRootDocument.getChild("Phase").getChildren("Edge")) {
                parseEdge(elem);
            }
            Map<Integer, Element> elemMap = new HashMap<Integer, Element>();
            for (Element elem : (List<Element>) elementRootDocument.getChild("Phase").getChildren("Node")) {
                logger.debug(elem.getAttribute("type"));
                if (elem.getAttributeValue("enabled").equals("enabled")) {
                    if (elem.getAttributeValue("type").equals("XML_EXTRACT")) {
                        parseBoxReader(elem);
                    } else if (elem.getAttributeValue("type").equals("EXT_XML_WRITER")) {
                        parseBoxWriter(elem);
                    } else {
                        Integer key = Integer.parseInt(elem.getAttributeValue("guiX"));
                        if (!elemMap.isEmpty()) {
                            while (elemMap.containsKey(key)) {
                                key = key.intValue() + 1;
                            }
                        }
                        elemMap.put(key, elem);
                    }
                }
            }

            List<Integer> listKey = new ArrayList<Integer>();
            listKey.addAll(elemMap.keySet());
            Collections.sort(listKey);
            for (Integer key : listKey) {
                parseBox(elemMap.get(key));
            }

            inStream.close();

        } catch (Exception ex) {
            logger.error("Parse error " + ex);
            throw new DAOException(ex);

        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private Document buildDOM(FileInputStream mappingFilePath) throws DAOException {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(mappingFilePath);
            logger.trace("Schema document:" + document.toString());
            return document;
        } catch (Exception jde) {
            logger.error(jde);
            throw new DAOException(jde);
        }
    }

//    public INodePosition positionNode (Element elem){
//        int x = Integer.parseInt(elem.getAttributeValue("guiX"));
//        int y = Integer.parseInt(elem.getAttributeValue("guiY"));
//       return NodePosition.getInstance().setPosition(x, y);
//    }
    private void parseBoxReader(Element elem) throws DAOException {
        try {

            EffortGraphNode reader = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
            //EffortGraphNode reader = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"),positionNode(elem));
            this.boxReader = reader;
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new ByteArrayInputStream(elem.getChild("attr").getText().getBytes()));
            Element elementRoot = document.getRootElement();
            String path = elementRoot.getChild("Mapping").getAttributeValue("element");
            this.metaSource = elem.getAttributeValue("schema");
            this.metaSource = this.metaSource.substring(this.metaSource.lastIndexOf("/") + 1);
            logger.info("Box Reader in: " + path + " metaSource schema " + this.metaSource);
            List<String> list = new ArrayList<String>();
            list.add(path);
            Map<String, String> metadata = searchMetadata(reader.getNodeId());
            for (String key : metadata.keySet()) {
                if (metadata.get(key).contains("_Flat")) {
                    list.add(metadata.get(key));
                }
            }
            if (list.size() > 1) {
                buildMappingReader(elementRoot.getChild("Mapping"), path, metadata);
            } else {
                buildMappingReader(elementRoot.getChild("Mapping"), path);
            }
            this.boxMappingMap.put(this.boxReader.getNodeId(), (String[]) list.toArray(new String[list.size()]));
        } catch (Exception ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    private void buildMappingReader(Element root, String path, Map<String, String> metadata) {
        String pattern = "\\.|\\{|\\}|\\/";
        for (Element elem : (List<Element>) root.getChildren("Mapping")) {
            path += "." + elem.getAttributeValue("element");
            String portOut = elem.getAttributeValue("outPort") + ".ou";
            logger.debug("Reader element Mapping " + portOut);
            if (elem.getAttributeValue("xmlFields") != null) {
                String[] mapping = elem.getAttributeValue("xmlFields").split(";");
                for (int i = 0; i < mapping.length; i++) {
                    String str = mapping[i].replaceAll(pattern, "");
                    logger.debug("Reader element Mapping adding edges from node " + path + "." + str + " to node " + metadata.get(portOut) + "." + str);
                    edgesList.add(new String[]{path + "." + str, metadata.get(portOut) + "." + str});
                }
            }
            if (!elem.getChildren().isEmpty()) {
                buildMappingReader(elem, path, metadata);
            }
        }
    }

    private void buildMappingReader(Element root, String path) {
        String pattern = "\\.|\\{|\\}|\\/";
        for (Element elem : (List<Element>) root.getChildren("Mapping")) {
            String portOut = elem.getAttributeValue("outPort") + ".ou";
            logger.trace("Reader element Mapping " + portOut);
            if (elem.getAttributeValue("xmlFields") != null) {
                String[] filds = elem.getAttributeValue("cloverFields").split(";");
                String[] mapping = elem.getAttributeValue("xmlFields").split(";");
                for (int i = 0; i < mapping.length; i++) {
                    String str = mapping[i].replaceAll(pattern, "");
                    String node = path + "." + str;
                    if (!mapping[i].contains("/")) {
                        node = path + "." + elem.getAttributeValue("element") + "." + str;
                    }
                    logger.debug("String mapping in Reader " + node + " to field " + this.metadataSchema.get(filds[i]));
                    this.readerMappingMap.put(node, this.metadataSchema.get(filds[i]));
                }
            }
            if (!elem.getChildren().isEmpty()) {
                path += "." + elem.getAttributeValue("element");
                buildMappingReader(elem, path);
            }
        }

    }

    private void parseBoxWriter(Element elem) throws DAOException {
        try {
            EffortGraphNode writer = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
            //EffortGraphNode writer = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"), positionNode(elem));
            this.boxWriter = writer;
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new ByteArrayInputStream(elem.getChild("attr").getText().getBytes()));
            Element elementRoot = document.getRootElement();
            String path = elementRoot.getName();
            this.metaTarget = elem.getAttributeValue("xmlSchemaURL");
            this.metaTarget = this.metaTarget.substring(this.metaTarget.lastIndexOf("/") + 1);
            logger.debug("Box Writer in: " + path + " metaTarget schema " + this.metaTarget);
            List<String> list = new ArrayList<String>();
            list.add(path);
            Map<String, String> metadata = searchMetadata(writer.getNodeId());
            for (String key : metadata.keySet()) {
                if (metadata.get(key).contains("_Flat")) {
                    list.add(metadata.get(key));
                }
            }
            buildMappingWriter((Element) elementRoot.getChildren().get(0), path, metadata);
            this.boxMappingMap.put(this.boxWriter.getNodeId(), (String[]) list.toArray(new String[list.size()]));
        } catch (Exception ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    private void buildMappingWriter(Element root, String path, Map<String, String> metadata) {
        String value = root.getText().replaceAll("\n|\\s", "");
        logger.trace("Writer element Mapping " + path + "." + root.getName() + " elem value " + value + " childrens " + root.getChildren().size());
        if (value.contains("$") && !this.readerMappingMap.isEmpty()) {
            String portIn = value.substring(1, 2) + ".in";
            logger.trace("port in for metadata writer: " + portIn);
            value = value.substring(value.indexOf(".") + 1);
            logger.debug("Writer element Mapping adding edges from node " + metadata.get(portIn) + "." + value + " to node " + path + "." + root.getName());
            edgesList.add(new String[]{metadata.get(portIn) + "." + value, path + "." + root.getName()});
        } else if (value.contains("$")) {
            String portIn = value.substring(1, 2) + ".in";
            logger.trace("port in for metadata writer: " + portIn);
            value = value.substring(value.indexOf(".") + 1);
            logger.debug("Writer element Mapping adding edges from node " + metadata.get(portIn) + "." + value + " to node " + path + "." + value);
            edgesList.add(new String[]{metadata.get(portIn) + "." + value, path + "." + value});
        }
        if (!root.getChildren().isEmpty()) {
            path += "." + root.getName();
            for (Object elem : root.getChildren()) {
                buildMappingWriter((Element) elem, path, metadata);
            }
        }
    }

    private void parseBox(Element elem) throws DAOException {
        if (elem.getAttributeValue("type").equals("PARTITION")) {
            buildBoxPartition(elem);
        } else if (elem.getAttributeValue("type").equals("REFORMAT") || elem.getAttributeValue("type").equals("EXT_HASH_JOIN")) {
            buildBoxReformat(elem);
        } else if (elem.getAttributeValue("type").equals("DEDUP") || elem.getAttributeValue("type").equals("EXT_SORT") || elem.getAttributeValue("type").equals("MERGE")) {
            EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
            //EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"), positionNode(elem));
            String attribute = elem.getAttributeValue("type").toLowerCase().replace("ext_", "");
            Map<String, String> metadataMap = searchMetadata(elem.getAttributeValue("id"));
            String[] arrayKey = elem.getAttributeValue(attribute + "Key").replaceAll("\\([^\\(]*\\)", "").split(";");
            logger.trace("Box Dedup, Sort or Merge: " + elem.getAttributeValue(attribute + "Key").replaceAll("\\([^\\(]*\\)", ""));
            for (int i = 0; i < arrayKey.length; i++) {
                logger.debug("item for array mapping of " + elem.getAttributeValue("id") + " : " + metadataMap.get("0.in") + "." + arrayKey[i]);
                arrayKey[i] = metadataMap.get("0.in") + "." + arrayKey[i];
            }
            boxList.add(box);
            this.boxMappingMap.put(box.getNodeId(), arrayKey);
        } else if (elem.getAttributeValue("type").equals("EXT_FILTER")) {
            buildBoxFilter(elem);
        } else if (elem.getAttributeValue("type").equals("AGGREGATE")) {
            buildBoxAggregate(elem);
        } else {
            EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
            //EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"), positionNode(elem));
            boxList.add(box);
        }
    }

    private void buildBoxFilter(Element elem) throws DAOException {
        EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
        boxList.add(box);
        Map<String, String> metadataMap = searchMetadata(elem.getAttributeValue("id"));
        Element filterExpression = searchFilterExpression(elem);
        if (filterExpression != null) {
            String[] attribute = filterExpression.getTextNormalize().replaceAll("\\$in.", "\\$").split(";");
            List<String> mapping = new ArrayList<String>();
            for (String string : attribute) {
                logger.trace("Value box Filter " + string);
                String map = string.replaceAll("\\(|\\)", "");
                if (map.contains("$")) {
                    mappingFilterComponent(metadataMap, map, mapping, string, box);
                }
            }
            if (!mapping.isEmpty()) {
                this.boxMappingMap.put(box.getNodeId(), mapping.toArray(new String[mapping.size()]));
            }
        }
    }

    //search attribute expression from filter
    private Element searchFilterExpression(Element elem) {
        for (Object elemFilter : elem.getChildren("attr")) {
            if (elemFilter instanceof Element) {
                if (((Element) elemFilter).getAttributeValue("name").equals("filterExpression")) {
                    return (Element) elemFilter;
                }
            }
        }
        return null;
    }

    //mapping node in the filter expression
    private void mappingFilterComponent(Map<String, String> metadataMap, String map, List<String> mapping, String string, EffortGraphNode box) throws DAOException {
        Pattern patter = Pattern.compile("\\w+\\(.+\\).*");
        //for expression with '='
        if (map.contains("=") && map.contains("$")) {
            map = map.substring(map.indexOf("$"), map.indexOf("="));
            logger.trace("mappingFilterComponent String map " + map);
            String port = map.substring(1, 2) + ".in";
            mapping.add(metadataMap.get(port) + "." + map.substring(2).replaceAll("\\W", ""));
            logger.debug("Value box Filter " + metadataMap.get(port) + "." + map.substring(2).replaceAll("\\W", ""));
        } else if (map.contains("$")) {
            //for expression without '='
            map = this.searchExpression(map);
            map = map.substring(map.indexOf("$"), map.indexOf("##"));
            String port = map.substring(1, 2) + ".in";
            mapping.add(metadataMap.get(port) + "." + map.substring(2).replaceAll("\\W", ""));
        }
        //condition with function
        if (patter.matcher(string).matches()) {
            logger.trace("Filter condition with function: " + string);
            functionForFilter(string, box, metadataMap);
        } else {
            //condition with annotation
            logger.trace("mapping filter component, string map: " + map);
            string = string.substring(string.indexOf(map)).replaceAll("\\s+", "");
            createLabelNode(string, box, metadataMap);
        }
    }

    private void functionForFilter(String string, EffortGraphNode box, Map metadata) throws DAOException {
        string = string.replaceAll("\\$in.", "\\$");
        String function = string.substring(0, string.lastIndexOf(")") + 1);
        logger.debug("Filter Box Function " + function);
        function = function.replaceAll("\\(.+\\)", "()");
        EffortGraphNode nodeFunction = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, function, box.getNodeId() + string);
        String edgeElem = string.substring(string.indexOf("(") + 1, string.indexOf(")"));
        logger.trace("Edges element for function in: " + edgeElem);
        this.boxList.add(nodeFunction);
        this.edgesList.add(new String[]{metadata.get(edgeElem.charAt(1) + ".in") + "." + edgeElem.substring(3).replaceAll("\\W", ""), nodeFunction.getNodeId()});
        if (string.contains("=")) {
            createAnnotationForFunction(string.substring(string.indexOf("=")), box, nodeFunction, metadata);
        }
    }

    //search logic or arithmetic operator and map it
    private String searchExpression(String expression) {
        for (String operator : OPERATORS_FUNCTION) {
            logger.trace("compare operator " + operator + " to expression " + expression);
            if (expression.contains(operator) && !(expression.contains("##" + operator) || expression.contains(operator + "__"))) {
                logger.trace("expression " + expression + " contains operator " + operator);
                expression = expression.replaceAll(operator, "##" + operator + "__");
            }
        }
        return expression;
    }

    private void buildBoxPartition(Element elem) throws DAOException {
        EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
        //EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"), positionNode(elem));
        Map<String, String> metadataMap = searchMetadata(elem.getAttributeValue("id"));
        if (elem.getAttribute("partitionKey") != null) {
            String[] attribute = elem.getAttribute("partitionKey").getValue().replaceAll("\\s|\\t", "").split(";");
            this.boxMappingMap.put(box.getNodeId(), attribute);
            for (int i = 0; i < attribute.length; i++) {
                logger.debug("item for attribute mapping PARTITION: " + metadataMap.get("0.in") + "." + attribute[i]);
                attribute[i] = metadataMap.get("0.in") + "." + attribute[i];
            }
        }
        if (elem.getChild("attr") != null) {
            String function = elem.getChild("attr").getValue().replaceAll("//.*\\n", "");
            logger.debug("Partition Function: "+ function);
            analyzePartitionFunction(function, metadataMap, box);
        }
        boxList.add(box);
    }

    private void analyzePartitionFunction(String function, Map<String, String> metadataMap, EffortGraphNode box) {
        if (function.contains("if(") || function.contains("if (") && function.contains("getOutputPort()")) {
            function = function.replace("if (", "if(");
            function = function.substring(function.indexOf("if(") + 3).replaceAll("\\n", "");
            function = function.replaceAll("\\$in.", "\\$");
            logger.debug("Partition Function" + function);
            String[] str = function.split("\\$");
            List<String> nodes = new ArrayList<String>();
            logger.debug("String array size: " + str.length);
            for (String string : str) {
                if (!string.isEmpty()) {
                    logger.debug("partition string analyzed: " + "$" + string);
                    string = string.replaceAll("!|\\||\\&", "=");
                    string = string.substring(string.indexOf("0.") + 2, string.indexOf("=")).trim();
                    function = function.replaceAll("\\$0." + string, "");
                    logger.debug("function Annotation: " + function);
                    logger.debug("partition string node: " + string);

                    nodes.add(metadataMap.get("0.in") + "." + string);
                }
            }
            //EffortGraphNode annotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, "if("+function.substring(0, function.indexOf(")")) + ")", function);
            // TODO-Modifica
            EffortGraphNode annotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, function.substring(0, function.indexOf(")")), function);
            this.boxList.add(annotation);
            // annotation = createFunctionToOperator(annotation);
            for (String node : nodes) {
                logger.debug("Add edge from: " + node + " to " + annotation.getNodeId());
                edgesList.add(new String[]{node, annotation.getNodeId()});
            }
            edgesList.add(new String[]{metadataMap.get("0.in"), box.getNodeId()});

        }

    }

    private void buildBoxAggregate(Element elem) throws DAOException {
        EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
        this.boxList.add(box);
        Map<String, String> metadataMap = searchMetadata(elem.getAttributeValue("id"));
        String aggregateKey = elem.getAttributeValue("aggregateKey");
        String portIn = 0 + ".in";
        String portOut = 0 + ".ou";
        for (String key : aggregateKey.split(";")) {
            edgesList.add(new String[]{metadataMap.get(portIn) + "." + key, box.getNodeId()});
        }
        String mapping = elem.getAttributeValue("mapping");
        for (String string : mapping.split(";")) {
            logger.trace("String node aggretation " + string);
            String nodeIn = string.substring(string.lastIndexOf(":=$") + 3);
            String nodeOut = string.substring(string.indexOf("$") + 1, string.indexOf(":"));
            analyzeExpressions(nodeIn, portIn, metadataMap);
            analyzeExpressions(nodeOut, portOut, metadataMap);
            if (nodeIn.contains("(") && nodeIn.contains(")")) {
                logger.trace("String node aggretation with function " + nodeIn);
                String function = string.replaceAll(":", "");
                function = function.replace("$", "$0.");
                //if function with same in-out
                if (function.contains("($0." + nodeOut + "")) {
                    logger.debug("****plus occurrence of $0." + nodeOut + " in " + function);
                    function = function.substring(0, function.indexOf("(") + 1) + function.substring(function.indexOf(")"));
                    this.edgesList.add(new String[]{box.getNodeId() + function, metadataMap.get("0.ou") + "." + nodeOut});
                }
                logger.debug("String node aggretation function to analyzed " + function);
                createLabelNode(function, box, metadataMap);
            } else {
                if (!(metadataMap.get(portIn) + "." + nodeIn).equals(metadataMap.get(portOut) + "." + nodeOut)) {
                    logger.debug("Add Edges for node aggregation " + metadataMap.get(portIn) + "." + nodeIn + " to " + metadataMap.get(portOut) + "." + nodeOut);
                    edgesList.add(new String[]{metadataMap.get(portIn) + "." + nodeIn, metadataMap.get(portOut) + "." + nodeOut});
                }
            }
        }
    }

    private void buildBoxReformat(Element elem) throws DAOException {
        EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"));
        //EffortGraphNode box = new EffortGraphNode(EffortGraphNode.TYPE_NODE_BOX, elem.getAttributeValue("guiName"), elem.getAttributeValue("id"), positionNode(elem));
        String function = elem.getChild("attr").getValue();
        String pattern = "(^\\s|\\n\\s|return+.*;|\\t|\\s|\\})";

        // function = function.substring(function.indexOf("function integer transform() {") + 30, function.indexOf("// function ")).replaceAll(pattern, "");
        function = function.substring(function.indexOf("function integer transform() {") + 30, function.indexOf("// function "));
        function = function.replaceAll("\\//.*\n", "");
        function = function.replaceAll(pattern, "");
        function = function.replaceAll("\\$out.", "\\$").replaceAll("\\$in.", "\\$");
        logger.debug("String function: " + function);
        String[] patt = function.split(";");
        List<String> mapping = new ArrayList<String>();
        Map<String, String> metadataMap = null;
        for (String str : patt) {
            logger.trace("String analyzed: " + str);
            if (str.contains("=$") && countOccurrences("$", str.substring(str.indexOf("="))) == 1) {
                metadataMap = searchMetadata(elem.getAttributeValue("id"));
                if (str.contains("){")) {
                    analyzeCondition(str, box, metadataMap);
                    str = str.substring(str.indexOf("{") + 1);
                }
                if (metadataMap != null) {
                    String portIn = str.substring(str.lastIndexOf("=$") + 2, str.lastIndexOf("=$") + 4) + "in";
                    String portOut = str.substring(1, 3) + "ou";
                    String nodeIn = str.substring(str.lastIndexOf("=$") + 4);
                    String nodeOut = str.substring(str.indexOf("$") + 3, str.indexOf("="));
                    nodeIn = analyzeExpressions(nodeIn, portIn, metadataMap);
                    nodeOut = analyzeExpressions(nodeOut, portOut, metadataMap);
                    logger.debug("Key for mapping edges: in " + portIn + ", out " + portOut + " from node "
                            + metadataMap.get(portIn) + "." + nodeIn + " to node " + metadataMap.get(portOut) + "." + nodeOut);
                    if (metadataMap.get(portIn) == null) {
                        portIn = portIn.replace("in", "ou");
                    }
                    edgesList.add(new String[]{metadataMap.get(portIn) + "." + nodeIn,
                        metadataMap.get(portOut) + "." + nodeOut});
                }
            } else {
                createLabelNode(str, box, metadataMap);
            }
        }
        if (metadataMap != null) {
            mapping.add(metadataMap.get("0.in"));
            for (String key : metadataMap.keySet()) {
                if (!key.contains("0.in")) {
                    mapping.add(metadataMap.get(key));
                }
            }
            if (elem.getAttributeValue("type").equals("EXT_HASH_JOIN")) {
                mapping = new ArrayList<String>();
                mappingJoinKey(elem, mapping, metadataMap);
            }

        }
        this.boxMappingMap.put(box.getNodeId(), mapping.toArray(new String[mapping.size()]));
        boxList.add(box);
    }

    //analyzes whether there is a mathematical expression and add an annotation
    private String analyzeExpressions(String node, String port, Map<String, String> metadata) {
        Pattern patternAnnotation = Pattern.compile(".*(\\+|\\-|\\*|\\/).*");
        if (patternAnnotation.matcher(node).matches()) {
            logger.trace("Node IN with annotation " + node);
            logger.trace("String cleaned " + node.replaceAll("\\W*\\d", ""));
            String nodeCleaned = node.replaceAll("(\\W*\\d*)", "");
            String annotation = node.replace(nodeCleaned, "");
            logger.trace("String annotation" + annotation);
            EffortGraphNode annotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, node);
            //TODO modifica
//            annotationNode = createFunctionToOperator(annotationNode);

            if (port.contains("in")) {
                logger.debug("edges: " + metadata.get(port) + "." + nodeCleaned + ", annotation [" + annotationNode.getLabel() + "]");
                edgesList.add(new String[]{metadata.get(port) + "." + nodeCleaned, annotationNode.getNodeId()});
            } else {
                logger.debug("edges: " + "annotation [" + annotationNode.getLabel() + "], " + metadata.get(port) + "." + nodeCleaned);
                edgesList.add(new String[]{annotationNode.getNodeId(), metadata.get(port) + "." + nodeCleaned});
            }
            this.boxList.add(annotationNode);
            return nodeCleaned;
        }
        return node;
    }

    //create node Function or Annotation
    private void createLabelNode(String str, EffortGraphNode box, Map metadata) throws DAOException {
        Pattern patter = Pattern.compile("\\w+\\(*.+\\).*");
        Pattern patterReturn = Pattern.compile("^return+\\w*");
        logger.debug("String node function or annotation: " + str);
        if (str.contains("if(") || str.contains("while(") || str.contains("for(")) {
            analyzeCondition(str, box, metadata);
            str = str.substring(str.indexOf("{") + 1);
        }
        logger.debug("Result of matcher: " + str.substring(str.indexOf("=") + 1) + " function? --> " + patter.matcher(str.substring(str.indexOf("=") + 1)).matches());
        if (patter.matcher(str.substring(str.indexOf("=") + 1)).matches() && !str.contains("\"(")) {
            String edgeArguments = str.substring(str.indexOf("("), str.indexOf(")"));
            logger.trace("Edges element for function in: " + edgeArguments);
            String strFn;
            if (str.contains("=")) {
                strFn = str.substring(str.indexOf("=") + 1, str.indexOf(")") + 1).replaceAll("\\(.+\\)", "()");
            } else {
                strFn = str.replaceAll("\\(.+\\)", "()");
            }
            EffortGraphNode nodeFunction = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, strFn, box.getNodeId() + str);
            //EffortGraphNode nodeFunction = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, strFn, box.getNodeId() + str, NodePosition.getInstance().setPosition(box.getPosition().getX() - 60, box.getPosition().getY()));
            this.mappingNodeFunction(edgeArguments, nodeFunction, str, metadata);
            this.createEdgesForFunctionLabelNode(str, nodeFunction, metadata, box);
            this.boxList.add(nodeFunction);
            //mappingLabelNode(str, box);
        } else if (!patterReturn.matcher(str).matches()) { //in this case ignore return statement
            logger.info("String node is annotation");
            str = mappingLabelNode(str, box, metadata);
            createNodeAnnotation(str, box, metadata);

        }
    }

    //TODO Test IT !!!
    //create out edges from functionNode
    private void createEdgesForFunctionLabelNode(String str, EffortGraphNode nodeFunction, Map metadata, EffortGraphNode box) throws DAOException {
        if (str.contains("=")) {
            String edgeElem = str.substring(0, str.indexOf("="));
            logger.trace("Edges element for function out: " + edgeElem);
            this.edgesList.add(new String[]{nodeFunction.getNodeId(), metadata.get(edgeElem.charAt(1) + ".ou") + "." + edgeElem.substring(3).replaceAll("\\W", "")});
            logger.debug("Edges out for function: " + nodeFunction.getNodeId() + " ->" + metadata.get(edgeElem.charAt(1) + ".ou") + "." + edgeElem.substring(3).replaceAll("\\W", ""));
        }
        String strSub = str.substring(str.indexOf(")"));
        if (strSub.length() > 1 && !strSub.contains(").")) {
            createAnnotationForFunction(strSub.substring(1), box, nodeFunction, metadata);
        }

    }

    //analyzed expression condition 
    private void analyzeCondition(String str, EffortGraphNode box, Map metadata) throws DAOException {
        logger.debug("=" + str.substring(str.indexOf("(") + 1, str.indexOf("){")));
        createLabelNode(str.substring(str.indexOf("(") + 1, str.indexOf("){")), box, metadata);
    }

    //create edges between schema nodes and function, depending on the arguments of function 
    private void mappingNodeFunction(String edgeElem, EffortGraphNode nodeFunction, String str, Map metadata) {
        if (countOccurrences("$", edgeElem) == 0) {
            logger.trace("occurences of $ " + countOccurrences("$", edgeElem));
            nodeFunction.setLabel(str.substring(str.lastIndexOf("=") + 1));
        } else {
            if (edgeElem.contains(",") && countOccurrences("$", edgeElem) > 1) {
                for (String arg : edgeElem.split(",")) {
                    arg = arg.replaceAll("\\,|\\(|\\)", "");
                    logger.trace("occurences of $ " + countOccurrences("$", arg) + " port " + arg.charAt(2));
                    this.edgesList.add(new String[]{metadata.get(arg.charAt(1) + ".in") + "." + arg.substring(3).replaceAll("\\W", ""), nodeFunction.getNodeId()});
                    logger.debug("Metadata edges for function: " + metadata.get(arg.charAt(2) + ".in") + "." + arg.substring(3).replaceAll("\\W", "") + " --> " + nodeFunction.getNodeId());
                    logger.debug("Metadata edges for function: (metadata ' " + metadata.get(arg.charAt(2) + ".in") + "')" + "." + arg.substring(3).replaceAll("\\W", "") + " --> " + nodeFunction.getNodeId());
                }
            } else {
                logger.trace("occurences of $ " + countOccurrences("$", edgeElem) + " port " + edgeElem.charAt(2));
                this.edgesList.add(new String[]{metadata.get(edgeElem.charAt(2) + ".in") + "." + edgeElem.substring(3).replaceAll("\\W", ""), nodeFunction.getNodeId()});
                logger.debug("Metadata edges for function: " + metadata.get(edgeElem.charAt(2) + ".in") + "." + edgeElem.substring(3).replaceAll("\\W", "") + " --> " + nodeFunction.getNodeId());
                logger.debug("Metadata edges for function: (metadata ' " + metadata.get(edgeElem.charAt(2) + ".in") + "')" + "." + edgeElem.substring(3).replaceAll("\\W", "") + " --> " + nodeFunction.getNodeId());
            }
        }
    }

    //creates node function when the annotation has an operator
    //TODO: modifica ignorata
//    private EffortGraphNode createFunctionToOperator(EffortGraphNode annotationNode) {
//        EffortGraphNode nodeF = annotationNode;
//        annotationNode.setNodeId(annotationNode.getNodeId() + this.getProgressiveNumber());
//        for (String operator : OPERATORS_FUNCTION) {
//            logger.trace("compare operator " + operator + " to annotation " + annotationNode.getLabel());
//            if (annotationNode.getLabel().contains(operator)) {
//                nodeF = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, operator, operator + annotationNode.getNodeId());
//                this.boxList.add(nodeF);
//                this.edgesList.add(new String[]{nodeF.getNodeId(), annotationNode.getNodeId()});
//                annotationNode.setLabel(annotationNode.getLabel().replace(operator, ""));
//            }
//        }
//        this.boxList.add(annotationNode);
//        return nodeF;
//    }
    private void createNodeAnnotation(String str, EffortGraphNode box, Map<String, String> metadata) {
        logger.trace("createNodeAnnotation - Annotation String :" + str);
        if (str != null && !str.equals("")) {
            String annotation = str;
            String source = "";
            if (annotation.contains("$")) {
                String expression = this.searchExpression(annotation);
                if (expression.contains("##")) {
                    String operator = expression.substring(expression.indexOf("##") + 2, expression.indexOf("__"));
                    int indexOperator = annotation.indexOf(operator);
                    int indexNode = annotation.indexOf("$");
                    if (indexOperator > indexNode) {
                        source = annotation.substring(indexNode, indexOperator);
                        annotation = annotation.substring(annotation.indexOf(source) + source.length());
                    } else {
                        source = annotation.substring(indexNode);
                        annotation = annotation.substring(0, indexOperator);
                    }
                }
            }
            if (!annotation.isEmpty()) {
                EffortGraphNode nodeAnnotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, box.getNodeId() + annotation);
                //EffortGraphNode nodeAnnotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, box.getNodeId() + annotation,NodePosition.getInstance().setPosition(box.getPosition().getX() - 60, box.getPosition().getY()));
                //TODO modifica ignorata
                //nodeAnnotation = createFunctionToOperator(nodeAnnotation);
                this.boxList.add(nodeAnnotation);
                if (!source.equals("")) {
                    this.edgesList.add(new String[]{metadata.get(source.substring(1, 2) + ".in") + "." + source.substring(3), nodeAnnotation.getNodeId()});
                    logger.debug("Edges for node annotation " + metadata.get(source.substring(1, 2) + ".in") + "." + source.substring(3) + " to " + nodeAnnotation.getNodeId());
                }
            }
        }
    }

    private void createAnnotationForFunction(String str, EffortGraphNode box, EffortGraphNode nodeFunction, Map<String, String> metaData) throws DAOException {
        str = mappingLabelNode(str, box, metaData);
        if (str != null && !str.equals("")) {
            String annotation = str;
            EffortGraphNode nodeAnnotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, box.getNodeId() + annotation);
            //TODO modifica ignorata
            //nodeAnnotation = createFunctionToOperator(nodeAnnotation);
            //EffortGraphNode nodeAnnotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, box.getNodeId() + annotation,NodePosition.getInstance().setPosition(box.getPosition().getX() - 60, box.getPosition().getY()));
            this.boxList.add(nodeAnnotation);
            this.edgesList.add(new String[]{nodeAnnotation.getNodeId(), nodeFunction.getNodeId()});
        }
    }

    //mapping box with node
    private String mappingLabelNode(String str, EffortGraphNode box, Map<String, String> metaData) throws DAOException {
        logger.debug("mappingLabelNode method: LabelNode string --> " + str);
        str = str.replaceAll("\\$out.", "\\$").replaceAll("\\$in.", "\\$");
        if (str.contains("$") && str.contains("=")) {
            if (countOccurrences("$", str.substring(str.indexOf("="))) == 1) {
                return this.mappingLabelNodeSingle(str, metaData);
            } else if (countOccurrences("$", str.substring(str.indexOf("="))) > 1) {
                return this.mappingLabelNodeMultiple(str, metaData, box);
            }
        }
        return str;
    }

    private String mappingLabelNodeSingle(String str, Map<String, String> metaData) {
        logger.trace("MetadataLabelNode: " + str);
        String portIn = str.substring(str.lastIndexOf("$") + 1, str.lastIndexOf("$") + 3) + "in";
        String portOut = str.substring(1, 3) + "ou";
        String source = str.substring(str.lastIndexOf("$") + 3).replaceAll("\\,.*|\\+.*|\\-.*|\\*.*|\\/.*|\\(.*|\\).*", "");
        String target = str.substring(str.indexOf("$") + 3, str.indexOf("="));
        logger.debug("Key for mapping edges: in " + portIn + ", out " + portOut + " from node " + metaData.get(portIn) + "." + source + " to node " + metaData.get(portOut) + "." + target);
        edgesList.add(new String[]{metaData.get(portIn) + "." + source,
            metaData.get(portOut) + "." + target});
        logger.trace("String original: " + str + "- Source string - " + source + " index " + str.indexOf(source) + " lenght source: " + source.length()
                + " -->" + str.substring(str.indexOf(source) + source.length()));
        return str.substring(str.indexOf(source) + source.length());
    }

    private String mappingLabelNodeMultiple(String str, Map<String, String> metaData, EffortGraphNode box) throws DAOException {
        String target = str.substring(str.indexOf("$"), str.indexOf("=") + 1);
        logger.trace("Target for mapping Label Annotation " + target);
        if (str.contains(")*")) {
            logger.debug("Label Annotation expression " + str);
            String node = target.substring(3, target.lastIndexOf("=")) + str.substring(str.lastIndexOf(")") + 1);
            String portOut = target.substring(1, 3) + "ou";
            logger.debug("Node analyzed: " + node + " port out - " + portOut);
            str = str.replace(str.substring(str.lastIndexOf(")") + 1), "").replaceAll("\\(|\\)", "");
            analyzeExpressions(node, portOut, metaData);
        }
        String[] splits = str.substring(str.indexOf("=") + 1).split("\\+|\\-|\\*|\\/|\\:");
        for (String string : splits) {
            logger.debug("Splitted string :" + target + string);
            str = mappingLabelNode((target + string), box, metaData);
        }
        return str;
    }

    private void mappingJoinKey(Element elem, List<String> mapping, Map<String, String> metadata) {
        String join = elem.getAttributeValue("joinKey");
        logger.trace("Join key: " + join);
        int port = 1;
        String in = "";
        for (String str : join.split(";")) {
            logger.debug("Join Mapping key: " + "0.in" + "." + str.substring(1, str.indexOf("=")));
            if (str.contains("#")) {
                port++;
                str = str.replace("#", "");
            }
            if (in.equals("") || !in.equals(metadata.get("0.in") + "." + str.substring(1, str.indexOf("=")))) {
                in = metadata.get("0.in") + "." + str.substring(1, str.indexOf("="));
                mapping.add(in);
            }
            mapping.add(metadata.get(port + ".in") + "." + str.substring(str.indexOf("=") + 2));

        }
        mapping.add(metadata.get("0.ou"));
    }

    private int countOccurrences(String find, String string) {
        int count = 0;
        int indexOf = 0;
        while (indexOf > -1) {
            indexOf = string.indexOf(find, indexOf + 1);
            if (indexOf > -1) {
                count++;
            }
        }
        return count;
    }

    private void parseEdge(Element elem) {
        String[] array = new String[2];
        String source = elem.getAttributeValue("fromNode");
        String target = elem.getAttributeValue("toNode");
        source = source.substring(0, source.indexOf(":"));
        target = target.substring(0, target.indexOf(":"));
        array[0] = source;
        array[1] = target;
        logger.debug("Source edges: " + array[0] + " Target edges: " + array[1]);
        this.edgesList.add(array);
    }

    private Map<String, String> searchMetadata(String nodeId) throws DAOException {
        String meta = "";
        logger.debug("search metadata for " + nodeId);
        for (Element element : (List<Element>) this.elementRootDocument.getChild("Phase").getChildren("Edge")) {
            String toNode = element.getAttributeValue("toNode").substring(0, element.getAttributeValue("toNode").indexOf(":"));
            String fromNode = element.getAttributeValue("fromNode").substring(0, element.getAttributeValue("fromNode").indexOf(":"));
            if (toNode.equals(nodeId)) {
                String port = element.getAttributeValue("toNode").substring(element.getAttributeValue("toNode").lastIndexOf(":") + 1);
                String attributeValueMetadata = getAttributeValueMetadata(element);
                if (attributeValueMetadata == null) logger.error("Unable to extract attributeValue for metadata in node: "+nodeId);
                meta += port + ".in." + attributeValueMetadata + ";";
            } else if (fromNode.equals(nodeId)) {
                String port = element.getAttributeValue("fromNode").substring(element.getAttributeValue("fromNode").lastIndexOf(":") + 1);
                String attributeValueMetadata = getAttributeValueMetadata(element);
                if (attributeValueMetadata == null) logger.error("Unable to extract attributeValue for metadata in node: "+nodeId);
                meta += port + ".ou." + attributeValueMetadata + ";";
            }
        }
        return writeMetadata(meta, nodeId);
    }

    private Map<String, String> writeMetadata(String meta, String nodeId) throws DAOException {
        logger.debug("MetaDataStringID=" + meta + "for node " + nodeId);
        if (!meta.equals("")) {
            logger.trace("scanning metadata");
            Map<String, String> mapMetadata = new HashMap<String, String>();
            for (Element element : (List<Element>) this.elementRootDocument.getChild("Global").getChildren("Metadata")) {
                if (meta.contains(element.getAttributeValue("id"))) {
                    meta = replaceMeta(meta, element, nodeId);
                }
            }
            logger.debug("MetaData=" + meta);
            for (String string : meta.split(";")) {
                //NOTE: key=portin.in or portout.ou
                String key = string.substring(0, string.indexOf(".")) + string.substring(1, 4);
                string = string.replace(key + ".", "");
                logger.debug("key " + key + " - metadata " + string);
                mapMetadata.put(key, string);
            }
            return mapMetadata;
        }
        return null;
    }

    private String replaceMeta(String meta, Element element, String nodeId) throws DAOException {
        try {
            String string = element.getAttributeValue("fileURL");
            logger.debug("metadata String fileURL " + string);
            String metaInfo = string.substring(string.lastIndexOf("/") + 1, string.indexOf(".fmt"));
            //String metaInfo = string.substring(string.indexOf() + 1, string.indexOf(".fmt"));
            logger.debug("Parsing metainfo: " + metaInfo);
            if (string.contains("_Flat.fmt") || string.contains("_flat.fmt") || string.contains("_FLAT.fmt")) {
                //Es. Schema.xsd_Element_Flat.fmt
                // metaInfo = string.substring(string.indexOf("/") + 1);
                metaInfo = string.substring(string.lastIndexOf("/") + 1);
                metaInfo = createNodeMetadata(metaInfo);
            } else if (string.contains("xsd_")) {
                logger.debug("metadata String with xsd_ ");
//                if (string.contains(this.metaSource)) {
//                    metaInfo = this.metadataSchema.get(metaInfo);
//                } else if (string.contains(this.metaTarget)) {
//                    //Es. SchemaTarger.xsd_Element.fmt
//                    metaInfo = this.metadataSchema.get(metaInfo);
//                }
                if (!this.metadataSchema.containsKey(metaInfo)) {
                    throw new IllegalArgumentException("Unable to extract metainfo: " + metaInfo + "\n in map:" + SpicyEngineUtility.printMap(metadataSchema));

                }
                metaInfo = this.metadataSchema.get(metaInfo);
            }
            logger.debug("Replace: " + element.getAttributeValue("id") + " with " + metaInfo + " in " + meta);
            return meta.replaceAll(element.getAttributeValue("id"), metaInfo);
        } catch (Exception ex) {
            logger.error("replaceMeta error " + ex);
            ex.printStackTrace();
            throw new DAOException(ex);
        }

    }

    private String createNodeMetadata(String metaFile) throws DAOException {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(this.metaDir + metaFile);
            Document document = buildDOM(inStream);
            Element elementRoot = document.getRootElement();
            String path = elementRoot.getAttributeValue("name") + "_Flat";
            if (this.metaNodeMap.get(path) == null) {
                this.metaNodeMap.put(path, new EffortGraphNode(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA, path, path));
                logger.trace("MetaNode root: " + elementRoot.getAttributeValue("name"));
                for (Element elem : (List<Element>) elementRoot.getChildren("Field")) {
                    String name = elem.getAttributeValue("name");
                    logger.debug("## Add Meta node " + path + "." + name);
                    this.metaNodeMap.put(path + "." + name, new EffortGraphNode(EffortGraphNode.TYPE_NODE_METADATA_SCHEMA, name, path + "." + name));
                }
            }
            inStream.close();
            return path;
        } catch (Exception ex) {
            logger.error("create Node Metadata " + ex);
            throw new DAOException(ex);

        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    //"schema="
    private void loadSchema(String schemaPath) throws DAOException {
        String schemaName = schemaPath.substring(schemaPath.lastIndexOf("/") + 1);
        logger.debug("load schema: schemaName " + schemaName);

        XSImplementation xsImplementation = new XSImplementationImpl();
        XSLoader xsLoader = xsImplementation.createXSLoader(null);
        XSModel xsModel = xsLoader.loadURI(schemaPath);
        XSNamedMap elementMap = xsModel.getComponents(XSConstants.ELEMENT_DECLARATION);
        XSElementDeclaration topElementDecl = (XSElementDeclaration) elementMap.item(0);
        processElementXSD(topElementDecl, topElementDecl.getName(), schemaName);
    }

    private void processElementXSD(XSElementDeclaration elementDecl, String parent, String schemaName) {
        if (elementDecl == null) {
            logger.debug("elementDecl is null.");
            return;
        }
        XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) elementDecl.getTypeDefinition();
        XSModelGroup modelGroup = (XSModelGroup) typeDef.getParticle().getTerm();
        XSObjectList particles = modelGroup.getParticles();
        for (int i = 0; i < particles.getLength(); ++i) {
            XSParticle particle = (XSParticle) particles.item(i);
            XSTerm term = particle.getTerm();
            if (term instanceof XSElementDeclaration) {
                XSElementDeclaration newElement = (XSElementDeclaration) term;
                if (newElement.getTypeDefinition().getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
                    logger.trace("Complex element: " + parent + "." + newElement.getName() + " key for element " + schemaName + "_" + newElement.getName());
                    metadataSchema.put(schemaName + "_" + newElement.getName(), parent + "." + newElement.getName());
                    processElementXSD((newElement), parent + "." + newElement.getName(), schemaName);
                } else if (newElement.getTypeDefinition().getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
                    metadataSchema.put(newElement.getName(), parent + "." + newElement.getName());
                }
            }
        }
    }

    public static void createDirectoryMetaClover(String scenarioPath, String dirMeta) {
        String path = scenarioPath.substring(0, scenarioPath.lastIndexOf(File.separator) + 1);
        path = path + "meta" + File.separator;
        logger.debug("Directory Meta " + path);
        File directoryMetaTarget = new File(path);
        try {
            directoryMetaTarget.mkdir();
            File directoryMetaSource = new File(dirMeta);
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    return (pathname.getName().contains("_Flat.fmt") || pathname.getName().contains("_flat.fmt") || pathname.getName().contains("_FLAT.fmt"));
                }
            };
            FileUtils.copyDirectory(directoryMetaSource, directoryMetaTarget, filter);
        } catch (Exception ex) {
            logger.error(" create Directory Meta Clover: " + ex);

        }
    }

    private String getAttributeValueMetadata(Element element) {
        if (logger.isTraceEnabled()) logger.trace(element.getAttributes());
        if (element.getAttributeValue("metadata") != null) return element.getAttributeValue("metadata");
        if (element.getAttributeValue("persistedImplicitMetadata") != null) return element.getAttributeValue("persistedImplicitMetadata");
        return null;
    }
}
