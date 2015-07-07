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

public class References {

//    private static final String testPrefix = "d:/sviluppo/unibas/spicy++/spicyBenchmark/misc/resources/datasources/";
//    private static final String testPrefix = "/media/Dati/Sviluppo/unibas/spicyPlusPlus/spicyBenchmark/misc/resources/datasources/";
//    private static final String testPrefix = "/Users/donatello/Projects/spicy/spicyBenchmark/misc/resources/datasources/";
//    private static final String testPrefix = "/Users/enzoveltri/svn/spicy/trunk/spicyBenchmark/misc/resources/datasources/";
    private static final String testPrefix = "/resources/datasources/";

    // e1
    private static final String e1_prefix = "e1_manyToMany/";
    public static final String e1_sol1_conf_file = testPrefix + e1_prefix + "e1_sol1_conf.properties";
    public static final String e1_sol2_conf_file = testPrefix + e1_prefix + "e1_sol2_conf.properties";
    public static final String e1_sol3_conf_file = testPrefix + e1_prefix + "e1_sol3_conf.properties";
    // e1 address
    private static final String e1_address_prefix = "e1_address/";
    public static final String e1_address_conf_file = testPrefix + e1_address_prefix + "e1_sol1_conf.properties";
    // e2
    private static final String e2_prefix = "e2_nestingAndJoin/";
    public static final String e2_sol1_1_conf = testPrefix + e2_prefix + "e2_sol1_1_conf.properties";
    public static final String e2_sol1_2_conf = testPrefix + e2_prefix + "e2_sol1_2_conf.properties";
    public static final String e2_sol1_3_conf = testPrefix + e2_prefix + "e2_sol1_3_conf.properties";
    public static final String e2_sol1_4_conf = testPrefix + e2_prefix + "e2_sol1_4_conf.properties";
    public static final String e2_sol2_2_conf = testPrefix + e2_prefix + "e2_sol2_2_conf.properties";
    public static final String e2_sol2_3_conf = testPrefix + e2_prefix + "e2_sol2_3_conf.properties";
    // e3
    private static final String e3_prefix = "e3_nestingAndJoinAndSk/";
    public static final String e3_sol1_conf = testPrefix + e3_prefix + "e3_sol1_conf.properties";
    public static final String e3_sol1_1_conf = testPrefix + e3_prefix + "e3_sol1_1_conf.properties";
    public static final String e3_sol1_2_conf = testPrefix + e3_prefix + "e3_sol1_2_conf.properties";
    public static final String e3_sol2_1_conf = testPrefix + e3_prefix + "e3_sol2_1_conf.properties";
    // e4
    private static final String e4_prefix = "e4_grouping/";
    public static final String e4_sol1_1_conf = testPrefix + e4_prefix + "e4_sol1_1_conf.properties";
    // statDB
    private static final String statDB_prefix = "statDB/";
    public static final String statDB_conf_file = testPrefix + statDB_prefix + "statDB.properties";
    public static final String statDB_1_conf_file = testPrefix + statDB_prefix + "statDB_1.properties";
    // statDB
    public static final String statDBSchema = testPrefix + "statDB/statDB.xsd";
    public static final String statDB_ExclusionList = testPrefix + "statDB/exclusionList.txt";
    public static final String expectedInstanceStatDB = testPrefix + "statDB/statDB-expectedInstance.xml";
    public static final String expectedInstanceStatDBDifferentKeys = testPrefix + "statDB/statDB-expectedInstanceDifferentKeys.xml";
    public static final String expectedInstanceStatDB_BIG = testPrefix + "statDB/statDB_BIG.xml";
    public static final String expectedInstanceStatDB2 = testPrefix + "statDB/statDB_100.xml";
    public static final String translatedInstanceStatDB2 = testPrefix + "statDB/statDB_100_1.xml";
    public static final String expectedInstanceStatDBDuplicates = testPrefix + "statDB/statDB-expectedInstanceDuplicates.xml";
    public static final String translatedInstanceStatDB = testPrefix + "statDB/statDB-translatedInstance.xml";
    public static final String translatedInstanceStatDBDuplicates = testPrefix + "statDB/statDB-translatedInstanceDuplicates.xml";
    public static final String translatedInstanceStatDBTuplesDiffPosition = testPrefix + "statDB/statDB-translatedInstanceTuplesDiffPosition.xml";
    public static final String univDBSchema = testPrefix + "univDB/univDB.xsd";
    public static final String expectedInstanceUnivDB = testPrefix + "univDB/univDB-expectedInstance.xml";
    public static final String translatedInstanceUnivDB = testPrefix + "univDB/univDB-translatedInstance.xml";
    public static final String mondialDBSchema = testPrefix + "mondial/city_Set.xsd";
    public static final String expectedInstanceMondial = testPrefix + "mondial/city_Set-expectedInstance.xml";
    public static final String translatedInstanceMondia = testPrefix + "mondial/city_Set-translatedInstance.xml";
    // hashing
    public static final String hashing_prefix = "hashing/";
    public static final String hashing__conf_file = testPrefix + hashing_prefix + "hashing.properties";
    public static final String hashing_univ_conf_file = testPrefix + hashing_prefix + "hashingUniv.properties";
    public static final String hashing__conf_file_base = testPrefix + hashing_prefix + "hashing";
    // hashing remove
    public static final String hashingGreedy_prefix = "hashingGreedy/";
    public static final String hashingGreedy__conf_file = testPrefix + hashingGreedy_prefix + "hashing.properties";
    public static final String hashingGreedy_univ_conf_file = testPrefix + hashingGreedy_prefix + "hashingUniv.properties";
    public static final String hashingGreedy__conf_file_base = testPrefix + hashingGreedy_prefix + "hashing";
    // blocking
    public static final String blocking_prefix = "blocking/";
    public static final String blocking__conf_file = testPrefix + blocking_prefix + "hashing.properties";
    public static final String blocking_univ_conf_file = testPrefix + blocking_prefix + "hashingUniv.properties";
    public static final String blocking__conf_file_base = testPrefix + blocking_prefix + "hashing";

}
