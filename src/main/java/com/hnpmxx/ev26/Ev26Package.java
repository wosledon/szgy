package com.hnpmxx.ev26;

import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.exceptions.Ev26ErrorCode;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.extensions.BasicTypeExtensions;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;
import com.hnpmxx.ev26.formatters.Ev26MessagePackWriter;
import com.hnpmxx.ev26.interfaces.Ev26PackageBase;
import com.hnpmxx.ev26.interfaces.IEv26MessagePackageFormatter;
import com.hnpmxx.ev26.interfaces.IMessageBody;
import com.hnpmxx.ev26.internals.Ev26MsgIdFactory;

public class Ev26Package extends Ev26PackageBase implements IEv26MessagePackageFormatter<Ev26Package>, IMessageBody {
    public Ev26Package() {
        begin = (short) ((0x78 << 8) + 0x78);
    }

    public Ev26Package(PackageType packageType) throws Exception {
        this.packageType = packageType;

        if (packageType.equals(PackageType.Type1)) {
            begin = (short) ((0x78 << 8) + 0x78);
        } else if (packageType.equals(PackageType.Type2)) {
            begin = (short) ((0x79 << 8) + 0x79);
        } else {
            throw new Exception("协议头写入异常");
        }
    }

    /**
     * 原始数据
     */
    public byte[] originalPackage;

    /**
     * 获取起始位
     *
     * @param packageType 协议类型
     * @return 起始位
     */
    public static short getBeginFlag(PackageType packageType) {
        if (packageType == PackageType.Type1) {
            return (short) ((0x78 << 8) + 0x78);
        }

        if (packageType == PackageType.Type2) {
            return (short) ((0x79 << 8) + 0x79);
        }

        return (short) ((0x78 << 8) + 0x78);
    }

    public void setBeginFlag(PackageType packageType) {
        if (packageType == PackageType.Type1) {
            begin = (short) ((0x78 << 8) + 0x78);
        }

        if (packageType == PackageType.Type2) {
            begin = (short) ((0x79 << 8) + 0x79);
        }

        begin = (short) ((0x78 << 8) + 0x78);
    }

    public static final short endFlag = (short) ((0x0D << 8) + 0x0A);

    @Override
    public void serialize(Ev26MessagePackWriter writer, Ev26Package value) throws Ev26Exception {
        int writeReturnPosition = 0;
        writer.writeStart();
        if (this.packageType.equals(PackageType.Type1)) {
            writeReturnPosition = writer.skip(1);
        } else {
            writeReturnPosition = writer.skip(2);
        }

        int headerLength = writer.getCurrentPosition();
        writer.writeByte(value.ev26Header.msgId);
        if (value.ev26Bodies != null) {
            if (!value.ev26Bodies.skipSerialization) {
                value.ev26Bodies.serialize(writer, value.ev26Bodies);
            }
        }
        writer.writeInt16(value.ev26Header.msgNum);
        value.ev26Header.length = (short) (writer.getCurrentPosition() - headerLength + 2);
        if (value.packageType.equals(PackageType.Type1)) {
            writer.writeByteReturn((byte) value.ev26Header.length, writeReturnPosition);
        } else {
            writer.writeInt16Return(value.ev26Header.length, writeReturnPosition);
        }
        writer.writeCrcForPackage();
        writer.writeEnd();
        writer.writeEncode();
    }

    @Override
    public Ev26Package deserialize(Ev26MessagePackReader reader, boolean isNeedStartEnd) throws Exception {
        Ev26MsgIdFactory factory = new Ev26MsgIdFactory();

        if (!reader.getCheckCrcVerify()) {
            throw new Ev26Exception(Ev26ErrorCode.crcVerifyFinal, "Crc校验失败");
        }

        Ev26Package packet = new Ev26Package(reader.packageType);
        if (isNeedStartEnd) packet.begin = reader.readStart();
        if (packet.begin == (short) ((0x79 << 8) + 0x79)) {
            packageType = PackageType.Type2;
        }
        packet.ev26Header = new Ev26Header(reader.packageType);
        if (packet.packageType == PackageType.Type1) {
            packet.ev26Header.length = reader.readByte();
        } else {
            packet.ev26Header.length = reader.readInt16();
        }
        packet.ev26Header.msgId = reader.readByte();
        packet.packageType = reader.packageType;
        if (BasicTypeExtensions.short2u(packet.ev26Header.length) - 5 > 0) {
            Object instance = factory.getValue(packet.ev26Header.msgId);
            if (instance != null) {
                try {
//                    Object t = packet.ev26Bodies.deserialize(reader, isNeedStartEnd);
                    packet.ev26Bodies = (Ev26Bodies) ((Ev26Bodies) instance).deserialize(reader, isNeedStartEnd);
                } catch (Exception e) {
                    throw new Ev26Exception(Ev26ErrorCode.bodiesParseError, e);
                }
            }
        }
        packet.ev26Header.msgNum = reader.readInt16();
        packet.ev26Header.crc = reader.readInt16();
        if (isNeedStartEnd) {
            packet.end = reader.readEnd();
        }

        //TODO
        if (packet.packageType == PackageType.Type1) {

        } else {

        }

        return packet;
    }
}
