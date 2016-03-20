package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.katzstudio.kreativity.ui.KrFontMetrics.metrics;

/**
 * A push button component.
 */
public class KrButton extends KrWidget {

    protected enum State {
        NORMAL, HOVERED, ARMED
    }

    protected State state = State.NORMAL;

    @Setter private Style style;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private KrAlignment textAlignment;

    private final List<Listener> listeners = Lists.newArrayList();

    public KrButton(String text) {
        this.text = text;
        setStyle(KrSkin.instance().getButtonStyle());
        setPadding(new KrPadding(5, 4));
        setTextAlignment(KrAlignment.MIDDLE_CENTER);
        setSize(calculatePreferredSize());
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getButtonStyle()) {
            style = style.copy();
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    protected void notifyClicked() {
        listeners.forEach(Listener::clicked);
    }

    @Override
    public Vector2 calculatePreferredSize() {
        Rectangle textBounds = metrics(style.font).bounds(text);
        return expandSizeWithPadding(new Vector2(textBounds.width, textBounds.height), getPadding());
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);
        setState(State.ARMED);
        return true;
    }

    @Override
    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        super.mouseReleasedEvent(event);
        if (state == State.ARMED) {
            setState(State.HOVERED);
            notifyClicked();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean enterEvent(KrEnterEvent event) {
        super.enterEvent(event);
        setState(State.HOVERED);
        return true;
    }

    @Override
    protected boolean exitEvent(KrExitEvent event) {
        super.exitEvent(event);
        setState(State.NORMAL);
        return true;
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        Drawable background = getBackgroundForState(state);

        renderer.setBrush(new KrDrawableBrush(background));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Rectangle textBounds = metrics(style.font).bounds(getText());
        float textWidth = textBounds.width;
        float textHeight = textBounds.height;
        Vector2 textPosition = KrAlignmentTool.alignRectangles(new Rectangle(0, 0, textWidth, textHeight),
                new Rectangle(0, 0, getWidth(), getHeight()), getTextAlignment());
        Vector2 textOffset = state == State.ARMED ? Vector2.Y : Vector2.Zero;
        textPosition.add(textOffset);

        renderer.setPen(new KrPen(1, style.foregroundColor));
        renderer.setFont(style.font);
        renderer.drawTextWithShadow(text, textPosition, style.textShadowOffset, style.textShadowColor);
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrButton").toString();
    }

    private Drawable getBackgroundForState(State state) {
        switch (state) {
            case NORMAL:
                return style.backgroundNormal;
            case HOVERED:
                return style.backgroundHovered;
            case ARMED:
                return style.backgroundArmed;
        }
        throw new IllegalArgumentException("State not supported: " + state);
    }

    protected void setState(State newState) {
        if (this.state != newState) {
            this.state = newState;
        }
    }

    public interface Listener {
        void clicked();
    }

    @AllArgsConstructor
    public static class Style {

        public Drawable backgroundNormal;

        public Drawable backgroundHovered;

        public Drawable backgroundArmed;

        public BitmapFont font;

        public Color foregroundColor;

        public Vector2 textShadowOffset;

        public Color textShadowColor;

        // TODO: icon

        public Style copy() {
            return new Style(backgroundNormal, backgroundHovered, backgroundArmed, font, foregroundColor, textShadowOffset, textShadowColor);
        }
    }

}
