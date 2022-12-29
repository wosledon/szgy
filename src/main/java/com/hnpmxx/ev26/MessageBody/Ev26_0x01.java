package com.hnpmxx.ev26.MessageBody;

import com.hnpmxx.ev26.Ev26Bodies;
import com.hnpmxx.ev26.Ev26Header;
import com.hnpmxx.ev26.Ev26Package;
import com.hnpmxx.ev26.Ev26Serializer;
import com.hnpmxx.ev26.enums.TimeZones;
import com.hnpmxx.ev26.extensions.HexExtensions;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;
import com.hnpmxx.ev26.formatters.Ev26MessagePackWriter;
import com.hnpmxx.ev26.interfaces.IMessageBody;
import com.hnpmxx.ev26.models.TimeZoneLanguageModel;

public class Ev26_0x01 extends Ev26Bodies<Ev26_0x01> implements IMessageBody {
    public Ev26_0x01() {
    }

    public Ev26_0x01(String terminalId,
                     short terminalType,
                     TimeZoneLanguageModel timeZoneLanguage) {
        this.terminalId = terminalId;
        this.terminalType = terminalType;
        this.timeZoneLanguage = timeZoneLanguage;
    }

    public byte msgId = 0x01;
    /**
     * 终端Id
     */
    public String terminalId;
    /**
     * 终端类型识别码
     */
    public short terminalType;

    /**
     * 时区语言
     */
    public TimeZoneLanguageModel timeZoneLanguage;

    public String description = "登录包";

    @Override
    public void serialize(Ev26MessagePackWriter writer, Ev26_0x01 value) {
        writer.writeBcd(value.terminalId, 16);
        writer.writeInt16(value.terminalType);
        writer.writeInt16(value.timeZoneLanguage.serialize());
    }

    @Override
    public Ev26_0x01 deserialize(Ev26MessagePackReader reader, boolean isNeedStartEnd) {
        Ev26_0x01 ev260x01 = new Ev26_0x01();

        ev260x01.terminalId = reader.readBcd(16);
        ev260x01.terminalType = reader.readInt16();
        ev260x01.timeZoneLanguage = reader.readTimeZoneLanguage();

        return ev260x01;
    }

    public static void main(String[] args) throws Exception {
//        Ev26Serializer serializer = new Ev26Serializer();
//        byte[] hex = HexExtensions.toHexBytes("7878 11 01 07 52 53 36 78 90 02 42 7000 3201 0005 1279 0D0A");
//
//        Ev26Package packet = serializer.deserialize(hex);
//        Ev26_0x01 body = (Ev26_0x01) packet.ev26Bodies;
//
//        byte[] result1 = serializer.serialize(packet);
//        byte[] result2 = serializer.serialize(packet);
//        byte[] result3 = serializer.serialize(packet);
//
//        System.out.println(HexExtensions.toHexString(hex));
//        System.out.println(HexExtensions.toHexString(result1));
//        System.out.println(HexExtensions.toHexString(result2));
//        System.out.println(HexExtensions.toHexString(result3));

        Ev26Package packet = new Ev26Package();
        packet.ev26Header = new Ev26Header();
        packet.ev26Header.msgId = 0x01;
        packet.ev26Header.msgNum = 0x0005;

        packet.ev26Bodies = new Ev26_0x01(
                "7 52 53 36 78 90 02 42".replace(" ", ""),
                (short) 0x7000,
                new TimeZoneLanguageModel(
                        5.13F,
                        TimeZones.西,
                        true, false, false
                )
        );

        Ev26Serializer serializer = new Ev26Serializer();

        //7878 11 01 07 52 53 36 78 90 02 42 7000 3201 0005 1279 0D0A
        String hex = HexExtensions.toHexString(serializer.serialize(packet));
        System.out.println(hex);
        Ev26Package revPacket = serializer.deserialize(HexExtensions.toHexBytes(hex));

        packet.ev26Bodies = new Ev26_0x01(
                "8 52 53 36 78 90 02 42".replace(" ", ""),
                (short) 0x7000,
                new TimeZoneLanguageModel(
                        5.13F,
                        TimeZones.西,
                        true, false, false
                )
        );

        hex = HexExtensions.toHexString(serializer.serialize(packet));
        //7878110108525336789002427000320100058D9D0D0A
        //7878110108525336789002427000320100058D9D0D0A
        System.out.println(hex);

        Ev26Package evPacket = serializer.deserialize(HexExtensions.toHexBytes(hex));

        System.out.println(1);
    }
}
