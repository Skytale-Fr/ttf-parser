package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class TTFUnknownTable extends TTFTable {

    public TTFUnknownTable(String tag, SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super(tag, sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        // This would not have to be implemented.
    }
}
