package fr.skytale.ttfparser.tables.head;

import fr.skytale.ttfparser.flags.BitFlag;
import fr.skytale.ttfparser.flags.UInt16Flag;

public class FontDecoration {

    public enum FontDecorationFlag implements BitFlag.UInt16 {
        BOLD(0x1),
        ITALIC(0x2),
        UNDERLINE(0x4),
        OUTLINE(0x8),
        SHADOW(0x10),
        CONDENSED(0x20),
        EXTENDED(0x40);

        // Bits 7 - 15 are reserved (set to 0)

        private int flag;
        FontDecorationFlag(int flag) {
            this.flag = flag;
        }
        @Override
        public java.lang.Integer getBitFlag() {
            return flag;
        }
    }

    private UInt16Flag macStyleFlag;

    public FontDecoration(int macStyle) {
        this.macStyleFlag = new UInt16Flag(macStyle);
    }

    public boolean isBold() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.BOLD);
    }

    public boolean isItalic() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.ITALIC);
    }
    public boolean isUnderline() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.UNDERLINE);
    }

    public boolean isOutline() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.OUTLINE);
    }

    public boolean isShadow() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.SHADOW);
    }

    public boolean isCondensed() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.CONDENSED);
    }

    public boolean isExtended() {
        return this.macStyleFlag.verifyFlag(FontDecorationFlag.EXTENDED);
    }

    @Override
    public String toString() {
        String decoration = "normal";
        if(isBold()) decoration = "bold";
        if(isItalic()) decoration = "italic";
        if(isUnderline()) decoration = "underline";
        if(isOutline()) decoration = "outline";
        if(isShadow()) decoration = "shadow";
        if(isCondensed()) decoration = "condensed";
        if(isExtended()) decoration = "extended";
        return "FontDecoration{" +
                "decoration=" + decoration +
                '}';
    }
}
