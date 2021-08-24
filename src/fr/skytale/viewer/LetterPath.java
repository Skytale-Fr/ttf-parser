package fr.skytale.viewer;

import fr.skytale.ttfparser.tables.TTFPoint;
import fr.skytale.ttfparser.TTFCharacter;
import fr.skytale.ttfparser.tables.glyf.Glyf;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class LetterPath extends Path2D.Double {

    private TTFCharacter character;

    public LetterPath(TTFCharacter character) {
        this.character = character;
        Glyf glyf = character.getGlyf();
        List<TTFPoint> points = glyf.getPoints();
        List<Integer> contourEnds = glyf.getContourEnds();

        int p = 0;
        int c = 0;
        boolean first = true;

        while(p < points.size()) {
            Point2D point = points.get(p);
            if(first) {
                super.moveTo(point.getX(), point.getY());
                first = false;
            } else {
                super.lineTo(point.getX(), point.getY());
            }

            if(p == contourEnds.get(c)) {
                c += 1;
                first = true;
                super.closePath();
            }
            p += 1;
        }
    }

}
