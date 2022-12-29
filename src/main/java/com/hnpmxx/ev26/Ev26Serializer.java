package com.hnpmxx.ev26;

import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.formatters.Ev26ArrayPool;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;
import com.hnpmxx.ev26.formatters.Ev26MessagePackWriter;
import com.hnpmxx.ev26.internals.Ev26FormatterFactory;
import com.hnpmxx.ev26.internals.Ev26MsgIdFactory;

public class Ev26Serializer {

    private static Ev26Package packet = new Ev26Package();

    private Ev26MsgIdFactory ev26MsgIdFactory;
    private Ev26FormatterFactory ev26FormatterFactory;

    private boolean isNeedStartEnd;

    public Ev26Serializer(boolean isNeedStartEnd) throws Ev26Exception, InstantiationException, IllegalAccessException {
        ev26MsgIdFactory = new Ev26MsgIdFactory();
        ev26FormatterFactory = new Ev26FormatterFactory();

        this.isNeedStartEnd = isNeedStartEnd;
    }

    public Ev26Serializer() throws Ev26Exception, InstantiationException, IllegalAccessException {
        ev26MsgIdFactory = new Ev26MsgIdFactory();
        ev26FormatterFactory = new Ev26FormatterFactory();

        this.isNeedStartEnd = true;
    }

    public byte[] serialize(Ev26Package aPackage, PackageType packageType, int minBufferSize) throws Exception {
        byte[] buffer = Ev26ArrayPool.Rent(minBufferSize);
        try {
            Ev26MessagePackWriter writer = new Ev26MessagePackWriter(buffer, aPackage.packageType);
            packet.serialize(writer, aPackage);

            return writer.flushAndGetEncodingArray();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Ev26ArrayPool.Return(buffer, false);
        }
    }

    public byte[] serialize(Ev26Package aPackage, PackageType packageType) throws Exception {
        return serialize(aPackage, packageType, 4096);
    }

    public byte[] serialize(Ev26Package aPackage) throws Exception {
        return serialize(aPackage, PackageType.Type1, 4096);
    }

    public Ev26Package deserialize(byte[] bytes, PackageType packageType, int minBufferSize) throws Exception {
        byte[] buffer = Ev26ArrayPool.Rent(minBufferSize);
        try {
            Ev26MessagePackReader reader = new Ev26MessagePackReader(bytes, packageType, isNeedStartEnd);
            reader.decode(buffer);
            return packet.deserialize(reader, isNeedStartEnd);
        } finally {
            Ev26ArrayPool.Return(buffer, false);
        }
    }

    public Ev26Package deserialize(byte[] bytes, PackageType packageType) throws Exception {
        return deserialize(bytes, packageType, 4096);
    }

    public Ev26Package deserialize(byte[] bytes) throws Exception {
        return deserialize(bytes, PackageType.Type1, 4096);
    }

    public Ev26HeaderPackage headerDeserialize(byte[] bytes, PackageType packageType, int minBufferSize) {
        byte[] buffer = Ev26ArrayPool.Rent(minBufferSize);
        try {
            Ev26MessagePackReader reader = new Ev26MessagePackReader(bytes, packageType, isNeedStartEnd);
            reader.decode(buffer);
            return new Ev26HeaderPackage(reader);
        } finally {
            Ev26ArrayPool.Return(buffer, false);
        }
    }
}
