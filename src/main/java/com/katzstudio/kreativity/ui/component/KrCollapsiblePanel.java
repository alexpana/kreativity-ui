package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.KrFontAwesomeGlyph;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrMouseListener;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import lombok.Getter;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A panel that can collapse to save space.
 */
public class KrCollapsiblePanel extends KrWidget {

    public static final int SLIDE_SPEED = 500;

    public static final int COLLAPSED_HEIGHT = 24;

    private enum State {
        EXPANDED, COLLAPSED, COLLAPSING, EXPANDING
    }

    private KrLabel titleLabel;

    private KrIconPanel iconPanel;

    private final KrPanel headerPanel;

    @Getter private final KrPanel bodyPanel;

    private State state = State.EXPANDED;

    private float realPreferredHeight = 0.0f;

    private float actualHeight = 0.0f;

    public KrCollapsiblePanel(String title) {
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrWidget.class));

        headerPanel = createHeaderPanel(title);

        bodyPanel = createBodyPanel();

        setLayout(new KrBorderLayout(0, 0));

        add(headerPanel, KrBorderLayout.Constraint.NORTH);
        add(bodyPanel, KrBorderLayout.Constraint.CENTER);
    }

    private KrPanel createHeaderPanel(String title) {
        KrPanel headerPanel = new KrPanel();

        titleLabel = new KrLabel(title);

        iconPanel = new KrIconPanel(KrFontAwesomeGlyph.CARET_DOWN);

        headerPanel.setLayout(new KrBorderLayout());
        headerPanel.setPadding(new KrPadding(4, 4, 4, 4));
        headerPanel.setBackground(getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_DARK));
        headerPanel.add(titleLabel, KrBorderLayout.Constraint.WEST);

        headerPanel.addMouseListener(new KrMouseListener.KrMouseAdapter() {
            @Override
            public void mousePressed(KrMouseEvent event) {
                toggleState();
            }
        });

        return headerPanel;
    }

    private KrPanel createBodyPanel() {
        return new KrPanel();
    }

    public boolean isCollapsed() {
        return state == State.COLLAPSED;
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

    private void setState(State newState) {
        state = newState;

        if (state == State.COLLAPSED || state == State.COLLAPSING) {
            iconPanel.setIconGlyph(KrFontAwesomeGlyph.CARET_RIGHT);
        } else {
            iconPanel.setIconGlyph(KrFontAwesomeGlyph.CARET_DOWN);
        }

        if (state == State.COLLAPSED) {
            bodyPanel.setVisible(false);
        } else {
            bodyPanel.setVisible(true);
        }
    }

    private void collapse() {
        setState(State.COLLAPSING);
    }

    private void expand() {
        setState(State.EXPANDING);
    }

    private void toggleState() {
        if (state == State.COLLAPSED || state == State.COLLAPSING) {
            expand();
        } else {
            collapse();
        }
    }

    private boolean collapseFinished() {
        return actualHeight <= COLLAPSED_HEIGHT;
    }

    private boolean expandFinished() {
        return actualHeight >= realPreferredHeight;
    }

    @Override
    public void update(float deltaSeconds) {
        super.update(deltaSeconds);

        if (state == State.EXPANDED || state == State.COLLAPSED) {
            return;
        }

        float slideOffset = SLIDE_SPEED * deltaSeconds;

        if (state == State.COLLAPSING) {
            if (!collapseFinished()) {
                if (actualHeight - slideOffset < COLLAPSED_HEIGHT) {
                    slideOffset = actualHeight - COLLAPSED_HEIGHT;
                }
                actualHeight -= slideOffset;
            } else {
                setState(State.COLLAPSED);
                actualHeight = COLLAPSED_HEIGHT;
            }
        }

        if (state == State.EXPANDING) {
            if (!expandFinished()) {
                if (actualHeight + slideOffset > realPreferredHeight) {
                    slideOffset = realPreferredHeight - actualHeight;
                }
                actualHeight += slideOffset;
            } else {
                setState(State.EXPANDED);
                actualHeight = realPreferredHeight;
            }
        }

        setMinHeight((int) actualHeight);
        setMaxHeight((int) actualHeight);
        setPreferredHeight((int) actualHeight);
        invalidate();
    }

    @Override
    public void setGeometry(float x, float y, float width, float height) {
        super.setGeometry(x, y, width, height);
        if (state != State.COLLAPSING && state != State.EXPANDING) {
            actualHeight = height;
        }
    }

    @Override
    public void validate() {
        super.validate();

        if (bodyPanel.isVisible()) {
            realPreferredHeight = getLayout().getPreferredSize().y;
        }
    }
}
