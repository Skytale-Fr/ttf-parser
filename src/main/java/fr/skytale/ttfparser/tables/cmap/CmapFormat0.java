package fr.skytale.ttfparser.tables.cmap;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class CmapFormat0 extends CmapFormat {

    private static final int GLYPH_INDEX_ARRAY_STATIC_LENGTH = 256;

    private int length;
    private int language;

    public CmapFormat0() {
        super((short) 0);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis) throws IOException {
        length = sbis.getUShort();
        language = sbis.getUShort();
        for(int i = 0; i < GLYPH_INDEX_ARRAY_STATIC_LENGTH; i++) {
            int index = sbis.getUByte();
            glyphIndexMap.put(i, index);
        }
    }
}
