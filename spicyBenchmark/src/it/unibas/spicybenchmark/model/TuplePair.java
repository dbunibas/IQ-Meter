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

public class TuplePair {

    private TupleNodeBenchmark firstTuple;
    private TupleNodeBenchmark secondTuple;
    private int firstSkolemPosition;
    private int secondSkolemPosition;

    public TuplePair(TupleNodeBenchmark firstTuple, TupleNodeBenchmark secondTuple) {
        this.firstTuple = firstTuple;
        this.secondTuple = secondTuple;
        this.firstSkolemPosition = -1;
        this.secondSkolemPosition = -1;
    }

    public TuplePair(TupleNodeBenchmark firstTuple, TupleNodeBenchmark secondTuple, int firstSkolemPosition, int secondSkolemPosition) {
        this.firstTuple = firstTuple;
        this.secondTuple = secondTuple;
        this.firstSkolemPosition = firstSkolemPosition;
        this.secondSkolemPosition = secondSkolemPosition;
    }

    public int getFirstSkolemPosition() {
        return firstSkolemPosition;
    }

    public TupleNodeBenchmark getFirstTuple() {
        return firstTuple;
    }

    public int getSecondSkolemPosition() {
        return secondSkolemPosition;
    }

    public TupleNodeBenchmark getSecondTuple() {
        return secondTuple;
    }

    @Override
    public String toString() {
        return "[P=" + firstSkolemPosition + "]" + firstTuple.toStringINode() + " - " + "[P=" + secondSkolemPosition + "]" + secondTuple.toStringINode();
    }

    public String toStringWithNoPositions() {
        return firstTuple.toStringINode() + " - " + secondTuple.toStringINode() + "\n";
    }

    public String toStringWithLocalIds() {
        return firstTuple.getLocalId() + "-" + secondTuple.getLocalId();
    }

    public String toStringWithGlobalIds() {
        return firstTuple.getGlobalId() + "-" + secondTuple.getGlobalId();
    }
}
