package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A brush that paints a {@link Drawable} image
 */
@RequiredArgsConstructor
public class KrDrawableBrush extends KrBrush {
    @Getter private final Drawable drawable;
}
