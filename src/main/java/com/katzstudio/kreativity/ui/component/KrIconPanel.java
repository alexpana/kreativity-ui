package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A transparent panel that renders an icon.
 */
public class KrIconPanel extends KrWidget<KrWidgetStyle> {

    @Getter @Setter private KrFontAwesomeGlyph iconGlyph;

    private final BitmapFont fontAwesome;

    public KrIconPanel(KrFontAwesomeGlyph iconGlyph) {
        this.iconGlyph = iconGlyph;
        this.fontAwesome = getDefaultToolkit().getSkin().getFontAwesome();
        setStyle(getDefaultToolkit().getSkin().getWidgetStyle());
        setSize(calculatePreferredSize());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        Rectangle bounds = getDefaultToolkit().fontMetrics().bounds(fontAwesome, iconGlyph.getRepresentation(), tmpRect);
        return rectangles(bounds).expand(getPadding()).size();
    }

    @Override
    public void ensureUniqueStyle() {
        // TODO
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        Rectangle bounds = getDefaultToolkit().fontMetrics().bounds(fontAwesome, iconGlyph.getRepresentation(), tmpRect);
        renderer.setFont(getDefaultToolkit().getSkin().getFontAwesome());
        renderer.drawText(iconGlyph.getRepresentation(), getX() + (getWidth() - bounds.width) / 2, getY() + getHeight() + (getHeight() - bounds.height) / 2);
    }
}
