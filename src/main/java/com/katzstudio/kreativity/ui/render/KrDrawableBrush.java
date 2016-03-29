package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A brush that paints a {@link Drawable} image
 */
@EqualsAndHashCode()
@AllArgsConstructor
public class KrDrawableBrush implements KrBrush {

    @Getter private final Drawable drawable;

    @Getter private float opacity;

    public KrDrawableBrush(Drawable drawable) {
        this.drawable = drawable;
        this.opacity = 1;
    }

    @Override
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
