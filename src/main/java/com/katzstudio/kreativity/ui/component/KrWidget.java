package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.*;
import com.katzstudio.kreativity.ui.event.*;
import com.katzstudio.kreativity.ui.event.listener.KrFocusListener;
import com.katzstudio.kreativity.ui.event.listener.KrKeyboardListener;
import com.katzstudio.kreativity.ui.event.listener.KrMouseListener;
import com.katzstudio.kreativity.ui.layout.KrAbsoluteLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import com.katzstudio.kreativity.ui.util.KrUpdateListener;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * Base class for all Kreativity Components
 */
@SuppressWarnings({"UnusedParameters", "unused"})
public class KrWidget<S extends KrWidgetStyle> implements KrUpdateListener {

    public static final String FOCUS_PROPERTY = "property.focus";

    @Getter private float x;

    @Getter private float y;

    @Getter float width;

    @Getter private float height;

    @Getter private final ArrayList<KrWidget<? extends KrWidgetStyle>> children = new ArrayList<>();

    @Getter private KrWidget parent;

    @Getter @Setter private KrLayout layout = new KrAbsoluteLayout();

    private Vector2 userPreferredSize;

    @Getter private boolean isFocused;

    @Getter @Setter private boolean isVisible = true;

    @Getter @Setter private boolean isEnabled = true;

    @Getter private boolean isValid = true;

    @Getter private boolean isFocusable = false;

    @Getter @Setter private String name;

    @Setter private KrCanvas canvas;

    private final List<KrKeyboardListener> keyboardListeners = new ArrayList<>();

    private final List<KrMouseListener> mouseListeners = new ArrayList<>();

    private final List<KrFocusListener> focusListeners = new ArrayList<>();

    private final List<KrWidgetListener> widgetListeners = new ArrayList<>();

    @Setter private Vector2 minSize;

    @Setter private Vector2 maxSize;

    @Setter private Vector2 preferredSize;

    @Getter @Setter private boolean clipRendering = true;

    @Getter @Setter protected S style;

    @Getter @Setter protected String tooltipText;

    @Getter @Setter protected KrWidget tooltipWidget;

    @Getter @Setter private float opacity = 1;

    protected Rectangle tmpRect = new Rectangle();

    protected final KrMeasuredString text = new KrMeasuredString("");

    public KrWidget() {
    }

    public KrWidget(S style) {
        setStyle(style);
    }

    public KrWidget(String name) {
        this.name = name;
    }

    private void setParent(KrWidget parent) {
        this.parent = parent;
    }

    public int getChildCount() {
        return children.size();
    }

    public KrWidget getChild(int index) {
        return children.get(index);
    }

    /**
     * Ensures that the style getDefaultToolkit owned by this widget is not shared with other widgets.
     * <p>
     * Changes to the style of this widget after calling {@code ensureUniqueStyle} will only
     * affect this widget getDefaultToolkit
     */
    public void ensureUniqueStyle() {
    }

    public Drawable getBackground() {
        return style.background;
    }

    public void setBackground(Drawable background) {
        ensureUniqueStyle();
        style.background = background;
    }

    public Color getForeground() {
        return style.foregroundColor;
    }

    public void setForeground(Color foreground) {
        ensureUniqueStyle();
        style.foregroundColor = foreground;
    }

    public KrPadding getPadding() {
        ensureUniqueStyle();
        return style.padding;
    }

    public void setPadding(KrPadding padding) {
        ensureUniqueStyle();
        style.padding = padding;
    }

    public KrCursor getCursor() {
        return style.cursor;
    }

    public void setCursor(KrCursor cursor) {
        ensureUniqueStyle();
        style.cursor = cursor;
    }

    public void setFont(BitmapFont font) {
        ensureUniqueStyle();
        style.font = font;
        text.setFont(font);
    }

    public BitmapFont getFont() {
        return style.font;
    }

    public void setText(String text) {
        this.text.setString(text);
    }

    public String getText() {
        return this.text.getString();
    }

    /**
     * Adds a child to this widget
     *
     * @param child the child to be added
     */
    public void add(KrWidget child) {
        add(child, null);
    }

    /**
     * Adds a child widget to this widget and to this widget's layout
     *
     * @param child            the child to be added
     * @param layoutConstraint the layout constraint
     */
    public void add(KrWidget child, Object layoutConstraint) {
        if (child.getParent() != null) {
            throw new IllegalArgumentException("Widget already has a parent: " + child.getParent());
        }
        //noinspection unchecked
        children.add(child);
        child.setParent(this);
        child.setCanvas(this.canvas);

        layout.addWidget(child, layoutConstraint);

        invalidate();
    }

    /**
     * Removes a child of this widget
     *
     * @param child the child to be removed
     */
    public void remove(KrWidget child) {

        layout.removeWidget(child);

        child.setCanvas(null);
        child.setParent(null);
        children.remove(child);

        if (child.isFocused) {
            getCanvas().clearFocus();
        }

        invalidate();
    }

    /**
     * Removes all the children of this widget
     */
    public void removeAll() {
        while (getChildCount() > 0) {
            remove(getChild(0));
        }
    }

    /**
     * Changes the position of this widget. The position of the widget is relative
     * to its parent (parent space).
     *
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    public void setPosition(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            invalidate();
        }
    }

    /**
     * Changes the position of this widget. The position of the widget is relative
     * to its parent (parent space).
     *
     * @param position the new position of the widget in parent space
     */
    public void setPosition(Vector2 position) {
        this.setPosition(position.x, position.y);
    }

    /**
     * Sets the size of the widget. This size has no connection to the min / max / preferred sizes.
     *
     * @param w the new width
     * @param h the new height
     */
    public void setSize(float w, float h) {
        if (this.width != w || this.height != h) {
            this.width = w;
            this.height = h;
            invalidate();
        }
    }

    /**
     * Sets the size of the widget. This size has no connection to the min / max / preferred sizes.
     *
     * @param size the new widget size
     */
    public void setSize(Vector2 size) {
        setSize(size.x, size.y);
    }

    /**
     * Sets the position and size of the widget in parent space.
     *
     * @param x      the new X coordinate
     * @param y      thew new Y coordinate
     * @param width  the new width
     * @param height the new height
     */
    public void setGeometry(float x, float y, float width, float height) {
        if (this.x == x && this.y == y && this.width == width && this.height == height) {
            return;
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        invalidate();
    }

    /**
     * Sets the position and size of this widget
     *
     * @param position the new position
     * @param size     the new size
     */
    public void setGeometry(Vector2 position, Vector2 size) {
        this.setGeometry(position.x, position.y, size.x, size.y);
    }

    /**
     * Sets the position and size of this widget
     *
     * @param geometry a rectangle describing the new position and size of the widget
     */
    public void setGeometry(Rectangle geometry) {
        this.setGeometry(geometry.x, geometry.y, geometry.width, geometry.height);
    }

    /**
     * Validates the widget by layouting its children
     */
    public void validate() {
        if (isValid) {
            return;
        }

        KrPadding padding = getPadding();
        Rectangle geometry = new Rectangle(0, 0, getWidth(), getHeight());
        Rectangle layoutRectangle = rectangles(geometry).shrink(padding).value();
        layout.setGeometry(layoutRectangle);
        isValid = true;
        Pools.free(layoutRectangle);
    }

    /**
     * Invalidating a widget requires the widget to be validated as soon as possible.
     * This is usually done by children when changing sizes to request the parent to re layout itself
     */
    public void invalidate() {
        isValid = false;
        notifyWidgetInvalidated();
        invalidateParent();
    }


    private void invalidateParent() {
        if (parent != null) {
            parent.invalidate();
        }
    }

    /**
     * Renders this widget and all its children
     *
     * @param renderer the renderer used to draw the widget
     */
    public void draw(KrRenderer renderer) {
        if (!isVisible()) {
            return;
        }

        renderer.translate(getX(), getY());
        boolean clipped = false;
        if (clipRendering) {
            clipped = renderer.beginClip(0, 0, getWidth(), getHeight());
        }
        float oldOpacity = renderer.setOpacity(getDrawOpacity());

        drawSelf(renderer);
        drawChildren(renderer);

        renderer.setOpacity(oldOpacity);
        renderer.translate(-getX(), -getY());
        if (clipped) {
            renderer.endClip();
        }
    }

    /**
     * Returns the opacity used when rendering this widget.
     * This opacity takes into consideration the opacity of the
     * widget hierarchy.
     *
     * @return the drawing opacity
     */
    public float getDrawOpacity() {
        return (parent != null ? parent.getDrawOpacity() : 1) * opacity;
    }

    @Override
    public void update(float deltaSeconds) {
        if (!isValid) {
            validate();
        }
        updateChildren(deltaSeconds);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void updateChildren(float deltaSeconds) {
        for (int i = 0; i < children.size(); ++i) {
            children.get(i).update(deltaSeconds);
        }
    }

    protected void drawSelf(KrRenderer renderer) {
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected void drawChildren(KrRenderer renderer) {
        for (int i = 0; i < children.size(); ++i) {
            children.get(i).draw(renderer);
        }
    }

    public Vector2 calculatePreferredSize() {
        return layout.getPreferredSize();
    }

    public boolean isMaxSizeSet() {
        return maxSize != null;
    }

    public Vector2 getMaxSize() {
        if (isMaxSizeSet()) {
            return maxSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return layout.getMaxSize();
        }

        return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    public float getMaxWidth() {
        return getMaxSize().x;
    }

    public float getMaxHeight() {
        return getMaxSize().y;
    }

    public void setMaxWidth(float maxWidth) {
        setMaxSize(new Vector2(maxWidth, maxSize.y));
    }

    public void setMaxHeight(float maxHeight) {
        setMaxSize(new Vector2(maxSize.x, maxHeight));
    }

    public boolean isMinSizeSet() {
        return minSize != null;
    }

    public Vector2 getMinSize() {
        if (isMinSizeSet()) {
            return minSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return layout.getMinSize();
        }

        return calculatePreferredSize();
    }

    public float getMinWidth() {
        return getMinSize().x;
    }

    public float getMinHeight() {
        return getMinSize().y;
    }

    public void setMinWidth(float minWidth) {
        setMinSize(new Vector2(minWidth, minSize.y));
    }

    public void setMinHeight(float minHeight) {
        setMinSize(new Vector2(minSize.x, minHeight));
    }

    public boolean isPreferredSizeSet() {
        return preferredSize != null;
    }

    public Vector2 getPreferredSize() {
        if (isPreferredSizeSet()) {
            return preferredSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return rectangles(layout.getPreferredSize()).expand(getPadding()).size();
        }

        return calculatePreferredSize();
    }

    public float getPreferredWidth() {
        return getPreferredSize().x;
    }

    public float getPreferredHeight() {
        return getPreferredSize().y;
    }

    public void setPreferredWidth(float preferredWidth) {
        setPreferredSize(new Vector2(preferredWidth, preferredSize.y));
    }

    public void setPreferredHeight(float preferredHeight) {
        setPreferredSize(new Vector2(preferredSize.x, preferredHeight));
    }

    private Rectangle getScreenGeometry() {
        float offsetX = 0;
        float offsetY = 0;
        if (parent != null) {
            Rectangle parentGeometry = parent.getScreenGeometry();
            offsetX = parentGeometry.x;
            offsetY = parentGeometry.y;
        }
        return new Rectangle(offsetX + getX(), offsetY + getY(), getWidth(), getHeight());
    }

    public Rectangle getGeometry() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getGeometry(Rectangle geometry) {
        return geometry.set(getX(), getY(), getWidth(), getHeight());
    }

    public Vector2 getSize() {
        return new Vector2(getWidth(), getHeight());
    }

    public KrWidget getTopLevelAncestor() {
        KrWidget widget = this;
        if (widget.parent != null) {
            return widget.parent.getTopLevelAncestor();
        } else {
            return this;
        }
    }

    public KrCanvas getCanvas() {
        if (canvas != null) {
            return canvas;
        }

        if (parent != null) {
            canvas = parent.getCanvas();
        }

        return canvas;
    }

    public boolean requestFocus() {
        return getCanvas() != null && getCanvas().requestFocus(this);
    }

    public void clearFocus() {
        KrCanvas canvas = getCanvas();
        if (canvas != null) {
            canvas.clearFocus();
        }
    }

    public boolean acceptsTabInput() {
        return false;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean handle(KrEvent event) {
        if (event instanceof KrMouseEvent) {
            KrMouseEvent mouseEvent = (KrMouseEvent) event;
            switch ((mouseEvent).getType()) {
                case MOVED:
                    return mouseMoveEvent(mouseEvent);
                case PRESSED:
                    return mousePressedEvent(mouseEvent);
                case RELEASED:
                    return mouseReleasedEvent(mouseEvent);
                case DOUBLE_CLICK:
                    return mouseDoubleClickEvent(mouseEvent);
            }
        }

        if (event instanceof KrKeyEvent) {
            KrKeyEvent keyEvent = (KrKeyEvent) event;
            switch (keyEvent.getType()) {
                case PRESSED:
                    return keyPressedEvent(keyEvent);
                case RELEASED:
                    return keyReleasedEvent(keyEvent);
            }
        }

        if (event instanceof KrScrollEvent) {
            return scrollEvent((KrScrollEvent) event);
        }

        if (event instanceof KrEnterEvent) {
            return enterEvent((KrEnterEvent) event);
        }

        if (event instanceof KrExitEvent) {
            return exitEvent((KrExitEvent) event);
        }

        if (event instanceof KrFocusEvent) {
            KrFocusEvent focusEvent = (KrFocusEvent) event;
            switch (focusEvent.getType()) {
                case FOCUS_GAINED:
                    return focusGainedEvent(focusEvent);
                case FOCUS_LOST:
                    return focusLostEvent(focusEvent);
            }
        }

        return false;
    }

    protected boolean keyPressedEvent(KrKeyEvent event) {
        notifyKeyPressed(event);
        return event.handled();
    }

    protected boolean keyReleasedEvent(KrKeyEvent event) {
        notifyKeyReleased(event);
        return event.handled();
    }

    protected boolean scrollEvent(KrScrollEvent event) {
        notifyMouseScrolled(event);
        return event.handled();
    }

    protected boolean mouseMoveEvent(KrMouseEvent event) {
        notifyMouseMoved(event);
        return event.handled();
    }

    protected boolean mousePressedEvent(KrMouseEvent event) {
        notifyMousePressed(event);
        return event.handled();
    }

    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        notifyMouseReleased(event);
        return event.handled();
    }

    protected boolean mouseDoubleClickEvent(KrMouseEvent mouseEvent) {
        notifyMouseDoubleClicked(mouseEvent);
        return mouseEvent.handled();
    }

    protected boolean enterEvent(KrEnterEvent event) {
        notifyMouseEnter(event);
        return event.handled();
    }

    protected boolean exitEvent(KrExitEvent event) {
        notifyMouseExit(event);
        return event.handled();
    }

    protected boolean focusGainedEvent(KrFocusEvent event) {
        isFocused = true;
        notifyFocusGained(event);
        return event.handled();
    }

    protected boolean focusLostEvent(KrFocusEvent event) {
        isFocused = false;
        notifyFocusLost(event);
        return event.handled();
    }

    public void addKeyboardListener(KrKeyboardListener listener) {
        keyboardListeners.add(listener);
    }

    public void removeKeyboardListener(KrKeyboardListener listener) {
        keyboardListeners.remove(listener);
    }

    protected void notifyKeyPressed(KrKeyEvent event) {
        keyboardListeners.forEach(l -> l.keyPressed(event));
    }

    protected void notifyKeyReleased(KrKeyEvent event) {
        keyboardListeners.forEach(l -> l.keyReleased(event));
    }

    public void addMouseListener(KrMouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(KrMouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    protected void notifyMouseScrolled(KrScrollEvent event) {
        mouseListeners.forEach(l -> l.scrolled(event));
    }

    protected void notifyMouseMoved(KrMouseEvent event) {
        mouseListeners.forEach(l -> l.mouseMoved(event));
    }

    protected void notifyMousePressed(KrMouseEvent event) {
        mouseListeners.forEach(l -> l.mousePressed(event));
    }

    protected void notifyMouseDoubleClicked(KrMouseEvent event) {
        mouseListeners.forEach(l -> l.mouseDoubleClicked(event));
    }

    protected void notifyMouseReleased(KrMouseEvent event) {
        mouseListeners.forEach(l -> l.mouseReleased(event));
    }

    protected void notifyMouseEnter(KrEnterEvent event) {
        mouseListeners.forEach(l -> l.enter(event));
    }

    protected void notifyMouseExit(KrExitEvent event) {
        mouseListeners.forEach(l -> l.exit(event));
    }

    public void addFocusListener(KrFocusListener focusListener) {
        focusListeners.add(focusListener);
    }

    public void removeFocusListener(KrFocusListener focusListener) {
        focusListeners.add(focusListener);
    }

    protected void notifyFocusGained(KrFocusEvent event) {
        focusListeners.forEach(l -> l.focusGained(event));
    }

    protected void notifyFocusLost(KrFocusEvent event) {
        focusListeners.forEach(l -> l.focusLost(event));
    }

    public void setFocusable(boolean focusable) {
        if (this.isFocusable != focusable) {
            this.isFocusable = focusable;
            notifyWidgetPropertyChanged(FOCUS_PROPERTY, !isFocusable, isFocusable);
        }
    }

    public void addWidgetListener(KrWidgetListener listener) {
        widgetListeners.add(listener);
    }

    public void removeWidgetListener(KrWidgetListener listener) {
        widgetListeners.remove(listener);
    }

    protected void notifyWidgetPropertyChanged(String propertyName, Object oldValue, Object newValue) {
        widgetListeners.forEach(listener -> listener.propertyChanged(propertyName, oldValue, newValue));
    }

    protected void notifyWidgetInvalidated() {
        widgetListeners.forEach(KrWidgetListener::invalidated);
    }

    protected final Vector2 expandSizeWithPadding(Vector2 size, KrPadding padding) {
        return new Vector2(size.x + padding.getHorizontalPadding(), size.y + padding.getVerticalPadding());
    }

    public Vector2 screenToLocal(Vector2 screenPosition) {
        return screenToLocal(screenPosition.x, screenPosition.y);
    }

    public Vector2 screenToLocal(float screenX, float screenY) {
        Rectangle screenGeometry = KrCanvas.getScreenGeometry(this);
        float localX = screenGeometry.x;
        float localY = screenGeometry.y;
        return new Vector2(screenX - localX, screenY - localY);
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    public KrWidgetToStringBuilder toStringBuilder() {
        return KrWidgetToStringBuilder.builder().name(name).geometry(getGeometry()).enabled(true).visible(true);
    }

    /**
     * Listener for {@link KrWidget} specific events like adding / removing children or property changes.
     */
    public interface KrWidgetListener {

        void childAdded(KrWidget child);

        void childRemoved(KrWidget child);

        void propertyChanged(String propertyName, Object oldValue, Object newValue);

        void invalidated();

        class KrAbstractWidgetListener implements KrWidgetListener {

            @Override
            public void childAdded(KrWidget child) {
            }

            @Override
            public void childRemoved(KrWidget child) {
            }

            @Override
            public void propertyChanged(String propertyName, Object oldValue, Object newValue) {
            }

            @Override
            public void invalidated() {
            }
        }
    }
}
