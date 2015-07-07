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
 
package it.unibas.spicybenchmark.operators.generators.ext.simpack;

import java.io.IOException;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import simpack.api.ITreeNode;
import simpack.util.xml.XMLIterator;
import simpack.util.xml.XMLVisitor;

public class CommonSimPack {
    
        public static ITreeNode generateSample(String xmlFilePath) {
        // load XML document into a DOM object
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(false);
        DocumentBuilder domBuilder;
        try {
            domBuilder = domFactory.newDocumentBuilder();
            Document xmlDoc = domBuilder.parse(xmlFilePath);
            Node rootNode = xmlDoc.getDocumentElement();

            // convert DOM object into a ITreeNode in order to calculate the
            // Tree Edit Distance
            XMLVisitor visitor = new XMLVisitor();
            XMLIterator xml = new XMLIterator(true, true);
            xml.setVisitor(visitor);
            xml.scanNodes(rootNode);
            ITreeNode tree = visitor.getTree();
            // printTree(tree, 0);

            // Enumeration en = tree.preorderEnumeration();
            // while (en.hasMoreElements()) {
            // System.out.println("aaaa");
            // System.out.println(en.nextElement().toString());
            // System.out.println("bbbb");
            // }

            return tree;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void printTree(ITreeNode root, int depth) {
        if (root != null) {
            indent(depth);
            Node n = (Node) root.getUserObject();

            if (n.getNodeType() == Node.TEXT_NODE) {
                Text textNode = (Text) n;
                String text = textNode.getData().trim(); // Strip off space
                if ((text != null) && text.length() > 0) // If non-empty
                {
                    System.out.println(text); // print text
                }
            } else {
                System.out.println(n.toString());
            }

            Enumeration en = root.children();

            while (en.hasMoreElements()) {
                printTree((ITreeNode) en.nextElement(), depth + 1);
            }
        }
    }

    private static void indent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
    }

}
