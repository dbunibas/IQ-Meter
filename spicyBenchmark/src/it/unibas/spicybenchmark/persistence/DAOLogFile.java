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

import it.unibas.spicy.persistence.DAOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DAOLogFile {

    private static Log logger = LogFactory.getLog(DAOLogFile.class);

    public static void saveLog(String log, String logFile) throws DAOException {
        BufferedWriter writer = null;
        try {
            File f = new File(logFile);
            f.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(logFile);
            writer = new BufferedWriter(fileWriter);
            writer.write(log);
        } catch (FileNotFoundException fnfe) {
            throw new DAOException(" File not found: " + fnfe);
        } catch (IOException ioe) {
            throw new DAOException(" Error: " + ioe);
        } catch (NoSuchElementException nse) {
            throw new DAOException(" Error in file format: " + nse);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ioe) {
            }
        }
    }
}
