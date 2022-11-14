package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.tables.glyf.CompositeGlyf;
import fr.skytale.ttfparser.tables.glyf.Glyf;
import fr.skytale.ttfparser.tables.glyf.SimpleGlyf;
import fr.skytale.ttfparser.tables.glyf.UnknownGlyf;

import java.io.IOException;
import java.util.*;

/**
 * GLYF Table documentation:
 * (Glyph Data)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/glyf
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6glyf.html
 */
public class TTFGlyfTable extends TTFTable {

    private LinkedList<Glyf> glyfs = new LinkedList<>();

    public TTFGlyfTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("glyf", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        TTFLocaTable locaTable = tableManager.getTable(TTFTableManager.LOCA);
        List<Long> locations = locaTable.getLocations();
        int locationCount = locations.size();

        TTFHeadTable headTable = tableManager.getTable(TTFTableManager.HEAD);
        short indexToLocFormat = headTable.getIndexToLocFormat();
        boolean fetchAsShort = indexToLocFormat == 0;

        TTFMaxpTable maxpTable = tableManager.getTable(TTFTableManager.MAXP);
        int numGlyphs = maxpTable.getNumGlyphs();
        long endOfGlyphs = locations.get(numGlyphs);

        for(int i = 0; i < numGlyphs; i++) {
//            int multiplier = (fetchAsShort ? 2 : 1);
            long locaOffset = locations.get(i); //* multiplier;
            if (endOfGlyphs != 0 && endOfGlyphs == locaOffset) {
                break;
            }
            if (locations.get(i + 1) <= locaOffset) {
                glyfs.add(new UnknownGlyf((short) 0, (short) 0, (short) 0, (short) 0, (short) 0));
                continue;
            }

            sbis.setPos(offset + locaOffset);

            // = Number of shape
            short numberOfContours = sbis.getShort();
            if (locaOffset + numberOfContours > endOfGlyphs) {
                glyfs.add(new UnknownGlyf((short) 0, (short) 0, (short) 0, (short) 0, (short) 0));
                continue;
            }
            short xMin = sbis.getShort();
            short yMin = sbis.getShort();
            short xMax = sbis.getShort();
            short yMax = sbis.getShort();
            List<TTFPoint> points = new LinkedList<>();
            List<Short> contourEnds = new LinkedList<>();

            Glyf glyf = null;

            if(numberOfContours == 0) {
                System.out.println("TTFParser: TTFGlygTable: Num contour zero for " + i);
            }
            if(numberOfContours >= 0) {
                glyf = new SimpleGlyf(numberOfContours, xMin, yMin, xMax, yMax);
//            } else if(numberOfContours == 0) {
//                // What the fuck is going on here ????
//                glyf = new UnknownGlyf(numberOfContours, xMin, yMin, xMax, yMax);
            } else {
                // Composite glyphs
                // For composite glyphs, numberOfContours should be equals to -1.
                glyf = new CompositeGlyf(numberOfContours, xMin, yMin, xMax, yMax);
            }
            glyf.loadPoints(sbis);

            glyfs.add(glyf);
        }
    }

    public List<Glyf> getGlyfs() {
        return glyfs;
    }

    @Override
    public String toString() {
        return "TTFGlyfTable{" +
                "glyfs=" + glyfs +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
