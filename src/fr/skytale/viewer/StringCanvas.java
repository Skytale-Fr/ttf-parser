package fr.skytale.viewer;

import fr.skytale.ttfparser.TTFString;

import java.awt.*;

public class StringCanvas extends Canvas {

    private StringDrawer drawer;

    public StringCanvas(TTFString string) {
        System.out.println(string);
        this.drawer = new StringDrawer(string);
    }

    public void setCharacter(TTFString string) {
        this.drawer = new StringDrawer(string);
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
