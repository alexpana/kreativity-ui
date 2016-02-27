package com.katzstudio.kreativity.ui.component;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.katzstudio.kreativity.ui.KrContext;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * A combo box component.
 */
public class KrComboBox<T> extends Widget implements Disableable {

    static final Vector2 temp = new Vector2();

    private Style style;

    private final Array<T> items = new Array<>();

    private final ArraySelection<T> selection = new ArraySelection<>(items);

    private ComboBoxList<T> selectBoxList;

    private final TextBounds bounds = new TextBounds();

    private float prefWidth;

    private float prefHeight;

    private ClickListener clickListener;

    private boolean isDisabled;

    private final Model<T> model;

    public KrComboBox(KrContext uiContext, Model<T> model) {
        this.model = model;

        setStyle(uiContext.getSkin().get("default", Style.class));
        setSize(getPrefWidth(), getPrefHeight());

        selection.setActor(this);
        selection.setRequired(true);

        selectBoxList = new ComboBoxList<>(this);

        addListener(clickListener = new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) return false;
                if (isDisabled) return false;
                if (selectBoxList.hasParent())
                    hideList();
                else
                    showList();
                return true;
            }
        });
    }

    /**
     * Set the max number of items to display when the select box is opened. Set to 0 (the default) to display as many as fit in
     * the stage height.
     */
    public void setMaxListCount(int maxListCount) {
        selectBoxList.maxListCount = maxListCount;
    }

    /**
     * @return Max number of items to display when the box is opened, or <= 0 to display them all.
     */
    public int getMaxListCount() {
        return selectBoxList.maxListCount;
    }

    @Override
    protected void setStage(Stage stage) {
        if (stage == null) selectBoxList.hide();
        super.setStage(stage);
    }

    public void setStyle(Style style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /**
     * Set the backing Array that makes up the choices available in the ComboBox
     */
    public void setItems(T... newItems) {
        setItems(Array.with(newItems));
    }

    /**
     * Set the backing Array that makes up the choices available in the ComboBox
     */
    public void setItems(Array<T> newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth();

        items.clear();
        items.addAll(newItems);
        selection.validate();
        selectBoxList.list.setItems(items);

        invalidate();
        if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
    }

    public void clearItems() {
        if (items.size == 0) return;
        items.clear();
        selection.clear();
        invalidateHierarchy();
    }

    public Array<T> getItems() {
        return items;
    }

    @Override
    public void layout() {
        Drawable bg = style.background;
        BitmapFont font = style.font;

        if (bg != null) {
            prefHeight = Math.max(bg.getTopHeight() + bg.getBottomHeight() + font.getCapHeight() - font.getDescent() * 2,
                    bg.getMinHeight());
        } else
            prefHeight = font.getCapHeight() - font.getDescent() * 2;

        float maxItemWidth = 0;
        for (int i = 0; i < items.size; i++)
            maxItemWidth = Math.max(font.getBounds(items.get(i).toString()).width, maxItemWidth);

        prefWidth = maxItemWidth;
        if (bg != null) prefWidth += bg.getLeftWidth() + bg.getRightWidth();

        KrList.Style listStyle = style.itemListStyle;
        ScrollPaneStyle scrollStyle = style.scrollStyle;
        prefWidth = Math.max(
                prefWidth,
                maxItemWidth
                        + (scrollStyle.background == null ? 0 : scrollStyle.background.getLeftWidth()
                        + scrollStyle.background.getRightWidth())
                        + listStyle.getSelection().getLeftWidth()
                        + listStyle.getSelection().getRightWidth()
                        + Math.max(style.scrollStyle.vScroll != null ? style.scrollStyle.vScroll.getMinWidth() : 0,
                        style.scrollStyle.vScrollKnob != null ? style.scrollStyle.vScrollKnob.getMinWidth() : 0));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        Drawable background = getBackground();
        BitmapFont font = style.font;
        Color fontColor = (isDisabled && style.disabledFontColor != null) ? style.disabledFontColor : style.fontColor;
        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (background != null) {
            background.draw(batch, x, y, width, height);
        }

        T selected = selection.first();
        if (selected != null) {
            String string = selected.toString();
            bounds.set(font.getBounds(string));
            if (background != null) {
                width -= background.getLeftWidth() + background.getRightWidth();
                height -= background.getBottomHeight() + background.getTopHeight();
                x += background.getLeftWidth();
                y += (int) (height / 2 + background.getBottomHeight() + bounds.height / 2);
            } else {
                y += (int) (height / 2 + bounds.height / 2);
            }
            int numGlyphs = font.computeVisibleGlyphs(string, 0, string.length(), width);
            font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
            font.draw(batch, string, x + 2, y, 0, numGlyphs);
        }
    }

    private Drawable getBackground() {
        Drawable background;
        if (isDisabled && style.backgroundDisabled != null)
            background = style.backgroundDisabled;
        else if (selectBoxList.hasParent() && style.backgroundOpen != null)
            background = style.backgroundOpen;
        else if (clickListener.isOver() && style.backgroundOver != null)
            background = style.backgroundOver;
        else if (style.background != null)
            background = style.background;
        else
            background = null;
        return background;
    }

    public T getSelected() {
        return selection.first();
    }

    /**
     * Sets the selection to only the passed item, if it is a possible choice, else selects the first item.
     */
    public void setSelected(T item) {
        if (items.contains(item, false))
            selection.set(item);
        else if (items.size > 0)
            selection.set(items.first());
        else
            selection.clear();
    }

    /**
     * @return The index of the first selected item. The top item has an index of 0. Nothing selected has an index of -1.
     */
    public int getSelectedIndex() {
        ObjectSet<T> selected = selection.items();
        return selected.size == 0 ? -1 : items.indexOf(selected.first(), false);
    }

    /**
     * Sets the selection to only the selected index.
     */
    public void setSelectedIndex(int index) {
        selection.set(items.get(index));
    }

    public void setDisabled(boolean disabled) {
        if (disabled && !this.isDisabled) hideList();
        this.isDisabled = disabled;
    }

    public float getPrefWidth() {
        validate();
        return prefWidth;
    }

    public float getPrefHeight() {
        validate();
        return prefHeight;
    }

    public void showList() {
        if (items.size == 0) return;
        selectBoxList.show(getStage());
    }

    public void hideList() {
        selectBoxList.hide();
    }

    /**
     * Returns the list shown when the select box is open.
     */
    public KrList<T> getList() {
        return selectBoxList.list;
    }

    /**
     * Returns the scroll pane containing the list that is shown when the select box is open.
     */
    public ScrollPane getScrollPane() {
        return selectBoxList;
    }

    protected void onShow(Actor selectBoxList, boolean below) {
        selectBoxList.getColor().a = 0;
        selectBoxList.addAction(fadeIn(0.3f, Interpolation.fade));
    }

    protected void onHide(Actor selectBoxList) {
        selectBoxList.getColor().a = 1;
        selectBoxList.addAction(sequence(fadeOut(0.15f, Interpolation.fade), removeActor()));
    }

    /**
     * @author Nathan Sweet
     */
    static class ComboBoxList<T> extends ScrollPane {
        private final KrComboBox<T> selectBox;
        int maxListCount;
        private final Vector2 screenPosition = new Vector2();
        final KrList<T> list;
        private InputListener hideListener;
        private Actor previousScrollFocus;

        public ComboBoxList(final KrComboBox<T> selectBox) {
            super(null, selectBox.style.scrollStyle);
            this.selectBox = selectBox;

            setOverscroll(false, false);
            setFadeScrollBars(false);

            list = new KrList(selectBox.style.itemListStyle);
            list.setTouchable(Touchable.disabled);
            setWidget(list);

            list.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    selectBox.selection.choose(list.getSelected());
                    hide();
                }

                public boolean mouseMoved(InputEvent event, float x, float y) {
                    list.setSelectedIndex(Math.min(selectBox.items.size - 1, (int) ((list.getHeight() - y) / list.getItemHeight())));
                    return true;
                }
            });

            addListener(new InputListener() {
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (toActor == null || !isAscendantOf(toActor)) list.getSelection().set(selectBox.getSelected());
                }
            });

            hideListener = new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Actor target = event.getTarget();
                    if (isAscendantOf(target)) return false;
                    list.getSelection().set(selectBox.getSelected());
                    hide();
                    return false;
                }

                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Keys.ESCAPE) hide();
                    return false;
                }
            };
        }

        public void show(Stage stage) {
            if (list.isTouchable()) return;

            stage.removeCaptureListener(hideListener);
            stage.addCaptureListener(hideListener);
            stage.addActor(this);

            selectBox.localToStageCoordinates(screenPosition.set(0, 0));

            // Show the list above or below the select box, limited to a number of items and the available height in the stage.
            float itemHeight = list.getItemHeight();
            float height = itemHeight * (maxListCount <= 0 ? selectBox.items.size : Math.min(maxListCount, selectBox.items.size));
            Drawable scrollPaneBackground = getStyle().background;
            if (scrollPaneBackground != null)
                height += scrollPaneBackground.getTopHeight() + scrollPaneBackground.getBottomHeight();
            Drawable listBackground = list.getStyle().getBackground();
            if (listBackground != null) height += listBackground.getTopHeight() + listBackground.getBottomHeight();

            float heightBelow = screenPosition.y;
            float heightAbove = stage.getCamera().viewportHeight - screenPosition.y - selectBox.getHeight();
            boolean below = true;
            if (height > heightBelow) {
                if (heightAbove > heightBelow) {
                    below = false;
                    height = Math.min(height, heightAbove);
                } else
                    height = heightBelow;
            }

            if (below)
                setY(screenPosition.y - height + 1);
            else
                setY(screenPosition.y + selectBox.getHeight() - 1);
            setX(screenPosition.x);
            setSize(Math.max(getPrefWidth(), selectBox.getWidth()), height);

            validate();
            scrollTo(0, list.getHeight() - selectBox.getSelectedIndex() * itemHeight - itemHeight / 2, 0, 0, true, true);
            updateVisualScroll();

            previousScrollFocus = null;
            Actor actor = stage.getScrollFocus();
            if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;
            stage.setScrollFocus(this);

            list.getSelection().set(selectBox.getSelected());
            list.setTouchable(Touchable.enabled);
            clearActions();
            selectBox.onShow(this, below);
        }

        public void hide() {
            if (!list.isTouchable() || !hasParent()) return;
            list.setTouchable(Touchable.disabled);

            Stage stage = getStage();
            if (stage != null) {
                stage.removeCaptureListener(hideListener);
                if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
                Actor actor = stage.getScrollFocus();
                if (actor == null || isAscendantOf(actor)) stage.setScrollFocus(previousScrollFocus);
            }

            clearActions();
            selectBox.onHide(this);
        }

        public void draw(Batch batch, float parentAlpha) {
            selectBox.localToStageCoordinates(temp.set(0, 0));
            if (!temp.equals(screenPosition)) {
                hide();
            }
            super.draw(batch, parentAlpha);
        }

        public void act(float delta) {
            super.act(delta);
            toFront();
        }
    }

    public interface Model<T> {
        List<T> getAvailableItems();

        T getSelectedItem();

        void setSelectedItem(T item);
    }

    @Builder
    static public class Style {

        @Getter private final BitmapFont font;

        @Getter private final Color fontColor = new Color(1, 1, 1, 1);

        @Getter private final Color disabledFontColor;

        @Getter private final Drawable background;

        @Getter private final ScrollPaneStyle scrollStyle;

        @Getter private final KrList.Style itemListStyle;

        @Getter private final Drawable backgroundOver;

        @Getter private final Drawable backgroundOpen;

        @Getter private final Drawable backgroundDisabled;
    }
}
