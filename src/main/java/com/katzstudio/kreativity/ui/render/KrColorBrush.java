package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A brush that paints a flat color
 */
@RequiredArgsConstructor
public class KrColorBrush extends KrBrush {
    @Getter private final Color color;
}
