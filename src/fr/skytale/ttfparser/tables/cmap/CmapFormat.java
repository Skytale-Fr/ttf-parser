package fr.skytale.ttfparser.tables.cmap;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.io.IOException;
import java.util.Objects;

/**
 * This class represents a Cmap GlyfIndexMap parser based
 * on the provided format.
 */
public abstract class CmapFormat {

    private short format;

    protected int length;
    protected int language;
    protected GlyfIndexMap glyphIndexMap = new GlyfIndexMap();

    public CmapFormat(short format) {
        this.format = format;
    }

    public abstract void loadAttributes(SuperBufferedInputStream sbis) throws IOException;

    public short getFormat() {
        return format;
    }

    public GlyfIndexMap getGlyphIndexMap() {
        return glyphIndexMap;
    }

    public int getLength() {
        return length;
    }

    public int getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CmapFormat that = (CmapFormat) o;
        return format == that.format;
    }

    @Override
    public int hashCode() {
        return Objects.hash(format);
    }

    @Override
    public String toString() {
        return "CmapFormat{" +
                "format=" + format +
                ", length=" + length +
                ", language=" + language +
                ", glyphIndexMap=" + glyphIndexMap +
                '}';
    }
}
