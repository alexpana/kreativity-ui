package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrOrientation;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.math.KrRange;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrScrollBarStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * Scroll bar component which can be embedded into other components to enable scrolling
 */
public class KrScrollBar extends KrWidget<KrScrollBarStyle> {

    private final List<Listener> listeners = new ArrayList<>();

    @Getter private final KrOrientation orientation;

    @Getter @Setter private float scrollStep = 10;

    @Getter protected float currentValue = 0;

    @Getter protected KrRange valueRange = new KrRange(0, 0);

    protected KrRange thumbRange = new KrRange(0, 0);

    protected float thumbLength = 20;

    protected float thumbPosition = 0;

    private boolean isDragging = false;

    /**
     * The drag position is allowed to go over the thumb position threshold. It is used to prevent the thumb from
     * moving while the mouse is outside the geometry of the scrollbar.
     */
    private float dragPosition;

    public KrScrollBar(KrOrientation orientation) {
        this.orientation = orientation;

        style = getDefaultStyle();

        valueRange = new KrRange(0, 100);
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultStyle()) {
            style = new KrScrollBarStyle(style);
        }
    }

    /**
     * Returns the geometry of the thumb in local space
     */
    protected Rectangle getThumbGeometry(Rectangle thumbGeometry) {
        if (orientation == VERTICAL) {
            thumbGeometry.set(0, thumbPosition, getWidth(), thumbLength);
        } else {
            thumbGeometry.set(thumbPosition, 0, thumbLength, getHeight());
        }
        return thumbGeometry;
    }

    protected float getTrackLength() {
        return orientation == VERTICAL ? getHeight() : getWidth();
    }

    private KrScrollBarStyle getDefaultStyle() {
        KrSkin skin = getDefaultToolkit().getSkin();
        return orientation == VERTICAL ? skin.getVerticalScrollBarStyle() : skin.getHorizontalScrollBarStyle();
    }

    public void setValue(float newValue) {
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
            thumbLength = (int) (trackLength / (1 + valueRange.length() / trackLength));
        } else {
            thumbLength = 0;
        }

        thumbRange = new KrRange(0, getTrackLength() - thumbLength);
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);

        Vector2 localMouseLocation = screenToLocal(event.getScreenPosition());

        if (!getThumbGeometry(tmpRect).contains(localMouseLocation)) {
            float positionOnTrack = orientation == VERTICAL ? localMouseLocation.y : localMouseLocation.x;
            dragPosition = positionOnTrack - thumbLength / 2;
            setThumbPosition(dragPosition);
        } else {
            dragPosition = thumbPosition;
        }

        isDragging = true;
        event.accept();
        return true;
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
            setValue(getCurrentValue() + getScrollStep() * event.getScrollAmount());
        }

        event.accept();
        return true;
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(style.track);
        renderer.fillRect(0, 0, getWidth(), getHeight());

        renderer.setBrush(style.thumb);
        renderer.fillRect(getThumbGeometry(tmpRect));
    }

    @Override
    public Vector2 calculatePreferredSize() {
        switch (orientation) {
            case VERTICAL:
                return new Vector2(style.size, 100);
            case HORIZONTAL:
                return new Vector2(100, style.size);
            default:
                return new Vector2(0, 0);
        }
    }

    @Override
    public void setGeometry(float x, float y, float width, float height) {
        super.setGeometry(x, y, width, height);
        updateThumbLength();
    }

    public void addScrollListener(Listener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
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

    public void setValueRange(float min, float max) {
        setValueRange(new KrRange(min, max));
    }

    public void setValueRange(KrRange valueRange) {
        this.valueRange = valueRange;
        updateThumbLength();
        updateValueFromPosition();
    }

    public interface Listener {
        void scrolled(float newScrollValue);
    }

}
