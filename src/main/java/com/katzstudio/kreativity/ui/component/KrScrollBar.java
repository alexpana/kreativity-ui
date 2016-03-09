package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.math.KrRange;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.katzstudio.kreativity.ui.component.KrScrollBar.Orientation.VERTICAL;

/**
 * Scroll bar component which can be embedded into other components to enable scrolling
 */
public class KrScrollBar extends KrWidget {

    private final List<Listener> listeners = new ArrayList<>();

    @Getter private final Orientation orientation;

    @Getter @Setter private float scrollStep = 10;

    @Setter protected Style style;

    @Getter protected float currentValue = 0;

    protected KrRange valueRange;

    protected KrRange thumbRange;

    protected float thumbLength = 20;

    protected float thumbPosition = 0;

    private boolean isDragging = false;

    /**
     * The drag position is allowed to go over the thumb position threshold. It is used to prevent the thumb from
     * moving while the mouse is outside the bounds of the scrollbar.
     */
    private float dragPosition;

    public KrScrollBar(Orientation orientation) {
        this.orientation = orientation;

        style = getDefaultStyle();

        valueRange = new KrRange(0, 100);
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultStyle()) {
            style = style.copy();
        }
    }

    /**
     * Returns the geometry of the thumb in local space
     */
    protected Rectangle getThumbGeometry() {
        return orientation == VERTICAL ?
                new Rectangle(0, thumbPosition, getWidth(), thumbLength) :
                new Rectangle(thumbPosition, 0, thumbLength, getHeight());

    }

    protected float getTrackLength() {
        return orientation == VERTICAL ? getHeight() : getWidth();
    }

    private Style getDefaultStyle() {
        KrSkin skin = KrSkin.instance();
        return orientation == VERTICAL ? skin.getVerticalScrollBarStyle() : skin.getHorizontalScrollBarStyle();
    }

    public void setCurrentValue(float newValue) {
        currentValue = valueRange.clamp(newValue);
        updatePositionFromValue();
        notifyScrolled(getCurrentValue());
    }

    private void updatePositionFromValue() {
        thumbPosition = KrRange.map(currentValue, valueRange, thumbRange);
    }

    protected void setThumbPosition(float newPosition) {
        float max = (orientation == VERTICAL ? getHeight() : getWidth()) - thumbLength;
        thumbPosition = clamp(newPosition, 0, max);
        updateValueFromPosition();
        notifyScrolled(getCurrentValue());
    }

    private void updateValueFromPosition() {
        currentValue = KrRange.map(thumbPosition, thumbRange, valueRange);
    }

    private void updateThumbLength() {
        float trackLength = getTrackLength();

        if (trackLength != 0 && (1 + valueRange.length() / trackLength) != 0) {
            thumbLength = trackLength / (1 + valueRange.length() / trackLength);
        } else {
            thumbLength = 0;
        }

        thumbRange = new KrRange(0, getTrackLength() - thumbLength);
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
            float delta = orientation == VERTICAL ? event.getDeltaMove().y : event.getDeltaMove().x;
            dragPosition = dragPosition + delta;
            setThumbPosition(dragPosition);
            event.accept();
            return true;
        }
        return false;
    }

    @Override
    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        super.mouseReleasedEvent(event);
        isDragging = false;
        dragPosition = thumbPosition;
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
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        updateThumbLength();
    }

    private boolean isOverThumb(float x, float y) {
        return getThumbGeometry().contains(x, y);
    }

    public void setMaxValue(float maxValue) {
        valueRange = new KrRange(valueRange.getMin(), maxValue);
        updateThumbLength();
        updateValueFromPosition();
    }

    public void setMinValue(float minValue) {
        valueRange = new KrRange(minValue, valueRange.getMax());
        updateThumbLength();
        updateValueFromPosition();
    }

    public void addScrollListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeScrollListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyScrolled(float newScrollValue) {
        listeners.stream().forEach(listener -> listener.scrolled(newScrollValue));
    }

    protected static float mapRange(float value, float min, float max, float newMin, float newMax) {
        if (min == max) {
            return newMin;
        }

        return newMin + (newMax - newMin) * (value - min) / (max - min);
    }

    public interface Listener {
        void scrolled(float newScrollValue);
    }

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    @AllArgsConstructor
    public static class Style {
        public Drawable trackDrawable;

        public Drawable thumbDrawable;

        public float size;

        public KrPadding padding;

        public Style copy() {
            return new Style(trackDrawable, thumbDrawable, size, padding);
        }
    }
}
