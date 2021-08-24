package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TTFGlyfTableORIGINAL extends TTFTable {

    private static enum PointConfig {
        ON_CURVE((byte) 1),
        X_IS_BYTE((byte) 2),
        Y_IS_BYTE((byte) 4),
        REPEAT((byte) 8),
        X_DELTA ((byte) 16),
        Y_DELTA((byte) 32);
        private byte flag;
        PointConfig(byte flag) {
            this.flag = flag;
        }
        public byte getFlag() {
            return flag;
        }
    }

    public class Glyf {
        private short numberOfContours;
        private short xMin;
        private short yMin;
        private short xMax;
        private short yMax;
        private List<Point2D> points;
        private List<Short> contourEnds;
        protected Glyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax, List<Point2D> points, List<Short> contourEnds) {
            this.numberOfContours = numberOfContours;
            this.xMin = xMin;
            this.yMin = yMin;
            this.xMax = xMax;
            this.yMax = yMax;
            this.points = points;
            this.contourEnds = contourEnds;
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
        public short getXMax() { return xMax; }
        public short getYMax() {
            return yMax;
        }
        public List<Point2D> getPoints() { return points; }
        public List<Short> getContourEnds() { return contourEnds; }

        @Override
        public String toString() {
            return "Glyf{" +
                    "numberOfContours=" + numberOfContours +
                    ", xMin=" + xMin +
                    ", yMin=" + yMin +
                    ", xMax=" + xMax +
                    ", yMax=" + yMax +
                    ", nbPoints=" + points.size() +
                    ", points=" + points +
                    ", nbContourEnds=" + contourEnds.size() +
                    ", contourEnds=" + contourEnds +
                    '}';
        }
    }

    private List<Glyf> glyfs = new ArrayList<>();

    public TTFGlyfTableORIGINAL(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        super("glyf", sbis, tableManager);
    }

    @Override
    public void loadAttributes(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
        TTFLocaTable locaTable = tableManager.getTable(TTFTableManager.LOCA);
        List<Long> locations = locaTable.getLocations();
        int locationCount = locations.size();

        TTFHeadTable headTable = tableManager.getTable(TTFTableManager.HEAD);
        short indexToLocFormat = headTable.getIndexToLocFormat();
        boolean fetchAsShort = indexToLocFormat == 0;

        for(int i = 0; i < locationCount - 1; i++) {
            int multiplier = (fetchAsShort ? 2 : 1);
            long locaOffset = locations.get(i) * multiplier;
            sbis.setPos((int) (offset + locaOffset));

            // = Number of shape
            short numberOfContours = sbis.getShort();
            short xMin = sbis.getShort();
            short yMin = sbis.getShort();
            short xMax = sbis.getShort();
            short yMax = sbis.getShort();
            List<Point2D> points = new ArrayList<>();
            List<Short> contourEnds = new ArrayList<>();
            if(numberOfContours != -1) {
                computePoints(sbis, numberOfContours, xMin, yMin, xMax, yMax, points, contourEnds);
            }
            Glyf glyf = new Glyf(numberOfContours, xMin, yMin, xMax, yMax, points, contourEnds);
            glyfs.add(glyf);
        }
    }

    private void computePoints(SuperBufferedInputStream sbis, short numberOfContours, short xMin, short yMin, short xMax, short yMax, List<Point2D> points, List<Short> contourEnds) throws IOException {
        for(int i = 0; i < numberOfContours; i++) {
            short value = sbis.getShort();
            contourEnds.add(value);
        }
        sbis.skipBytes(2);
        if(numberOfContours == 0) {
            return;
        }
        int numPoints = contourEnds.stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
        numPoints += 1;

        List<Byte> flags = new ArrayList<>();

        for(int i = 0; i < numPoints; i++) {
            byte flag = sbis.getByte();
            flags.add(flag);
            if(verifyFlag(flag, PointConfig.REPEAT)) {
                byte repeatCount = sbis.getByte();
                i += repeatCount;
                while(repeatCount-- > 0) {
                    flags.add(flag);
                }
            }
        }

        for(int i = 0; i < numPoints; i++) {
            int x = processprocessPointCoordX(sbis, i, xMin, xMax, flags);
            int y = processprocessPointCoordY(sbis, i, yMin, yMax, flags);
            points.add(new Point2D.Double(x, y));
        }
    }

    private int processPointCoord(SuperBufferedInputStream sbis, int pointIndex, byte byteFlag, byte deltaFlag, short min, short max, List<Byte> flags) throws IOException {
        int value = 0;
        byte flag = flags.get(pointIndex);
        if(verifyFlag(flag, byteFlag)) {
            if(verifyFlag(flag, deltaFlag)) {
                value += sbis.getByte();
            } else {
                value -= sbis.getByte();
            }
        } else if(verifyFlag((byte) ~flag, deltaFlag)) {
            value += sbis.getByte();
        } else {
            // value is unchanged.
        }
        return value;
    }
    private int processprocessPointCoordX(SuperBufferedInputStream sbis, int pointIndex, short min, short max, List<Byte> flags) throws IOException {
        return processPointCoord(sbis, pointIndex, PointConfig.X_IS_BYTE.getFlag(), PointConfig.X_DELTA.getFlag(), min, max, flags);
    }
    private int processprocessPointCoordY(SuperBufferedInputStream sbis, int pointIndex, short min, short max, List<Byte> flags) throws IOException {
        return processPointCoord(sbis, pointIndex, PointConfig.Y_IS_BYTE.getFlag(), PointConfig.Y_DELTA.getFlag(), min, max, flags);
    }

    private boolean verifyFlag(byte b, byte flag) {
        byte result = (byte) (b & flag);
        return result == flag;
    }
    private boolean verifyFlag(byte b, PointConfig flag) {
        return verifyFlag(b, flag.getFlag());
    }

    public List<Glyf> getGlyfs() {
        return glyfs;
    }

    @Override
    public String toString() {
        return "TTFGlyfTable{" +
                "glyfs=" + glyfs +
                ", checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
