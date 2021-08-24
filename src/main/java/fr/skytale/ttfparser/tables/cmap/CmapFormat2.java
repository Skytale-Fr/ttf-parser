package fr.skytale.ttfparser.tables.cmap;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public class CmapFormat2 extends CmapFormat {

    private static final int SUB_HEADER_KEYS_STATIC_LENGTH = 256;

    private int length;
    private int language;

    private int[] subHeaderKeys = new int[SUB_HEADER_KEYS_STATIC_LENGTH];

    public CmapFormat2() {
        super((short) 2);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis) throws IOException {
        length = sbis.getUShort();
        language = sbis.getUShort();

        for(int i = 0; i < SUB_HEADER_KEYS_STATIC_LENGTH; i++) {
            int value = sbis.getUShort();
            subHeaderKeys[i] = value;

            int k = value / 8;
            // TODO: Needs to be completed.
        }

    }
}
