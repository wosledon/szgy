package com.hnpmxx.ev26.extensions;

import java.util.Arrays;

public class BufferExtensions {
    public static byte[] Slice(byte[] buffer, int start, int length) {
        return Arrays.copyOfRange(buffer, start, start + length);
    }

    public static byte[] Slice(byte[] buffer, int start) {
        return Arrays.copyOfRange(buffer, start, buffer.length);
    }

    public static char[] Slice(char[] buffer, int start, int length) {
        return Arrays.copyOfRange(buffer, start, start + length);
    }

    public static char[] Slice(char[] buffer, int start) {
        return Arrays.copyOfRange(buffer, start, buffer.length);
    }
}
