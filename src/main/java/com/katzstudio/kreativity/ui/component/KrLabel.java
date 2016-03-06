package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrPadding;
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

    @Setter @Getter private KrAlignment textAlignment;

    public KrLabel(String text) {
        this.text = text;
        this.textAlignment = KrAlignment.MIDDLE_LEFT;

        setStyle(KrSkin.instance().getLabelStyle());
        setPadding(new KrPadding(3, 3));
        setSize(calculatePreferredSize());
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getLabelStyle()) {
            style = style.copy();
        }
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return rectangles(getTextBounds()).expand(getPadding()).size();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        boolean componentClip = renderer.beginClip(getX(), getY(), getWidth(), getHeight());

        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(getX(), getY(), getWidth(), getHeight());

        Vector2 textBounds = getTextBounds();
        Rectangle alignmentReference = rectangles(getGeometry()).shrink(getPadding()).value();
        Vector2 textPosition = KrAlignmentTool.alignRectangles(new Rectangle(0, 0, textBounds.x, textBounds.y), alignmentReference, getTextAlignment());
        renderer.setPen(new KrPen(1, style.foregroundColor));
        renderer.setFont(style.font);
        renderer.drawText(text, textPosition);

        if (componentClip) {
            renderer.endClip();
        }
    }

    protected Vector2 getTextBounds() {
        float textWidth = metrics(style.font).bounds(text).width;
        float textHeight = metrics(style.font).textHeight();
        return new Vector2(textWidth, textHeight);
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

        Style copy() {
            return new Style(background, font, foregroundColor);
        }
    }
}
