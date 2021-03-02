package com.katzstudio.kreativity.ui.backend.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * {@link KrInputSource} implementation for the libgdx backend.
 */
public class KrGdxInputSource extends InputAdapter implements KrInputSource {

    private static final float KEY_REPEAT_INITIAL_TIME = 0.4f;

    private static final float KEY_REPEAT_TIME = 0.1f;

    private static final List<Integer> metaKeys = Arrays.asList(
            ALT_LEFT, ALT_RIGHT, CONTROL_LEFT, CONTROL_RIGHT, SHIFT_LEFT, SHIFT_RIGHT);

    private static final List<Integer> textModifierKeys = Arrays.asList(
            DEL, BACKSPACE, FORWARD_DEL, ENTER, TAB);

    private static final List<Integer> navigationKeys = Arrays.asList(
            LEFT, RIGHT, UP, DOWN, PAGE_DOWN, PAGE_UP, HOME, END, ESCAPE);

    private static final List<Integer> functionKeys = Arrays.asList(
            F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12);

    private final List<KrInputEventListener> listeners = new ArrayList<>();

    private boolean isAltDown;

    private boolean isCtrlDown;

    private boolean isShiftDown;

    private boolean isDragging;

    private int pressedKeyCode;

    private boolean keyRepeat = true;

    private float lastMousePressedTime = 0;

    private KrMouseEvent.Button lastMousePressedButton = null;

    private KeyRepeatTask keyRepeatTask;

    private int inputOffsetX = 0;

    private int inputOffsetY = 0;

    private Vector2 mousePosition = new Vector2();

    public KrGdxInputSource() {
        // TODO: investigate pointer offset on MAC OSX. Compensating here with a small hack
        if (((String) System.getProperties().get("os.name")).contains("Mac")) {
            inputOffsetX = -4;
            inputOffsetY = -3;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        isAltDown = isAltDown || isAlt(keycode);
        isCtrlDown = isCtrlDown || isCtrl(keycode);
        isShiftDown = isShiftDown || isShift(keycode);

        pressedKeyCode = keycode;

        if (pressedKeyCode == LEFT || pressedKeyCode == RIGHT) {
            scheduleKeyRepeatTask(keycode);
        }

        KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.PRESSED, pressedKeyCode);

        notifyKeyPressed(keyEvent);

        return keyEvent.handled();
    }

    @Override
    public boolean keyTyped(char character) {
        if (hasStringRepresentation(pressedKeyCode)) {
            KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.PRESSED, character).toBuilder().keycode(pressedKeyCode).build();
            notifyKeyPressed(keyEvent);
            return keyEvent.handled();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        isAltDown = isAltDown && !isAlt(keycode);
        isCtrlDown = isCtrlDown && !isCtrl(keycode);
        isShiftDown = isShiftDown && !isShift(keycode);

        if (keyRepeat) {
            getKeyRepeatTask().cancel();
        }

        KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.RELEASED, keycode);
        if (isShiftDown) {
            keyEvent = keyEvent.toBuilder().value(keyEvent.getValue().toUpperCase()).build();
        }

        notifyKeyReleased(keyEvent);

        return keyEvent.handled();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int buttonIndex) {

        screenX += inputOffsetX;
        screenY += inputOffsetY;

        isDragging = true;
        KrMouseEvent.Button button = getButtonFor(buttonIndex);

        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.PRESSED, screenX, screenY, buttonIndex);

        long nanoTime = System.nanoTime();
        if (lastMousePressedTime == 0) {
            lastMousePressedTime = nanoTime;
            lastMousePressedButton = button;
        } else {
            float deltaTime = nanoTime - lastMousePressedTime;
            if (button == lastMousePressedButton && deltaTime < 200000000) {
                mouseEvent = createMouseEvent(KrMouseEvent.Type.DOUBLE_CLICK, screenX, screenY, buttonIndex);
            }
            lastMousePressedButton = button;
            lastMousePressedTime = nanoTime;
        }

        if (mouseEvent.getType() == KrMouseEvent.Type.PRESSED) {
            notifyMousePressed(mouseEvent);
        } else {
            notifyMouseDoubleClicked(mouseEvent);
        }

        return mouseEvent.handled();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        screenX += inputOffsetX;
        screenY += inputOffsetY;

        isDragging = false;
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.RELEASED, screenX, screenY, button);

        notifyMouseReleased(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        screenX += inputOffsetX;
        screenY += inputOffsetY;

        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);
        mousePosition.set(screenX, screenY);

        notifyMouseMoved(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        screenX += inputOffsetX;
        screenY += inputOffsetY;

        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);
        mousePosition.set(screenX, screenY);

        notifyMouseMoved(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        KrScrollEvent scrollEvent = new KrScrollEvent(amountX, amountY);

        notifyScrolledEvent(scrollEvent);

        return scrollEvent.handled();
    }

    private KrKeyEvent createKeyEvent(KrKeyEvent.Type type, int keycode) {
        return KrKeyEvent.builder()
                .type(type)
                .keycode(keycode)
                .value("")
                .isAltDown(isAltDown)
                .isCtrlDown(isCtrlDown)
                .isShiftDown(isShiftDown)
                .build();
    }

    private KrKeyEvent createKeyEvent(KrKeyEvent.Type type, char character) {
        return KrKeyEvent.builder()
                .type(type)
                .keycode(character)
                .value(String.valueOf(character))
                .isAltDown(isAltDown)
                .isCtrlDown(isCtrlDown)
                .isShiftDown(isShiftDown)
                .build();
    }

    private KrMouseEvent createMouseEvent(KrMouseEvent.Type type, int screenX, int screenY, int button) {
        KrMouseEvent.Button eventButton = getButtonFor(button);
        Vector2 mousePosition = new Vector2(screenX, screenY);
        Vector2 mouseDelta = new Vector2(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
        return KrMouseEvent.builder()
                .type(type)
                .button(eventButton)
                .screenPosition(mousePosition)
                .deltaMove(mouseDelta)
                .isAltDown(isAltDown)
                .isCtrlDown(isCtrlDown)
                .isShiftDown(isShiftDown)
                .build();
    }

    public void scheduleKeyRepeatTask(int keycode) {
        if (!keyRepeat) {
            return;
        }

        KeyRepeatTask keyRepeatTask = getKeyRepeatTask();
        if (!keyRepeatTask.isScheduled() || keyRepeatTask.keycode != keycode) {
            keyRepeatTask.keycode = keycode;
            keyRepeatTask.cancel();
            Timer.schedule(keyRepeatTask, KEY_REPEAT_INITIAL_TIME, KEY_REPEAT_TIME);
        }
    }

    private KeyRepeatTask getKeyRepeatTask() {
        if (keyRepeatTask == null) {
            keyRepeatTask = new KeyRepeatTask();
        }
        return keyRepeatTask;
    }

    @Override
    public boolean isAltDown() {
        return isAltDown;
    }

    @Override
    public boolean isCtrlDown() {
        return isCtrlDown;
    }

    @Override
    public boolean isShiftDown() {
        return isShiftDown;
    }

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public Vector2 getMousePosition() {
        return mousePosition;
    }

    @Override
    public void addEventListener(KrInputEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(KrInputEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyMouseMoved(KrMouseEvent event) {
        listeners.forEach(l -> l.mouseMoved(event));
    }

    private void notifyMousePressed(KrMouseEvent event) {
        listeners.forEach(l -> l.mousePressed(event));
    }

    private void notifyMouseReleased(KrMouseEvent event) {
        listeners.forEach(l -> l.mouseReleased(event));
    }

    private void notifyMouseDoubleClicked(KrMouseEvent event) {
        listeners.forEach(l -> l.mouseDoubleClicked(event));
    }

    private void notifyKeyPressed(KrKeyEvent event) {
        listeners.forEach(l -> l.keyPressed(event));
    }

    private void notifyKeyReleased(KrKeyEvent event) {
        listeners.forEach(l -> l.keyReleased(event));
    }

    private void notifyScrolledEvent(KrScrollEvent event) {
        listeners.forEach(l -> l.scrolledEvent(event));
    }

    private static KrMouseEvent.Button getButtonFor(int button) {
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


    private static boolean hasStringRepresentation(int keycode) {
        return !metaKeys.contains(keycode) &&
                !textModifierKeys.contains(keycode) &&
                !navigationKeys.contains(keycode) &&
                !functionKeys.contains(keycode);
    }

    private static boolean isAlt(int keycode) {
        return keycode == ALT_LEFT || keycode == ALT_RIGHT;
    }

    private static boolean isCtrl(int keycode) {
        return keycode == CONTROL_LEFT || keycode == CONTROL_RIGHT;
    }

    private static boolean isShift(int keycode) {
        return keycode == SHIFT_LEFT || keycode == SHIFT_RIGHT;
    }

    /**
     * Used to schedule repeated key presses for arrows
     */
    private class KeyRepeatTask extends Timer.Task {
        public int keycode;

        public void run() {
            keyDown(keycode);
        }
    }
}
