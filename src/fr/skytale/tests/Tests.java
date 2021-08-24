package fr.skytale.tests;

import fr.skytale.Main;
import fr.skytale.ttfparser.TTFAlphabet;
import fr.skytale.ttfparser.TTFParser;
import fr.skytale.ttfparser.tables.*;
import fr.skytale.ttfparser.tables.cmap.GlyfIndexMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private static TTFAlphabet ttfAlphabet;
    private static TTFTableManager tableManager;

    @BeforeAll
    public static void initAll() throws URISyntaxException {
        URL fontURL = Main.class.getClassLoader().getResource("resources/Minecraft.ttf");
        TTFParser ttfParser = new TTFParser(fontURL);
        ttfAlphabet = ttfParser.parse();

        tableManager = ttfParser.getTableManager();
    }

    @Test
    public void testTableMaxp() {
        TTFMaxpTable maxpTable = tableManager.getTable(TTFTableManager.MAXP);
        assertEquals(1, maxpTable.getVersion(), "version attribute is incorrect.");
        assertEquals(110, maxpTable.getNumGlyphs(), "numGlyphs attribute is incorrect.");
        assertEquals(146, maxpTable.getMaxPoints(), "maxPoints attribute is incorrect.");
        assertEquals(23, maxpTable.getMaxContours(), "maxContours attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxCompositePoints(), "maxCompositePoints attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxCompositeContours(), "maxCompositeContours attribute is incorrect.");
        assertEquals(2, maxpTable.getMaxZones(), "maxZones attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxTwilightPoints(), "maxTwilightPoints attribute is incorrect.");
        assertEquals(1, maxpTable.getMaxStorage(), "maxStorage attribute is incorrect.");
        assertEquals(1, maxpTable.getMaxFunctionDefs(), "maxFunctionDefs attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxInstructionDefs(), "maxInstructionDefs attribute is incorrect.");
        assertEquals(64, maxpTable.getMaxStackElements(), "maxStackElements attribute is incorrect.");
        assertEquals(46, maxpTable.getMaxSizeOfInstructions(), "maxSizeOfInstructions attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxComponentElements(), "maxComponentElements attribute is incorrect.");
        assertEquals(0, maxpTable.getMaxComponentDepth(), "maxComponentDepth attribute is incorrect.");
    }

    @Test
    public void testHheaTable() {
        TTFHheaTable hheaTable = tableManager.getTable(TTFTableManager.HHEA);
        assertEquals(1, hheaTable.getHheaVersion(), "hheaVersion attribute is incorrect.");
        assertEquals(768, hheaTable.getAscender(), "ascender attribute is incorrect.");
        assertEquals(-256, hheaTable.getDescender(), "descender attribute is incorrect.");
        assertEquals(92, hheaTable.getLineGap(), "lineGap attribute is incorrect.");
        assertEquals(1024, hheaTable.getAdvanceWidthMax(), "advanceWidthMax attribute is incorrect.");
        assertEquals(0, hheaTable.getMinLeftSideBearing(), "minLeftSideBearing attribute is incorrect.");
        assertEquals(0, hheaTable.getMinRightSideBearing(), "minRightSideBearing attribute is incorrect.");
        assertEquals(896, hheaTable.getxMaxExtent(), "xMaxExtent attribute is incorrect.");
        assertEquals(1, hheaTable.getCaretSlopeRise(), "caretSlopeRise attribute is incorrect.");
        assertEquals(0, hheaTable.getCaretSlopeRun(), "caretSlopeRun attribute is incorrect.");
        assertEquals(0, hheaTable.getCaretOffset(), "caretOffset attribute is incorrect.");
        assertEquals(0, hheaTable.getMetricDataFormat(), "metricDataFormat attribute is incorrect.");
        assertEquals(110, hheaTable.getNumerOfHMetrics(), "numberOfHMetrics attribute is incorrect.");
    }

    @Test
    public void testHmtxTable() {
        TTFHmtxTable hmtxTable = tableManager.getTable(TTFTableManager.HMTX);
        // TODO: Find a TTF viewer that shows this table
    }

    @Test
    public void testCmapTable() throws URISyntaxException, IOException {
        TTFCmapTable cmapTable = tableManager.getTable(TTFTableManager.CMAP);
        assertEquals(0, cmapTable.getVersion(), "version attribute is incorrect.");
        assertEquals(3, cmapTable.getNumTables(), "numTables attribute is incorrect.");
        assertEquals(4, cmapTable.getFormat(), "format attribute is incorrect.");
        assertEquals(56, cmapTable.getCmapFormatLength(), "cmapFormatLength attribute is incorrect.");
        assertEquals(0, cmapTable.getLanguage(), "language attribute is incorrect.");

        GlyfIndexMap glyfIndexMap = cmapTable.getGlyfIndexMap();
        Set<Map.Entry<Integer, Integer>> entries = glyfIndexMap.entrySet();

        URL dataFile = Tests.class.getClassLoader().getResource("resources/glyphIndexMap.txt");
        FileReader fr = new FileReader(new File(dataFile.toURI()));
        BufferedReader br = new BufferedReader(fr);
        for(Map.Entry<Integer, Integer> entry : entries) {
            String line = br.readLine();
            if(line == null) {
                Assertions.fail("Too many elements in GlyfIndexMap");
            }
            String[] splitted = line.split("=");
            int key = Integer.parseInt(splitted[0]);
            int value = Integer.parseInt(splitted[1]);

            assertEquals(key, entry.getKey(), "GlyfIndexMap entry key isn't correct.\n" + glyfIndexMap);
            assertEquals(value, entry.getValue(), "GlyfIndexMap entry value isn't correct.\n" + glyfIndexMap);
        }
        String line = br.readLine();
        if(line != null) {
            Assertions.fail("Not enough elements in GlyfIndexMap");
        }

        br.close();
        fr.close();

    }

}
