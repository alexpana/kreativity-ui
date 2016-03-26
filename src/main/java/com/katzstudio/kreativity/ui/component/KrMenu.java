package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrContext;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A menu widget displays a vertical list of actions that can be
 * performed by clicking on them.
 */
public class KrMenu extends Table {

    private static final List<KrMenu> INSTANCES = Lists.newArrayList();

    private static final Listener EMPTY_LISTENER = new EmptyListener();

    private final List<Item> items = Lists.newArrayList();

    private final List<MenuItemEntry> menuItemEntries = Lists.newArrayList();

    @Setter
    private Listener listener = EMPTY_LISTENER;

    private Label titleLabel;

    @Getter
    private Vector2 displayLocation = Vector2.Zero.cpy();

    private KrContext context;

    public KrMenu(KrContext context) {
        this.context = context;

        titleLabel = new Label("", context.getSkin());

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(titleLabel.getStyle());
        titleLabelStyle.fontColor = new Color(0x808080FF);

        titleLabel.setStyle(titleLabelStyle);

        this.setBackground(new NinePatchDrawable(new NinePatch(context.getSkin().getRegion("panel-w-border"), 1, 1, 2, 1)));

        INSTANCES.add(this);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void addItem(Item item) {
        items.add(item);
        updateItems();
    }

    @SuppressWarnings("unused")
    public void removeItem(Item item) {
        items.remove(item);
        updateItems();
    }

    public void showAt(Vector2 position) {
        hideAllMenuInstances();

        refreshItems();

        context.getStage().addActor(this);
        setVisible(true);
        setBounds(position.x, Gdx.graphics.getHeight() - (position.y + getPrefHeight()), getPrefWidth(), getPrefHeight());
        displayLocation = position;
    }

    public void hide() {
        for (MenuItemEntry entry : menuItemEntries) {
            entry.setHovered(false);
        }

        setVisible(false);
        remove();
    }

    public void setMenuListener(Listener listener) {
        if (listener == null) {
            this.listener = EMPTY_LISTENER;
        } else {
            this.listener = listener;
        }
    }


    private void updateItems() {
        clear();
        menuItemEntries.clear();
        add(titleLabel).left().padLeft(4).row();
        for (Item item : items) {
            MenuItemEntry menuItemEntry = new MenuItemEntry(this, item, context.getSkin());
            menuItemEntries.add(menuItemEntry);
            add(menuItemEntry).fillX().expandX().left().row();
        }
    }

    private void refreshItems() {
        clear();
        add(titleLabel).left().padLeft(4).padBottom(3).row();
        for (MenuItemEntry entry : menuItemEntries) {
            entry.setHovered(false);
            add(entry).fillX().expandX().left().row();
        }
    }

    private void itemActivated(MenuItemEntry item) {
        listener.itemActivated(item.getItem());
        hide();
    }

    public void dispose() {
        INSTANCES.remove(this);
    }

    public static void hideAllMenuInstances() {
        INSTANCES.forEach(KrMenu::hide);
    }

    public interface Listener {
        void itemActivated(Item item);
    }

    private static class EmptyListener implements Listener {
        @Override
        public void itemActivated(Item item) {
        }
    }

    private static class MenuItemEntry extends Table {
        private final KrMenu menu;

        @Getter
        private final Item item;

        private boolean isHovered;

        private static final Color HOVER_COLOR = new Color(0x00000033);

        private static final Drawable hoveredBackground = getDefaultToolkit().getDrawable(HOVER_COLOR);

        // Apparently we receive an exit event after a click.
        boolean ignoreNextExitEvent = false;

        public MenuItemEntry(KrMenu menu, Item item, Skin skin) {
            this.menu = menu;
            this.item = item;
            addItemIcon(item);

            Cell<Label> cell = addItemText(item, skin);

            initListeners();

            pad(1, 6, 4, 6);
            padRight(8);
        }

        private Cell<Label> addItemText(Item item, Skin skin) {
            return add(new Label(item.getTitle(), skin)).fillX().expandX();
        }

        private void addItemIcon(Item item) {
            add(new Image(item.getIcon()));
        }

        private void initListeners() {
            this.addListener(new InputListener() {

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    setHovered(true);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (!ignoreNextExitEvent) {
                        setHovered(false);
                    } else {
                        ignoreNextExitEvent = false;
                    }
                }

                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    setHovered(true);
                    return true;
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    menu.itemActivated(MenuItemEntry.this);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    ignoreNextExitEvent = true;
                    if (new Rectangle(getX(), getY(), getWidth(), getHeight()).contains(x, y)) {
                        setHovered(true);
                    }
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    setHovered(false);
                }
            });
        }

        private void setHovered(boolean hovered) {
            if (isHovered != hovered) {
                isHovered = hovered;
                setBackground(isHovered ? hoveredBackground : null);
            }

            if (!isHovered) {
                ignoreNextExitEvent = false;
            }
        }
    }

    public static class Item {
        @Getter private final String title;

        @Getter private final List<Item> children = Lists.newArrayList();

        @Getter private Drawable icon;

        public Item(String title) {
            this.title = title;
        }

        public void addChild(Item item) {
            children.add(item);
        }

        public void setIcon(TextureRegion icon) {
            this.icon = new TextureRegionDrawable(icon);
        }
    }
}
