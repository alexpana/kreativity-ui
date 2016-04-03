package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrActionListener;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;
import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A menu widget displays a vertical list of actions that can be
 * performed by clicking on them.
 */
public class KrMenu extends KrWidget<KrWidgetStyle> {

    private final List<KrMenuItem> menuItems = new ArrayList<>();

    private KrLabel titleLabel;

    private KrPopup popup;

    @Getter private Vector2 displayLocation = Vector2.Zero.cpy();

    public KrMenu() {
        popup = new KrPopup();
        popup.setContentWidget(this);

        setStyle(getDefaultToolkit().getSkin().getWidgetStyle());
        setLayout(new KrFlowLayout(VERTICAL, 0, 0));
        setPadding(new KrPadding(1));
        setBackground(getDefaultToolkit().getDrawable(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER)));
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
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

    @SuppressWarnings("unused")
    public void removeMenuItem(KrMenuItem item) {
        menuItems.remove(item);
        item.parentMenu = null;
        updateItems();
    }

    public void showAt(Vector2 position) {
        popup.setSize(this.getPreferredSize());
        popup.show(position);
    }

    public void hide() {
        popup.hide();
    }

    private void updateItems() {
        removeAll();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < menuItems.size(); ++i) {
            add(menuItems.get(i));
        }
        popup.validate();
    }

    public static class KrMenuItem extends KrWidget<KrWidgetStyle> {

        private final Drawable selectedBackground;

        private final Drawable defaultBackground;

        private final KrActionListener actionListener;

        private KrMenu parentMenu;

        public KrMenuItem(String title, KrActionListener actionListener) {
            this.text.setString(title);
            this.actionListener = actionListener;
            KrToolkit toolkit = getDefaultToolkit();

            setStyle(toolkit.getSkin().getWidgetStyle());

            Color selectionColor = toolkit.getSkin().getColor(KrSkin.ColorKey.SELECTION_BACKGROUND);
            selectedBackground = toolkit.getDrawable(selectionColor);
            defaultBackground = toolkit.getDrawable(toolkit.getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));

            setBackground(defaultBackground);
            setPadding(new KrPadding(5, 6));
        }

        @Override
        public void ensureUniqueStyle() {
            if (style == getDefaultToolkit().getSkin().getWidgetStyle()) {
                style = new KrWidgetStyle(style);
            }
        }

        @Override
        protected void drawSelf(KrRenderer renderer) {
            renderer.setBrush(getBackground());
            renderer.fillRect(0, 0, getWidth(), getHeight());

            Rectangle alignmentReference = rectangles(0.0f, 0.0f, getWidth(), getHeight()).shrink(getPadding()).value();
            Vector2 textPosition = KrAlignmentTool.alignRectangles(text.getBounds(), alignmentReference, KrAlignment.MIDDLE_LEFT);

            renderer.drawText(text.getString(), textPosition);

            Pools.free(textPosition);
            Pools.free(alignmentReference);
        }

        @Override
        public Vector2 calculatePreferredSize() {
            return rectangles(text.getBounds()).expand(getPadding()).size(new Vector2());
        }

        @Override
        protected boolean enterEvent(KrEnterEvent event) {
            setBackground(selectedBackground);
            event.accept();
            return true;
        }

        @Override
        protected boolean exitEvent(KrExitEvent event) {
            setBackground(defaultBackground);
            event.accept();
            return true;
        }

        @Override
        protected boolean mouseReleasedEvent(KrMouseEvent event) {
            if (actionListener != null) {
                actionListener.actionPerformed();
            }
            parentMenu.hide();
            event.accept();
            return true;
        }
    }
}
