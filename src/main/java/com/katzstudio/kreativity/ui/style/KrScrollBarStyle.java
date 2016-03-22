package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.component.KrScrollBar;
import lombok.NoArgsConstructor;

/**
 * Specialized style class for {@link KrScrollBar} widgets.
 */
@NoArgsConstructor
public class KrScrollBarStyle extends KrWidgetStyle {

    public Drawable track;

    public Drawable thumb;

    public float size;

    public KrCursor splitterCursor;

    public KrScrollBarStyle(KrWidgetStyle other) {
        super(other);
    }

    public KrScrollBarStyle(KrScrollBarStyle other) {
        super(other);
        this.track = other.track;
        this.thumb = other.thumb;
        this.size = other.size;
        this.splitterCursor = other.splitterCursor;
    }

    public KrScrollBarStyle copy() {
        return new KrScrollBarStyle(this);
    }
}
