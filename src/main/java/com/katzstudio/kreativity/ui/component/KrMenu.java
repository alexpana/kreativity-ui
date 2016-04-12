package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.*;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrActionListener;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;
import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A menu widget displays a vertical list of actions that can be
 * performed by clicking on them.
 */
public class KrMenu extends KrWidget {

    private final List<KrMenuItem> menuItems = new ArrayList<>();

    private final List<KrMenuListener> listeners = new ArrayList<>();

    @Getter private KrPopup popup;

    @Getter private Vector2 displayLocation = Vector2.Zero.cpy();

    public KrMenu() {
        popup = new KrPopup();
        popup.setContentWidget(this);

        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrWidget.class));
        setLayout(new KrFlowLayout(VERTICAL, 0, 0));
        setPadding(new KrPadding(1));
        setBackground(getDefaultToolkit().getDrawable(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER)));
    }

    public void addMenuItem(KrMenuItem item) {
        menuItems.add(item);
        item.parentMenu = this;
        updateItems();
    }

    public void addMenuItem(int index, KrMenuItem item) {
        menuItems.add(index, item);
        item.parentMenu = this;
        updateItems();
    }

    public void addMenuItems(Collection<KrMenuItem> menuItems) {
        this.menuItems.addAll(menuItems);
        updateItems();
    }

    @SuppressWarnings("unused")
    public void removeMenuItem(KrMenuItem item) {
        menuItems.remove(item);
        item.parentMenu = null;
        updateItems();
    }

    public void clearMenuItems() {
        menuItems.clear();
        updateItems();
    }

    public void showAt(Vector2 position) {
        showAt((int) position.x, (int) position.y);
    }

    public void showAt(int x, int y) {
        popup.setSize(this.getPreferredSize());
        popup.show(x, y);
    }

    public void hide() {
        popup.hide();
    }

    public boolean isShowing() {
        return popup.isVisible();
    }

    private void updateItems() {
        removeAll();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < menuItems.size(); ++i) {
            add(menuItems.get(i));
        }
        popup.validate();
    }

    public void addMenuListener(KrMenuListener listener) {
        listeners.add(listener);
    }

    public void removeMenuListener(KrMenuListener listener) {
        listeners.remove(listener);
    }

    protected void notifyItemSelected(KrMenuItem selectedItem) {
        for (int i = 0; i < listeners.size(); ++i) {
            listeners.get(i).itemSelected(selectedItem);
        }
    }

    public static class KrMenuItem extends KrWidget {

        private final Drawable selectedBackground;

        private final Drawable defaultBackground;

        private final KrActionListener actionListener;

        private KrMenu parentMenu;

        public KrMenuItem(String title) {
            this(title, null);
        }

        public KrMenuItem(String title, KrActionListener actionListener) {
            this.text.setString(title);
            this.actionListener = actionListener;
            KrToolkit toolkit = getDefaultToolkit();

            setDefaultStyle(toolkit.getSkin().getStyle(KrWidget.class));

            Color selectionColor = toolkit.getSkin().getColor(KrSkin.ColorKey.SELECTION_BACKGROUND);
            selectedBackground = toolkit.getDrawable(selectionColor);
            defaultBackground = toolkit.getDrawable(toolkit.getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));

            setBackground(defaultBackground);
            setPadding(new KrPadding(5, 6));
        }

        @Override
        protected void drawSelf(KrRenderer renderer) {
            renderer.setBrush(getBackground());
            renderer.fillRect(0, 0, getWidth(), getHeight());

            Rectangle alignmentReference = rectangles(0.0f, 0.0f, getWidth(), getHeight()).shrink(getPadding()).value();
            Vector2 textPosition = KrAlignmentTool.alignRectangles(text.getBounds(), alignmentReference, KrAlignment.MIDDLE_LEFT);

            renderer.setPen(1, getForeground());
            renderer.drawText(text.getString(), textPosition);

            Pools.free(textPosition);
            Pools.free(alignmentReference);
        }

        @Override
        public Vector2 calculatePreferredSize() {
            return rectangles(text.getBounds()).expand(getPadding()).size(new Vector2());
        }

        @Override
        protected void enterEvent(KrEnterEvent event) {
            super.enterEvent(event);

            setBackground(selectedBackground);
            event.accept();
        }

        @Override
        protected void exitEvent(KrExitEvent event) {
            super.exitEvent(event);

            setBackground(defaultBackground);
            event.accept();
        }

        @Override
        protected void mouseReleasedEvent(KrMouseEvent event) {
            super.mouseReleasedEvent(event);

            notifySelected();
            parentMenu.hide();
            event.accept();
        }

        private void notifySelected() {
            if (actionListener != null) {
                actionListener.actionPerformed();
            }

            if (parentMenu != null) {
                parentMenu.notifyItemSelected(this);
            }
        }
    }

    public static class KrMenuItemSeparator extends KrMenuItem {

        public KrMenuItemSeparator() {
            super("", null);
        }

        @Override
        protected void enterEvent(KrEnterEvent event) {
            // prevent super.enterEvent
        }

        @Override
        protected void exitEvent(KrExitEvent event) {
            // prevent super.exitEvent
        }

        @Override
        public Vector2 calculatePreferredSize() {
            return new Vector2(0, 7);
        }

        @Override
        protected void drawSelf(KrRenderer renderer) {
            super.drawSelf(renderer);

            renderer.setPen(1, getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER));

            int y = (int) getHeight() / 2;
            renderer.drawLine(0, y, getWidth(), y);
        }
    }

    public interface KrMenuListener {
        void itemSelected(KrMenuItem selectedItem);
    }
}
