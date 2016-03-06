package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrFontMetrics.metrics;
import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * A simple label component
 */
public class KrLabel extends KrWidget {

    @Getter @Setter private String text;

    @Setter private Style style;

    public KrLabel(String text) {
        setStyle(KrSkin.instance().getLabelStyle());
        this.text = text;
        setSize(calculatePreferredSize());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        KrFontMetrics metrics = metrics(style.font);
        float textWidth = metrics.bounds(text).width;
        float textHeight = metrics.textHeight();
        return rectangles(textWidth, textHeight).expand(getPadding()).size();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        boolean componentClip = renderer.beginClip(getX(), getY(), getWidth(), getHeight());

        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(getX(), getY(), getWidth(), getHeight());

        style.font.setColor(style.foregroundColor);

        renderer.setFont(style.font);
        renderer.setPen(new KrPen(1, style.foregroundColor));

        renderer.drawText(text, getX() + getPadding().left, getY() + getPadding().top);

        if (componentClip) {
            renderer.endClip();
        }
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrLabel").toString();
    }

    @AllArgsConstructor
    public static final class Style {

        public Drawable background;

        public BitmapFont font;

        public Color foregroundColor;
    }
}
