package com.katzstudio.kreativity.ui.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base class for all event objects.
 */
@ToString
@EqualsAndHashCode
public class KrEvent {
    private boolean isHandled = false;

    public void accept() {
        isHandled = true;
    }

    public void ignore() {
        isHandled = false;
    }

    public boolean handled() {
        return isHandled;
    }
}
