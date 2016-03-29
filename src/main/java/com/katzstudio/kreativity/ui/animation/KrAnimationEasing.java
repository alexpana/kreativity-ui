package com.katzstudio.kreativity.ui.animation;

/**
 * The easing function is used as to make animations more realistic.
 * <p>
 * Easing functions replace the linear interpolation by mapping the
 * linear interpolation to a easing curve.
 * <p>
 * Animations are updated with a linear value from 0 (the start of
 * the animation) to 1 (the end of the animation). By plugging this
 * value into an easing function, the result can be calculated from
 * a non-linear interpolation.
 */
public class KrAnimationEasing {

    public static final KrEaseFunction LINEAR = v -> v;

    public static final KrEaseFunction EASE_IN = v -> (v * v);

    public static final KrEaseFunction EASE_OUT = v -> (-v * v + 2 * v);

    // TODO(alex): this doesn't work!
    public static final KrEaseFunction EASE_IN_OUT = v -> {
        if (v < 0.5) {
            return EASE_IN.apply(v);
        } else {
            return EASE_OUT.apply(v);
        }
    };

    public interface KrEaseFunction {
        float apply(float v);
    }
}
