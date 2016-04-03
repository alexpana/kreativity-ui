package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

/**
 * The {@link KrFontMetrics} class offers detailed information about the size, position and offset
 * of the glyphs and text rendered with a specific font.
 */
public abstract class KrFontMetrics {

    public Rectangle bounds(BitmapFont font, String text) {
        return bounds(font, text, 0, text.length(), new Rectangle());
    }

    public Rectangle bounds(BitmapFont font, String text, int from, int count) {
        return bounds(font, text, from, count, new Rectangle());
    }

    public Rectangle bounds(BitmapFont font, String text, Rectangle bounds) {
        return bounds(font, text, 0, text.length(), bounds);
    }

    public abstract Rectangle bounds(BitmapFont font, String text, int from, int to, Rectangle bounds);
}
