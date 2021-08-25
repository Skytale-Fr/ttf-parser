package fr.skytale.ttfparser;

import fr.skytale.ttfparser.tables.TTFHmtxTable;
import fr.skytale.ttfparser.tables.glyf.Glyf;

import java.util.Objects;

public class TTFCharacter implements Cloneable {

    private char character;
    private double lsb;
    private double rsb;
    private Glyf glyf;
    private TTFHmtxTable.HMetric hMetric;

    public TTFCharacter(char character, double lsb, double rsb, Glyf glyf, TTFHmtxTable.HMetric hMetric) {
        this.character = character;
        this.lsb = lsb;
        this.rsb = rsb;
        this.glyf = glyf;
        this.hMetric = hMetric;
    }

    public char getCharacter() {
        return character;
    }

    public double getLsb() {
        return lsb;
    }

    public double getRsb() {
        return rsb;
    }

    public Glyf getGlyf() { return glyf; }

    public TTFHmtxTable.HMetric getHMetric() { return hMetric; }

    public TTFCharacter scale(double scaleX, double scaleY) {
        TTFCharacter clone = clone();
        clone.lsb = lsb * scaleX;
        clone.rsb = rsb * scaleY;
        clone.glyf = glyf.scale(scaleX, scaleY);
        clone.hMetric = hMetric.scale(scaleX, scaleY);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTFCharacter that = (TTFCharacter) o;
        return character == that.character;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }

    @Override
    public String toString() {
        return "TTFCharacter{" +
                "character=" + character +
                ", lsb=" + lsb +
                ", rsb=" + rsb +
                ", glyf=" + glyf +
                ", hMetric=" + hMetric +
                '}';
    }

    @Override
    public TTFCharacter clone() {
        try {
            TTFCharacter clone = (TTFCharacter) super.clone();
            clone.character = character;
            clone.lsb = lsb;
            clone.rsb = rsb;
            clone.glyf = glyf.clone();
            clone.hMetric = hMetric.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
