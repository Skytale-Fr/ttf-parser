package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * HMTX Table documentation:
 * (Horizontal Metrics Table)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/hmtx
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6hmtx.html
 *
 * Windows' header documentation:
 * Glyph metrics used for horizontal text layout include glyph advance widths,
 * side bearings and X-direction min and max values (xMin, xMax).
 * These are derived using a combination of the glyph
 * outline data ('glyf', 'CFF ' or CFF2) and the horizontal metrics table.
 * The horizontal metrics ('hmtx') table provides glyph advance
 * widths and left side bearings.
 */
public class TTFHmtxTable extends TTFTable {

    public class HMetric implements Cloneable {
        private double advanceWidth;
        private double leftSideBearing;
        protected HMetric(int advanceWidth, short leftSideBearing) {
            this.advanceWidth = advanceWidth;
            this.leftSideBearing = leftSideBearing;
        }
        public double getAdvanceWidth() {
            return advanceWidth;
        }
        public double getLeftSideBearing() {
            return leftSideBearing;
        }
        public HMetric scale(double scaleX, double scaleY) {
            HMetric clone = clone();
            clone.advanceWidth = advanceWidth * scaleX;
            clone.leftSideBearing = leftSideBearing * scaleX;
            return clone;
        }
        @Override
        public String toString() {
            return "HMetric{" +
                    "advanceWidth=" + advanceWidth +
                    ", leftSideBearing=" + leftSideBearing +
                    '}';
        }

        @Override
        public HMetric clone() {
            try {
                HMetric clone = (HMetric) super.clone();
                clone.advanceWidth = advanceWidth;
                clone.leftSideBearing = leftSideBearing;
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    private List<HMetric> hMetrics = new ArrayList<>();
    private List<Short> leftSideBearings = new ArrayList<>();

    public TTFHmtxTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("hmtx", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        TTFHheaTable hheaTable = tableManager.getTable(TTFTableManager.HHEA);
        int numberOfHMetrics = hheaTable.getNumerOfHMetrics();

        TTFMaxpTable maxpTable = tableManager.getTable(TTFTableManager.MAXP);
        short numGlyphs = maxpTable.getNumGlyphs();

        for(int i = 0; i < numberOfHMetrics; i++) {
            int advanceWidth = sbis.getUShort();
            short leftSideBearing = sbis.getShort();
            HMetric hMetric = new HMetric(advanceWidth, leftSideBearing);
            hMetrics.add(hMetric);
        }

        for(int i = 0; i < numGlyphs - numberOfHMetrics; i++) {
            short leftSideBearing = sbis.getShort();
            leftSideBearings.add(leftSideBearing);
        }
    }

    public List<HMetric> getHMetrics() {
        return hMetrics;
    }

    public List<Short> getLeftSideBearings() {
        return leftSideBearings;
    }

    @Override
    public String toString() {
        return "TTFHmtxTable{" +
                "hMetrics=" + hMetrics +
                ", leftSideBearings=" + leftSideBearings +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
