package fr.skytale.ttfparser.tables.glyf;

public interface Flag {
    public interface Byte {
        public byte getFlag();
    }
    public interface Int16 {
        public short getFlag();
    }
    public interface Int32 {
        public int getFlag();
    }
}
