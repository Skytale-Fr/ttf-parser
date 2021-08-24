package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.ByteFlag;
import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * HEAD Table documentation:
 * (Font Header Table)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/head
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6head.html
 *
 * Windows' header documentation:
 * This table gives global information about the font.
 * The bounding box values should be computed using only glyphs that have contours.
 * Glyphs with no contours should be ignored for the purposes of these calculations.
 */
public class TTFHeadTable extends TTFTable {

    private static final int EXPECTED_MAGIC_NUMBER = 1594834165; // The attribute 'magicNumber' should always be this.

    private short majorVersion;
    private short minorVersion;
    private int fontRevision; // Fixed value ???
    private int checksumAdjustment;
    private int magicNumber;
    private ByteFlag flags;
    private short unitsPerEm;
    private LocalDateTime created;
    private LocalDateTime modified;
    private short xMin;
    private short yMin;
    private short xMax;
    private short yMax;
    private short maxStyle;
    private short lowestRecPPEM;
    private short fontDirectionHint;
    private short indexToLocFormat;
    private short glyphDataFormat;


    public TTFHeadTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("head", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        majorVersion = sbis.getShort();
        minorVersion = sbis.getShort();
        fontRevision = sbis.getFixed();
        checksumAdjustment = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
        magicNumber = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
        if(magicNumber != EXPECTED_MAGIC_NUMBER) throw new TTFTableParseException("magicNumber", magicNumber, EXPECTED_MAGIC_NUMBER);
        flags = new ByteFlag(sbis.getShort());
        unitsPerEm = sbis.getShort();
        created = sbis.getDate();
        modified = sbis.getDate();
        xMin = sbis.getShort();
        yMin = sbis.getShort();
        xMax = sbis.getShort();
        yMax = sbis.getShort();
        maxStyle = sbis.getShort();
        lowestRecPPEM = sbis.getShort();
        fontDirectionHint = sbis.getShort();
        indexToLocFormat = sbis.getShort();
        glyphDataFormat = sbis.getShort();
    }

    public short getMajorVersion() {
        return majorVersion;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public int getFontRevision() {
        return fontRevision;
    }

    public int getChecksumAdjustment() {
        return checksumAdjustment;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public ByteFlag getFlags() {
        return flags;
    }

    public short getUnitsPerEm() {
        return unitsPerEm;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public short getxMin() {
        return xMin;
    }

    public short getyMin() {
        return yMin;
    }

    public short getxMax() {
        return xMax;
    }

    public short getyMax() {
        return yMax;
    }

    public short getMaxStyle() {
        return maxStyle;
    }

    public short getLowestRecPPEM() {
        return lowestRecPPEM;
    }

    public short getFontDirectionHint() {
        return fontDirectionHint;
    }

    public short getIndexToLocFormat() {
        return indexToLocFormat;
    }

    public short getGlyphDataFormat() {
        return glyphDataFormat;
    }

    @Override
    public String toString() {
        return "TTFHeadTable{" +
                "majorVersion=" + majorVersion +
                ", minorVersion=" + minorVersion +
                ", fontRevision=" + fontRevision +
                ", checksumAdjustment=" + checksumAdjustment +
                ", magicNumber=" + magicNumber +
                ", flags=" + flags +
                ", unitsPerEm=" + unitsPerEm +
                ", created=" + created +
                ", modified=" + modified +
                ", xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                ", maxStyle=" + maxStyle +
                ", lowestRecPPEM=" + lowestRecPPEM +
                ", fontDirectionHint=" + fontDirectionHint +
                ", indexToLocFormat=" + indexToLocFormat +
                ", glyphDataFormat=" + glyphDataFormat +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
