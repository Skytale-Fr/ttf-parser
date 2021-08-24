package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * LOCA Table documentation:
 * (Index to Location)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/loca
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6loca.html
 *
 * Windows' header documentation:
 * The indexToLoc table stores the offsets to the locations of the glyphs in the font,
 * relative to the beginning of the glyphData table.
 * In order to compute the length of the last glyph element,
 * there is an extra entry after the last valid index.
 */
public class TTFLocaTable extends TTFTable {

    private List<Long> locations = new ArrayList<>();

    public TTFLocaTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("loca", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        TTFHeadTable headTable = tableManager.getTable(TTFTableManager.HEAD);
        short indexToLocFormat = headTable.getIndexToLocFormat();
        boolean fetchAsShort = indexToLocFormat == 0;

        TTFMaxpTable maxpTable = tableManager.getTable(TTFTableManager.MAXP);
        short numGlyphs = maxpTable.getNumGlyphs();

        for(int i = 0; i < numGlyphs + 1; i++) {
            long location = 0;
            if(fetchAsShort) {
                // The actual local offset divided by 2 is stored.
                location = sbis.getUShort() * 2L;
            } else {
                // The actual local offset is stored.
                location = sbis.getUInt(SuperBufferedInputStream.INTFormat.INT32);
            }
            locations.add(location);
        }
    }

    public List<Long> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "TTFLocaTable{" +
                "locations=" + locations +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
