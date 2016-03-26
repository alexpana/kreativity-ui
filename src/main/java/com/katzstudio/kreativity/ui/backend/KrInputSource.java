package com.katzstudio.kreativity.ui.backend;

import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;

/**
 */
public interface KrInputSource {

    boolean isAltDown();

    boolean isCtrlDown();

    boolean isShiftDown();

    boolean isDragging();

    void addEventListener(KrInputEventListener listener);

    void removeEventListener(KrInputEventListener listener);

    interface KrInputEventListener {
        void mouseMoved(KrMouseEvent event);

        void mousePressed(KrMouseEvent event);

        void mouseReleased(KrMouseEvent event);

        void mouseDoubleClicked(KrMouseEvent event);

        void keyPressed(KrKeyEvent event);

        void keyReleased(KrKeyEvent event);

        void scrolledEvent(KrScrollEvent event);
    }
}
