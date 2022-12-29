package com.hnpmxx.ev26.extensions;

public class StringExtensions {
    public static String trimStart(String value, char c) {
        return value.replaceFirst("^" + c + "*", "");
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null || value == "" || value.equals("") || value.equals(null)) {
            return true;
        }
        return false;
    }
}
