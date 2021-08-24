package fr.skytale.ttfparser.flags;

public abstract class Flag<T> {

    private T flag;
    private FlagType<T> flagType;

    public Flag(T flag, FlagType<T> flagType) {
        this.flag = flag;
        this.flagType = flagType;
    }

    public boolean verifyFlag(BitFlag<T> bitFlag) {
        return flagType.verifyTag(flag, bitFlag);
    }

}
