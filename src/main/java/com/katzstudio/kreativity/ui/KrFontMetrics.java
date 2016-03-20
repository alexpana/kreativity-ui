package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

    public Rectangle bounds(BitmapFont font, String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        return new Rectangle(0, 0, layout.width, layout.height);
    }
}
