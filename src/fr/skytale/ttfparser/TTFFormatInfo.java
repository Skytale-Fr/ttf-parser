package fr.skytale.ttfparser;

public class TTFFormatInfo {

    private int scalarType;
    private short numTables;
    private short searchRange;
    private short entrySelector;
    private short rangeShift;

    public TTFFormatInfo(int scalarType, short numTables, short searchRange, short entrySelector, short rangeShift) {
        this.scalarType = scalarType;
        this.numTables = numTables;
        this.searchRange = searchRange;
        this.entrySelector = entrySelector;
        this.rangeShift = rangeShift;
    }

    public int getScalarType() {
        return scalarType;
    }

    public short getNumTables() {
        return numTables;
    }

    public short getSearchRange() {
        return searchRange;
    }

    public short getEntrySelector() {
        return entrySelector;
    }

    public short getRangeShift() {
        return rangeShift;
    }

    @Override
    public String toString() {
        return "TTFFormatInfo{" +
                "scalarType=" + scalarType +
                ", numTables=" + numTables +
                ", searchRange=" + searchRange +
                ", entrySelector=" + entrySelector +
                ", rangeShift=" + rangeShift +
                '}';
    }
}
