package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.component.KrSplitPanel;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrSplitPanel} widgets.
 */
@NoArgsConstructor
public class KrSplitPanelStyle extends KrWidgetStyle {

    public Drawable splitterBackground;

    public Drawable splitterGrip;

    public KrSplitPanelStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrSplitPanelStyle(KrSplitPanelStyle other) {
        super(other);
        this.splitterBackground = other.splitterBackground;
        this.splitterGrip = other.splitterGrip;
    }

    public KrSplitPanelStyle copy() {
        return new KrSplitPanelStyle(this);
    }
}
