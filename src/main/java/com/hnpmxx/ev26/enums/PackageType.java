package com.hnpmxx.ev26.enums;

public enum PackageType {
    Type1(1),
    Type2(2);

    private int value = 0;

    PackageType(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}

