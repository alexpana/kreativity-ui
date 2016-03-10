package com.katzstudio.kreativity.ui.event.listener;

import com.katzstudio.kreativity.ui.event.KrFocusEvent;

/**
 * Listener for focus events.
 */
public interface KrFocusListener {

    void focusGained(KrFocusEvent event);

    void focusLost(KrFocusEvent event);
}
