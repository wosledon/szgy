package com.hnpmxx.ev26.models;

import com.hnpmxx.ev26.enums.TimeZones;
import com.hnpmxx.ev26.extensions.TimeZoneLanguageExtensions;

@SuppressWarnings("ALL")
public class TimeZoneLanguageModel {
    public TimeZoneLanguageModel() {
    }

    public TimeZoneLanguageModel(float timeZoneTime,
                                 TimeZones timeZone,
                                 boolean reservedBits,
                                 boolean languageChoose2,
                                 boolean languageChoose1) {

        this.timeZoneTime = timeZoneTime;
        this.timeZone = timeZone;
        this.reservedBits = reservedBits;
        this.languageChoose2 = languageChoose2;
        this.languageChoose1 = languageChoose1;
    }

    /**
     * 数据
     */
    public short data = 0;
    /**
     * 高位, 1.5字节
     */
    public short highParts = 0;
    /**
     * 低位, 0.5字节
     */
    public short lowParts = 0;
    /**
     * 时区时间
     */
    public float timeZoneTime = 0;

    /**
     * 东西时区
     */
    public TimeZones timeZone = TimeZones.东;

    /**
     * 保留位
     */
    public boolean reservedBits = false;
    /**
     * 语言选择位bit1
     */
    public boolean languageChoose2 = false;
    /**
     * 语言选择位bit0
     */
    public boolean languageChoose1 = false;

    private final byte timeZoneFlag = 1;
    private final byte reservedBitsFlag = 1 << 1;
    private final byte languageChoose2Flag = 1 << 2;
    private final byte languageChoose1Flag = 1 << 3;

    /**
     * 序列化时区语言
     *
     * @return
     */
    public short serialize() {
        short high = (short) (timeZoneTime * 100);
        for (int i = 3; i > 0; i--) {
            if ((high >> (i * 4)) != 0) {
                high = (short) (high << (4 * (3 - i)));
                break;
            }
        }

        highParts = (short) (high >> 4);
        if (timeZone == TimeZones.西) lowParts |= timeZoneFlag;
        if (reservedBits) lowParts |= reservedBitsFlag;
        if (languageChoose2) lowParts |= languageChoose2Flag;
        if (languageChoose1) lowParts |= languageChoose1Flag;

        lowParts = (short) (lowParts << 12);
        data = (short) (highParts | lowParts);

        return data;
    }

    /**
     * 反序列化时区语言
     *
     * @param timeZone
     * @return
     */
    public TimeZoneLanguageModel Deserialize(short timeZone) {
        data = timeZone;
        highParts = (short) ((short) (data << 4) >> 4);
        timeZoneTime = (float) (highParts) / 100F;

        lowParts = (byte) (data >> 12);
        this.timeZone = (lowParts & (short) TimeZones.东.value()) == 0 ? TimeZones.东 : TimeZones.西;
        reservedBits = (lowParts & reservedBitsFlag) >= 1;
        languageChoose2 = (lowParts & languageChoose2Flag) >= 1;
        languageChoose1 = (lowParts & languageChoose1Flag) >= 1;

        return this;
    }

    /**
     * 反序列化时区语言
     *
     * @param timeZone
     * @return
     */
    public TimeZoneLanguageModel Deserialize(byte[] timeZone) {
        data = (short) ((timeZone[0] << 8) + timeZone[1]);
        highParts = (short) ((short) (data << 4) >> 4);
        timeZoneTime = (float) (highParts / 100F);

        lowParts = (byte) (data >> 12);
        this.timeZone = (lowParts & (short) TimeZones.东.value()) == 0 ? TimeZones.东 : TimeZones.西;
        reservedBits = (lowParts & reservedBitsFlag) >= 1;
        languageChoose2 = (lowParts & languageChoose2Flag) >= 1;
        languageChoose1 = (lowParts & languageChoose1Flag) >= 1;

        return this;
    }

    /**
     * 格式化输出
     *
     * @return
     */
    @Override
    public String toString() {

        return String.format("%s%f区,GMT%s%s",
                this.timeZone.toString(),
                timeZoneTime,
                (timeZone == TimeZones.东 ? "+" : "-"),
                TimeZoneLanguageExtensions.FormatTimeZoneLanguageTime(timeZoneTime));
    }

    public static void main(String[] args) {
        TimeZoneLanguageModel tl = new TimeZoneLanguageModel();
        tl.Deserialize((short) 0X3201);


//        TimeZoneLanguageModel tzl = new TimeZoneLanguageModel();
//        tzl.timeZoneTime = 8;
//        tzl.timeZone = TimeZones.东;

        short result = tl.serialize();

        String t = tl.toString();

        System.out.println(1);
    }
}
