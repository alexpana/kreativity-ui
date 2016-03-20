package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.Timer;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.*;
import com.katzstudio.kreativity.ui.libgdx.KrLibGdxInputHelper;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import static com.badlogic.gdx.Input.Keys.*;
import static com.katzstudio.kreativity.ui.libgdx.KrLibGdxInputHelper.*;

/**
 * Top level container for UI elements. Delegates input events to top level components.
 */
public class KrCanvas implements InputProcessor {

    private static final float KEY_REPEAT_INITIAL_TIME = 0.4f;

    private static final float KEY_REPEAT_TIME = 0.1f;

    @Getter private final KrPanel rootComponent;

    @Getter private final Clipboard clipboard;

    private final KrRenderer renderer;

    private KeyRepeatTask keyRepeatTask;

    @Getter @Setter private boolean keyRepeat = true;

    @Getter private float width;

    @Getter private float height;

    @Getter private KrWidget keyboardFocusHolder;

    @Getter private KrWidget mouseFocusHolder;

    private KrWidget currentlyHoveredWidget = null;

    private boolean isAltDown = false;

    private boolean isCtrlDown = false;

    private boolean isShiftDown = false;

    private int pressedKeyCode = 0;

    private float lastMousePressedTime = 0;

    private KrMouseEvent.Button lastMousePressedButton = null;

    @Getter private final KrFocusManager focusManager;

    public KrCanvas() {
        this(new KrRenderer(), Gdx.app.getClipboard(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public KrCanvas(KrRenderer renderer, Clipboard clipboard, float width, float height) {
        this.clipboard = clipboard;

        rootComponent = new KrPanel();
        rootComponent.setName("root");
        rootComponent.setCanvas(this);

        focusManager = new KrFocusManager(rootComponent);

        rootComponent.addWidgetListener(new KrWidget.KrWidgetListener.KrAbstractWidgetListener() {
            @Override
            public void invalidated() {
                focusManager.refresh();
            }
        });

        this.renderer = renderer;
        setSize(width, height);
    }

    /**
     * Updates the size of the canvas. This is usually the size of the window.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        rootComponent.setSize(width, height);
        renderer.setViewportSize(width, height);
    }

    /**
     * Call every frame to allow components to update themselves.
     *
     * @param deltaSeconds the time, in seconds, since the last update
     */
    public void update(float deltaSeconds) {
        // TODO(alex): call update for each widget
        rootComponent.update(deltaSeconds);
    }

    private KeyRepeatTask getKeyRepeatTask() {
        if (keyRepeatTask == null) {
            keyRepeatTask = new KeyRepeatTask();
        }
        return keyRepeatTask;
    }

    /**
     * Draws the UI.
     */
    public void draw() {
        renderer.beginFrame();
        renderer.setFont(KrSkin.instance().getDefaultFont());
        renderer.setPen(new KrPen(1, KrSkin.instance().getColor(KrSkin.ColorKey.FOREGROUND)));
        rootComponent.draw(renderer);
        renderer.endFrame();
    }

    @Override
    public boolean keyDown(int keycode) {
        isAltDown = isAltDown || isAlt(keycode);
        isCtrlDown = isCtrlDown || isCtrl(keycode);
        isShiftDown = isShiftDown || isShift(keycode);
        pressedKeyCode = keycode;

        if (keyboardFocusHolder == null) {
            return false;
        }

        // handle tab key
        if (pressedKeyCode == TAB && !keyboardFocusHolder.acceptsTabInput()) {
            if (isShiftDown) {
                focusPrevious();
            } else {
                focusNext();
            }
            return true;
        }

        if (pressedKeyCode == LEFT || pressedKeyCode == RIGHT) {
            scheduleKeyRepeatTask(keycode);
        }

        // dispatch special key
        KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.PRESSED, pressedKeyCode);
        return dispatchEvent(keyboardFocusHolder, keyEvent);
    }

    @Override
    public boolean keyTyped(char character) {
        if (keyboardFocusHolder == null) {
            return false;
        }

        // dispatch visible key
        if (KrLibGdxInputHelper.hasStringRepresentation(pressedKeyCode)) {
            KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.PRESSED, character).toBuilder().keycode(pressedKeyCode).build();
            return dispatchEvent(keyboardFocusHolder, keyEvent);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        isAltDown = isAltDown && !isAlt(keycode);
        isCtrlDown = isCtrlDown && !isCtrl(keycode);
        isShiftDown = isShiftDown && !isShift(keycode);

        if (isKeyRepeat()) {
            getKeyRepeatTask().cancel();
        }

        KrKeyEvent keyEvent = createKeyEvent(KrKeyEvent.Type.RELEASED, keycode);
        if (isShiftDown) {
            keyEvent = keyEvent.toBuilder().value(keyEvent.getValue().toUpperCase()).build();
        }

        dispatchEvent(keyboardFocusHolder, keyEvent);
        return keyEvent.handled();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int buttonIndex) {
        KrMouseEvent.Button button = getButtonFor(buttonIndex);

        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.PRESSED, screenX, screenY, buttonIndex);
        mouseFocusHolder = findWidgetAt(rootComponent, screenX, screenY);
        if (mouseFocusHolder != keyboardFocusHolder) {
            requestFocus(mouseFocusHolder);
        }


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


        dispatchEvent(mouseFocusHolder, mouseEvent);
        return mouseEvent.handled();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.RELEASED, screenX, screenY, button);
        dispatchEvent(mouseFocusHolder, mouseEvent);
        return mouseEvent.handled();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);
        dispatchEvent(mouseFocusHolder, mouseEvent);
        return mouseEvent.handled();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        KrWidget hoveredWidget = findWidgetAt(rootComponent, screenX, screenY);

        if (hoveredWidget != currentlyHoveredWidget) {
            if (currentlyHoveredWidget != null) {
                currentlyHoveredWidget.handle(new KrExitEvent());
                hoveredWidget.handle(new KrEnterEvent());
            }

            currentlyHoveredWidget = hoveredWidget;
        }

        KrMouseEvent mouseEvent = createMouseEvent(KrMouseEvent.Type.MOVED, screenX, screenY, -1);
        dispatchEvent(hoveredWidget, mouseEvent);
        return mouseEvent.handled();
    }

    @Override
    public boolean scrolled(int amount) {
        KrScrollEvent scrollEvent = new KrScrollEvent(amount);
        KrWidget destinationWidget = findWidgetAt(rootComponent, Gdx.input.getX(), Gdx.input.getY());
        dispatchEvent(destinationWidget, scrollEvent);
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
        if (!isKeyRepeat()) {
            return;
        }

        KeyRepeatTask keyRepeatTask = getKeyRepeatTask();
        if (!keyRepeatTask.isScheduled() || keyRepeatTask.keycode != keycode) {
            keyRepeatTask.keycode = keycode;
            keyRepeatTask.cancel();
            Timer.schedule(keyRepeatTask, KEY_REPEAT_INITIAL_TIME, KEY_REPEAT_TIME);
        }
    }

    private boolean dispatchEvent(KrWidget widget, KrEvent event) {
        if (widget == null) {
            return false;
        }

        boolean handled = widget.handle(event);

        while (!handled && widget.getParent() != null) {
            widget = widget.getParent();
            handled = widget.handle(event);
        }

        return handled;
    }

    /**
     * Finds the topmost widget (a leaf in the hierarchy) whose geometry contains the requested screen coordinates.
     *
     * @param root the root of the widget hierarchy
     * @param x    the requested x position relative to the root widget
     * @param y    the requested y position relative to the root widget
     * @return the topmost widget that contains the requested coordinates
     */
    public static KrWidget findWidgetAt(KrWidget root, float x, float y) {
        ArrayList<KrWidget> childList = root.getChildren();
        for (int i = childList.size() - 1; i >= 0; --i) {
            KrWidget child = childList.get(i);
            if (getScreenGeometry(child).contains(x, y) && child.isVisible()) {
                return findWidgetAt(child, x, y);
            }
        }

        return root;
    }

    /**
     * Returns the screen geometry of the widget. This accounts for the widget hierarchy and the relative
     * positions of the parents.
     *
     * @param widget the widget whose geometry are queried
     * @return the screen-space geometry of the queried widget
     */
    public static Rectangle getScreenGeometry(KrWidget widget) {
        Rectangle widgetGeometry = new Rectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());

        while (widget.getParent() != null) {
            widget = widget.getParent();
            widgetGeometry.setX(widget.getX() + widgetGeometry.x);
            widgetGeometry.setY(widget.getY() + widgetGeometry.y);
        }

        return widgetGeometry;
    }

    /**
     * Converts a point location from the local space of a widget to the screen space
     *
     * @param point  a position local to the widget
     * @param widget the widget which contains the requested point
     * @return the point's position in screen space
     */
    public static Vector2 convertPointToScreen(Vector2 point, KrWidget widget) {
        Rectangle widgetGeometry = getScreenGeometry(widget);
        return widgetGeometry.getPosition(new Vector2()).add(point);
    }

    public boolean clearFocus() {
        return requestFocus(null);
    }

    public boolean requestFocus(KrWidget widget) {
        if (widget != null && widget.getCanvas() != this) {
            throw new IllegalArgumentException("Cannot focus a widget that doesn't belong to this canvas.");
        }

        if (keyboardFocusHolder != widget) {
            if (keyboardFocusHolder != null) {
                keyboardFocusHolder.handle(new KrFocusEvent(KrFocusEvent.Type.FOCUS_LOST));
            }

            if (widget != null) {
                widget.handle(new KrFocusEvent(KrFocusEvent.Type.FOCUS_GAINED));
            }
            keyboardFocusHolder = widget;
            return true;
        }

        return false;
    }

    public void focusNext() {
        requestFocus(focusManager.nextFocusable(keyboardFocusHolder));
    }

    public void focusPrevious() {
        requestFocus(focusManager.previousFocusable(keyboardFocusHolder));
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
