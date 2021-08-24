package fr.skytale.ttfparser.flags;

public interface FlagType<T> {

    public boolean verifyTag(T flag, BitFlag<T> bit);

    public static FlagType<Byte> BYTE = (flag, bit) -> {
        byte bitFlag = bit.getBitFlag();
        byte result = (byte) (flag & bitFlag);
        return result > 0;
    };
    public static FlagType<Byte> INT8 = BYTE;

    public static FlagType<Short> SHORT = (flag, bit) -> {
        short bitFlag = bit.getBitFlag();
        short result = (short) (flag & bitFlag);
        return result > 0;
    };
    public static FlagType<Short> INT16 = SHORT;
    public static FlagType<Short> UBYTE = SHORT;
    public static FlagType<Short> UINT8 = SHORT;

    public static FlagType<Integer> INTEGER = (flag, bit) -> {
        int bitFlag = bit.getBitFlag();
        int result = flag & bitFlag;
        return result > 0;
    };
    public static FlagType<Integer> INT32 = INTEGER;
    public static FlagType<Integer> UINT16 = INTEGER;
    public static FlagType<Integer> USHORT = INTEGER;

    public static FlagType<Long> LONG = (flag, bit) -> {
        long bitFlag = bit.getBitFlag();
        long result = flag & bitFlag;
        return result > 0;
    };
    public static FlagType<Long> INT64 = LONG;
    public static FlagType<Long> UINT32 = LONG;

}
