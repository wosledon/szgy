package com.hnpmxx.ev26.enums;

@SuppressWarnings("NonAsciiCharacters")
public enum PackageType0x17 {
    终端地址请求包(41),
    服务器地址请求包中文回复(39);

    private int value = 0;
    PackageType0x17(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
