package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * A simple panel that can host other components. It can be styled with a background.
 */
public class KrPanel extends KrWidget {
    @Setter private Style style;

    public KrPanel() {
        setStyle(KrSkin.instance().getPanelStyle());
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getPanelStyle()) {
            style = style.copy();
        }
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return getLayout().getPreferredSize();
    }

    @AllArgsConstructor
    public static final class Style {
        public Drawable background;

        public Style copy() {
            return new Style(background);
        }
    }
}
