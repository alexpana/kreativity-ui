package com.katzstudio.kreativity.ui.event.listener;

import com.katzstudio.kreativity.ui.event.KrKeyEvent;

/**
 * Listener for keyboard events.
 */
public interface KrKeyboardListener {

    void keyPressed(KrKeyEvent event);

    void keyReleased(KrKeyEvent event);
}
