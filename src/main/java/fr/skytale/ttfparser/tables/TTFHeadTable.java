package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.tables.head.FontDecoration;

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

    private int majorVersion;
    private int minorVersion;
    private int fontRevision;
    private long checksumAdjustment;
    private long magicNumber;
    private int flags;
    private int unitsPerEm;
    private LocalDateTime created;
    private LocalDateTime modified;
    private short xMin;
    private short yMin;
    private short xMax;
    private short yMax;
    private int macStyle;
    private int lowestRecPPEM;
    private short fontDirectionHint;
    private short indexToLocFormat;
    private short glyphDataFormat;

    private FontDecoration fontDecoration;

    public TTFHeadTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("head", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        majorVersion = sbis.getUShort();
        minorVersion = sbis.getUShort();
        fontRevision = sbis.getFixed();
        checksumAdjustment = sbis.getUInt(SuperBufferedInputStream.INTFormat.INT32);
        magicNumber = sbis.getUInt(SuperBufferedInputStream.INTFormat.INT32);
        if(magicNumber != EXPECTED_MAGIC_NUMBER) throw new TTFTableParseException("magicNumber", magicNumber, EXPECTED_MAGIC_NUMBER);
        flags = sbis.getUShort();
        unitsPerEm = sbis.getUShort();
        created = sbis.getDate();
        modified = sbis.getDate();
        xMin = sbis.getShort();
        yMin = sbis.getShort();
        xMax = sbis.getShort();
        yMax = sbis.getShort();
        macStyle = sbis.getUShort();
        fontDecoration = new FontDecoration(macStyle);
        lowestRecPPEM = sbis.getUShort();
        fontDirectionHint = sbis.getShort();
        indexToLocFormat = sbis.getShort();
        glyphDataFormat = sbis.getShort();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getFontRevision() {
        return fontRevision;
    }

    public long getChecksumAdjustment() {
        return checksumAdjustment;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public int getFlags() {
        return flags;
    }

    public int getUnitsPerEm() {
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

    public int getMacStyle() {
        return macStyle;
    }

    public int getLowestRecPPEM() {
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

    public FontDecoration getFontDecoration() {
        return fontDecoration;
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
                ", macStyle=" + macStyle +
                ", lowestRecPPEM=" + lowestRecPPEM +
                ", fontDirectionHint=" + fontDirectionHint +
                ", indexToLocFormat=" + indexToLocFormat +
                ", glyphDataFormat=" + glyphDataFormat +
                ", fontDecoration=" + fontDecoration +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
