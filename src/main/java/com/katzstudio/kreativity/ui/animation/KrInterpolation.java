package com.katzstudio.kreativity.ui.animation;

/**
 * Utility class for interpolating values of various types.
 */
public class KrInterpolation {
    public static float interpolate(float value, float min, float max) {
        if (value <= 0) {
            return min;
        }

        if (value >= 1) {
            return max;
        }

        return min + (max - min) * value;
    }
}
