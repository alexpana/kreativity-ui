package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.katzstudio.kreativity.ui.KrColor;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrToolkit;
import lombok.Getter;

/**
 * A panel component that displays scrollbars when it's size is smaller then it's children.
 */
public class KrScrollPanel extends Table {

    private static final float SCROLLBAR_SIZE = 5;

    private final KrScrollBar verticalScrollBar;

    private final KrScrollBar horizontalScrollBar;

    private final Actor innerComponent;

    @Getter
    private boolean expandY;

    @Getter
    private boolean expandX;

    public KrScrollPanel(KrContext uiContext, Actor innerComponent) {
        this.verticalScrollBar = new KrScrollBar();
        this.horizontalScrollBar = new KrScrollBar();
        this.innerComponent = innerComponent;

        add(this.innerComponent);
        add(verticalScrollBar);
        add(horizontalScrollBar);

        verticalScrollBar.addScrollListener(event -> layout());
        horizontalScrollBar.addScrollListener(event -> layout());

        setBackground(KrToolkit.createColorDrawable(KrColor.TRANSPARENT));

        setClip(true);

        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                uiContext.getStage().setScrollFocus(verticalScrollBar);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                uiContext.getStage().setScrollFocus(null);
            }
        });
    }

    public void setExpandX(boolean expandX) {
        this.expandX = expandX;
        layout();
    }

    public void setExpandY(boolean expandY) {
        this.expandY = expandY;
        layout();
    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public void layout() {
        float childWidth;
        float childHeight;

        Vector2 childPreferredSize = KrToolkit.getPreferredSize(innerComponent);
        childWidth = childPreferredSize.x;
        childHeight = childPreferredSize.y;

//        if (expandX) {
            childWidth = getWidth();
//        }

        if (expandY) {
            childHeight = Math.max(getHeight(), childHeight);
        }

        boolean requiresVerticalScrollbar = this.getHeight() < childHeight;
        boolean requiresHorizontalScrollbar = this.getWidth() < childWidth;

        float childX = 0;
        float childY = getHeight() - childHeight;

        if (requiresHorizontalScrollbar) {
            float widthOverflow = childWidth - this.getWidth();

            horizontalScrollBar.setVisible(true);
            horizontalScrollBar.setBounds(0, 0, getWidth() - SCROLLBAR_SIZE, SCROLLBAR_SIZE);
            horizontalScrollBar.setMaxValue(widthOverflow);
            childX += horizontalScrollBar.getCurrentValue();
        } else {
            horizontalScrollBar.setVisible(false);
        }

        if (requiresVerticalScrollbar) {
            float heightOverflow = childHeight - this.getHeight();

            verticalScrollBar.setVisible(true);
            verticalScrollBar.setBounds(
                    getWidth() - SCROLLBAR_SIZE,
                    requiresHorizontalScrollbar ? SCROLLBAR_SIZE : 0,
                    SCROLLBAR_SIZE,
                    getHeight());
            verticalScrollBar.setMaxValue(heightOverflow);

            childY += verticalScrollBar.getCurrentValue();
        } else {
            verticalScrollBar.setVisible(false);
        }

        float horizontalPadding = (childWidth >= getWidth() - SCROLLBAR_SIZE && requiresVerticalScrollbar) ? SCROLLBAR_SIZE : 0;
        float verticalPadding = (childHeight >= getHeight() - SCROLLBAR_SIZE && requiresHorizontalScrollbar) ? SCROLLBAR_SIZE : 0;

        innerComponent.setBounds(
                (int) childX,
                (int) childY,
                (int) childWidth - horizontalPadding,
                (int) childHeight - verticalPadding);
    }

    @Override
    public float getPrefWidth() {
        return KrToolkit.getPreferredSize(innerComponent).x;
    }

    public float getPrefHeight() {
        return KrToolkit.getPreferredSize(innerComponent).y;
    }

    @Override
    protected void sizeChanged() {
        layout();
    }
}
