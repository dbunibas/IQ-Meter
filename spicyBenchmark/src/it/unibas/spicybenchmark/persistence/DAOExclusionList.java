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

import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.persistence.DAOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DAOExclusionList {

    private static Log logger = LogFactory.getLog(DAOExclusionList.class);

    public List<String> loadExclusionList(IDataSourceProxy schema, String exclusionFile) throws DAOException {
        List<String> result = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            FileReader fileReader = new FileReader(exclusionFile);
            reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (logger.isDebugEnabled()) logger.debug(" > Current Line: " + line);

                // the following code is useful to check the correctness of paths
                new FindNode().findNodeInSchema(line, schema);

                result.add(line);
            }
        } catch (FileNotFoundException fnfe) {
            throw new DAOException(" File not found: " + fnfe);
        } catch (IOException ioe) {
            throw new DAOException(" Error: " + ioe);
        } catch (NoSuchElementException nse) {
            throw new DAOException(" Error in file format: " + nse);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe) {
            }
        }
        return result;
    }
}
