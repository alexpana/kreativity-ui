package com.katzstudio.kreativity.ui.backend.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.libgdx.KrLibGdxInputHelper;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.katzstudio.kreativity.ui.libgdx.KrLibGdxInputHelper.*;

/**
 * {@link KrInputSource} implementation for libgdx lwjgl3 backend.
 */
public class KrLwjgl3InputSource extends InputAdapter implements KrInputSource {

    private static final float KEY_REPEAT_INITIAL_TIME = 0.4f;

    private static final float KEY_REPEAT_TIME = 0.1f;

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
        if (KrLibGdxInputHelper.hasStringRepresentation(pressedKeyCode)) {
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
        isDragging = false;
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.RELEASED, screenX, screenY, button);

        notifyMouseReleased(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);

        notifyMouseMoved(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);

        notifyMouseMoved(mouseEvent);

        return mouseEvent.handled();
    }

    @Override
    public boolean scrolled(int amount) {
        KrScrollEvent scrollEvent = new KrScrollEvent(amount);

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
