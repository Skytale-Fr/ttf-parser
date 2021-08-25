package fr.skytale.ttfparser;

import fr.skytale.ttfparser.tables.*;
import fr.skytale.ttfparser.tables.cmap.GlyfIndexMap;
import fr.skytale.ttfparser.tables.glyf.Glyf;
import fr.skytale.ttfparser.tables.glyf.UnknownGlyf;

import java.util.*;

public class TTFAlphabet {

    private TTFTableManager tableManager;
    private TTFCharacter unknownCharacter;
    private TTFCharacter spaceCharacter;
    private Map<Integer, TTFCharacter> characterMap = new HashMap<>();

    public TTFAlphabet(SuperBufferedInputStream sbis, TTFTableManager tableManager) {
        this.tableManager = tableManager;

        TTFCmapTable cmapTable = tableManager.getTable(TTFTableManager.CMAP);
        GlyfIndexMap glyfIndexMap = cmapTable.getGlyfIndexMap();

        TTFGlyfTable glyfTable = tableManager.getTable(TTFTableManager.GLYF);
        List<Glyf> glyfs = glyfTable.getGlyfs();
        TTFHmtxTable hmtxTable = tableManager.getTable(TTFTableManager.HMTX);
        List<TTFHmtxTable.HMetric> hMetrics = hmtxTable.getHMetrics();

        Set<Integer> charCodes = glyfIndexMap.keySet();

        unknownCharacter = createCharacter(0, glyfs.get(0), hMetrics.get(0));
        Integer spaceIndex = glyfIndexMap.get((int) ' ');
        spaceCharacter = createCharacter((int) ' ', new UnknownGlyf((short) 0, (short) 0, (short) 0, (short) 0, (short) 0), hMetrics.get(spaceIndex));

        for(int charCode : charCodes) {
            char c = (char) charCode;

            Integer index = glyfIndexMap.get(charCode);
            if(index == null) {
                index = 0;
                return;
            }

            try {
                Glyf glyf = glyfs.get(index);
                TTFHmtxTable.HMetric hMetric = hMetrics.get(index);
                processCharacter(sbis, charCode, glyf, hMetric);
            } catch(IndexOutOfBoundsException e) {
                System.out.println("Metrics not found for " + c + "(" + charCode + ") at index " + index + ".");
            }
        }
    }

    public boolean supportCharacter(char c) {
        TTFCharacter ttfCharacter = characterMap.get((int) c);
        return ttfCharacter != null;
    }

    public boolean supportString(String s) {
        if(s.isEmpty()) return true;

        String[] characters = s.split("");
        for(String character : characters) {
            char c = character.charAt(0);
            if(!supportCharacter(c)) return false;
        }
        return true;
    }

    public TTFCharacter getCharacter(char c) throws TTFCharacterNotSupportedException {
        return getCharacter(c, 1);
    }

    public TTFCharacter getCharacter(char c, double fontSize) throws TTFCharacterNotSupportedException {
        TTFHeadTable headTable = tableManager.getTable(TTFTableManager.HEAD);
        int unitsPerEm = headTable.getUnitsPerEm();
        double scaleFactor = (1.0d / unitsPerEm) * fontSize;

        if(!supportCharacter(c)) {
            return unknownCharacter.scale(scaleFactor, scaleFactor);
        }
        if(c == ' ') {
            return spaceCharacter.scale(scaleFactor, scaleFactor);
        }
        TTFCharacter ttfCharacter = characterMap.get((int) c);
        return ttfCharacter.scale(scaleFactor, scaleFactor);
    }

    public TTFString getString(String s) {
        return getString(s, 1);
    }

    public TTFString getString(String s, double fontSize) throws TTFCharacterNotSupportedException {
        if(!supportString(s))
            throw new TTFCharacterNotSupportedException(String.format("The provided string '%s' is not supported by the loaded font.", s));
        TTFCharacter[] ttfCharacters = new TTFCharacter[s.length()];

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            TTFCharacter ttfCharacter = getCharacter(c, fontSize);
            ttfCharacters[i] = ttfCharacter;
        }

        TTFString ttfString = new TTFString(ttfCharacters);
        return ttfString;
    }

    private TTFCharacter createCharacter(int charCode, Glyf glyf, TTFHmtxTable.HMetric hMetric) {
        double lsb = hMetric.getLeftSideBearing();
        double rsb = hMetric.getAdvanceWidth() - hMetric.getLeftSideBearing() - (glyf.getXMax() - glyf.getXMin());
        return new TTFCharacter((char) charCode, lsb, rsb, glyf, hMetric);
    }

    private void processCharacter(SuperBufferedInputStream sbis, int charCode, Glyf glyf, TTFHmtxTable.HMetric hMetric) {
        TTFCharacter ttfCharacter = createCharacter(charCode, glyf, hMetric);
        characterMap.put(charCode, ttfCharacter);
    }

}
