package com.katzstudio.kreativity.ui.util;

import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrToolkit;

/**
 * The {@link KrTimer} class is a special timer that's updated by the {@link KrCanvas}.
 */
public class KrTimer implements KrUpdateListener {

    private boolean isRegistered = false;

    private float timePassed = 0.0f;

    private float delay;

    private float period;

    private boolean passedDelay = false;

    private Runnable runnable;

    public KrTimer(float delay, Runnable runnable) {
        this.delay = delay;
        this.period = 0;
        this.runnable = runnable;
    }

    public KrTimer(float delay, float period, Runnable runnable) {
        this.delay = delay;
        this.period = period;
        this.runnable = runnable;
    }

    public void start() {
        if (!isRegistered) {
            KrToolkit.getDefaultToolkit().registerUpdateListener(this);
            isRegistered = true;
        }
        timePassed = 0;
        passedDelay = false;
    }

    public void stop() {
        KrToolkit.getDefaultToolkit().unregisterUpdateListener(this);
        isRegistered = false;
    }

    public void restart() {
        stop();
        start();
    }

    @Override
    public void update(float deltaSeconds) {
        timePassed += deltaSeconds;

        // first check delay
        if (!passedDelay && timePassed > delay) {
            passedDelay = true;

            callRunnable();

            if (period == 0) {
                stop();
            } else {
                timePassed -= delay;
            }
        }

        if (passedDelay && timePassed > period) {
            callRunnable();
            timePassed -= period;
        }
    }

    private void callRunnable() {
        if (runnable != null) {
            runnable.run();
        }
    }
}
