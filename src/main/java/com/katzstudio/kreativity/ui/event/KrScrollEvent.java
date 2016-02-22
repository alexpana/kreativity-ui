package com.katzstudio.kreativity.ui.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The {@link KrScrollEvent} class contains parameters that describe mouse scroll events.
 */
@RequiredArgsConstructor
public class KrScrollEvent extends KrEvent {
    @Getter private final float scrollAmount;
}
