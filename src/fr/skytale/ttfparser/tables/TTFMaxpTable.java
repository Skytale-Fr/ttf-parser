package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

/**
 * MAXP Table documentation:
 * (Maximum Profile)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/maxp
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6maxp.html
 *
 * Windows' header documentation:
 * This table establishes the memory requirements for this font.
 * Fonts with CFF data must use Version 0.5 of this table,
 * specifying only the numGlyphs field.
 * Fonts with TrueType outlines must use Version 1.0 of this table,
 * where all data is required.
 */
public class TTFMaxpTable extends TTFTable {

    private int version;
    private short numGlyphs;
    private short maxPoints;
    private short maxContours;
    private short maxCompositePoints;
    private short maxCompositeContours;
    private short maxZones;
    private short maxTwilightPoints;
    private short maxStorage;
    private short maxFunctionDefs;
    private short maxInstructionDefs;
    private short maxStackElements;
    private short maxSizeOfInstructions;
    private short maxComponentElements;
    private short maxComponentDepth;

    public TTFMaxpTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("maxp", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        version = sbis.getFixed();
        numGlyphs = sbis.getShort();
        maxPoints = sbis.getShort();
        maxContours = sbis.getShort();
        maxCompositePoints = sbis.getShort();
        maxCompositeContours = sbis.getShort();
        maxZones = sbis.getShort();
        maxTwilightPoints = sbis.getShort();
        maxStorage = sbis.getShort();
        maxFunctionDefs = sbis.getShort();
        maxInstructionDefs = sbis.getShort();
        maxStackElements = sbis.getShort();
        maxSizeOfInstructions = sbis.getShort();
        maxComponentElements = sbis.getShort();
        maxComponentDepth = sbis.getShort();
    }

    public int getVersion() {
        return version;
    }

    public short getNumGlyphs() {
        return numGlyphs;
    }

    public short getMaxPoints() {
        return maxPoints;
    }

    public short getMaxContours() {
        return maxContours;
    }

    public short getMaxCompositePoints() {
        return maxCompositePoints;
    }

    public short getMaxCompositeContours() {
        return maxCompositeContours;
    }

    public short getMaxZones() {
        return maxZones;
    }

    public short getMaxTwilightPoints() {
        return maxTwilightPoints;
    }

    public short getMaxStorage() {
        return maxStorage;
    }

    public short getMaxFunctionDefs() {
        return maxFunctionDefs;
    }

    public short getMaxInstructionDefs() {
        return maxInstructionDefs;
    }

    public short getMaxStackElements() {
        return maxStackElements;
    }

    public short getMaxSizeOfInstructions() {
        return maxSizeOfInstructions;
    }

    public short getMaxComponentElements() {
        return maxComponentElements;
    }

    public short getMaxComponentDepth() {
        return maxComponentDepth;
    }

    @Override
    public String toString() {
        return "TTFMaxpTable{" +
                "version=" + version +
                ", numGlyphs=" + numGlyphs +
                ", maxPoints=" + maxPoints +
                ", maxContours=" + maxContours +
                ", maxCompositePoints=" + maxCompositePoints +
                ", maxCompositeContours=" + maxCompositeContours +
                ", maxZones=" + maxZones +
                ", maxTwilightPoints=" + maxTwilightPoints +
                ", maxStorage=" + maxStorage +
                ", maxFunctionDefs=" + maxFunctionDefs +
                ", maxInstructionDefs=" + maxInstructionDefs +
                ", maxStackElements=" + maxStackElements +
                ", maxSizeOfInstructions=" + maxSizeOfInstructions +
                ", maxComponentElements=" + maxComponentElements +
                ", maxComponentDepth=" + maxComponentDepth +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
