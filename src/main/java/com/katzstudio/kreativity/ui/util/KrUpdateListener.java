package com.katzstudio.kreativity.ui.util;

/**
 * An updatable component can be updated each frame through
 * its update method.
 */
public interface KrUpdateListener {
    void update(float deltaSeconds);
}
