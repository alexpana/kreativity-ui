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

    abstract class KrMouseAdapter implements KrMouseListener {
        @Override
        public void scrolled(KrScrollEvent event) {
        }

        @Override
        public void mouseMoved(KrMouseEvent event) {
        }

        @Override
        public void mousePressed(KrMouseEvent event) {
        }

        @Override
        public void mouseReleased(KrMouseEvent event) {
        }

        @Override
        public void enter(KrEnterEvent event) {
        }

        @Override
        public void exit(KrExitEvent event) {
        }
    }
}
