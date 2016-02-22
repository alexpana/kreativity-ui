package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import lombok.RequiredArgsConstructor;

/**
 * The {@link FontMetrics} class offers detailed information about the size, position and offset
 * of the glyphs and text rendered with a specific font.
 */
@RequiredArgsConstructor
public class FontMetrics {

    private final BitmapFont bitmapFont;

    public static FontMetrics metrics(BitmapFont bitmapFont) {
        return new FontMetrics(bitmapFont);
    }

    public Rectangle bounds(String text) {
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(text);
        return new Rectangle(0, 0, bounds.width, bounds.height);
    }
}
