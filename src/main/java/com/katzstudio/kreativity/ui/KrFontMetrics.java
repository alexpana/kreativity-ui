package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link KrFontMetrics} class offers detailed information about the size, position and offset
 * of the glyphs and text rendered with a specific font.
 */
@RequiredArgsConstructor
public class KrFontMetrics {

    private final static Map<BitmapFont, KrFontMetrics> CACHE = new HashMap<>();

    private final BitmapFont bitmapFont;

    public static KrFontMetrics metrics(BitmapFont bitmapFont) {
        if (!CACHE.containsKey(bitmapFont)) {
            CACHE.put(bitmapFont, new KrFontMetrics(bitmapFont));
        }
        return CACHE.get(bitmapFont);
    }

    public Rectangle bounds(String text) {
        BitmapFont.TextBounds bounds = bitmapFont.getBounds(text);
        return new Rectangle(0, 0, bounds.width, bounds.height);
    }

    public float lineHeight() {
        return bitmapFont.getLineHeight();
    }

    public float textHeight() {
        // TODO(alex): figure out what libgdx thinks these numbers mean
        return bitmapFont.getCapHeight();
    }
}
