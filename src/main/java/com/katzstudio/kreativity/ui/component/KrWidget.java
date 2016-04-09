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
import com.katzstudio.kreativity.ui.icon.KrIcon;
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
 * Base class for all Kreativity UI Components
 */
@SuppressWarnings("ALL")
public class KrWidget implements KrUpdateListener {

    public static final String FOCUS_PROPERTY = "property.focus";

    @Getter private float x;

    @Getter private float y;

    @Getter float width;

    @Getter private float height;

    @Getter private final ArrayList<KrWidget> children = new ArrayList<>();

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

    @Getter private KrWidgetStyle defaultStyle;

    @Getter private KrWidgetStyle style;

    @Getter @Setter protected String tooltipText;

    @Getter @Setter protected KrWidget tooltipWidget;

    @Getter @Setter private float opacity = 1;

    protected Rectangle tmpRect = new Rectangle();

    protected final KrMeasuredString text = new KrMeasuredString("");

    /**
     * Default constructor.
     */
    public KrWidget() {
        this("");
    }

    /**
     * Creates a new {@link KrWidget}
     *
     * @param style the widget style
     */
    public KrWidget(KrWidgetStyle style) {
        setDefaultStyle(style);
    }

    /**
     * Creates a new {@link KrWidget}
     *
     * @param name the widget name (not text) used to uniquely identify this widget
     */
    public KrWidget(String name) {
        this.name = name;
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrWidget.class));
    }

    /**
     * Ensures that the style getDefaultToolkit owned by this widget is not shared with other widgets.
     * <p>
     * Changes to the style of this widget after calling {@code ensureUniqueStyle} will only
     * affect this widget getDefaultToolkit
     */
    public void ensureUniqueStyle() {
        if (style == null || style == defaultStyle) {
            style = defaultStyle.copy();
        }
    }

    /**
     * Sets the widget style.
     */
    public void setStyle(KrWidgetStyle style) {
        this.style = style;
    }

    /**
     * Returns the style of the widget.
     */
    public KrWidgetStyle getStyle() {
        return style != null ? style : defaultStyle;
    }

    /**
     * Sets the default style of the widget.
     */
    public void setDefaultStyle(KrWidgetStyle style) {
        this.defaultStyle = style;
        this.style = null;
    }

    /**
     * Sets the widget parent. This method is called by the parent when this widget is added
     * as a child.
     *
     * @param parent the new parent of this widget
     */
    private void setParent(KrWidget parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of child widgets this widget has.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns the child widget at the specified index.
     */
    public KrWidget getChild(int index) {
        return children.get(index);
    }

    /**
     * Returns the background used when rendering this widget.
     */
    public Drawable getBackground() {
        return style.background;
    }

    /**
     * Sets the background used when rendering this widget.
     */
    public void setBackground(Drawable background) {
        ensureUniqueStyle();
        style.background = background;
    }

    /**
     * Gets the foreground color used for rendering the widget text.
     */
    public Color getForeground() {
        return style.foregroundColor;
    }

    /**
     * Sets the foreground color used for rendering the widget text.
     */
    public void setForeground(Color foreground) {
        ensureUniqueStyle();
        style.foregroundColor = foreground;
    }

    /**
     * Returns the padding preferred by the widget. The padding is the
     * space between the inner contents of the widget and its outer bounds.
     * <p>
     * The background of the widget will cover the outer bounds.
     */
    public KrPadding getPadding() {
        ensureUniqueStyle();
        return style.padding;
    }

    /**
     * Sets the preferred padding of the widget.
     */
    public void setPadding(KrPadding padding) {
        ensureUniqueStyle();
        style.padding = padding;
    }

    /**
     * Returns the widget's cursor. When the mouse is hovering this widget,
     * the widget's cursor will be used to represent the mouse pointer.
     */
    public KrCursor getCursor() {
        return style.cursor;
    }

    /**
     * Sets the widget's cursor.
     */
    public void setCursor(KrCursor cursor) {
        ensureUniqueStyle();
        style.cursor = cursor;
    }

    /**
     * Returns the widget's icon.
     */
    public KrIcon getIcon() {
        return style.icon;
    }

    /**
     * Sets the widget icon.
     */
    public void setIcon(KrIcon icon) {
        ensureUniqueStyle();
        style.icon = icon;
    }

    /**
     * Returns the font used to render the text of the widget.
     */
    public BitmapFont getFont() {
        return style.font;
    }

    /**
     * Sets the font used to render the text of the widget.
     */
    public void setFont(BitmapFont font) {
        ensureUniqueStyle();
        style.font = font;
        text.setFont(font);
    }

    /**
     * Sets the widget's text. The text is only used by some of the widgets such
     * as labels, buttons, checkboxes, etc. to display the meaning of the widget.
     */
    public void setText(String text) {
        this.text.setString(text);
    }

    /**
     * Returns this widget's text.
     */
    public String getText() {
        return this.text.getString();
    }

    /**
     * Adds a child widget to this widget. A {@code null} layout constraint is used.
     * This method invalidates the widget, making sure the layout is called to position
     * the child widget.
     *
     * @param child the child to be added
     */
    public void add(KrWidget child) {
        add(child, null);
    }

    /**
     * Adds a child widget to this widget.
     * This method invalidates the widget, making sure the layout is called to position
     * the child widget.
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
     * Removes a child of this widget.
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
     * Removes all the children of this widget.
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

    /**
     * Calls {@code invalidate()} on the parent widget, if any.
     */
    private void invalidateParent() {
        if (parent != null) {
            parent.invalidate();
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
    }

    /**
     * Renders this widget and all its children. Please don't override this
     * method, unless you explicity setup the renderer and ensure all children
     * are properly rendered.
     * <p>
     * Instead, consider overriding {@code drawSelf}
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
     * Draws only this widget. The default implementation fills the background
     * with the background drawable of the style. Override this method to
     * implement custom widgets.
     *
     * @param renderer the system renderer used to draw this widget.
     */
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(getBackground());
        renderer.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws the children widgets.
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    private void drawChildren(KrRenderer renderer) {
        for (int i = 0; i < children.size(); ++i) {
            children.get(i).draw(renderer);
        }
    }

    /**
     * Calculates a size preferred by this widget. This size is enough to hold all
     * the content of the widget, including padding.
     * <p>
     * This method does not take into account the children of the widget. The layout
     * is used to compute the preferred size of this widget, when children are
     * involved.
     *
     * @return the preferred size of this widget, ignoring any children.
     */
    public Vector2 calculatePreferredSize() {
        return layout.getPreferredSize();
    }

    /**
     * Returns true if the user has specifically set a max size.
     */
    public boolean isMaxSizeSet() {
        return maxSize != null;
    }

    /**
     * Returns the maximum size of this widget.
     */
    public Vector2 getMaxSize() {
        if (isMaxSizeSet()) {
            return maxSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return layout.getMaxSize();
        }

        return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    /**
     * Returns the maximum width of this widget.
     */
    public float getMaxWidth() {
        return getMaxSize().x;
    }

    /**
     * Returns the maximum height of this widget.
     */
    public float getMaxHeight() {
        return getMaxSize().y;
    }

    /**
     * Sets the maximum width of this widget.
     */
    public void setMaxWidth(float maxWidth) {
        setMaxSize(new Vector2(maxWidth, maxSize.y));
    }

    /**
     * Sets the maximum height of this widget.
     */
    public void setMaxHeight(float maxHeight) {
        setMaxSize(new Vector2(maxSize.x, maxHeight));
    }

    /**
     * Returns true if the user has set a specific minimum size for this widget.
     */
    public boolean isMinSizeSet() {
        return minSize != null;
    }

    /**
     * Returns the minimum size of the widget.
     */
    public Vector2 getMinSize() {
        if (isMinSizeSet()) {
            return minSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return layout.getMinSize();
        }

        return calculatePreferredSize();
    }

    /**
     * Returns the minimum width of the widget.
     */
    public float getMinWidth() {
        return getMinSize().x;
    }

    /**
     * Returns the minimum height of the widget.
     */
    public float getMinHeight() {
        return getMinSize().y;
    }

    /**
     * Sets the minimum width of the widget.
     */
    public void setMinWidth(float minWidth) {
        setMinSize(new Vector2(minWidth, minSize.y));
    }

    /**
     * Sets the minimum height of the widget.
     */
    public void setMinHeight(float minHeight) {
        setMinSize(new Vector2(minSize.x, minHeight));
    }

    /**
     * Returns true if the user has set a specific preferred size for this widget.
     */
    public boolean isPreferredSizeSet() {
        return preferredSize != null;
    }

    /**
     * Returns the preferred size of the widget.
     */
    public Vector2 getPreferredSize() {
        if (isPreferredSizeSet()) {
            return preferredSize;
        }

        if (!(layout instanceof KrAbsoluteLayout)) {
            return rectangles(layout.getPreferredSize()).expand(getPadding()).size();
        }

        return calculatePreferredSize();
    }

    /**
     * Returns the preferred width of the widget.
     */
    public float getPreferredWidth() {
        return getPreferredSize().x;
    }

    /**
     * Returns the preferred height of the widget.
     */
    public float getPreferredHeight() {
        return getPreferredSize().y;
    }

    /**
     * Sets the preferred width of the widget.
     */
    public void setPreferredWidth(float preferredWidth) {
        setPreferredSize(new Vector2(preferredWidth, preferredSize.y));
    }

    /**
     * Sets the preferred height of the widget.
     */
    public void setPreferredHeight(float preferredHeight) {
        setPreferredSize(new Vector2(preferredSize.x, preferredHeight));
    }

    /**
     * Returns the geometry of this widget in screen space.
     */
    public Rectangle getScreenGeometry() {
        float offsetX = 0;
        float offsetY = 0;
        if (parent != null) {
            Rectangle parentGeometry = parent.getScreenGeometry();
            offsetX = parentGeometry.x;
            offsetY = parentGeometry.y;
        }
        return new Rectangle(offsetX + getX(), offsetY + getY(), getWidth(), getHeight());
    }

    /**
     * Returns the geometry of this widget in parent space.
     */
    public Rectangle getGeometry() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Returns the geometry of this widget in parent space.
     *
     * @param geometry this object is updated with the return value, then returned.
     */
    public Rectangle getGeometry(Rectangle geometry) {
        return geometry.set(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Returns the size of the widget.
     */
    public Vector2 getSize() {
        return new Vector2(getWidth(), getHeight());
    }

    /**
     * Returns the canvas that hosts this object. Returns {@code null} if this
     * object does not belong to the canvas.
     */
    public KrCanvas getCanvas() {
        if (canvas != null) {
            return canvas;
        }

        if (parent != null) {
            canvas = parent.getCanvas();
        }

        return canvas;
    }

    /**
     * Requests to be the focused widget. Returns true if the request is successful,
     * or false otherwise.
     * <p>
     * Note that if the request is fulfilled, a {@link KrFocusEvent} of type
     * {@code FOCUS_GAINED} is received before this method returns.
     *
     * @return
     */
    public boolean requestFocus() {
        return getCanvas() != null && getCanvas().requestFocus(this);
    }

    /**
     * Removes the focus from this widget.
     */
    public void clearFocus() {
        KrCanvas canvas = getCanvas();
        if (canvas != null) {
            canvas.clearFocus();
        }
    }

    /**
     * Returns true if this widget can be focused via pressing the {@code TAB} key.
     */
    public boolean acceptsTabInput() {
        return false;
    }

    /**
     * Handles an event. This is usually called by the {@link KrCanvas} when an event
     * is dispatched to this widget, but it can be called manually.
     * <p>
     * This method delegates the event to one of the specific event handling methods.
     *
     * @param event the event received by this widget
     */
    @SuppressWarnings("SimplifiableIfStatement")
    public void handle(KrEvent event) {
        if (event instanceof KrMouseEvent) {
            KrMouseEvent mouseEvent = (KrMouseEvent) event;
            switch ((mouseEvent).getType()) {
                case MOVED:
                    mouseMoveEvent(mouseEvent);
                    break;
                case PRESSED:
                    mousePressedEvent(mouseEvent);
                    break;
                case RELEASED:
                    mouseReleasedEvent(mouseEvent);
                    break;
                case DOUBLE_CLICK:
                    mouseDoubleClickEvent(mouseEvent);
                    break;
            }
        }

        if (event instanceof KrKeyEvent) {
            KrKeyEvent keyEvent = (KrKeyEvent) event;
            switch (keyEvent.getType()) {
                case PRESSED:
                    keyPressedEvent(keyEvent);
                    break;
                case RELEASED:
                    keyReleasedEvent(keyEvent);
                    break;
            }
        }

        if (event instanceof KrScrollEvent) {
            scrollEvent((KrScrollEvent) event);
        }

        if (event instanceof KrEnterEvent) {
            enterEvent((KrEnterEvent) event);
        }

        if (event instanceof KrExitEvent) {
            exitEvent((KrExitEvent) event);
        }

        if (event instanceof KrFocusEvent) {
            KrFocusEvent focusEvent = (KrFocusEvent) event;
            switch (focusEvent.getType()) {
                case FOCUS_GAINED:
                    focusGainedEvent(focusEvent);
                    break;
                case FOCUS_LOST:
                    focusLostEvent(focusEvent);
                    break;
            }
        }
    }

    /**
     * Handle a key pressed event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyKeyPressed} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void keyPressedEvent(KrKeyEvent event) {
        notifyKeyPressed(event);
    }

    /**
     * Handle a key released event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyKeyReleased} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void keyReleasedEvent(KrKeyEvent event) {
        notifyKeyReleased(event);
    }

    /**
     * Handle a mouse scroll event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseScrolled} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void scrollEvent(KrScrollEvent event) {
        notifyMouseScrolled(event);
    }

    /**
     * Handle a mouse move event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseMoved} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void mouseMoveEvent(KrMouseEvent event) {
        notifyMouseMoved(event);
    }

    /**
     * Handle a mouse pressed event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMousePressed} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void mousePressedEvent(KrMouseEvent event) {
        notifyMousePressed(event);
    }

    /**
     * Handle a mouse released event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseReleased} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void mouseReleasedEvent(KrMouseEvent event) {
        notifyMouseReleased(event);
    }

    /**
     * Handle a mouse double click event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseDoubleClicked} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void mouseDoubleClickEvent(KrMouseEvent mouseEvent) {
        notifyMouseDoubleClicked(mouseEvent);
    }

    /**
     * Handle an enter event. This event is dispatched when the mouse enters
     * the surface of the widget and begins hovering it.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseEnter} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void enterEvent(KrEnterEvent event) {
        notifyMouseEnter(event);
    }

    /**
     * Handle an exit event. This event is dispatched when the mouse leaves
     * the surface of the widget and stops hovering it.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyMouseExit} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void exitEvent(KrExitEvent event) {
        notifyMouseExit(event);
    }

    /**
     * Handle a focus gained event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyFocusGained} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void focusGainedEvent(KrFocusEvent event) {
        isFocused = true;
        notifyFocusGained(event);
    }

    /**
     * Handle a focus lost event.
     * <p>
     * Note: when overriding this method, make sure to call super, or manually
     * notify the event listeners using the {@code notifyFocusLost} method.
     * Certain things may break when overriding this method without calling super.
     */
    protected void focusLostEvent(KrFocusEvent event) {
        isFocused = false;
        notifyFocusLost(event);
    }

    public void addKeyboardListener(KrKeyboardListener listener) {
        keyboardListeners.add(listener);
    }

    public void removeKeyboardListener(KrKeyboardListener listener) {
        keyboardListeners.remove(listener);
    }

    public void addMouseListener(KrMouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(KrMouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    public void addFocusListener(KrFocusListener focusListener) {
        focusListeners.add(focusListener);
    }

    public void removeFocusListener(KrFocusListener focusListener) {
        focusListeners.add(focusListener);
    }

    public void addWidgetListener(KrWidgetListener listener) {
        widgetListeners.add(listener);
    }

    public void removeWidgetListener(KrWidgetListener listener) {
        widgetListeners.remove(listener);
    }

    /**
     * Sets whether or not this widget can receive focus events and hold input focus
     *
     * @param focusable whether or not the widget can be focused
     */
    public void setFocusable(boolean focusable) {
        if (this.isFocusable != focusable) {
            this.isFocusable = focusable;
            notifyWidgetPropertyChanged(FOCUS_PROPERTY, !isFocusable, isFocusable);
        }
    }

    /**
     * Converts a point from screen space to local space
     *
     * @param screenPosition the position of the point in screen space
     * @return the position of the point translated in local space
     */
    public Vector2 screenToLocal(Vector2 screenPosition) {
        return screenToLocal(screenPosition.x, screenPosition.y);
    }

    /**
     * Converts a point from screen space to local space.
     *
     * @param screenX the screen X position
     * @param screenY the screen Y position
     * @return the position translated to local space
     */
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

    /**
     * Returns a builder used to create a string representation of this width.
     */
    protected KrWidgetToStringBuilder toStringBuilder() {
        return KrWidgetToStringBuilder.builder().name(name).geometry(getGeometry()).enabled(true).visible(true);
    }

    protected void notifyKeyPressed(KrKeyEvent event) {
        keyboardListeners.forEach(l -> l.keyPressed(event));
    }

    protected void notifyKeyReleased(KrKeyEvent event) {
        keyboardListeners.forEach(l -> l.keyReleased(event));
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

    protected void notifyFocusGained(KrFocusEvent event) {
        focusListeners.forEach(l -> l.focusGained(event));
    }

    protected void notifyFocusLost(KrFocusEvent event) {
        focusListeners.forEach(l -> l.focusLost(event));
    }

    protected void notifyWidgetPropertyChanged(String propertyName, Object oldValue, Object newValue) {
        widgetListeners.forEach(listener -> listener.propertyChanged(propertyName, oldValue, newValue));
    }

    protected void notifyWidgetInvalidated() {
        widgetListeners.forEach(KrWidgetListener::invalidated);
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
