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
package it.unibas.spicybenchmark.model;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.AttributeNode;
import java.util.ArrayList;
import java.util.List;

public class TupleNodeBenchmark {

    private String localId;
    private String globalId;
    private INode iNode;
    private boolean matched = false;

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public INode getINode() {
        return iNode;
    }

    public void setINode(INode iNode) {
        this.iNode = iNode;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n");
        result.append("Local ID: ").append(localId).append("\n");
        result.append("Global ID: ").append(globalId).append("\n");
        result.append("iNode Label: ").append(iNode.getLabel()).append("\n");
        return result.toString();
    }

    public String toStringOnlyLocalId() {
        StringBuilder result = new StringBuilder();
        result.append("Local ID: ").append(localId).append("\n");
        return result.toString();
    }

    public String toStringOnlyGlobalId() {
        StringBuffer result = new StringBuffer();
        result.append("Global ID: ").append(globalId).append("\n");
        return result.toString();
    }

    public String toStringINode() {
        StringBuilder result = new StringBuilder();
        result.append(iNode.getLabel()).append(" [");
        for (int i = 0; i < iNode.getChildren().size(); i++) {
            INode child = iNode.getChildren().get(i);
            if (child instanceof AttributeNode) {
                result.append(child.getLabel()).append(": ").append(child.getChild(0).getValue());
            }
            if (i != iNode.getChildren().size() - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    public List<String> getAttributes() {
        List<String> attributes = new ArrayList<String>();
        for (int i = 0; i < iNode.getChildren().size(); i++) {
            INode child = iNode.getChildren().get(i);
            if (child instanceof AttributeNode) {
                attributes.add(child.getLabel());
            }
        }
        return attributes;
    }

    public int getNumberOfTotalAttributes(List<String> attributesToExclude) {
        int size = 0;
        for (String attributeName : this.getAttributes()) {
            if (attributesToExclude.contains(attributeName)) {
                continue;
            }
            size++;
        }
        return size;
    }
}
