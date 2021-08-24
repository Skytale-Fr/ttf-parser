package fr.skytale.ttfparser;

import fr.skytale.ttfparser.tables.TTFGlyfTable;
import fr.skytale.ttfparser.tables.TTFHmtxTable;
import fr.skytale.ttfparser.tables.TTFTableManager;
import fr.skytale.ttfparser.tables.cmap.GlyfIndexMap;
import fr.skytale.ttfparser.tables.TTFCmapTable;
import fr.skytale.ttfparser.tables.glyf.Glyf;

import java.util.*;

public class TTFAlphabet {

    private static final String SUPPORTED_ALPHABET =
            "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    private TTFTableManager tableManager;
    private Map<Character, TTFCharacter> characterMap = new HashMap<>();

    public TTFAlphabet(SuperBufferedInputStream sbis, TTFTableManager tableManager) {
        this.tableManager = tableManager;

        TTFCmapTable cmapTable = tableManager.getTable(TTFTableManager.CMAP);
        GlyfIndexMap glyfIndexMap = cmapTable.getGlyfIndexMap();

        TTFGlyfTable glyfTable = tableManager.getTable(TTFTableManager.GLYF);
        List<Glyf> glyfs = glyfTable.getGlyfs();
        TTFHmtxTable hmtxTable = tableManager.getTable(TTFTableManager.HMTX);
        List<TTFHmtxTable.HMetric> hMetrics = hmtxTable.getHMetrics();

        String[] characters = SUPPORTED_ALPHABET.split("");
        for(String character : characters) {
            char c = character.charAt(0);
            int charCode = (int) c;

            Integer index = glyfIndexMap.get(charCode);
            if(index == null) {
                System.out.println("index null for " + c);
                return;
            }

            try {
                Glyf glyf = glyfs.get(index);
                TTFHmtxTable.HMetric hMetric = hMetrics.get(index);
                processCharacter(sbis, c, glyf, hMetric);
            } catch(IndexOutOfBoundsException e) {
                System.out.println("Metrics not found for " + c + "(" + charCode + ") at index " + index + ".");
            }
        }
    }

    public boolean supportCharacter(char c) {
        TTFCharacter ttfCharacter = characterMap.get(c);
        return ttfCharacter != null;
    }

    public boolean supportString(String s) {
        String[] characters = s.split("");
        for(String character : characters) {
            char c = character.charAt(0);
            if(!supportCharacter(c)) return false;
        }
        return true;
    }

    public TTFCharacter getCharacter(char c) throws TTFCharacterNotSupportedException {
        if(!supportCharacter(c)) throw new TTFCharacterNotSupportedException(c);
        return characterMap.get(c);
    }

    public TTFString getString(String s) throws TTFCharacterNotSupportedException {
        if(!supportString(s))
            throw new TTFCharacterNotSupportedException(String.format("The provided string '%s' is not supported by the loaded font.", s));
        TTFCharacter[] ttfCharacters = new TTFCharacter[s.length()];
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            TTFCharacter ttfCharacter = getCharacter(c);
            ttfCharacters[i] = ttfCharacter;
        }
        return new TTFString(ttfCharacters);
    }

    private void processCharacter(SuperBufferedInputStream sbis, char c, Glyf glyf, TTFHmtxTable.HMetric hMetric) {
//        if(glyf.getNumberOfContours() == 0) {
//            System.out.println("Number of contours equals to zero for " + c);
//            System.out.println(glyf);
//            return;
//        }

        short lsb = hMetric.getLeftSideBearing();
        short rsb = (short) (hMetric.getAdvanceWidth() - hMetric.getLeftSideBearing() - (glyf.getXMax() - glyf.getXMin()));
        TTFCharacter ttfCharacter = new TTFCharacter(c, lsb, rsb, glyf, hMetric);
        characterMap.put(c, ttfCharacter);
    }

}
