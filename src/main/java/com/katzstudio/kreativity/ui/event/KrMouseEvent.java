package com.katzstudio.kreativity.ui.event;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * The {@link KrMouseEvent} class contains parameters that describe generic mouse events.
 */
@RequiredArgsConstructor
@ToString
public class KrMouseEvent extends KrEvent {
    public enum Type {
        MOVED, PRESSED, RELEASED
    }

    public enum Button {
        LEFT, RIGHT, MIDDLE, NONE
    }

    @Getter private final Type type;

    @Getter private final Button button;

    @Getter private final Vector2 deltaMove;

    @Getter private final Vector2 position;
}
