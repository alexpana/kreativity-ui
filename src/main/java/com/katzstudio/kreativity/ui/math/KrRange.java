package com.katzstudio.kreativity.ui.math;

import lombok.Getter;

/**
 * Data structure defining a range
 */
public class KrRange {
    @Getter private final float min;

    @Getter private final float max;

    public KrRange(float from, float to) {
        min = Math.min(from, to);
        max = Math.max(from, to);
    }

    public float length() {
        return max - min;
    }

    public float clamp(float value) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public static float map(float sourceValue, KrRange sourceRange, KrRange targetRange) {
        if (sourceRange.length() == 0) {
            return targetRange.min;
        }

        return targetRange.min + (targetRange.length()) * (sourceValue - sourceRange.min) / sourceRange.length();
    }
}
