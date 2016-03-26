package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrPanelStyle;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple panel that can host other components. It can be styled with a checkboxBackground.
 */
public class KrPanel extends KrWidget<KrPanelStyle> {

    public KrPanel() {
        this(new KrAbsoluteLayout());
    }

    public KrPanel(KrLayout layout) {
        setStyle(getDefaultToolkit().getSkin().getPanelStyle());
        setLayout(layout);
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getPanelStyle()) {
            style = new KrPanelStyle(style);
        }
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return getLayout().getPreferredSize();
    }

}
