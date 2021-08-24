package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;

public abstract class TTFTable {

    private String tag;
    protected int checksum;
    protected int offset;
    protected int length;

    public TTFTable(String tag, SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        this.tag = tag;
        this.checksum = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
        this.offset = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
        this.length = sbis.getInt(SuperBufferedInputStream.INTFormat.INT32);
    }

    public void parseAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        long previousPos = sbis.getPos();
        sbis.setPos(offset);
        loadAttributes(sbis, tableManager);
        sbis.setPos(previousPos);
    }

    public abstract void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException;

    public String getTag() {
        return tag;
    }

    public int getChecksum() {
        return checksum;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "TTFTable{" +
                "tag='" + tag + '\'' +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
