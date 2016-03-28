package com.katzstudio.kreativity.ui.animation;

import com.katzstudio.kreativity.ui.animation.easing.KrAnimationEasing.KrEaseFunction;
import com.katzstudio.kreativity.ui.util.KrUpdateListener;

/**
 */
public abstract class KrAnimation implements KrUpdateListener {

    private final float duration;

    protected KrEaseFunction easing;

    float currentTime;

    float currentValue;

    boolean isFinished = false;

    Runnable onFinishCallback;

    Runnable onStartCallback;

    public KrAnimation(float duration, KrEaseFunction easing) {
        this.duration = duration;
        this.easing = easing;
    }

    @Override
    public void update(float deltaSeconds) {
        currentTime += deltaSeconds;
        currentValue = Math.min(currentTime / duration, 1);
        isFinished = currentTime >= duration;

        doUpdate(currentValue);
    }

    public boolean finished() {
        return isFinished;
    }

    public abstract void doUpdate(float currentValue);

    public KrAnimation onFinish(Runnable callback) {
        onFinishCallback = callback;
        return this;
    }

    public KrAnimation onStart(Runnable callback) {
        onStartCallback = callback;
        return this;
    }
}
