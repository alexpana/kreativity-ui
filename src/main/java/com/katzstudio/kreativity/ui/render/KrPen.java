package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The {@link KrPen} class is used for describing the width and color
 * of the outlines drawn by the {@link KrRenderer}.
 * <p>
 * It's also used for specifying the text color.
 */
@Builder(toBuilder = true)
@AllArgsConstructor
public class KrPen {

    @Getter private final float width;

    @Getter private final Color color;
}
