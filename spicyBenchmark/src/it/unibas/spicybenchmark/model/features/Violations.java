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
package it.unibas.spicybenchmark.model.features;

import it.unibas.spicybenchmark.model.TupleNodeBenchmark;
import java.util.ArrayList;
import java.util.List;

public class Violations {

    private List extraElements = new ArrayList();
    private List missingElements = new ArrayList();

    public List getExtraElements() {
        return extraElements;
    }

    public List getMissingElements() {
        return missingElements;
    }

    public void addMissingElement(Object missingElement) {
        this.missingElements.add(missingElement);
    }

    public void addExtraElement(Object extraElement) {
        this.extraElements.add(extraElement);
    }

    public String printViolations() {
        StringBuilder result = new StringBuilder();
        result.append("-------- Violations ---------\n");
        result.append("Extra elements: \n");
        for (Object object : extraElements) {
            if (object instanceof TupleNodeBenchmark) {
                result.append("\t").append(((TupleNodeBenchmark) object).getLocalId()).append("\n");
            }else{
                result.append("\t").append(object).append("\n");
            }
        }
        result.append("Missing elements: \n");
        for (Object object : missingElements) {
            if (object instanceof TupleNodeBenchmark) {
                result.append("\t").append(((TupleNodeBenchmark) object).getLocalId()).append("\n");
            }else{
                result.append("\t").append(object).append("\n");
            }
        }
        result.append("-----------------------------\n");
        return result.toString();
    }
}
