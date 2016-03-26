package com.katzstudio.kreativity.ui;

import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrEvent;

/**
 * The cursor manager changes the cursor based on the
 * currently hovered widget.
 */
public class KrCursorManager {

    private KrCursor currentCursor = KrCursor.ARROW;

    public KrCursorManager(KrCanvas canvas) {
        canvas.addInputListener(this::onEventDispatched);
        KrToolkit.getDefaultToolkit().setCursor(currentCursor);
    }

    private void onEventDispatched(KrWidget widget, KrEvent event) {
        if (event instanceof KrEnterEvent) {
            System.out.println("event = " + event);
            setCursor(widget.getCursor());
        }
    }

    private void setCursor(KrCursor cursor) {
        if (currentCursor != cursor) {
            KrToolkit.getDefaultToolkit().setCursor(cursor);
            currentCursor = cursor;
        }
    }
}
