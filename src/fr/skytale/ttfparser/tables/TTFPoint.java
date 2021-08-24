package fr.skytale.ttfparser.tables;

import java.awt.geom.Point2D;

public class TTFPoint extends Point2D.Double implements Cloneable {

    private boolean onCurve = false;
    private boolean lastPointOfContour = false;

    public TTFPoint() {
        super();
    }

    public TTFPoint(double x, double y) {
        super(x, y);
    }

    public TTFPoint(double x, double y, boolean onCurve, boolean lastPointOfContour) {
        super(x, y);
        this.onCurve = onCurve;
        this.lastPointOfContour = lastPointOfContour;
    }

    public boolean isOnCurve() {
        return onCurve;
    }

    public void setOnCurve(boolean onCurve) {
        this.onCurve = onCurve;
    }

    public boolean isLastPointOfContour() {
        return lastPointOfContour;
    }

    public void setLastPointOfContour(boolean lastPointOfContour) {
        this.lastPointOfContour = lastPointOfContour;
    }

    public void setX(double x) { super.x = x; }
    public void setY(double y) {
        super.y = y;
    }

    @Override
    public String toString() {
        return "TTFPoint{" +
                "onCurve=" + onCurve +
                ", lastPointOfContour=" + lastPointOfContour +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public TTFPoint clone() {
        TTFPoint clone = (TTFPoint) super.clone();
        clone.onCurve = onCurve;
        clone.lastPointOfContour = lastPointOfContour;
        return clone;
    }
}
