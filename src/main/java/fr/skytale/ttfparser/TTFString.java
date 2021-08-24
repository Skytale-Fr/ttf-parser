package fr.skytale.ttfparser;

import fr.skytale.ttfparser.tables.TTFHmtxTable;
import fr.skytale.ttfparser.tables.glyf.Glyf;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

public class TTFString implements Cloneable {

    private Point2D.Double[] charactersLocations;
    private TTFCharacter[] characters;

    /**
     * Creates a deep copy of the provided array.
     * @param characters
     */
    public TTFString(TTFCharacter[] characters) {
        this.characters = new TTFCharacter[characters.length];
        this.charactersLocations = new Point2D.Double[characters.length];
        double xPadding = 0;
        double yPadding = 0;
        for(int i = 0; i < characters.length; i++) {
            TTFCharacter character = characters[i];
            TTFCharacter clone = character.clone();

            this.characters[i] = clone;
            this.charactersLocations[i] = new Point2D.Double(xPadding, yPadding);

            Glyf glyf = clone.getGlyf();
            TTFHmtxTable.HMetric hMetric = clone.getHMetric();
            double advanceWidth = hMetric.getAdvanceWidth();
            xPadding += advanceWidth;
        }
    }

    public int length() {
        return charactersLocations.length;
    }

//    public Point2D.Double getPosition(char c) {
//        Optional<TTFCharacter> optCharacter = getCharacter(c);
//        if(optCharacter.isPresent()) {
//            TTFCharacter character = optCharacter.get();
//            return getPosition(character);
//        }
//        return new Point2D.Double(0, 0);
//    }

    public Point2D.Double getPosition(int index) {
        if(index < 0 || index >= characters.length) throw new IllegalArgumentException(String.format("The provided index is not correct (provided: %d, length: %d)", index, characters.length));
        return charactersLocations[index];
    }

//    public Optional<TTFCharacter> getCharacter(char c) {
//        Set<TTFCharacter> keys = charactersLocations.keySet();
//        return keys.stream().filter(ttfCharacter -> ttfCharacter.getCharacter() == c).findFirst();
//    }

    public TTFCharacter getCharacter(int index) {
        if(index < 0 || index >= characters.length) throw new IllegalArgumentException(String.format("The provided index is not correct (provided: %d, length: %d)", index, characters.length));
        return this.characters[index];
    }

    public TTFString scale(double scaleX, double scaleY) {
        TTFString clone = clone();

        for(int i = 0; i < characters.length; i++) {
            clone.characters[i] = characters[i].scale(scaleX, scaleY);
        }

        for(int i = 0; i < charactersLocations.length; i++) {
            Point2D.Double point = (Point2D.Double) charactersLocations[i].clone();
            point.x *= scaleX;
            point.y *= scaleY;
            clone.charactersLocations[i] = point;
        }

        return clone;
    }

    @Override
    public String toString() {
        return "TTFString{" +
                "characters=" + Arrays.stream(characters).map(c -> c.getCharacter()).collect(Collectors.toList()).toString() +
                '}';
    }

    @Override
    public TTFString clone() {
        try {
            TTFString clone = (TTFString) super.clone();
            clone.characters = new TTFCharacter[characters.length];
            for(int i = 0; i < characters.length; i++) {
                clone.characters[i] = characters[i].clone();
            }

            clone.charactersLocations = new Point2D.Double[charactersLocations.length];
            for(int i = 0; i < charactersLocations.length; i++) {
                clone.charactersLocations[i] = (Point2D.Double) charactersLocations[i].clone();
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
