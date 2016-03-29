package com.katzstudio.kreativity.ui.animation;

import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.util.KrUpdateListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manager to create and run animations. Contains helpful functions
 * for quickly creating and registering widget specific animations.
 */
public class KrAnimations implements KrUpdateListener {

    private final static float DEFAULT_ANIMATION_LENGTH = 0.4f; // in seconds

    private List<KrAnimation> animations = new ArrayList<>();

    public KrAnimations() {
    }

    public void runAnimation(KrAnimation animation) {
        animations.add(animation);
        runStartCallback(animation);
    }

    @Override
    public void update(float deltaSeconds) {
        Iterator<KrAnimation> iterator = animations.iterator();
        while (iterator.hasNext()) {
            KrAnimation animation = iterator.next();
            animation.update(deltaSeconds);

            if (animation.finished()) {
                runFinishCallback(animation);
                iterator.remove();
            }
        }
    }

    private void runFinishCallback(KrAnimation animation) {
        if (animation.onFinishCallback != null) {
            animation.onFinishCallback.run();
        }
    }

    private void runStartCallback(KrAnimation animation) {
        if (animation.onStartCallback != null) {
            animation.onStartCallback.run();
        }
    }

    /**
     * Runs an animation to set the opacity of the widget to targetOpacity.
     * The default animation duration is used.
     *
     * @param widget        the widget affected by the animation
     * @param targetOpacity the final opacity of the widget at the end of the animation
     * @return the animation instance
     */
    public KrAnimation setOpacity(KrWidget widget, float targetOpacity) {
        KrWidgetOpacityAnimation animation = new KrWidgetOpacityAnimation(widget, targetOpacity, DEFAULT_ANIMATION_LENGTH, KrAnimationEasing.EASE_IN);
        runAnimation(animation);
        return animation;
    }
}
