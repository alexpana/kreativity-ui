package com.katzstudio.kreativity.ui.event;

import com.katzstudio.kreativity.ui.component.KrWidget;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * The {@link KrFocusEvent} class contains parameters for widget focus events.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class KrFocusEvent extends KrEvent {
    public enum Type {
        FOCUS_GAINED, FOCUS_LOST
    }

    @Getter private final Type type;

    @Getter private final KrWidget oldFocusHolder;

    @Getter private final KrWidget newFocusHolder;
}
