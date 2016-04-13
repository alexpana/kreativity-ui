package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple panel that can host other components.
 */
public class KrPanel extends KrWidget {

    public KrPanel() {
        this(new KrAbsoluteLayout());
    }

    public KrPanel(KrLayout layout) {
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrPanel.class));
        setLayout(layout);
    }
}
