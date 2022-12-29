package com.hnpmxx.ev26;

import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.extensions.BasicTypeExtensions;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;
import com.hnpmxx.ev26.formatters.Ev26MessagePackWriter;
import com.hnpmxx.ev26.interfaces.IEv26Header;
import com.hnpmxx.ev26.interfaces.IEv26MessagePackageFormatter;

public class Ev26Header extends IEv26Header implements IEv26MessagePackageFormatter<Ev26Header> {
    public Ev26Header() {
        packageType = PackageType.Type1;
    }

    public Ev26Header(PackageType packageType) {
        this.packageType = packageType;
    }


    @Override
    public void serialize(Ev26MessagePackWriter writer, Ev26Header value) throws Ev26Exception {
        int writeReturnPosition = 0;

        if (value.packageType == packageType.Type1) {
            writeReturnPosition = writer.skip(1);
        } else {
            writeReturnPosition = writer.skip(2);
        }

        int headerLength = writer.getCurrentPosition();
        writer.writeByte(value.msgId);
        if (value.bodies != null) {
            if (!bodies.skipSerialization) {

            }
        }

        writer.writeInt16(value.msgNum);
        writer.writeCrcForHeader();

        value.length = (short) (writeReturnPosition - headerLength);
        if (value.packageType == PackageType.Type1) {
            writer.writeByteReturn((byte) value.length, writeReturnPosition);
        } else {
            writer.writeInt16Return(value.length, writeReturnPosition);
        }
    }

    @Override
    public Ev26Header deserialize(Ev26MessagePackReader reader, boolean isNeedStartEnd) {
        Ev26Header header = new Ev26Header(reader.packageType);

        if (packageType == PackageType.Type1) {
            header.length = reader.readByte();
        } else {
            header.length = reader.readInt16();
        }

        header.msgId = reader.readByte();
        reader.skip(BasicTypeExtensions.short2u(header.length) - 5);
        header.msgNum = reader.readInt16();
        header.crc = reader.readInt16();

        return header;
    }
}
