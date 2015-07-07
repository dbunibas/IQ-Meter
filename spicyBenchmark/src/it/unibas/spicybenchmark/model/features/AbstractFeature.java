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

import it.unibas.spicybenchmark.operators.StandardEvaluateFeature;
import it.unibas.spicybenchmark.operators.IEvaluateFeature;
import java.util.List;

public abstract class AbstractFeature implements IFeature {

    protected List expectedObjects;
    protected List translatedObjects;

    public List getExpectedObjects() {
        return this.expectedObjects;
    }

    public List getTranslatedObjects() {
        return this.translatedObjects;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public boolean isEvaluable() {
        if (expectedObjects.isEmpty() && translatedObjects.isEmpty()) {
            return false;
        }
        return true;
    }

    public IEvaluateFeature getFeatureEvaluator() {
        return new StandardEvaluateFeature();
    }
}
