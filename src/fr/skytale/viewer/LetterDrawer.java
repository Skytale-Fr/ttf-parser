package fr.skytale.viewer;

import fr.skytale.ttfparser.tables.TTFPoint;
import fr.skytale.ttfparser.TTFCharacter;
import fr.skytale.ttfparser.tables.glyf.Glyf;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class LetterDrawer {

    private TTFCharacter ttfCharacter;
    private LetterPath letterPath;

    public LetterDrawer(TTFCharacter ttfCharacter) {
        this.ttfCharacter = ttfCharacter;
        this.letterPath = new LetterPath(ttfCharacter);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(letterPath);
        g2d.setColor(Color.RED);
        Glyf glyf = ttfCharacter.getGlyf();
        java.util.List<TTFPoint> points = glyf.getPoints();
        List<Integer> contourEnds = glyf.getContourEnds();
        for(TTFPoint point : points) {
            if(point.isOnCurve()) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.MAGENTA);
            }
            g2d.drawOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
        }

        g2d.setColor(Color.GREEN);
        g2d.drawOval((int) -3, -3, 6, 6);

        g2d.setColor(Color.BLUE);
        for(int contourEnd : contourEnds) {
            Point2D point = points.get(contourEnd);
            g2d.drawOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
        }
        g2d.drawRect(glyf.getXMin(), glyf.getYMin(), ttfCharacter.getGlyf().getWidth(), ttfCharacter.getGlyf().getHeight());

    }

}
