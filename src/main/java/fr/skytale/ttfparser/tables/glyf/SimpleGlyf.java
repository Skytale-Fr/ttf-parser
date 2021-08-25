package fr.skytale.ttfparser.tables.glyf;

import fr.skytale.ttfparser.flags.BitFlag;
import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.flags.ByteFlag;
import fr.skytale.ttfparser.flags.UInt8Flag;
import fr.skytale.ttfparser.tables.TTFPoint;
import fr.skytale.ttfparser.tables.TTFTableParseException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class SimpleGlyf extends Glyf {

    private static enum SimpleFlag implements BitFlag.UInt8 {
        // If set, the point is on the curve;
        // Otherwise, it is off the curve.
        ON_CURVE((short) 0x01),

        // If set, the corresponding x-coordinate is 1 byte long;
        // Otherwise, the corresponding x-coordinate is 2 bytes long
        X_IS_BYTE((short) 0x02),
        // 	If set, the corresponding y-coordinate is 1 byte long;
        // Otherwise, the corresponding y-coordinate is 2 bytes long
        Y_IS_BYTE((short) 0x04),

        // 	If set, the next byte specifies the number of additional
        // 	times this set of flags is to be repeated. In this way,
        // 	the number of flags listed can be smaller than the number
        // 	of points in a character.
        REPEAT((short) 0x08),

        // This flag has one of two meanings, depending
        // on how the x-Short Vector flag is set:
        //    - If the x-Short Vector bit is set, this bit describes the
        //    sign of the value, with a value of 1 equalling positive and
        //    a zero value negative.
        //    - If the x-short Vector bit is not set, and this bit is set,
        //    then the current x-coordinate is the same as
        //    the previous x-coordinate.
        //    - If the x-short Vector bit is not set, and this bit is not set,
        //    the current x-coordinate is a signed 16-bit delta vector.
        //    In this case, the delta vector is the change in x.
        X_DELTA((short) 0x10),

        // 	This flag has one of two meanings, depending
        // 	on how the y-Short Vector flag is set:
        //     - If the y-Short Vector bit is set, this bit describes the
        //     sign of the value, with a value of 1 equalling positive and
        //     a zero value negative.
        //     - If the y-short Vector bit is not set, and this bit is set,
        //     then the current y-coordinate is the same as
        //     the previous y-coordinate.
        //     - If the y-short Vector bit is not set, and this bit is not set,
        //     the current y-coordinate is a signed 16-bit delta vector.
        //     In this case, the delta vector is the change in y.
        Y_DELTA((short) 0x20),

        // If set, contours in the glyph description may overlap.
        // Use of this flag is not required in OpenType — that is,
        // it is valid to have contours overlap without having this flag set.
        // It may affect behaviors in some platforms, however.
        // (See the discussion of "Overlapping contours" in Apple’s specification
        // for details regarding behavior in Apple platforms.)
        // When used, it must be set on the first flag byte for the glyph. See additional details below.
        OVERLAP_SIMPLE((short) 0x40);

        // Bit 7 are reserved and set to zero.
        private short flag;

        SimpleFlag(short flag) {
            this.flag = flag;
        }

        public java.lang.Short getBitFlag() {
            return flag;
        }
    }

    public SimpleGlyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax) {
        super(numberOfContours, xMin, yMin, xMax, yMax);
    }

    @Override
    public void loadPoints(SuperBufferedInputStream sbis) throws IOException {
        List<UInt8Flag> flags = new LinkedList<>();
        UInt8Flag flag = null;
        for(int i = 0; i < numberOfContours; i++) {
            int value = sbis.getUShort();
            contourEnds.add(value);
        }

        instructionLength = sbis.getUShort();
        for(int i = 0; i < instructionLength; i++) {
            byte value = sbis.getByte();
            instructions.add(value);
        }

        int numPoints = contourEnds.stream().mapToInt(v -> v).max().orElse(0);
        if(numPoints > 0) numPoints += 1;
//        int numPoints = contourEnds.get(contourEnds.size() - 1) + 1;
        for(int i = 0; i < numPoints; i++) {
            flag = new UInt8Flag(sbis.getUByte());
            flags.add(flag);
            if(flag.verifyFlag(SimpleFlag.REPEAT)) {
                byte repeatCount = sbis.getByte();
                for(int j = 0; j < repeatCount; j++) {
                    flags.add(flag);
                    i += 1;
                }
            }
        }

        if(flags.size() != numPoints) {
            throw new TTFTableParseException("flagCount", flags.size(), numPoints);
        }

        if(contourEnds.size() > 0) {
            if(numPoints > 0) {
                for(int i = 0; i < numPoints; i++) {
                    flag = flags.get(i);
                    TTFPoint point = new TTFPoint();
                    point.setOnCurve(flag.verifyFlag(SimpleFlag.ON_CURVE));
                    point.setLastPointOfContour(contourEnds.indexOf(i) >= 0);
                    points.add(point);
                }

                double px = 0;
                for(int i = 0; i < numPoints; i++) {
                    flag = flags.get(i);
                    TTFPoint point = points.get(i);
                    point.setX(parseGlyphCoordinate(sbis, flag, px, SimpleFlag.X_IS_BYTE, SimpleFlag.X_DELTA));
                    px = point.getX();
                }

                double py = 0;
                for(int i = 0; i < numPoints; i++) {
                    flag = flags.get(i);
                    TTFPoint point = points.get(i);
                    point.setY(parseGlyphCoordinate(sbis, flag, py, SimpleFlag.Y_IS_BYTE, SimpleFlag.Y_DELTA));
                    py = point.getY();
                }
            }
        }
    }

    /**
     *
     * @param sbis
     * @param flag
     * @param previousValue
     * @param shortVectorBitMask should be X_IS_BYTE or Y_IS_BYTE.
     * @param sameBitMask should be X_DELTA or Y_DELTA.
     * @return
     * @throws IOException
     */
    private double parseGlyphCoordinate(SuperBufferedInputStream sbis, UInt8Flag flag, double previousValue, SimpleFlag shortVectorBitMask, SimpleFlag sameBitMask) throws IOException {
        double v = 0;
        // If the coordinate is 1 byte long.
        if(flag.verifyFlag(shortVectorBitMask)) {
            // Read 1 byte.
            v = sbis.getUByte();

            // If set, the value is positive,
            // otherwise the value is negative.
            if(!flag.verifyFlag(sameBitMask)) {
                v = -v;
            }

            // As the provided location is relative from the previous,
            // we need to add the previousValue.
            v = previousValue + v;
        } else {
            // If the coordinate is 2 bytes long.

            // If the sameBitMask is set, then the current coordinate is
            // the same as the previous coordinate.
            if(flag.verifyFlag(sameBitMask)) {
                v = previousValue;
            } else {
                // Otherwise, the current x-coordinate is a signed 16-bit delta vector.
                // In this case, the delta vector is the change in x / y.
                v = previousValue + sbis.getShort();
            }
        }

        return v;
    }

    @Override
    public String toString() {
        return "SimpleGlyf{" +
                "numberOfContours=" + numberOfContours +
                ", xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                ", points=" + points +
                ", contourEnds=" + contourEnds +
                ", instructionLength=" + instructionLength +
                ", instructions=" + instructions +
                '}';
    }
}
