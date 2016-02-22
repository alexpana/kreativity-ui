package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrRenderer;
import com.katzstudio.kreativity.ui.KreativitySkin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple panel that can host other components. It can be styled with a background.
 */
public class KrPanel extends KrWidget {
    @Getter @Setter private Style style;

    public KrPanel() {
        setStyle(KreativitySkin.instance().getPanelStyle());
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.renderDrawable(getStyle().background, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public Vector2 getSelfPreferredSize() {
        return getLayout().getPreferredSize();
    }

    @AllArgsConstructor
    public static final class Style {
        public Drawable background;
    }
}
