package fr.skytale.ttfparser.tables.glyf;

import fr.skytale.ttfparser.flags.BitFlag;
import fr.skytale.ttfparser.SuperBufferedInputStream;
import fr.skytale.ttfparser.flags.UInt16Flag;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

public class CompositeGlyf extends Glyf {

    public class Component {
        private int glyphIndex;
        // Default values
        private short xScale = 1;
        private short yScale = 1;
        private short scale01 = 0;
        private short scale10 = 0;
        private short dx = 0;
        private short dy = 0;
        private AbstractMap.SimpleEntry<Integer, Integer> matchedPoint;

        public Component(int glyphIndex, short xScale, short yScale, short scale01, short scale10, short dx, short dy) {
            this.glyphIndex = glyphIndex;
            this.xScale = xScale;
            this.yScale = yScale;
            this.scale01 = scale01;
            this.scale10 = scale10;
            this.dx = dx;
            this.dy = dy;
        }
        public Component(int glyphIndex) {
            this.glyphIndex = glyphIndex;
        }
        public int getGlyphIndex() {
            return glyphIndex;
        }
        public short getXScale() {
            return xScale;
        }
        public short getYScale() {
            return yScale;
        }
        public short getScale01() {
            return scale01;
        }
        public short getScale10() {
            return scale10;
        }
        public short getDX() {
            return dx;
        }
        public short getDY() {
            return dy;
        }
        @Override
        public String toString() {
            return "Component{" +
                    "glyphIndex=" + glyphIndex +
                    ", xScale=" + xScale +
                    ", yScale=" + yScale +
                    ", scale01=" + scale01 +
                    ", scale10=" + scale10 +
                    ", dx=" + dx +
                    ", dy=" + dy +
                    '}';
        }
    }

    private static enum CompositeFlag implements BitFlag.UInt16 {
        // If this is set, the arguments are 16-bit (uint16 or int16);
        // otherwise, they are bytes (uint8 or int8).
        ARG_1_AND_2_ARE_WORDS(0x0001),

        // If this is set, the arguments are signed xy values;
        // otherwise, they are unsigned point numbers.
        ARGS_ARE_XY_VALUES(0x0002),

        // For the xy values if the preceding is true.
        ROUND_XY_TO_GRID(0x0004),

        // This indicates that there is a simple scale for the component.
        // Otherwise, scale = 1.0.
        WE_HAVE_A_SCALE(0x0008),

        // Indicates at least one more glyph after this one.
        MORE_COMPONENTS(0x0020),

        // The x direction will use a different scale from the y direction.
        WE_HAVE_AN_X_AND_Y_SCALE(0x0040),

        // There is a 2 by 2 transformation that will be used to scale the component.
        WE_HAVE_A_TWO_BY_TWO(0x0080),

        // Following the last component are instructions for the composite character.
        WE_HAVE_INSTRUCTIONS(0x0100),

        // If set, this forces the aw and lsb (and rsb) for the composite
        // to be equal to those from this original glyph.
        // This works for hinted and unhinted characters.
        USE_MY_METRICS(0x0200),

        // If set, the components of the compound glyph overlap.
        // Use of this flag is not required in OpenType — that is,
        // it is valid to have components overlap without having this flag set.
        // It may affect behaviors in some platforms, however.
        // (See Apple’s specification for details regarding behavior in Apple platforms.)
        // When used, it must be set on the flag word for the first component.
        // See additional remarks, above, for the similar OVERLAP_SIMPLE flag used
        // in simple-glyph descriptions.
        OVERLAP_COMPOUND(0x0400),

        // The composite is designed to have the component offset scaled.
        SCALED_COMPONENT_OFFSET(0x0800),

        // The composite is designed not to have the component offset scaled.
        UNSCALED_COMPONENT_OFFSET(0x1000);

        // *********************************************
        // Bits 4, 13, 14 and 15 are reserved: set to 0.
        // *********************************************

        private int flag;

        CompositeFlag(int flag) {
            this.flag = flag;
        }

        public java.lang.Integer getBitFlag() {
            return flag;
        }
    }

    private List<Component> components = new LinkedList<>();

    public CompositeGlyf(short numberOfContours, short xMin, short yMin, short xMax, short yMax) {
        super(numberOfContours, xMin, yMin, xMax, yMax);
    }

    @Override
    public void loadPoints(SuperBufferedInputStream sbis) throws IOException {
        boolean moreComponents = true;
        UInt16Flag flags = null;
        while(moreComponents) {
            flags = new UInt16Flag(sbis.getUShort());
            int glyphIndex = sbis.getUShort();
            Component component = new Component(glyphIndex);

            if(flags.verifyFlag(CompositeFlag.ARG_1_AND_2_ARE_WORDS)) {
                // The arguments are words
                if(flags.verifyFlag(CompositeFlag.ARGS_ARE_XY_VALUES)) {
                    // Values are offset
                    component.dx = sbis.getShort();
                    component.dy = sbis.getShort();
                } else {
                    int min = sbis.getUShort();
                    int max = sbis.getUShort();
                    component.matchedPoint = new AbstractMap.SimpleEntry<Integer, Integer>(min, max);
                }
            } else {
                // The arguments are bytes
                if(flags.verifyFlag(CompositeFlag.ARGS_ARE_XY_VALUES)) {
                    // Values are offset
                    component.dx = sbis.getByte();
                    component.dy = sbis.getByte();
                } else {
                    int min = sbis.getByte();
                    int max = sbis.getByte();
                    component.matchedPoint = new AbstractMap.SimpleEntry<Integer, Integer>(min, max);
                }
            }

            if(flags.verifyFlag(CompositeFlag.WE_HAVE_A_SCALE)) {
                // We have a scale
                short scale = sbis.getF2Dot14();
                component.xScale = scale;
                component.yScale = scale;
            } else if(flags.verifyFlag(CompositeFlag.WE_HAVE_AN_X_AND_Y_SCALE)) {
                component.xScale = sbis.getF2Dot14();
                component.yScale = sbis.getF2Dot14();
            } else if(flags.verifyFlag(CompositeFlag.WE_HAVE_A_TWO_BY_TWO)) {
                component.xScale = sbis.getF2Dot14();
                component.scale01 = sbis.getF2Dot14();
                component.scale10 = sbis.getF2Dot14();
                component.yScale = sbis.getF2Dot14();
            }

            components.add(component);
            moreComponents = flags.verifyFlag(CompositeFlag.MORE_COMPONENTS);
        }
        if(flags.verifyFlag(CompositeFlag.WE_HAVE_INSTRUCTIONS)) {
            instructionLength = sbis.getUShort();
            for(int i = 0; i < instructionLength; i++) {
                instructions.add(sbis.getByte());
            }
        }
    }

    @Override
    public String toString() {
        return "CompositeGlyf{" +
                "components=" + components +
                ", numberOfContours=" + numberOfContours +
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
