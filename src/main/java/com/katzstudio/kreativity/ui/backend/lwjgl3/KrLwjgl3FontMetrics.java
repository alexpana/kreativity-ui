package com.katzstudio.kreativity.ui.backend.lwjgl3;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.KrFontMetrics;

/**
 * {@link KrFontMetrics} implementation for the libgdx lwjgl3 backend
 */
public class KrLwjgl3FontMetrics implements KrFontMetrics {

    @Override
    public Rectangle bounds(BitmapFont font, String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        return new Rectangle(0, 0, layout.width, layout.height);
    }
}