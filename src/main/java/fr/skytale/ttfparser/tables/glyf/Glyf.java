package fr.skytale.ttfparser.tables.glyf;

import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.tables.TTFPoint;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class Glyf implements Cloneable {

    protected short numberOfContours;
    protected short xMin;
    protected short yMin;
    protected short xMax;
    protected short yMax;
    protected short width;
    protected short height;

    protected List<TTFPoint> points = new LinkedList<>();
    protected List<Integer> contourEnds = new LinkedList<>();

    protected int instructionLength = 0;
    protected List<Byte> instructions = new LinkedList<>();

    public Glyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax) {
        this.numberOfContours = numberOfContours;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.width = (short) (getXMax() - getXMin());
        this.height = (short) (getYMax() - getYMin());
    }

    public abstract void loadPoints(SuperBufferedInputStream sbis) throws IOException;

    protected boolean verifyFlag(byte b, byte flag) {
        byte result = (byte) (b & flag);
        return result > 0;
    }
    protected boolean verifyFlag(short b, short flag) {
        byte result = (byte) (b & flag);
        return result > 0;
    }
    protected boolean verifyFlag(int b, int flag) {
        byte result = (byte) (b & flag);
        return result > 0;
    }
    protected boolean verifyFlag(byte b, Flag.Byte flag) {
        return verifyFlag(b, flag.getFlag());
    }
    protected boolean verifyFlag(short b, Flag.Int16 flag) {
        return verifyFlag(b, flag.getFlag());
    }
    protected boolean verifyFlag(int b, Flag.Int32 flag) {
        return verifyFlag(b, flag.getFlag());
    }

    public short getNumberOfContours() {
        return numberOfContours;
    }

    public short getXMin() {
        return xMin;
    }

    public short getYMin() {
        return yMin;
    }

    public short getXMax() {
        return xMax;
    }

    public short getYMax() {
        return yMax;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public List<TTFPoint> getPoints() {
        return points;
    }

    public List<Integer> getContourEnds() {
        return contourEnds;
    }

    public List<List<TTFPoint>> getContours() {
        List<List<TTFPoint>> contours = new LinkedList<>();
        int contourEndsIndex = 0;
        for(int i = 0; i < points.size(); i++) {
            List<TTFPoint> contour = new LinkedList<>();
            TTFPoint point = points.get(i);
            contour.add(point);
            if(contourEnds.get(contourEndsIndex) == i) {
                contours.add(contour);
                contour = new LinkedList<>();
                contourEndsIndex++;
            }
        }
        return contours;
    }

    public Glyf scale(double scaleX, double scaleY) {
        Glyf clone = clone();
        clone.xMin = (short) (xMin * scaleX);
        clone.yMin = (short) (yMin * scaleY);
        clone.xMax = (short) (xMax * scaleX);
        clone.yMax = (short) (yMax * scaleY);
        this.width = (short) (getXMax() - getXMin());
        this.height = (short) (getYMax() - getYMin());
        for(TTFPoint point : clone.points) {
            point.x *= scaleX;
            point.y *= scaleY;
        }
        return clone;
    }

    @Override
    public String toString() {
        return "Glyf{" +
                "numberOfContours=" + numberOfContours +
                ", xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                ", points=" + points +
                ", contourEnds=" + contourEnds +
                '}';
    }

    @Override
    public Glyf clone() {
        try {
            Glyf clone = (Glyf) super.clone();
            clone.numberOfContours = numberOfContours;
            clone.xMin = xMin;
            clone.yMin = yMin;
            clone.xMax = xMax;
            clone.yMax = yMax;
            clone.width = width;
            clone.height = height;

            // This list needs a deep copy.
            clone.points = new LinkedList<>();
            for(TTFPoint point : points) {
                clone.points.add(point.clone());
            }

            // Those lists copy can be made with a simple
            // shallow one.
            clone.contourEnds = new LinkedList<>();
            clone.contourEnds.addAll(contourEnds);

            clone.instructions = new LinkedList<>();
            clone.instructions.addAll(instructions);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
