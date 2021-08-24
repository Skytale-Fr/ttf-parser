package fr.skytale.ttfparser.tables.cmap;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CmapFormat4 extends CmapFormat {

    private int segCountX2;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;
    private List<Integer> endCodes = new ArrayList<>();
    private List<Integer> startCodes = new ArrayList<>();
    private List<Integer> idDeltas = new ArrayList<>();
    private List<Integer> idRangeOffsets = new ArrayList<>();

    public CmapFormat4() {
        super((short) 4);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis) throws IOException {
        length = sbis.getUShort();
        language = sbis.getUShort();
        segCountX2 = sbis.getUShort();
        searchRange = sbis.getUShort();
        entrySelector = sbis.getUShort();
        rangeShift = sbis.getUShort();

        // Segment count is stored doubled. That's why we need
        // to divide segCountX2 by 2.
        short segCount = (short) (segCountX2 / 2);
        for(int i = 0; i < segCount; i++) {
            int value = sbis.getUShort();
            endCodes.add(value);
        }

        sbis.skipBytes(2); // Skip short reserved slot.

        for(int i = 0; i < segCount; i++) {
            int value = sbis.getUShort();
            startCodes.add(value);
        }

        for(int i = 0; i < segCount; i++) {
            int value = sbis.getUShort();
            idDeltas.add(value);
        }

        long idRangeOffsetsStart = sbis.getPos();

        for(int i = 0; i < segCount; i++) {
            int value = sbis.getUShort();
            idRangeOffsets.add(value);
        }

        // Starting Glyf mapping

        // The table was designed to help search inside it, so, for example,
        // the last segment is a special one with ``0xFFFF`` as both its start and end code.
        // But we will not use that since we are just producing JS object mapping.
        for(int i = 0; i < segCount - 1; i++) {
            int glyphIndex = 0;
            int endCode = endCodes.get(i);
            int startCode = startCodes.get(i);
            int idDelta = idDeltas.get(i);
            int idRangeOffset = idRangeOffsets.get(i);

            for(int c = startCode; c < endCode + 1; c++) {
                if(idRangeOffset != 0) {
                    int startCodeOffset = (c - startCode) * 2;
                    int currentRangeOffset = i * 2; // 2 because the numbers are 2 byte big.

                    long glyphIndexOffset = idRangeOffsetsStart + // where all offsets started
                            currentRangeOffset + // offset for the current range
                            idRangeOffset + // offset between the id range table and the glyphIdArray[]
                            startCodeOffset; // gets us finally to the character

                    sbis.setPos(glyphIndexOffset);
                    glyphIndex = sbis.getUShort();
                    if(glyphIndex != 0) {
                        // & 0xffff is modulo 65536.
                        glyphIndex = (glyphIndex + idDelta) & 0xffff;
                    }
                } else {
                    glyphIndex = (c + idDelta) & 0xffff;
                }
                // TODO: Parse error ?
                // It seems to avoid some indices at the end.
                glyphIndexMap.put(c, glyphIndex);
            }
        }
    }

    public int getSegCountX2() {
        return segCountX2;
    }
    public int getSegCount() { return (short) (segCountX2 / 2); }

    public int getSearchRange() {
        return searchRange;
    }

    public int getEntrySelector() {
        return entrySelector;
    }

    public int getRangeShift() {
        return rangeShift;
    }

    public List<Integer> getEndCodes() {
        return endCodes;
    }

    public List<Integer> getStartCodes() {
        return startCodes;
    }

    public List<Integer> getIdDeltas() {
        return idDeltas;
    }

    public List<Integer> getIdRangeOffsets() {
        return idRangeOffsets;
    }

    @Override
    public String toString() {
        return "CmapFormat4{" +
                "length=" + length +
                ", language=" + language +
                ", segCountX2=" + segCountX2 +
                ", searchRange=" + searchRange +
                ", entrySelector=" + entrySelector +
                ", rangeShift=" + rangeShift +
                ", endCodes=" + endCodes +
                ", startCodes=" + startCodes +
                ", idDeltas=" + idDeltas +
                ", idRangeOffsets=" + idRangeOffsets +
                ", glyphIndexMap=" + glyphIndexMap +
                '}';
    }
}
