package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrLabelStyle;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple label component
 */
public class KrLabel extends KrWidget<KrLabelStyle> {

    @Setter @Getter private KrAlignment textAlignment;

    public KrLabel(String text) {
        setText(text);
        this.textAlignment = KrAlignment.MIDDLE_LEFT;

        setStyle(getDefaultToolkit().getSkin().getLabelStyle());
        setPadding(new KrPadding(3, 3));
        setSize(calculatePreferredSize());
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getLabelStyle()) {
            style = new KrLabelStyle(style);
        }
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return rectangles(text.getBounds()).expand(getPadding()).size();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        boolean componentClip = renderer.beginClip(0, 0, getWidth(), getHeight());

        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Rectangle textBounds = text.getBounds();
        Rectangle alignmentReference = rectangles(new Rectangle(0, 0, getWidth(), getHeight())).shrink(getPadding()).value();
        Vector2 textPosition = KrAlignmentTool.alignRectangles(textBounds, alignmentReference, getTextAlignment());
        renderer.setPen(new KrPen(1, style.foregroundColor));
        renderer.setFont(style.font);
        renderer.drawText(text.getString(), textPosition);

        if (componentClip) {
            renderer.endClip();
        }
    }

    @Override
    public KrLabelStyle getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrLabel").toString();
    }

}
