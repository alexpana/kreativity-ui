package com.katzstudio.kreativity.ui.event;

import lombok.*;

/**
 * The {@link KrKeyEvent} class contains parameters that describe keyboard events.
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class KrKeyEvent extends KrEvent {
    public enum Type {
        PRESSED, RELEASED
    }

    @Getter private final Type type;

    @Getter private final int keycode;

    @Getter private final String value;

    @Getter private boolean isAltDown = false;

    @Getter private boolean isCtrlDown = false;

    @Getter private boolean isShiftDown = false;
}
