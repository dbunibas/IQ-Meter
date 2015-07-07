package it.unibas.spicybenchmark.persistence;

import it.unibas.spicy.persistence.DAOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class DAOXmlDirty {

    private static final String START_NULL = "_L";
    private int percentage = 5;

    public void dirtyWithLLuns(String xmlInput, String xmlOutput, List<String> attributes) throws DAOException {
        int counter = 0;
        try {
            File input = new File(xmlInput);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(input);

            for (String attribute : attributes) {
                NodeList attributeElements = doc.getElementsByTagName(attribute);
                int dirtyElementNumber = attributeElements.getLength() * percentage / 100;
                System.out.println("Dirty elements: " + dirtyElementNumber);
                List<Integer> positions = generatePositions(attributeElements.getLength() - 1, dirtyElementNumber);
                System.out.println("Positions generated");
                for (Integer position : positions) {
                    attributeElements.item(position).setTextContent(START_NULL + counter);
                    counter++;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlOutput));
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new DAOException("Error generating dirty xml for: " + xmlInput);
        }

    }

    public void dirtyWithLLunsSAX(String xmlInput, String xmlOutput, List<String> attributes, int size) throws DAOException {
        try {
            XMLReader readerSax = new MyXmlReader(attributes, size);

            Source src = new SAXSource(readerSax, new InputSource(xmlInput));
            Result res = new StreamResult(new File(xmlOutput));
            TransformerFactory.newInstance().newTransformer().transform(src, res);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Error generating dirty xml for: " + xmlInput);
        }
    }

    private List<Integer> generatePositions(int end, int dirtyElementNumber) {
        Random random = new Random();
        List<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < dirtyElementNumber; i++) {
            int nextInt = random.nextInt(end);
            if (positions.contains(nextInt)) {
                nextInt = random.nextInt(end);
                while (!positions.contains(nextInt)) {
                    nextInt = random.nextInt(end);
                }
                positions.add(nextInt);
            } else {
                positions.add(nextInt);
            }
        }
        return positions;
    }

    private class MyXmlReader extends XMLFilterImpl {

        private List<String> attributes;
        private int size;

        private String tagName = "";
        private int counter = 0;

        private Map<String, List<Integer>> mapAttributePositionToDirty = new HashMap<String, List<Integer>>();
        private Map<String, Integer> mapAttributePosition = new HashMap<String, Integer>();

        public MyXmlReader(List<String> attributes, int size) throws Exception{
            super(XMLReaderFactory.createXMLReader());
            this.attributes = attributes;
            this.size = size;
            initMaps();

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            for (String attribute : attributes) {
                if (attribute.equalsIgnoreCase(tagName)) {
                    int position = mapAttributePosition.get(attribute);
                    List<Integer> positionsToDirty = mapAttributePositionToDirty.get(attribute);
                    if (positionsToDirty.contains(position)) {
                        String nulls = START_NULL + counter;
                        counter++;
                        ch = nulls.toCharArray();
                        start = 0;
                        length = ch.length;
                        break;
                    }
                }
            }
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            tagName = "";
            super.endElement(uri, localName, qName);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            tagName = qName;
            for (String attribute : attributes) {
                if (tagName.equalsIgnoreCase(attribute)) {
                    mapAttributePosition.put(attribute, mapAttributePosition.get(attribute) + 1);
                }
            }
            super.startElement(uri, localName, qName, atts);
        }

        private void initMaps() {
            for (String attribute : attributes) {
                mapAttributePosition.put(attribute, -1);
                mapAttributePositionToDirty.put(attribute, generatePositions(size, size*percentage/100));
            }
        }

    }

}
