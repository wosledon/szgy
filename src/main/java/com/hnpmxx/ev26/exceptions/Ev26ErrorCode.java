package com.hnpmxx.ev26.exceptions;

public enum Ev26ErrorCode {
    crcVerifyFinal(1001),
    headerParseError(2001),
    bodiesParseError(2002),
    latOrLngError(3001),
    outOfRangeException(4001),
    readerException(4002),
    writerException(4003),
    initException(5001);

    private int value = 0;

    Ev26ErrorCode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
