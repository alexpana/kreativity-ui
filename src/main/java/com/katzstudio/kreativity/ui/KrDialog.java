package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Simple implementation of a {@link KrAbstractDialog} that displays some content.
 */
public class KrDialog extends KrAbstractDialog<Object> {

    private final Actor content;

    public KrDialog(UiContext uiContext, String title, Actor content) {
        super(uiContext, title);
        this.content = content;
    }

    @Override
    protected Actor getContent() {
        return content;
    }

    @Override
    protected void doAction() {
        hide();
        notifyListener(null);
    }

    @Override
    public void show() {
        getUiContext().getStage().addActor(this);
        this.setVisible(true);
        this.setSize(getPrefWidth(), getPrefHeight());
        KrToolkit.centerWindow(this);
    }

    @Override
    public void hide() {
        this.setVisible(false);
        remove();
    }
}
