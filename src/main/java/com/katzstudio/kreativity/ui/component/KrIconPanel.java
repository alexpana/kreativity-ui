package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;
import com.katzstudio.kreativity.ui.KreativitySkin;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;
import lombok.Setter;

/**
 * A transparent panel that renders an icon.
 */
public class KrIconPanel extends KrWidget {

    @Getter @Setter private KrFontAwesomeGlyph iconGlyph;

    public KrIconPanel(KrFontAwesomeGlyph iconGlyph) {
        this.iconGlyph = iconGlyph;
        setSize(getSelfPreferredSize());
    }

    @Override
    public Vector2 getSelfPreferredSize() {
        final BitmapFont.TextBounds bounds = KreativitySkin.instance().getFontAwesome().getBounds(iconGlyph.getRepresentation());
        return expandSizeWithPadding(new Vector2(bounds.width, bounds.height), getPadding());
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        final BitmapFont.TextBounds bounds = KreativitySkin.instance().getFontAwesome().getBounds(iconGlyph.getRepresentation());
        renderer.setFont(KreativitySkin.instance().getFontAwesome());
        renderer.drawText(iconGlyph.getRepresentation(), getX() + (getWidth() - bounds.width) / 2, getY() + getHeight() + (getHeight() - bounds.height) / 2);
    }
}
