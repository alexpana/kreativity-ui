package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.component.KrComboBox;

/**
 * Specialized style class for {@link KrComboBox} widget.
 */
public class KrComboBoxStyle extends KrWidgetStyle {

    public Drawable pressedBackground;

    public KrComboBoxStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrComboBoxStyle(KrComboBoxStyle other) {
        super(other);
        this.pressedBackground = other.pressedBackground;
    }

    public KrComboBoxStyle copy() {
        return new KrComboBoxStyle(this);
    }
}
