package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.math.KrRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;

/**
 * Scroll bar component which can be embedded into other components to enable scrolling
 */
public abstract class KrScrollBar extends KrWidget {

    private final List<Listener> listeners = new ArrayList<>();

    @Getter @Setter private float scrollStep = 10;

    @Setter protected Style style;

    @Getter protected float currentValue = 0;

    protected KrRange valueRange;

    protected KrRange thumbRange;

    protected float thumbLength = 20;

    protected float thumbPosition = 0;

    public KrScrollBar() {
        valueRange = new KrRange(0, 100);
    }

    @Override
    public Object getStyle() {
        return style;
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
        thumbPosition = clamp(newPosition, 0, getHeight() - thumbLength);
        updateValueFromPosition();
        notifyScrolled(getCurrentValue());
    }

    private void updateValueFromPosition() {
        currentValue = KrRange.map(thumbPosition, thumbRange, valueRange);
    }

    private void updateThumbLength() {
        float height = getHeight();

        if (height != 0 && (1 + valueRange.length() / height) != 0) {
            thumbLength = height / (1 + valueRange.length() / height);
        } else {
            thumbLength = 0;
        }

        thumbRange = new KrRange(0, getTrackLength() - thumbLength);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        updateThumbLength();
    }

    /**
     * Returns the geometry of the thumb in local space
     */
    protected abstract Rectangle getThumbGeometry();

    protected abstract float getTrackLength();

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
