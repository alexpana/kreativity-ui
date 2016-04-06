package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrCheckbox} widgets.
 */
@NoArgsConstructor
public class KrCheckboxStyle extends KrWidgetStyle {

    public Drawable checkboxBackground;

    public Drawable mark;

    public KrCheckboxStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrCheckboxStyle(KrCheckboxStyle other) {
        super(other);
        this.checkboxBackground = other.checkboxBackground;
        this.mark = other.mark;
    }

    @Override
    public KrCheckboxStyle copy() {
        return new KrCheckboxStyle(this);
    }
}
