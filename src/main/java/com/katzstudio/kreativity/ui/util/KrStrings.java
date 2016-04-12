package com.katzstudio.kreativity.ui.util;

/**
 * Utilities functions for strings.
 */
public class KrStrings {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static String toString(Object object) {
        return object != null ? object.toString() : "";
    }
}
