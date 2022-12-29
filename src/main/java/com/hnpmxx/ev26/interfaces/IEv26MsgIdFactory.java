package com.hnpmxx.ev26.interfaces;

public interface IEv26MsgIdFactory extends IEv26ExternalRegister {
    Object getValue(byte msgId);

    IEv26MsgIdFactory setMap();
}
