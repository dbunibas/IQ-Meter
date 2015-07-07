/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.persistence.effortgraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author francescodefino
 */
@SuppressWarnings("unchecked")
public class DAOMapforce {

    private Log logger = LogFactory.getLog(this.getClass());
    private String fileName;
    private Document documento;
    private Element root;
    private Element component;
    private Element structure;
    private Element children;
    private Element graph;
    private int countSource;
    private int countTarget;
    private String schemaS;
    private String schemaT;
    private Map<String, String> vertexMap;
    private Map<String, String> functionMap;
    private Map<String, String> annotationMap;
    private List<String> vertexKeyList;
    private List<String[]> vertexList;
    private List<String[]> vertexLinkLisk;
    private List<String> functionUidList;
    private List<String[]> functionListTmp;
    private List<String[]> functionList;
    private Map<String, String> mappaFunzioni;
    private List<String> listaKeyFunzioni;
    private List<String> listPosSchemaSource;
    private List<String> listPosSchemaTarget;

    public List<String[]> getVertexLinkList() {
        return this.vertexLinkLisk;
    }

    public List<String> getFunctionUidList() {
        return this.functionUidList;
    }

    public String getVertexName(String key) {
        return vertexMap.get(key);
    }

    public String getFunctionName(String uid) {
        return functionMap.get(uid);
    }

    public String getAnnotationValue(String uid) {
        return annotationMap.get(uid);
    }

    public List<String> getVertexKeyList() {
        return this.vertexKeyList;
    }

    public List<String[]> getVertexList() {
        return this.vertexList;
    }

    public int getCountSource() {
        return this.countSource;
    }

    public int getCountTarget() {
        return this.countTarget;
    }

    public String getVertex(String name) {
        for (String key : vertexKeyList) {
            if (name.equals(vertexMap.get(key))) {
                return key;
            }
        }
        return null;
    }

    public List<String[]> getFunctionList() {
        return this.functionList;
    }

    public String[] getVertexInList(String key) {
        for (String[] vertex : vertexList) {
            if (vertex[0] != null && vertex[0].equals(key)) {
                return vertex;
            }
        }
        return null;
    }

    public List<String> getListPosSchema(String type) {
        if (type.equals("S")) {
            return this.listPosSchemaSource;
        } else {
            return this.listPosSchemaTarget;
        }
    }

    public int getPosition(String type, String pos) {
        if (type.equals("S")) {
            for (int i = 0; i < listPosSchemaSource.size(); i++) {
                if (listPosSchemaSource.get(i).equals(pos)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < listPosSchemaTarget.size(); i++) {
                if (listPosSchemaTarget.get(i).equals(pos)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void parse(String fileName, String schemaS, String schemaT) {
        logger.debug("FileName: " + fileName + " , schemaS: " + schemaS + ", schemaT: " + schemaT);
        clearCollection();
        this.fileName = fileName;
        this.schemaS = schemaS;
        this.schemaT = schemaT;
        Document document = caricaDocumento();
        root = document.getRootElement();
        logger.debug("Element root " + root.getName());
        component = root.getChild("component");
        structure = component.getChild("structure");
        children = structure.getChild("children");
        graph = structure.getChild("graph");
        analisiGraph(graph);
        analisiComponents(children);

        setPosition();

        sortPosition();
        if (logger.isDebugEnabled()) {
            printVertexList();
        }

//        logger.info(listPosSchemaTarget);
    }

    private void clearCollection() {
        vertexMap = new HashMap<String, String>();
        functionMap = new HashMap<String, String>();
        annotationMap = new HashMap<String, String>();
        vertexKeyList = new ArrayList<String>();
        vertexList = new ArrayList<String[]>();
        vertexLinkLisk = new ArrayList<String[]>();
        functionUidList = new ArrayList<String>();
        countSource = 1;
        countTarget = 1;
        functionListTmp = new ArrayList<String[]>();
        functionList = new ArrayList<String[]>();
        mappaFunzioni = new HashMap<String, String>();
        listaKeyFunzioni = new ArrayList<String>();
        listPosSchemaSource = new ArrayList<String>();
        listPosSchemaTarget = new ArrayList<String>();
    }

    private Document caricaDocumento() {
        SAXBuilder builder = new SAXBuilder();
        try {
            documento = builder.build(fileName);
        } catch (JDOMException jde) {
            logger.error(jde);
        } catch (IOException ioe) {
            System.out.println(ioe);
            logger.error(ioe);
        }
        return documento;
    }

    private void analisiComponents(Element children) {
        List<Element> listaComponent = children.getChildren("component");
        for (Element elemento : listaComponent) {
            getComponents(elemento);
        }
    }

    private void getComponents(Element children) {
        List<Element> listaComponentDocument = new ArrayList<Element>();
        List<Element> listaComponentFunction = new ArrayList<Element>();
        String valoreAttributo = children.getAttributeValue("name");
        logger.trace("get Component valore attributo: " + valoreAttributo);

        if (valoreAttributo.equals("document") || valoreAttributo.equals(schemaS) || valoreAttributo.equals(schemaT)) {
            logger.trace("getData ");
            getData(children);
        } else {
            logger.trace("getFunctions");
            getFunctions(children);
        }
    }

    private void getData(Element component) {
        //nuovo
        Element view = component.getChild("view");
        String ltx = view.getAttributeValue("ltx");
        if (ltx == null) {
            ltx = Integer.toString(0);
        }
//        logger.info(ltx);
        //
        Element data = component.getChild("data");
        Element rootData = data.getChild("root");
        Element entryFile = rootData.getChild("entry");
        Element entryDoc = entryFile.getChild("entry");
        Element entryVertex = entryDoc.getChild("entry");
        String attributeValue = entryVertex.getAttributeValue("name");
        if (attributeValue.equalsIgnoreCase("Source")) {
            getSourceData(entryVertex, attributeValue, ltx);
            listPosSchemaSource.add(ltx);
            countSource++;
        } else {
            getTargetData(entryVertex, attributeValue, ltx);
            listPosSchemaTarget.add(ltx);
            countTarget++;
        }
        logger.trace("get data couter --> source: " + countSource + " target: " + countTarget);
    }

    private void getSourceData(Element entryVertex, String attributeValue, String ltx) {
        String valueInpkey = entryVertex.getAttributeValue("inpkey");
        String valueOutkey = entryVertex.getAttributeValue("outkey");
        String[] values = new String[5];
        values[0] = valueInpkey;
        values[1] = attributeValue;
        values[2] = null;
        values[3] = "S";
        values[4] = ltx;
        String[] values2 = new String[5];
        values2[0] = valueOutkey;
        values2[1] = attributeValue;
        values2[2] = null;
        values2[3] = "S";
        values2[4] = ltx;
        vertexList.add(values);
        vertexList.add(values2);
        if (valueInpkey != null && valueOutkey == null) {
            vertexMap.put(valueInpkey, attributeValue);
//            values[0] = valueInpkey;
//            values[1] = attributeValue;
//            values[2] = null;
//            values[3] = "S";
//            values[4] = ltx;
//            vertexList.add(values);
        }
        if (valueOutkey != null && valueInpkey == null) {
            vertexMap.put(valueOutkey, attributeValue);
//            values[0] = valueOutkey;
//            values[1] = attributeValue;
//            values[2] = null;
//            values[3] = "S";
//            values[4] = ltx;
//            vertexList.add(values);
        }
        if (valueInpkey != null && valueOutkey != null) {
            vertexMap.put(valueInpkey, attributeValue);
            vertexMap.put(valueOutkey, attributeValue);
//            values[0] = valueInpkey;
//            values[1] = attributeValue;
//            values[2] = null;
//            values[3] = "S";
//            values[4] = ltx;
//            String[] values2 = new String[5];
//            values2[0] = valueOutkey;
//            values2[1] = attributeValue;
//            values2[2] = null;
//            values2[3] = "S";
//            values2[4] = ltx;
//            vertexList.add(values);
//            vertexList.add(values2);
        }
        putInVertexMap(entryVertex, attributeValue, countSource, "S", ltx);
    }

    private void getTargetData(Element entryVertex, String attributeValue, String ltx) {
        String valueInpkey = entryVertex.getAttributeValue("inpkey");
        String valueOutkey = entryVertex.getAttributeValue("outkey");
        String[] values = new String[5];
        values[0] = valueInpkey;
        values[1] = attributeValue;
        values[2] = null;
        values[3] = "T";
        values[4] = ltx;
        String[] values2 = new String[5];
        values2[0] = valueOutkey;
        values2[1] = attributeValue;
        values2[2] = null;
        values2[3] = "T";
        values2[4] = ltx;
        vertexList.add(values);
        vertexList.add(values2);
        if (valueInpkey != null && valueOutkey == null) {
            vertexMap.put(valueInpkey, attributeValue);
        }
        if (valueOutkey != null && valueInpkey == null) {
            vertexMap.put(valueOutkey, attributeValue);
        }
        if (valueInpkey != null && valueOutkey != null) {
            vertexMap.put(valueInpkey, attributeValue);
            vertexMap.put(valueOutkey, attributeValue);
        }
        putInVertexMap(entryVertex, attributeValue, countTarget, "T", ltx);
    }

    private void putInVertexMap(Element entry, String path, int count, String type, String ltx) {
        List<Element> entryVertexList = entry.getChildren("entry");
        for (Element vertex : entryVertexList) {
            String[] values = new String[5];
            String valueInpkey = vertex.getAttributeValue("inpkey");
            String valueOutkey = vertex.getAttributeValue("outkey");
            String vertexChildrenName = vertex.getAttributeValue("name");
            String pathChild = path + "." + vertexChildrenName;
            values[0] = valueInpkey;
            values[1] = pathChild;
            values[2] = null;
            values[3] = type;
            values[4] = ltx;
            String[] values2 = new String[5];
            values2[0] = valueOutkey;
            values2[1] = pathChild;
            values2[2] = null;
            values2[3] = type;
            values2[4] = ltx;
            vertexList.add(values);
            vertexList.add(values2);
            if (valueInpkey != null && valueOutkey == null) {
                vertexMap.put(valueInpkey, pathChild);
            }
            if (valueOutkey != null && valueInpkey == null) {
                vertexMap.put(valueOutkey, pathChild);
            }
            if (valueInpkey != null && valueOutkey != null) {
                vertexMap.put(valueInpkey, pathChild);
                vertexMap.put(valueOutkey, pathChild);
            }
            putInVertexMap(vertex, pathChild, count, type, ltx);
        }
    }

    private void analisiGraph(Element graph) {
        Element vertices = graph.getChild("vertices");
        List<Element> listaVertici = vertices.getChildren();
        for (Element elem : listaVertici) {
            String vertexS = elem.getAttributeValue("vertexkey");
            Element edges = elem.getChild("edges");
            List<Element> listaEdge = edges.getChildren("edge");
            vertexKeyList.add(elem.getAttributeValue("vertexkey"));
            for (Element edge : listaEdge) {
                String[] edgeBetweenNodes = new String[2];
                vertexKeyList.add(edge.getAttributeValue("vertexkey"));
                String vertexT = edge.getAttributeValue("vertexkey");
                edgeBetweenNodes[0] = vertexS;
                edgeBetweenNodes[1] = vertexT;
                vertexLinkLisk.add(edgeBetweenNodes);
            }
        }
    }

    private void getFunctions(Element function) {
        Element sources = function.getChild("sources");
        Element targets = function.getChild("targets");
        String functionName = function.getAttributeValue("name");
        String uid = function.getAttributeValue("uid");
        String[] values = new String[2];
        if (functionName.equals("constant")) {
            getAnnotationValue(function, uid);
        }
        if (sources != null && targets != null) {
            functionMap.put(uid, functionName);
            functionUidList.add(uid);
            values[0] = uid;
            values[1] = functionName;
            getDatapoint(targets, uid);
            getDatapoint(sources, uid);
            functionListTmp.add(values);
            //vertexList.add(values);
        }
        if (sources == null && targets != null) {
            functionMap.put(uid, functionName);
            functionUidList.add(uid);
            getDatapoint(targets, uid);
            values[0] = uid;
            values[1] = functionName;
            functionListTmp.add(values);
            //vertexList.add(values);
        }
        if (sources != null && targets == null) {
            functionMap.put(uid, functionName);
            functionUidList.add(uid);
            getDatapoint(sources, uid);
            values[0] = uid;
            values[1] = functionName;
            functionListTmp.add(values);
            //vertexList.add(values);
        }
    }

    private void getDatapoint(Element element, String uid) {
        List<Element> datapointList = element.getChildren("datapoint");
        for (Element datapoint : datapointList) {
            String key = datapoint.getAttributeValue("key");
            if (key != null) {
                vertexMap.put(key, uid);
                vertexKeyList.add(key);
                mappaFunzioni.put(key, uid);
                listaKeyFunzioni.add(key);
            }
        }
    }

    private void printVertexList() {
        for (String[] values : vertexList) {
            logger.debug(values[0] + " " + values[1] + " " + values[2] + " " + values[4]);
        }
    }

    private void getAnnotationValue(Element functionConstant, String uid) {
        Element data = functionConstant.getChild("data");
        Element constant = data.getChild("constant");
        String annotationValue = constant.getAttributeValue("value");
        annotationMap.put(uid, annotationValue);
    }

    //nuovo
    private void setPosition() {
        List<String[]> sourceList = new ArrayList<String[]>();
        List<String[]> targetList = new ArrayList<String[]>();
        divideType(sourceList, targetList);
        removeDuplicate(sourceList);
        removeDuplicate(targetList);
        sortList(sourceList);
        sortList(targetList);
        addPosition(sourceList);
        addPosition(targetList);
        if (logger.isDebugEnabled()) {
            for (String[] values : targetList) {
                logger.debug(values[1] + " " + values[2] + " " + values[4]);
            }
        }


    }

    private void divideType(List<String[]> sourceList, List<String[]> targetList) {
        for (String[] values : vertexList) {
            if (values[3].equals("S")) {
                sourceList.add(values);
            } else if (values[3].equals("T")) {
                targetList.add(values);
            }
        }
    }

    private void sortList(List<String[]> list) {
        logger.trace("sort list is empty " + list.isEmpty());
        Comparator<String[]> comparatore = new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                int pos1 = Integer.parseInt(o1[4]);
                int pos2 = Integer.parseInt(o2[4]);
                if (pos1 == 0) {
                    pos1 = pos2 + 1;
                } else {
                    pos2 = pos1 + 1;
                }
                if (pos1 > pos2) {
                    return 1;
                }
                return -1;
            }
        };
        Collections.sort(list, comparatore);
    }

    private void removeDuplicate(List<String[]> list) {
        Comparator<String[]> comparatore = new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                String name1 = o1[1];
                String name2 = o2[1];
//                String pos1 = o1[2];
//                String pos2 = o2[2];
                int posX1 = Integer.parseInt(o1[4]);
                int posX2 = Integer.parseInt(o2[4]);

                if (name1.equals(name2) && posX1 == posX2) {
                    return 1;
                }
                return -1;
            }
        };

        for (int i = 0; i < list.size(); i++) {
            String[] currObj = list.get(i);
            for (int k = i + 1; k < list.size(); k++) {
                String[] tmpObj = list.get(k);
                if (comparatore.compare(currObj, tmpObj) == 1) {
                    list.remove(k);
                    k--;
                }
            }
        }

    }

    private void addPosition(List<String[]> list) {
        logger.trace("list empty " + list.isEmpty());
        String[] values = list.get(0);
        int count = 1;

        values[2] = Integer.toString(count);
        String tmp = values[4];
        logger.trace("addPosition values: " + values[2] + ", temp " + tmp);
        for (int i = 1; i < list.size(); i++) {
            String[] valuesTmp = list.get(i);
            if (tmp.equals(valuesTmp[4])) {
                valuesTmp[2] = Integer.toString(count);
            } else {
                tmp = valuesTmp[4];
                count++;
                valuesTmp[2] = Integer.toString(count);
            }
        }
    }
    //

    private void sortPosition() {
        Comparator<String> comparatore = new Comparator<String>() {
            public int compare(String o1, String o2) {
                int pos1 = Integer.parseInt(o1);
                int pos2 = Integer.parseInt(o2);

                if (pos1 > pos2) {
                    return 1;
                }
                return -1;
            }
        };
        Collections.sort(listPosSchemaSource, comparatore);
        Collections.sort(listPosSchemaTarget, comparatore);
    }
}