package com.katzstudio.kreativity.ui.style;

/**
 * Specialized style class for {@link KrSliderStyle} widgets.
 */
public class KrSliderStyle extends KrScrollBarStyle {

    public KrSliderStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrSliderStyle(KrSliderStyle other) {
        super(other);
    }

    public KrSliderStyle copy() {
        return new KrSliderStyle(this);
    }
}
