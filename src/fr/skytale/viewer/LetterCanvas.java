package fr.skytale.viewer;

import fr.skytale.ttfparser.TTFCharacter;

import java.awt.*;

public class LetterCanvas extends Canvas {

    private LetterDrawer drawer;

    public LetterCanvas(TTFCharacter character) {
        this.drawer = new LetterDrawer(character);
    }

    public void setCharacter(TTFCharacter character) {
        this.drawer = new LetterDrawer(character);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(100, 400);
        g2d.scale(0.4, -0.4);
        this.drawer.draw(g);
    }

}
