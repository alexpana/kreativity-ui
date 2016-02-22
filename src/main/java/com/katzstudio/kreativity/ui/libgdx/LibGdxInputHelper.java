package com.katzstudio.kreativity.ui.libgdx;

import com.badlogic.gdx.Input;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Class for binding libgdx input events to kreativity events.
 */
public class LibGdxInputHelper {
    private static final List<Integer> metaKeys = Lists.newArrayList(
            ALT_LEFT, ALT_RIGHT, CONTROL_LEFT, CONTROL_RIGHT, SHIFT_LEFT, SHIFT_RIGHT
    );

    private static final List<Integer> textModifierKeys = Lists.newArrayList(
            DEL, BACKSPACE, FORWARD_DEL, ENTER, TAB
    );

    private static final List<Integer> navigationKeys = Lists.newArrayList(
            LEFT, RIGHT, UP, DOWN, PAGE_DOWN, PAGE_UP, HOME, END, ESCAPE
    );

    private static final List<Integer> functionKeys = Lists.newArrayList(
            F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12
    );

    private LibGdxInputHelper() {
    }

    public static KrMouseEvent.Button getButtonFor(int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                return KrMouseEvent.Button.LEFT;
            case Input.Buttons.RIGHT:
                return KrMouseEvent.Button.RIGHT;
            case Input.Buttons.MIDDLE:
                return KrMouseEvent.Button.MIDDLE;
        }

        return KrMouseEvent.Button.NONE;
    }

    public static String getRepresentation(int keycode) {
        switch (keycode) {
            case SPACE:
                return " ";
            case NUMPAD_0:
                return "0";
            case NUMPAD_1:
                return "1";
            case NUMPAD_2:
                return "2";
            case NUMPAD_3:
                return "3";
            case NUMPAD_4:
                return "4";
            case NUMPAD_5:
                return "5";
            case NUMPAD_6:
                return "6";
            case NUMPAD_7:
                return "7";
            case NUMPAD_8:
                return "8";
            case NUMPAD_9:
                return "9";
            case PLUS:
                return "+";
            default:
                return hasStringRepresentation(keycode) ? Input.Keys.toString(keycode).toLowerCase() : "";
        }
    }

    public static boolean hasStringRepresentation(int keycode) {
        return !metaKeys.contains(keycode) &&
                !textModifierKeys.contains(keycode) &&
                !navigationKeys.contains(keycode) &&
                !functionKeys.contains(keycode);
    }

    public static boolean isAlt(int keycode) {
        return keycode == ALT_LEFT || keycode == ALT_RIGHT;
    }

    public static boolean isCtrl(int keycode) {
        return keycode == CONTROL_LEFT || keycode == CONTROL_RIGHT;
    }

    public static boolean isShift(int keycode) {
        return keycode == SHIFT_LEFT || keycode == SHIFT_RIGHT;
    }
}
