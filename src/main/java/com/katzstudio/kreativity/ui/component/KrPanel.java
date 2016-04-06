package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple panel that can host other components. It can be styled with a checkboxBackground.
 */
public class KrPanel extends KrWidget {

    public KrPanel() {
        this(new KrAbsoluteLayout());
    }

    public KrPanel(KrLayout layout) {
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrPanel.class));
        setLayout(layout);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(getStyle().background);
        renderer.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return getLayout().getPreferredSize();
    }

}
