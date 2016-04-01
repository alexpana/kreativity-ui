package com.katzstudio.kreativity.ui.backend.lwjgl3;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.KrFontMetrics;

/**
 * {@link KrFontMetrics} implementation for the libgdx lwjgl3 backend
 */
public class KrLwjgl3FontMetrics implements KrFontMetrics {

    private GlyphLayout layout = new GlyphLayout();

    @Override
    public Rectangle bounds(BitmapFont font, String text) {
        layout.setText(font, text);
        return new Rectangle(0, 0, layout.width, layout.height);
    }

    public void getBounds(BitmapFont font, String text, Rectangle bounds) {
        layout.setText(font, text);
        bounds.setWidth(layout.width);
        bounds.setHeight(layout.height);
    }
}