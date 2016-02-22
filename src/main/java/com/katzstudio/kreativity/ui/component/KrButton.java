package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.AlignmentTool;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrRenderer;
import com.katzstudio.kreativity.ui.KreativitySkin;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A push button component.
 */
public class KrButton extends KrWidget {

    private enum State {
        NORMAL, HOVERED, ARMED
    }

    private State state = State.NORMAL;

    @Getter @Setter private Style style;

    @Getter @Setter private String text;

    @Getter @Setter private KrAlignment textAlignment;

    private final List<Listener> listeners = Lists.newArrayList();

    public KrButton(String text) {
        this.text = text;
        setStyle(KreativitySkin.instance().getButtonStyle());
        setPadding(new KrPadding(4, 4, 5, 5));
        setTextAlignment(KrAlignment.MIDDLE_CENTER);
        setSize(getSelfPreferredSize());
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyClicked() {
        listeners.forEach(Listener::clicked);
    }

    @Override
    public Vector2 getSelfPreferredSize() {
        BitmapFont.TextBounds bounds = style.font.getBounds(text);
        return expandSizeWithPadding(new Vector2(bounds.width, bounds.height), getPadding());
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
        setState(State.HOVERED);
        notifyClicked();
        return true;
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
        renderer.renderDrawable(background, getX(), getY(), getWidth(), getHeight());

        renderer.setForeground(style.foregroundColor);
        renderer.setFont(style.font);
        renderer.setTextShadow(style.textShadowOffset, style.textShadowColor);

        BitmapFont.TextBounds bounds = style.font.getBounds(getText());
        Vector2 textPosition = AlignmentTool.alignRectangles(new Rectangle(0, 0, bounds.width, bounds.height), getGeometry(), getTextAlignment());

        Vector2 textOffset = state == State.ARMED ? new Vector2(0, 1) : Vector2.Zero;

        renderer.renderText(text, textPosition.x + textOffset.x, textPosition.y + textOffset.y);
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

    private void setState(State newState) {
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
    }

}
