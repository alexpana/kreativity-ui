package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.*;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

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
        boolean componentClip = renderer.beginClip(0, 0, getWidth(), getHeight());

        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Vector2 textBounds = getTextBounds();
        Rectangle alignmentReference = rectangles(new Rectangle(0, 0, getWidth(), getHeight())).shrink(getPadding()).value();
        Vector2 textPosition = KrAlignmentTool.alignRectangles(new Rectangle(0, 0, textBounds.x, textBounds.y), alignmentReference, getTextAlignment());
        renderer.setPen(new KrPen(1, style.foregroundColor));
        renderer.setFont(style.font);
        renderer.drawText(text, textPosition);

        if (componentClip) {
            renderer.endClip();
        }
    }

    protected Vector2 getTextBounds() {
        KrFontMetrics metrics = getDefaultToolkit().fontMetrics();
        return metrics.bounds(style.font, text).getSize(new Vector2());
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
