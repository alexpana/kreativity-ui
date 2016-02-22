package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import lombok.Getter;
import lombok.Setter;

/**
 * created by Alex
 * on 3/28/2015.
 */
public abstract class KrAbstractDialog<E> extends Window {
    @Setter
    private Listener<E> listener;

    private final TextButton cancelButton;

    @Getter
    private final TextButton actionButton;

    @Getter
    private final UiContext uiContext;

    public KrAbstractDialog(UiContext uiContext, String title) {
        super(title, uiContext.getSkin());
        this.uiContext = uiContext;

        actionButton = new TextButton("Save", uiContext.getSkin());
        actionButton.pad(0, 5, 3, 5);

        cancelButton = new TextButton("Cancel", uiContext.getSkin());
        cancelButton.pad(0, 5, 3, 5);

        initListeners();

        setResizable(false);
        setMovable(false);
        setModal(false);
    }

    public void packLayout() {
        Table buttonsTable = new Table();
        buttonsTable.add(actionButton).right().padRight(2);
        buttonsTable.add(cancelButton).left().row();

        add(getContent()).padBottom(8).padTop(4).expand().fill().row();
        add(buttonsTable);

        pack();
    }

    private void initListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    hide();
                    return true;
                }
                return false;
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        actionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                doAction();
            }
        });
    }

    protected abstract Actor getContent();

    protected abstract void doAction();

    public abstract void show();

    public abstract void hide();

    protected void notifyListener(E event) {
        if (listener != null) {
            listener.dialogAccepted(event);
        }
    }

    public interface Listener<E> {
        void dialogAccepted(E event);
    }
}
