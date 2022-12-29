package com.hnpmxx.ev26.interfaces;

import com.hnpmxx.ev26.Ev26Bodies;
import com.hnpmxx.ev26.enums.PackageType;

public abstract class IEv26Header {
    /**
     * 消息Id
     */
    public byte msgId = 0;
    /**
     * 包长度
     */
    public short length = 0;
    /**
     * 消息体
     */
    public Ev26Bodies bodies = null;
    /**
     * 消息流水号
     */
    public short msgNum = 0;
    /**
     * crc校验
     */
    public short crc = 0;

    /**
     * 协议类型
     */
    public PackageType packageType = PackageType.Type1;
}
