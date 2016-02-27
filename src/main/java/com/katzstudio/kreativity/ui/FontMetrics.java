package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link FontMetrics} class offers detailed information about the size, position and offset
 * of the glyphs and text rendered with a specific font.
 */
@RequiredArgsConstructor
public class FontMetrics {

    private final static Map<BitmapFont, FontMetrics> CACHE = new HashMap<>();

    private final BitmapFont bitmapFont;

    public static FontMetrics metrics(BitmapFont bitmapFont) {
        if (!CACHE.containsKey(bitmapFont)) {
            CACHE.put(bitmapFont, new FontMetrics(bitmapFont));
        }
        return CACHE.get(bitmapFont);
    }

    public Rectangle bounds(String text) {
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(text);
        return new Rectangle(0, 0, bounds.width, bounds.height);
    }
}
