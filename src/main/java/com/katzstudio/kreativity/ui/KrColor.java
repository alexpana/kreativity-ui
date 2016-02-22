package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;

/**
 * Color utilities
 */
public class KrColor {
    public static final Color TRANSPARENT = new Color(0x00000000);
    
    public static Color rgb(int rgb) {
        return new Color((rgb << 8) + 0xff);
    }

    public static Color rgba(int rgba) {
        return new Color(rgba);
    }
}
