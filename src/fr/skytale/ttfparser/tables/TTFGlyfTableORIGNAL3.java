package fr.skytale.ttfparser.tables;

import fr.skytale.ttfparser.SuperBufferedInputStream;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class TTFGlyfTableORIGNAL3 extends TTFTable {

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
        private List<TTFPoint> points;
        private List<Short> contourEnds;
        protected Glyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax, List<TTFPoint> points, List<Short> contourEnds) {
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
        public List<TTFPoint> getPoints() { return points; }
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

    public TTFGlyfTableORIGNAL3(SuperBufferedInputStream sbis, TTFTableManager tableManager) throws IOException {
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
            List<TTFPoint> points = new LinkedList<>();
            List<Short> contourEnds = new LinkedList<>();

            List<Byte> flags = new LinkedList<>();
            byte flag = 0;

            short instructionLength = 0;
            List<Byte> instructions = new LinkedList<>();

            if(numberOfContours > 0) {
                for(int j = 0; j < numberOfContours; j++) {
                    short value = sbis.getShort();
                    contourEnds.add(value);
                }

                instructionLength = sbis.getShort();
                for(int j = 0; j < instructionLength; j++) {
                    byte value = sbis.getByte();
                    instructions.add(value);
                }

                int numPoints = contourEnds.stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
                numPoints += 1;
                for(int j = 0; j < numPoints; j++) {
                    flag = sbis.getByte();
                    flags.add(flag);
                    if((flag & 8) > 0) {
                        byte repeatCount = sbis.getByte();
                        for(int r = 0; r < repeatCount; r++) {
                            flags.add(flag);
                            j += 1;
                        }
                    }
                }

                assert flags.size() == numPoints : "Bad flags.";

                if(contourEnds.size() > 0) {
                    if(numPoints > 0) {
                        for(int j = 0; j < numPoints; j++) {
                            flag = flags.get(j);
                            TTFPoint point = new TTFPoint();
                            point.setOnCurve(!!((flag & 1) > 0));
                            point.setLastPointOfContour(contourEnds.indexOf(j) >= 0);
                            points.add(point);
                        }

                        double px = 0;
                        for(int j = 0; j < numPoints; j++) {
                            flag = flags.get(j);
                            TTFPoint point = points.get(j);
                            point.setX(parseGlyphCoordinate(sbis, flag, px, (byte) 2, (byte) 16));
                            px = point.getX();
                        }

                        double py = 0;
                        for(int j = 0; j < numPoints; j++) {
                            flag = flags.get(j);
                            TTFPoint point = points.get(j);
                            point.setY(parseGlyphCoordinate(sbis, flag, py, (byte) 4, (byte) 32));
                            py = point.getY();
                        }
                    }
                }

            } else if(numberOfContours == 0) {
            } else {
//                // The glyph is a composite one.
//                boolean moreComponents = true;
//                while(moreComponents) {
//                    flag = sbis.getByte();
//                    // TODO: Repair
//                    sbis.getShort();
//
//                    if((flag & 1) > 0) {
//                        if((flag & 2) > 0) {
//                            sbis.getShort();
//                            sbis.getShort();
//                        } else {
//                            sbis.getShort();
//                            sbis.getShort();
//                        }
//                    } else {
//                        if((flag & 2) > 0) {
//                            sbis.getByte();
//                            sbis.getByte();
//                        } else {
//                            sbis.getByte();
//                            sbis.getByte();
//                        }
//                    }
//
//                    if((flag & 8) > 0) {
//                        sbis.getShort();
//                    } else if((flag & 64) > 0) {
//                        sbis.getShort();
//                        sbis.getShort();
//                    } else if((flag & 128) > 0) {
//                        sbis.getShort();
//                        sbis.getShort();
//                        sbis.getShort();
//                        sbis.getShort();
//                    }
//                    // blablabla
//                    // blablabla
//                }
//                if(verifyFlag(flag, (byte) 0x100)) {
//                    sbis.getShort();
//                    for(int j = 0; j < instructionLength; j++) {
//                        sbis.getShort();
//                    }
//                }
             }

            Glyf glyf = new Glyf(numberOfContours, xMin, yMin, xMax, yMax, points, contourEnds);
            glyfs.add(glyf);
        }
    }

    private double parseGlyphCoordinate(SuperBufferedInputStream sbis, byte flag, double previousValue, byte shortVectorBitMask, byte sameBitMask) throws IOException {
        double v;
        if((flag & shortVectorBitMask) > 0) {
            v = sbis.getByte();
            if((flag & sameBitMask) == 0) {
                v = -v;
            }

            v = previousValue + v;
        } else {
            if((flag & sameBitMask) > 0) {
                v = previousValue;
            } else {
                v = previousValue + sbis.getShort();
            }
        }

        return v;
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
            if((flag & 8) > 0) {
                byte repeatCount = sbis.getByte();
                i += repeatCount;
                while(repeatCount-- > 0) {
                    flags.add(flag);
                }
            }
        }

        double px = 0;
        double py = 0;
        for(int i = 0; i < numPoints; i++) {
            double x = processprocessPointCoordX(sbis, i, px, flags);
            double y = processprocessPointCoordY(sbis, i, py, flags);
            px = x;
            py = y;
            points.add(new Point2D.Double(x, y));
        }
    }

    private double processPointCoord(SuperBufferedInputStream sbis, int pointIndex, byte byteFlag, byte deltaFlag, double previousValue, List<Byte> flags) throws IOException {
        double value = 0;
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
        return value + previousValue;
    }
    private double processprocessPointCoordX(SuperBufferedInputStream sbis, int pointIndex, double previousValue, List<Byte> flags) throws IOException {
        return processPointCoord(sbis, pointIndex, PointConfig.X_IS_BYTE.getFlag(), PointConfig.X_DELTA.getFlag(), previousValue, flags);
    }
    private double processprocessPointCoordY(SuperBufferedInputStream sbis, int pointIndex, double previousValue, List<Byte> flags) throws IOException {
        return processPointCoord(sbis, pointIndex, PointConfig.Y_IS_BYTE.getFlag(), PointConfig.Y_DELTA.getFlag(), previousValue, flags);
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
