package com.katzstudio.kreativity.ui.backend.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.KrFontMetrics;

/**
 * {@link KrFontMetrics} implementation for the libgdx backend
 */
public class KrGdxFontMetrics extends KrFontMetrics {

    private GlyphLayout layout = new GlyphLayout();

    @Override
    public Rectangle bounds(BitmapFont font, String text, int from, int count, Rectangle bounds) {
        layout.setText(font, text);
        bounds.setWidth(layout.width);
        bounds.setHeight(layout.height);
        return bounds;
    }
}