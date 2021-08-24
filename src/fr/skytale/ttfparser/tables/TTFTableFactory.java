package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class TTFTableFactory {

    public static final TTFTable createTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        String tag = sbis.getString(4);
        switch(tag) {
            case "head": return new TTFHeadTable(sbis, tableManager);
            case "maxp": return new TTFMaxpTable(sbis, tableManager);
            case "hhea": return new TTFHheaTable(sbis, tableManager);
            case "hmtx": return new TTFHmtxTable(sbis, tableManager);
            case "loca": return new TTFLocaTable(sbis, tableManager);
            case "glyf": return new TTFGlyfTable(sbis, tableManager);
            case "cmap": return new TTFCmapTable(sbis, tableManager);
            default: return new TTFUnknownTable(tag, sbis, tableManager);
        }
    }

    public static final TTFTableID<?> getTableID(TTFTable table) throws IOException {
        String tag = table.getTag();
        switch(tag) {
            case "head": return TTFTableManager.HEAD;
            case "maxp": return TTFTableManager.MAXP;
            case "hhea": return TTFTableManager.HHEA;
            case "hmtx": return TTFTableManager.HMTX;
            case "loca": return TTFTableManager.LOCA;
            case "glyf": return TTFTableManager.GLYF;
            case "cmap": return TTFTableManager.CMAP;
            default: return new TTFTableID<TTFUnknownTable>(tag);
        }
    }

}
