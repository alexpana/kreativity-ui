package com.katzstudio.kreativity.ui.icon;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;

/**
 * {@link KrIcon} implementation that uses the FontAwesome
 * iconic font to render the icon.
 */
public class KrFontAwesomeIcon implements KrIcon {

    private static final int RENDER_OFFSET_X = 2;

    private static final int RENDER_OFFSET_Y = -2;

    @Getter private final Vector2 size;

    private final KrFontAwesomeGlyph glyph;

    public KrFontAwesomeIcon(KrFontAwesomeGlyph glyph) {
        this.glyph = glyph;
        this.size = new Vector2(14, 14);
    }

    @Override
    public void draw(KrRenderer renderer, int x, int y) {
        BitmapFont originalFont = renderer.getFont();
        renderer.setFont(KrToolkit.getDefaultToolkit().getSkin().getFontAwesome());
        renderer.drawText(glyph.getRepresentation(), x + RENDER_OFFSET_X, y + RENDER_OFFSET_Y);
        renderer.setFont(originalFont);
    }
}
