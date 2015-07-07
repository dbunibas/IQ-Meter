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
 
package it.unibas.spicybenchmark.test;

import it.unibas.spicy.persistence.xml.DAOXsd;
import it.unibas.spicybenchmark.model.features.FeatureResult;
import it.unibas.spicybenchmark.model.features.SimilarityResult;
import it.unibas.spicybenchmark.persistence.DAOConfiguration;

public class TEvalutate extends TestCompare {

    public TEvalutate(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        daoXsd = new DAOXsd();
    }

    public void testRun() throws Exception {
//        configuration = DAOConfiguration.loadConfigurationFile(References.e1_address_conf_file);
        configuration = DAOConfiguration.loadConfigurationFile(References.statDB_conf_file);
//        configuration = DAOConfiguration.loadConfigurationFile(References.statDB_1_conf_file);
        evaluateAndPrint(configuration);
    }


}
