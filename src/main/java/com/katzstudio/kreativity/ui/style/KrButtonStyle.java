package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.component.KrButton;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrButton} widgets.
 */
@NoArgsConstructor
public class KrButtonStyle extends KrWidgetStyle {

    public Drawable backgroundNormal;

    public Drawable backgroundHovered;

    public Drawable backgroundArmed;

    public KrButtonStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrButtonStyle(KrButtonStyle other) {
        super(other);
        this.backgroundNormal = other.backgroundNormal;
        this.backgroundHovered = other.backgroundHovered;
        this.backgroundArmed = other.backgroundArmed;
    }

    public KrButtonStyle copy() {
        return new KrButtonStyle(this);
    }
}
