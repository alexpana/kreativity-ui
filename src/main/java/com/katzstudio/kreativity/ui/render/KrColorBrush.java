package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import com.katzstudio.kreativity.ui.KrColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A brush that paints a flat color
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class KrColorBrush implements KrBrush {

    @Getter private final Color color;

    public KrColorBrush(int color) {
        this.color = KrColor.rgb(color);
    }

    public KrColorBrush(int color, float alpha) {
        Color rgb = KrColor.rgb(color);
        this.color = new Color(rgb.r, rgb.g, rgb.b, alpha);
    }

    @Override
    public void setOpacity(float opacity) {
        this.color.a = opacity;
    }
}
