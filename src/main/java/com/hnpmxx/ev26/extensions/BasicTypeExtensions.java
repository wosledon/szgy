package com.hnpmxx.ev26.extensions;

public class BasicTypeExtensions {
    public static int short2u(short value) {
        return Math.abs((value & 0XFFFF));
    }

    public static short byte2u(byte value) {
        return (short) Math.abs((value & 0XFF));
    }

    public static long int2u(int value) {
        return Math.abs(value & 0XFFFFFFFFL);
    }

    public static void main(String[] args) {
        short value1 = -1;
        System.out.println(BasicTypeExtensions.short2u(value1));

        byte value2 = -1;
        System.out.println(BasicTypeExtensions.byte2u(value2));

        int value3 = -1;
        System.out.println(BasicTypeExtensions.int2u(value3));
    }
}
