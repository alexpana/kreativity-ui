package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;
import lombok.Getter;

/**
 * A panel that can collapse to save space.
 */
public class KrCollapsiblePanel extends Table {

    public static final int SLIDE_SPEED = 1000;

    private static final int HEADER_HEIGHT = 18;

    private static final int STATE_EXPANDED = 0;

    private static final int STATE_COLLAPSED = 1;

    private static final int STATE_COLLAPSING = 2;

    private static final int STATE_EXPANDING = 3;

    public static final int COLLAPSED_HEIGHT = 24;

    private static final int CONTENT_PADDING = 4;

    @Getter private String title;

    @Getter private Actor content;

    private final Label titleLabel;

    private final KrIconPanel iconPanel;

    private final KrContext uiContext;

    private int state = STATE_EXPANDED;

    private float realPreferredHeight = 0.0f;

    private float actualHeight = 0.0f;

    public KrCollapsiblePanel(KrContext uiContext, String title) {
        this.uiContext = uiContext;
        this.title = title;

        titleLabel = new Label(title, uiContext.getSkin());
        titleLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toggleState();
                return true;
            }
        });

        iconPanel = new KrIconPanel(KrFontAwesomeGlyph.CARET_DOWN);

        background(uiContext.getSkin().getDrawable("collapsiblepanel.background"));
        getBackground().setTopHeight(2);

        add(titleLabel);
//        add(iconPanel);
    }


    public void setContent(Actor content) {
        if (this.content != null) {
            this.removeActor(this.content);
        }

        this.content = content;

        if (this.content != null) {
            add(this.content);
        }
    }

    @Override
    public void layout() {
        titleLabel.setBounds(20, getHeight() - HEADER_HEIGHT, getWidth(), HEADER_HEIGHT);
        iconPanel.setGeometry(2, getHeight() - HEADER_HEIGHT + 1, HEADER_HEIGHT - 5, HEADER_HEIGHT);
        content.setBounds(0, 0, getWidth(), getHeight() - HEADER_HEIGHT - CONTENT_PADDING);
    }

    @Override
    protected void sizeChanged() {
        System.out.println("getHeight() = " + getHeight());
    }

    public boolean isCollapsed() {
        return state == STATE_COLLAPSED;
    }

    public void setCollapsed(boolean collapsed) {
        if (collapsed == isCollapsed()) {
            return;
        }

        if (collapsed) {
            collapse();
        } else {
            expand();
        }
    }

    private void setState(int newState) {
        state = newState;

        if (state == STATE_COLLAPSED || state == STATE_COLLAPSING) {
            iconPanel.setIconGlyph(KrFontAwesomeGlyph.CARET_RIGHT);
        } else {
            iconPanel.setIconGlyph(KrFontAwesomeGlyph.CARET_DOWN);
        }

        if (state == STATE_COLLAPSED) {
            content.setVisible(false);
        } else {
            content.setVisible(true);
        }
    }

    private void collapse() {
        setState(STATE_COLLAPSING);
    }

    private void expand() {
        setState(STATE_EXPANDING);
    }

    private void toggleState() {
        if (state == STATE_COLLAPSED || state == STATE_COLLAPSING) {
            expand();
        } else {
            collapse();
        }
    }

    private boolean collapseFinished() {
        return getHeight() <= COLLAPSED_HEIGHT;
    }

    private boolean expandFinished() {
        return getHeight() >= realPreferredHeight;
    }

    @Override
    public void act(float delta) {
        int slideOffset = (int) (SLIDE_SPEED * delta);

        if (state == STATE_COLLAPSING) {
            if (!collapseFinished()) {
                if (actualHeight - slideOffset < COLLAPSED_HEIGHT) {
                    slideOffset = (int) (actualHeight - COLLAPSED_HEIGHT);
                }
                actualHeight -= slideOffset;
                setY(getY() + slideOffset);
            } else {
                setState(STATE_COLLAPSED);
                actualHeight = COLLAPSED_HEIGHT;
            }
        }

        if (state == STATE_EXPANDING) {
            if (!expandFinished()) {
                if (actualHeight + slideOffset > realPreferredHeight) {
                    slideOffset = (int) (realPreferredHeight - actualHeight);
                }
                actualHeight += slideOffset;
                setY(getY() - slideOffset);
            } else {
                setState(STATE_EXPANDED);
                actualHeight = realPreferredHeight;
            }
        }

        titleLabel.setText(getTitle());
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
//        realPreferredHeight = (isCollapsed() ? COLLAPSED_HEIGHT : HEADER_HEIGHT) + (content != null ? KrToolkit.getPreferredSize(content).y : 0) + 2 * CONTENT_PADDING;

        if (!isCollapsed()) {
            actualHeight = realPreferredHeight;
        }
    }

    @Override
    public float getPrefHeight() {
        return actualHeight;
    }

    @Override
    public float getMaxHeight() {
        if (isCollapsed()) {
            return COLLAPSED_HEIGHT;
        } else return 100000;
    }

    @Override
    public float getMinHeight() {
        if (isCollapsed()) {
            return COLLAPSED_HEIGHT;
        }

        return super.getMinHeight();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        setHeight(height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        layout();
        super.draw(batch, parentAlpha);
    }
}
