package fr.skytale.ttfparser;

import java.io.*;
import java.nio.ByteBuffer;
import java.time.*;

public class SuperBufferedInputStream extends RandomAccessFile {
    public enum INTFormat {
        INT8(1),
        INT16(2),
        INT32(4);
        private final int length;
        INTFormat(int length) {
            this.length = length;
        }
        public int getLength() {
            return this.length;
        }
    }

    public SuperBufferedInputStream(File file) throws FileNotFoundException {
        super(file, "r");
    }

    public long getPos() {
        try {
            return super.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setPos(long pos) throws IOException {
        super.seek(pos);
    }

    public String getString(int length) throws IOException {
        byte[] bytes = new byte[length];
        super.read(bytes);
//        ByteBuffer byteBuffer = ByteBuffer.allocate(2 * 4);
//        byteBuffer.put(bytes);
        return new String(bytes);
    }

    public byte getByte() throws IOException {
        return readByte();
    }

    public short getUByte() throws IOException {
//        int ch = super.read();
//        if (ch < 0)
//            throw new EOFException();
//        return ch;
        return (short) (getByte() & 0xff);
    }

    public short getShort() throws IOException {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
//        byte[] bytes = readNBytes(2);
//        byteBuffer.put(bytes);
//        return byteBuffer.getShort(0);
        return readShort();
    }

    public int getUShort() throws IOException {
//        int ch1 = super.read();
//        int ch2 = super.read();
//        if ((ch1 | ch2) < 0) {
//            System.out.println(super.available());
//            throw new EOFException();
//        }
//        return (ch1 << 8) + (ch2 << 0);
        return getShort() & 0xffff;
    }

    public int getInt(INTFormat format) throws IOException {
        int formatLength = format.getLength();
        ByteBuffer byteBuffer = ByteBuffer.allocate(formatLength);
        byte[] bytes = new byte[formatLength];
        read(bytes);
        byteBuffer.put(bytes);

        switch(format) {
            case INT8: {
                return (int) bytes[0];
            }
            case INT16: {
                return (int) byteBuffer.getShort(0);
            }
            case INT32: {
                return byteBuffer.getInt(0);
            }
            default: return -1;
        }
    }

    public long getUInt(INTFormat format) throws IOException {
        switch(format) {
            case INT8: {
                return getUByte();
            }
            case INT16: {
                return getUShort();
            }
            case INT32: {
                final long UNSIGNED_INT_BITS = 0xffffffffL;
                int signedIntValue = getInt(INTFormat.INT32);
                long unsignedIntValue = UNSIGNED_INT_BITS & signedIntValue;
                return unsignedIntValue;
            }
            default: throw new IllegalArgumentException("Not supported INTFormat operation.");
        }
    }

    public int getFixed() throws IOException {
        return getInt(INTFormat.INT32) / (1 << 16);
    }

    public short getF2Dot14() throws IOException {
        return (short) (getShort() / 16384);
    }

    public LocalDateTime getDate() throws IOException {
        // TODO: Fix date computation
        long macTime = getInt(INTFormat.INT32) * 4294967296L + getInt(INTFormat.INT32);
        long utcTime = macTime * 1000 + 2082844800000L;
        return LocalDateTime.now(new Clock() {
            @Override
            public ZoneId getZone() {
                return ZoneOffset.UTC;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return this;
            }

            @Override
            public Instant instant() {
                return Instant.ofEpochMilli(utcTime);
            }
        });
    }


}
