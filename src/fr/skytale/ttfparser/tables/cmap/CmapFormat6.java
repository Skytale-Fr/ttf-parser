package fr.skytale.ttfparser.tables.cmap;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class CmapFormat6 extends CmapFormat {

    private int firstCode;
    private int entryCount;

    public CmapFormat6() {
        super((short) 6);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis) throws IOException {
        length = sbis.getUShort();
        language = sbis.getUShort();
        firstCode = sbis.getUShort();
        entryCount = sbis.getUShort();

        for(int i = 0; i < entryCount; i++) {
            int glyphIndex = sbis.getUShort();
            glyphIndexMap.put(firstCode + i, glyphIndex);
        }
    }

    public int getFirstCode() {
        return firstCode;
    }

    public int getEntryCount() {
        return entryCount;
    }

    @Override
    public String toString() {
        return "CmapFormat6{" +
                "length=" + length +
                ", language=" + language +
                ", firstCode=" + firstCode +
                ", entryCount=" + entryCount +
                ", glyphIndexMap=" + glyphIndexMap +
                '}';
    }
}
