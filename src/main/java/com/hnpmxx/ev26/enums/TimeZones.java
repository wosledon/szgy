package com.hnpmxx.ev26.enums;

@SuppressWarnings("NonAsciiCharacters")
public enum TimeZones {
    东(0),
    西(1);

    private int value = 0;
    TimeZones(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
