package fr.skytale.ttfparser;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public class ByteFlag {

    private byte[] bytes;

    public ByteFlag(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteFlag(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        bytes = buffer.array();
    }

    public ByteFlag(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        bytes = buffer.array();
    }

    public boolean isTrue(int flagIndex) {
        if(flagIndex < 0 && flagIndex >= bytes.length * 8) throw new IllegalArgumentException("Provided flagIndex should fit provided flag byte array length.");
        int byteIndex = (int) Math.floor(flagIndex / 8.0d);
        int bitIndex = flagIndex % 8;
        byte b = bytes[byteIndex];

        byte verificator = (byte) Math.pow(2, bitIndex);
        byte result = (byte) (b & verificator);
        return result == verificator;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        for(byte b : bytes) {
            sj.add(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        return "ByteFlag{" +
                "bytes=" + sj.toString() +
                '}';
    }
}
