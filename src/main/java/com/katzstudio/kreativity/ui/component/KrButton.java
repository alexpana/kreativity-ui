package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrRectangles;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrButtonStyle;
import com.katzstudio.kreativity.ui.util.KrStrings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A push button component.
 */
public class KrButton extends KrWidget {

    private static final int ICON_TEXT_PADDING = 2;

    enum State {
        NORMAL, HOVERED, ARMED;
    }

    private Vector2 tmpVec = new Vector2();

    private State state = State.NORMAL;

    @Getter @Setter private KrAlignment textAlignment;

    private final List<KrButtonListener> listeners = new ArrayList<>();

    public KrButton(String text) {
        setText(text);
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrButton.class));
        setPadding(new KrPadding(5, 4));
        setTextAlignment(KrAlignment.MIDDLE_CENTER);
        setSize(calculatePreferredSize());
    }

    public void addListener(KrButtonListener listener) {
        listeners.add(listener);
    }

    public void removeListener(KrButtonListener listener) {
        listeners.remove(listener);
    }

    protected void notifyClicked() {
        listeners.forEach(KrButtonListener::clicked);
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return rectangles(getContentSize()).expand(getPadding()).size();
    }

    private Vector2 getContentSize() {
        return getContentSize(new Vector2());
    }

    private Vector2 getContentSize(Vector2 contentSize) {
        KrRectangles rects = rectangles(0, 0);

        boolean hasIcon = hasIcon();
        boolean hasText = hasText();

        if (hasIcon) {
            Vector2 iconSize = getStyle().icon.getSize();
            rects.expand(iconSize);
        }

        Rectangle textBounds = text.getBounds();
        if (hasText) {
            int textWidth = (int) text.getBounds().getWidth();
            int textHeight = (int) textBounds.getHeight();
            rects.minHeight(textHeight);

            if (hasIcon) {
                rects.expand(ICON_TEXT_PADDING, 0);
            }

            rects.expand(textWidth, 0);
        }

        return rects.size(contentSize);
    }

    private boolean hasText() {
        return !KrStrings.isNullOrEmpty(text.getString());
    }

    private boolean hasIcon() {
        return getStyle().icon != null;
    }

    @Override
    protected void mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);
        setState(State.ARMED);
        event.accept();
    }

    @Override
    protected void mouseReleasedEvent(KrMouseEvent event) {
        super.mouseReleasedEvent(event);
        if (state == State.ARMED) {
            setState(State.HOVERED);
            notifyClicked();
            event.accept();
        }
    }

    @Override
    protected void enterEvent(KrEnterEvent event) {
        super.enterEvent(event);
        setState(State.HOVERED);
        event.accept();
    }

    @Override
    protected void exitEvent(KrExitEvent event) {
        super.exitEvent(event);
        setState(State.NORMAL);
        event.accept();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        Drawable background = getBackgroundForState(state);

        renderer.setBrush(background);
        renderer.setOpacity(getOpacity());
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Vector2 contentSize = getContentSize(tmpVec);
        Vector2 textPosition = KrAlignmentTool.alignRectangles(0, 0, contentSize.x, contentSize.y, 0, 0, getWidth(), getHeight(), getTextAlignment());
        Vector2 textOffset = state == State.ARMED ? Vector2.Y : Vector2.Zero;
        textPosition.add(textOffset);

        // render icon
        if (hasIcon()) {
            getStyle().icon.draw(renderer, (int) textPosition.x, (int) textPosition.y);
            textPosition.x += ICON_TEXT_PADDING + getStyle().icon.getSize().x;
        }

        // render text
        if (hasText()) {
            textPosition.y += (contentSize.y - text.getBounds().height) / 2;
            renderer.setPen(1, getStyle().foregroundColor);
            renderer.setFont(getStyle().font);
            renderer.drawTextWithShadow(text.getString(), textPosition, getStyle().textShadowOffset, getStyle().textShadowColor);
        }

        Pools.free(textPosition);
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrButton").toString();
    }

    private Drawable getBackgroundForState(State state) {
        KrButtonStyle buttonStyle = ((KrButtonStyle) getStyle());
        switch (state) {
            case NORMAL:
                return buttonStyle.backgroundNormal;
            case HOVERED:
                return buttonStyle.backgroundNormal;
            case ARMED:
                return buttonStyle.backgroundArmed;
        }
        throw new IllegalArgumentException("State not supported: " + state);
    }

    protected void setState(State newState) {
        if (this.state != newState) {
            this.state = newState;
        }
    }

    public interface KrButtonListener {
        void clicked();
    }
}
