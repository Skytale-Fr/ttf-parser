package fr.skytale.viewer;

import fr.skytale.ttfparser.TTFCharacter;
import fr.skytale.ttfparser.TTFString;

import java.awt.*;
import java.awt.geom.Point2D;

public class StringDrawer {

    private TTFString ttfString;
    private LetterDrawer[] letterDrawers;

    public StringDrawer(TTFString ttfString) {
        this.ttfString = ttfString;
        this.letterDrawers = new LetterDrawer[ttfString.length()];

        for(int i = 0; i < ttfString.length(); i++) {
            TTFCharacter ttfCharacter = ttfString.getCharacter(i);
            LetterDrawer letterDrawer = new LetterDrawer(ttfCharacter);
            this.letterDrawers[i] = letterDrawer;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        System.out.println(ttfString.length());
        for(int i = 0; i < ttfString.length(); i++) {
            TTFCharacter ttfCharacter = ttfString.getCharacter(i);
            System.out.println("Printing character: " + ttfCharacter.getCharacter());
            LetterDrawer letterDrawer = this.letterDrawers[i];
            Point2D.Double position = ttfString.getPosition(i);
            g2d.translate(position.getX(), position.getY());
            letterDrawer.draw(g);
            g2d.translate(-position.getX(), -position.getY());
        }
    }

}
