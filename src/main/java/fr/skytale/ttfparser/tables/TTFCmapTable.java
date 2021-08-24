package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.tables.cmap.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CMAP Table documentation:
 * (Character to Glyph Index Mapping Table)
 * Windows: https://docs.microsoft.com/en-us/typography/opentype/spec/cmap
 * Apple: https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6cmap.html
 *
 * This table defines the mapping of character codes to
 * the glyph index values used in the font.
 * It may contain more than one subtable, in order
 * to support more than one character encoding scheme.
 *
 */
public class TTFCmapTable extends TTFTable {

    public class EncodingRecord {
        private int platformID;
        private int encodingID;
        private long offset;
        public EncodingRecord(int platformID, int encodingID, long offset) {
            this.platformID = platformID;
            this.encodingID = encodingID;
            this.offset = offset;
        }
        public int getPlatformID() {
            return platformID;
        }
        public int getEncodingID() {
            return encodingID;
        }
        public long getOffset() {
            return offset;
        }
        @Override
        public String toString() {
            return "EncodingRecord{" +
                    "platformID=" + platformID +
                    ", encodingID=" + encodingID +
                    ", offset=" + offset +
                    '}';
        }
    }

    private static final short SUPPORTED_VERSION = 0;
    private static final short SUPPORTED_FORMAT = 4;

    private int version;
    private int numTables;
    private int format;
    private int language;
    private int cmapFormatLength;
    private List<EncodingRecord> encodingRecords = new ArrayList<>();
    private GlyfIndexMap glyfIndexMap;

    public TTFCmapTable(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("cmap", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        version = sbis.getUShort();
        if(version != SUPPORTED_VERSION) throw new TTFTableParseException(String.format("Unsupported CMAP version (version: %d, expected: %d)", version, SUPPORTED_VERSION));
        numTables = sbis.getUShort();

        for(int i = 0; i < numTables; i++) {
            // For more information about platformIDs and encodingIDs,
            // you can check the following web page:
            // https://docs.microsoft.com/en-us/typography/opentype/spec/cmap#platform-ids
            int platformID = sbis.getUShort();
            int encodingID = sbis.getUShort();
            long offset = sbis.getUInt(SuperBufferedInputStream.INTFormat.INT32);
            EncodingRecord encodingRecord = new EncodingRecord(platformID, encodingID, offset);
            encodingRecords.add(encodingRecord);
        }

        // The next process can be expressed as "OUCH"
        long selectedOffset = -1;
        for(int i = 0; i < numTables; i++) {
            EncodingRecord encodingRecord = encodingRecords.get(i);
            int platformID = encodingRecord.getPlatformID();
            int encodingID = encodingRecord.getEncodingID();
            long offset = encodingRecord.getOffset();

            boolean isWindowsPlatform = platformID == 3 &&
                    (encodingID == 0 || encodingID == 1 || encodingID == 10);
            boolean isUnicodePlatform = platformID == 0 && List.of(new Integer[]{0, 1, 2, 3, 4}).contains(Integer.valueOf(encodingID));

            if(isWindowsPlatform || isUnicodePlatform) {
                selectedOffset = offset;
                break;
            }
        }

        // If there is no offset found in parsed records
        if(selectedOffset == -1) {
            throw new TTFTableParseException(String.format("(CMAP) The font doesn't contain any recognized platform and encoding."));
        }

        format = sbis.getUShort();
        CmapFormat formatParser;
        switch(format) {
            // TODO: Implement following formats:
            //      - Format 0: Byte encoding table (DONE)
            //      - Format 2: High-byte mapping through table
            //      - Format 8: mixed 16-bit and 32-bit coverage
            //      - Format 10: Trimmed array
            //      - Format 12: Segmented coverage
            //      - Format 13: Many-to-one range mappings
            //      - Format 14: Unicode Variation Sequences
            case 0: {
                formatParser = new CmapFormat0();
                break;
            }
            case 4: {
                formatParser = new CmapFormat4();
                break;
            }
            case 6: {
                formatParser = new CmapFormat6();
                break;
            }
            default: {
                throw new TTFTableParseException(String.format("Unsupported CMAP format (format: %d, expected: %d)", format, SUPPORTED_FORMAT));
            }
        }
        formatParser.loadAttributes(sbis);
        glyfIndexMap = formatParser.getGlyphIndexMap();
        cmapFormatLength = formatParser.getLength();
        language = formatParser.getLanguage();
    }

    public int getVersion() {
        return version;
    }

    public int getNumTables() {
        return numTables;
    }

    public int getFormat() {
        return format;
    }

    public int getLanguage() {
        return language;
    }

    public int getCmapFormatLength() {
        return cmapFormatLength;
    }

    public List<EncodingRecord> getEncodingRecords() {
        return encodingRecords;
    }

    public GlyfIndexMap getGlyfIndexMap() {
        return glyfIndexMap;
    }

    @Override
    public String toString() {
        return "TTFCmapTable{" +
                "version=" + version +
                ", numTables=" + numTables +
                ", format=" + format +
                ", language=" + language +
                ", encodingRecords=" + encodingRecords +
                ", glyfIndexMap=" + glyfIndexMap +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
