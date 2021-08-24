package fr.skytale.ttfparser.tables;

import java.util.Objects;

public class TTFTableID<T extends TTFTable> {

    private String tableTag;

    public TTFTableID(String tableTag) {
        this.tableTag = tableTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTFTableID<?> that = (TTFTableID<?>) o;
        return Objects.equals(tableTag, that.tableTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableTag);
    }

    @Override
    public String toString() {
        return "TTFTableID{" +
                "tableTag='" + tableTag + '\'' +
                '}';
    }
}
