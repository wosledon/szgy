package com.hnpmxx.ev26;

import com.hnpmxx.ev26.interfaces.IEv26MessagePackageFormatter;

public abstract class Ev26Bodies<T> implements IEv26MessagePackageFormatter<T> {
    public boolean skipSerialization = false;

    public byte msgId;
    public String description;

    public boolean isSkipSerialization() {
        return skipSerialization;
    }

    public void setSkipSerialization(boolean skipSerialization) {
        this.skipSerialization = skipSerialization;
    }

    public byte getMsgId() {
        return msgId;
    }

    public void setMsgId(byte msgId) {
        this.msgId = msgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
