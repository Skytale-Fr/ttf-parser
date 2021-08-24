package fr.skytale.ttfparser;

import fr.skytale.ttfparser.tables.TTFTableManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TTFParser {

    private File fontFile;

    private TTFFormatInfo formatInfo;
    private TTFTableManager tableManager = new TTFTableManager();
    private TTFAlphabet ttfAlphabet;

    public TTFParser(URL fontURL) throws URISyntaxException {
        this(fontURL.toURI());
    }

    public TTFParser(URI fontURI) {
        this.fontFile = new File(fontURI);
    }
    public TTFParser(File fontFile) { this.fontFile = fontFile; }

    public TTFAlphabet parse() {
        try {
            SuperBufferedInputStream sbis = new SuperBufferedInputStream(this.fontFile);

            int scalarType = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
            short numTables = sbis.getShort();
            short searchRange = sbis.getShort();
            short entrySelector = sbis.getShort();
            short rangeShift = sbis.getShort();

            this.formatInfo = new TTFFormatInfo(scalarType, numTables, searchRange, entrySelector, rangeShift);
            System.out.println(formatInfo);

            for(int tableIndex = 0; tableIndex < numTables; tableIndex++) {
                tableManager.createTable(sbis);
            }
            tableManager.parseAttributes(sbis);

            ttfAlphabet = new TTFAlphabet(sbis, tableManager);

            sbis.close();
            return ttfAlphabet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to parse the provided TTF file.");
    }

    public TTFTableManager getTableManager() {
        return tableManager;
    }

}
