package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

/**
 * HHEA Table documentation:
 * (Horizontal Header Table)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/hhea
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6hhea.html
 *
 * Windows' header documentation:
 * This table contains information for horizontal layout.
 * The values in the minRightSidebearing, minLeftSideBearing and xMaxExtent
 * should be computed using only glyphs that have contours.
 * Glyphs with no contours should be ignored for the purposes of these calculations.
 * All reserved areas must be set to 0.
 */
public class TTFHheaTable extends TTFTable {

//    private short hheaMajorVersion;
//    private short hheoMinorVersion;
    private int hheaVersion;
    private short ascender;
    private short descender;
    private short lineGap;
    private int advanceWidthMax;
    private short minLeftSideBearing;
    private short minRightSideBearing;
    private short xMaxExtent;
    private short caretSlopeRise;
    private short caretSlopeRun;
    private short caretOffset;
    // 4 short reserved slots
    private short metricDataFormat;
    private int numerOfHMetrics;

    public TTFHheaTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("hhea", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
//        hheaMajorVersion = sbis.getShort();
//        hheoMinorVersion = sbis.getShort();
        hheaVersion = sbis.getFixed();
        ascender = sbis.getShort();
        descender = sbis.getShort();
        lineGap = sbis.getShort();
        advanceWidthMax = sbis.getUShort();
        minLeftSideBearing = sbis.getShort();
        minRightSideBearing = sbis.getShort();
        xMaxExtent = sbis.getShort();
        caretSlopeRise = sbis.getShort();
        caretSlopeRun = sbis.getShort();
        caretOffset = sbis.getShort();
        sbis.skipBytes(2 * 4);
        metricDataFormat = sbis.getShort();
        numerOfHMetrics = sbis.getUShort();
    }

//    public short getHheaMajorVersion() {
//        return hheaMajorVersion;
//    }
//
//    public short getHheoMinorVersion() {
//        return hheoMinorVersion;
//    }

    public int getHheaVersion() {
        return hheaVersion;
    }

    public short getAscender() {
        return ascender;
    }

    public short getDescender() {
        return descender;
    }

    public short getLineGap() {
        return lineGap;
    }

    public int getAdvanceWidthMax() {
        return advanceWidthMax;
    }

    public short getMinLeftSideBearing() {
        return minLeftSideBearing;
    }

    public short getMinRightSideBearing() {
        return minRightSideBearing;
    }

    public short getxMaxExtent() {
        return xMaxExtent;
    }

    public short getCaretSlopeRise() {
        return caretSlopeRise;
    }

    public short getCaretSlopeRun() {
        return caretSlopeRun;
    }

    public short getCaretOffset() {
        return caretOffset;
    }

    public short getMetricDataFormat() {
        return metricDataFormat;
    }

    public int getNumerOfHMetrics() {
        return numerOfHMetrics;
    }

    @Override
    public String toString() {
        return "TTFHheaTable{" +
//                "hheaMajorVersion=" + hheaMajorVersion +
//                ", hheoMinorVersion=" + hheoMinorVersion +
                ", hheaVersion=" + hheaVersion +
                ", ascender=" + ascender +
                ", descender=" + descender +
                ", lineGap=" + lineGap +
                ", advanceWidthMax=" + advanceWidthMax +
                ", minLeftSideBearing=" + minLeftSideBearing +
                ", minRightSideBearing=" + minRightSideBearing +
                ", xMaxExtent=" + xMaxExtent +
                ", caretSlopeRise=" + caretSlopeRise +
                ", caretSlopeRun=" + caretSlopeRun +
                ", caretOffset=" + caretOffset +
                ", metricDataFormat=" + metricDataFormat +
                ", numerOfHMetrics=" + numerOfHMetrics +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
