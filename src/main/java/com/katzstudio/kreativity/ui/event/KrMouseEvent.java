package com.katzstudio.kreativity.ui.event;

import com.badlogic.gdx.math.Vector2;
import lombok.*;

/**
 * The {@link KrMouseEvent} class contains parameters that describe generic mouse events.
 */
@RequiredArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class KrMouseEvent extends KrEvent {
    public enum Type {
        MOVED, PRESSED, RELEASED, DOUBLE_CLICK
    }

    public enum Button {
        LEFT, RIGHT, MIDDLE, NONE
    }

    @Getter private final Type type;

    @Getter private final Button button;

    @Getter private final Vector2 deltaMove;

    @Getter private final Vector2 screenPosition;

    @Getter private boolean isAltDown = false;

    @Getter private boolean isCtrlDown = false;

    @Getter private boolean isShiftDown = false;
}
