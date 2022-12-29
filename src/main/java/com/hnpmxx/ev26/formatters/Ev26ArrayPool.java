package com.hnpmxx.ev26.formatters;

public class Ev26ArrayPool {
    private static byte[] arrayPool;

    public Ev26ArrayPool() {
        arrayPool = new byte[4096];
    }

    public static byte[] Rent(int minimumLength) {
        return new byte[4096];
    }

    public static void Return(byte[] array, boolean clearArray) {
        if (clearArray) arrayPool = new byte[4096];
    }
}
