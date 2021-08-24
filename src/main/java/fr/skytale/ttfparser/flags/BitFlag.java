package fr.skytale.ttfparser.flags;

public interface BitFlag<T> {

    T getBitFlag();

    public interface Byte extends BitFlag<java.lang.Byte> {}
    public interface Int8 extends Byte {}
    public interface UByte extends Int16 {}
    public interface UInt8 extends Int16 {}

    public interface Short extends BitFlag<java.lang.Short> {}
    public interface Int16 extends Short {}
    public interface UInt16 extends Int32 {}
    public interface UShort extends Int32 {}

    public interface Integer extends BitFlag<java.lang.Integer> {}
    public interface Int32 extends Integer {}
    public interface UInt32 extends Int64 {}

    public interface Long extends BitFlag<java.lang.Long> {}
    public interface Int64 extends Long {}

}
