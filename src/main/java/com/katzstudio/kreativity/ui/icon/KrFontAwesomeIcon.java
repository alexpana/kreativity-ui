package com.katzstudio.kreativity.ui.icon;

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

    private static final int RENDER_OFFSET_X = 1;

    private static final int RENDER_OFFSET_Y = -6;

    @Getter private final Vector2 size;

    private final KrFontAwesomeGlyph glyph;

    public KrFontAwesomeIcon(KrFontAwesomeGlyph glyph, int width, int height) {
        this.glyph = glyph;
        this.size = new Vector2(width, height);
    }

    @Override
    public void draw(KrRenderer renderer, int x, int y) {
        renderer.setFont(KrToolkit.getDefaultToolkit().getSkin().getFontAwesome());
        renderer.drawText(glyph.getRepresentation(), x + RENDER_OFFSET_X, y + RENDER_OFFSET_Y);
    }
}
