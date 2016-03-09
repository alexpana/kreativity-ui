package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;

/**
 * A vertical scroll bar.
 */
public class KrVerticalScrollBar extends KrScrollBar {

    private boolean isDragging = false;

    public KrVerticalScrollBar() {
        style = KrSkin.instance().getVerticalScrollBarStyle();
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getVerticalScrollBarStyle()) {
            style = style.copy();
        }
    }

    @Override
    protected Rectangle getThumbGeometry() {
        return new Rectangle(0, thumbPosition, getWidth(), thumbLength);
    }

    @Override
    protected float getTrackLength() {
        return getHeight();
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);

        Vector2 localMouseLocation = screenToLocal(event.getScreenPosition());
        if (getThumbGeometry().contains(localMouseLocation)) {
            isDragging = true;
            event.accept();
            return true;
        }

        return false;
    }

    @Override
    protected boolean mouseMoveEvent(KrMouseEvent event) {
        super.mouseMoveEvent(event);
        if (isDragging) {
            setThumbPosition(thumbPosition + event.getDeltaMove().y);
            event.accept();
            return true;
        }
        return false;
    }

    @Override
    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        super.mouseReleasedEvent(event);
        isDragging = false;
        event.accept();
        return true;
    }

    @Override
    protected boolean scrollEvent(KrScrollEvent event) {
        super.scrollEvent(event);

        if (!isDragging) {
            setCurrentValue(getCurrentValue() + getScrollStep() * event.getScrollAmount());
        }

        event.accept();
        return true;
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(new KrDrawableBrush(style.trackDrawable));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        renderer.setBrush(new KrDrawableBrush(style.thumbDrawable));
        renderer.fillRect(getThumbGeometry());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(style.size, 0);
    }
}
