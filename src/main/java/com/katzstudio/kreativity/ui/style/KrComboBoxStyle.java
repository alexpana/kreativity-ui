package com.katzstudio.kreativity.ui.style;

import com.katzstudio.kreativity.ui.component.KrComboBox;

/**
 * Specialized style class for {@link KrComboBox} widget.
 */
public class KrComboBoxStyle extends KrWidgetStyle {

    public KrComboBoxStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrComboBoxStyle(KrComboBoxStyle other) {
    }

    public KrComboBoxStyle copy() {
        return new KrComboBoxStyle(this);
    }
}
