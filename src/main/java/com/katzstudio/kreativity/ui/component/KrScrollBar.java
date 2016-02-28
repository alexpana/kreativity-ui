package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.BACKGROUND_DARK;

/**
 * Scroll bar component which can be embedded into other components to enable scrolling
 */
public class KrScrollBar extends Widget {

    private float sliderSize = 20;

    @Getter
    private float minValue = 0;

    @Getter
    private float maxValue = 100;

    @Getter
    private float currentValue = 0;

    private float sliderPosition = 0;

    private final List<Listener> listeners = Lists.newArrayList();

    @Getter
    @Setter
    private float scrollAmount = 10;

    public KrScrollBar() {
        setTouchable(Touchable.enabled);

        ClickListener inputListener = new ClickListener() {
            private boolean dragging;
            private float dragBeginY;
            private float beginDragSliderPosition;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isOverThumb(x, y)) {
                    dragBeginY = y;
                    beginDragSliderPosition = sliderPosition;
                    dragging = true;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragging) {
                    float moveDelta = (dragBeginY - y);
                    setSliderPosition(beginDragSliderPosition + moveDelta);
                }
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                setCurrentValue(currentValue + scrollAmount * amount);
                return true;
            }
        };

        addListener(inputListener);
    }

    public void setCurrentValue(float newValue) {
        currentValue = clamp(newValue, minValue, maxValue);
        updatePositionFromValue();

        notifyScrolled(getCurrentValue());
    }

    private void setSliderPosition(float newPosition) {
        sliderPosition = clamp(newPosition, 0, getHeight() - sliderSize);
        updateValueFromPosition();

        notifyScrolled(getCurrentValue());
    }

    private void updateValueFromPosition() {
        currentValue = mapRange(sliderPosition, 0, getHeight() - sliderSize, minValue, maxValue);
    }

    private void updatePositionFromValue() {
        sliderPosition = mapRange(currentValue, minValue, maxValue, 0, getHeight() - sliderSize);
    }

    public void updateSliderSize() {
        if (getHeight() != 0 && (1 + (maxValue - minValue) / getHeight()) != 0) {
            sliderSize = this.getHeight() / (1 + (maxValue - minValue) / getHeight());
        } else {
            sliderSize = 0;
        }
    }

    private boolean isOverThumb(float x, float y) {
        Rectangle rectangle = new Rectangle(0, getHeight() - sliderPosition - sliderSize, getWidth(), sliderSize);
        return rectangle.contains(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // draw slider
        Drawable slider = KrToolkit.createColorDrawable(KrSkin.getColor(BACKGROUND_DARK));
        slider.draw(batch, getX(), getY() + getHeight() - sliderPosition - sliderSize, getWidth(), sliderSize);
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        updateSliderSize();
        updateValueFromPosition();
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        updateSliderSize();
        updateValueFromPosition();
    }

    @Override
    protected void sizeChanged() {
        updateSliderSize();
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

    public interface Listener {
        void scrolled(float newScrollValue);
    }

    protected static float mapRange(float value, float min, float max, float newMin, float newMax) {
        if (min == max) {
            return newMin;
        }

        return newMin + (newMax - newMin) * (value - min) / (max - min);
    }
}
