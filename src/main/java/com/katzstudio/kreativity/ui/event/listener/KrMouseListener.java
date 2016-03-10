package com.katzstudio.kreativity.ui.event.listener;

import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;

/**
 * Listener for mouse events.
 */
public interface KrMouseListener {

    void scrolled(KrScrollEvent event);

    void mouseMoved(KrMouseEvent event);

    void mousePressed(KrMouseEvent event);

    void mouseReleased(KrMouseEvent event);

    void enter(KrEnterEvent event);

    void exit(KrExitEvent event);
}
