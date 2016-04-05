package com.katzstudio.kreativity.ui.util;

/**
 * An updatable component can be updated each frame through
 * its update method.
 */
public interface KrUpdateListener {

    /**
     * This method is called each frame.
     *
     * @param deltaSeconds the number of seconds passed since
     *                     the last {@code update()} call
     */
    void update(float deltaSeconds);
}
