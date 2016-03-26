package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.*;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.badlogic.gdx.Input.Keys.TAB;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * Top level container for UI elements. Delegates input events to top level components.
 */
public class KrCanvas implements KrInputSource.KrInputEventListener {

    @Getter private final KrPanel rootPanel;

    @Getter private final KrPanel overlayPanel;

    private final KrRenderer renderer;

    @Getter private float width;

    @Getter private float height;

    @Getter private KrWidget keyboardFocusHolder;

    @Getter private KrWidget mouseFocusHolder;

    private KrWidget currentlyHoveredWidget = null;

    @Getter private final KrFocusManager focusManager;

    private final List<KrInputListener> listeners = new ArrayList<>();

    private KrTooltipManager tooltipManager;

    private KrCursorManager cursorManager;

    private KrInputSource input;

    KrCanvas(KrInputSource input, KrRenderer renderer, float width, float height) {

        this.input = input;

        this.renderer = renderer;

        rootPanel = new KrPanel();
        rootPanel.setName("root");
        rootPanel.setCanvas(this);

        overlayPanel = new KrPanel();
        overlayPanel.setName("overlay");
        overlayPanel.setCanvas(this);

        focusManager = new KrFocusManager(rootPanel);

        tooltipManager = new KrTooltipManager(this);

        cursorManager = new KrCursorManager(this);

        rootPanel.addWidgetListener(new KrWidget.KrWidgetListener.KrAbstractWidgetListener() {
            @Override
            public void invalidated() {
                focusManager.refresh();
            }
        });

        overlayPanel.add(tooltipManager.getTooltipWidget());

        setSize(width, height);

        input.addEventListener(this);
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
        rootPanel.setSize(width, height);
        overlayPanel.setSize(width, height);
        renderer.setViewportSize(width, height);
    }

    /**
     * Call every frame to allow components to update themselves.
     *
     * @param deltaSeconds the time, in seconds, since the last update
     */
    public void update(float deltaSeconds) {
        Queue<KrWidget> widgets = new LinkedList<>();
        widgets.add(rootPanel);
        widgets.add(overlayPanel);

        while (!widgets.isEmpty()) {
            KrWidget widget = widgets.poll();
            widget.update(deltaSeconds);
            widgets.addAll(widget.getChildren());
        }

        tooltipManager.update(deltaSeconds);
    }

    /**
     * Draws the UI.
     */
    public void draw() {
        renderer.beginFrame();
        renderer.setFont(getDefaultToolkit().getSkin().getDefaultFont());
        renderer.setPen(new KrPen(1, getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.FOREGROUND)));

        rootPanel.draw(renderer);
        overlayPanel.draw(renderer);

        renderer.endFrame();
    }


    @Override
    public void mouseMoved(KrMouseEvent event) {
        KrWidget hoveredWidget = findWidgetAt(rootPanel, event.getScreenPosition());

        if (!input.isDragging() && hoveredWidget != currentlyHoveredWidget) {
            if (currentlyHoveredWidget != null) {
                dispatchEventWithoutBubbling(currentlyHoveredWidget, new KrExitEvent());
                dispatchEventWithoutBubbling(hoveredWidget, new KrEnterEvent());
            }

            currentlyHoveredWidget = hoveredWidget;
        } else {
            dispatchEvent(currentlyHoveredWidget, event);
        }
    }

    @Override
    public void mousePressed(KrMouseEvent event) {
        mouseFocusHolder = findWidgetAt(rootPanel, event.getScreenPosition());
        if (mouseFocusHolder != keyboardFocusHolder) {
            requestFocus(mouseFocusHolder);
        }

        dispatchEvent(mouseFocusHolder, event);
    }

    @Override
    public void mouseReleased(KrMouseEvent event) {
        dispatchEvent(mouseFocusHolder, event);
    }

    @Override
    public void mouseDoubleClicked(KrMouseEvent event) {
        dispatchEvent(mouseFocusHolder, event);
    }

    @Override
    public void keyPressed(KrKeyEvent event) {
        if (keyboardFocusHolder == null) {
            return;
        }

        if (event.getKeycode() == TAB && !keyboardFocusHolder.acceptsTabInput()) {
            if (input.isShiftDown()) {
                focusPrevious();
            } else {
                focusNext();
            }
            event.accept();
            return;
        }

        dispatchEvent(keyboardFocusHolder, event);
    }

    @Override
    public void keyReleased(KrKeyEvent event) {
        dispatchEvent(keyboardFocusHolder, event);
    }

    @Override
    public void scrolledEvent(KrScrollEvent event) {
        KrWidget destinationWidget = findWidgetAt(rootPanel, Gdx.input.getX(), Gdx.input.getY());
        dispatchEvent(destinationWidget, event);
    }

    public interface KrInputListener {
        void eventDispatched(KrWidget widget, KrEvent event);
    }

    private boolean dispatchEvent(KrWidget widget, KrEvent event) {
        if (widget == null) {
            return false;
        }

        notifyEventDispatched(widget, event);

        boolean handled = widget.handle(event);

        while (!handled && widget.getParent() != null) {
            widget = widget.getParent();
            handled = widget.handle(event);
        }

        return handled;
    }

    private boolean dispatchEventWithoutBubbling(KrWidget widget, KrEvent event) {
        if (widget == null) {
            return false;
        }

        notifyEventDispatched(widget, event);
        widget.handle(event);

        return event.handled();
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

    public static KrWidget findWidgetAt(KrWidget root, Vector2 screenPosition) {
        return findWidgetAt(root, screenPosition.x, screenPosition.y);
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
                dispatchEventWithoutBubbling(keyboardFocusHolder, new KrFocusEvent(KrFocusEvent.Type.FOCUS_LOST));
            }

            if (widget != null) {
                dispatchEventWithoutBubbling(widget, new KrFocusEvent(KrFocusEvent.Type.FOCUS_GAINED));
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

    public void addInputListener(KrInputListener listener) {
        listeners.add(listener);
    }

    public void removeListener(KrInputListener listener) {
        listeners.remove(listener);
    }

    protected void notifyEventDispatched(KrWidget widget, KrEvent event) {
        listeners.forEach(l -> l.eventDispatched(widget, event));
    }
}
