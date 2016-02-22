package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.katzstudio.kreativity.ui.component.KrList;

/**
 * Created by alex
 * on 30.03.2015.
 */
public class KrToolkit {

    private static long DOUBLE_CLICK_THRESHOLD = 500;

    private static KreativitySkin.Cursor currentCursor;

    public static void setCursor(KreativitySkin.Cursor cursor) {
        if (cursor == null) {
            cursor = KreativitySkin.Cursor.POINTER;
        }
        currentCursor = cursor;
        Gdx.input.setCursorImage(cursor.getCursorPixmap(), cursor.getHotSpotX(), cursor.getHotSpotY());
    }

    public static KreativitySkin.Cursor getCursor() {
        return currentCursor;
    }

    public static void centerWindow(Window window) {
        window.setPosition((int) ((Gdx.graphics.getWidth() - window.getWidth()) / 2), (int) ((Gdx.graphics.getHeight() - window.getHeight()) / 2));
    }

    public static void centerWindowOnTop(Window window) {
        window.setPosition((int) ((Gdx.graphics.getWidth() - window.getWidth()) / 2), (int) (Gdx.graphics.getHeight() - window.getHeight() - 50));
    }

    public static void addDoubleClickListener(final Actor actor, final ActionListener listener) {
        actor.addListener(new InputListener() {
            long previousClickTime = 0;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - previousClickTime < DOUBLE_CLICK_THRESHOLD) {
                    if (listener != null) {
                        listener.action();
                    }
                } else {
                    previousClickTime = currentTime;
                }
                return true;
            }
        });
    }

    public static void addClickListener(final Actor actor, final ActionListener listener) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (listener != null) {
                    listener.action();
                    return true;
                }
                return false;
            }
        });
    }

    public static void addInputListener(Actor actor, final InputListener inputListener) {
        actor.addListener(inputListener);
    }

    public static void addActionListener(Actor actor, final ActionListener actionListener) {
        actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionListener.action();
            }
        });
    }

    public static <T> void addSelectionListener(final KrList<T> list, final SingleSelectionListener listener) {
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.selectionChanged(list.getSelectedIndex());
            }
        });
    }

    public static Drawable createColorDrawable(Color color) {
        // TODO(alex): cache this !
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    public static void ensureUniqueStyle(Label label) {
        label.setStyle(new Label.LabelStyle(label.getStyle()));
    }

    public static void ensureUniqueStyle(TextField textField) {
        textField.setStyle(new TextField.TextFieldStyle(textField.getStyle()));
    }

    public static Vector2 getPreferredSize(Actor actor) {
        if (actor instanceof Widget) {
            Widget widget = (Widget) actor;
            return new Vector2(widget.getPrefWidth(), widget.getPrefHeight());
        }

        if (actor instanceof WidgetGroup) {
            WidgetGroup widgetGroup = (WidgetGroup) actor;
            return new Vector2(widgetGroup.getPrefWidth(), widgetGroup.getPrefHeight());
        }

        throw new IllegalArgumentException("Actor is not a widget");
    }

    public static Vector2 getMinSize(Actor actor) {
        if (actor instanceof Widget) {
            Widget widget = (Widget) actor;
            return new Vector2(widget.getMinWidth(), widget.getMinHeight());
        }

        if (actor instanceof WidgetGroup) {
            WidgetGroup widgetGroup = (WidgetGroup) actor;
            return new Vector2(widgetGroup.getMinWidth(), widgetGroup.getMinHeight());
        }

        throw new IllegalArgumentException("Actor is not a widget");
    }

    public static Vector2 getMaxSize(Actor actor) {
        if (actor instanceof Widget) {
            Widget widget = (Widget) actor;
            return new Vector2(widget.getMaxWidth(), widget.getMaxHeight());
        }

        if (actor instanceof WidgetGroup) {
            WidgetGroup widgetGroup = (WidgetGroup) actor;
            return new Vector2(widgetGroup.getMaxWidth(), widgetGroup.getMaxHeight());
        }

        throw new IllegalArgumentException("Actor is not a widget");
    }

    public interface SingleSelectionListener {
        void selectionChanged(int selectionIndex);
    }

    public interface ActionListener {
        void action();
    }
}
