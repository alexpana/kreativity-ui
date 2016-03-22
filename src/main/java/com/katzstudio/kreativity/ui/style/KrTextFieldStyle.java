package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.component.KrTextField;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrTextField} widgets.
 */
@NoArgsConstructor
public class KrTextFieldStyle extends KrWidgetStyle {

    public Drawable backgroundNormal;

    public Drawable backgroundHovered;

    public Drawable backgroundFocused;

    public Color caretColor;

    public KrTextFieldStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrTextFieldStyle(KrTextFieldStyle other) {
        super(other);
        backgroundNormal = other.backgroundNormal;
        backgroundHovered = other.backgroundHovered;
        backgroundFocused = other.backgroundFocused;
        caretColor = other.caretColor;
    }

    public KrTextFieldStyle copy() {
        return new KrTextFieldStyle(this);
    }
}
