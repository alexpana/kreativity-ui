package com.katzstudio.kreativity.ui.style;

import com.katzstudio.kreativity.ui.component.KrButtonGroup;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrButtonGroup} widgets.
 */
@NoArgsConstructor
public class KrButtonGroupStyle extends KrWidgetStyle {

    public KrButtonStyle singleButtonStyle;

    public KrButtonStyle firstButtonStyle;

    public KrButtonStyle middleButtonStyle;

    public KrButtonStyle lastButtonStyle;

    public KrButtonGroupStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrButtonGroupStyle(KrButtonGroupStyle other) {
        super(other);
        this.singleButtonStyle = other.singleButtonStyle;
        this.firstButtonStyle = other.firstButtonStyle;
        this.middleButtonStyle = other.middleButtonStyle;
        this.lastButtonStyle = other.lastButtonStyle;
    }

    public KrButtonGroupStyle copy() {
        return new KrButtonGroupStyle(this);
    }
}
