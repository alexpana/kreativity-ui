package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.math.KrRange;
import lombok.Getter;

import static com.katzstudio.kreativity.ui.component.KrScrollBar.Orientation.HORIZONTAL;
import static com.katzstudio.kreativity.ui.component.KrScrollBar.Orientation.VERTICAL;

/**
 * A panel component that displays scrollbars when it's size is smaller then its children.
 */
public class KrScrollPanel extends KrPanel {

    @Getter private final KrScrollBar verticalScrollBar;

    @Getter private final KrScrollBar horizontalScrollBar;

    @Getter private final KrWidget innerComponent;

    public KrScrollPanel(KrWidget innerComponent) {
        this.verticalScrollBar = new KrScrollBar(VERTICAL);
        this.horizontalScrollBar = new KrScrollBar(HORIZONTAL);
        this.innerComponent = innerComponent;

        setLayout(new LayoutManager());
        add(innerComponent);
        add(verticalScrollBar);
        add(horizontalScrollBar);

        verticalScrollBar.addScrollListener(this::onVerticalScroll);
        horizontalScrollBar.addScrollListener(this::onHorizontalScroll);
    }

    private void onVerticalScroll(float value) {
        innerComponent.setPosition(innerComponent.getX(), -value);
    }

    private void onHorizontalScroll(float value) {
        innerComponent.setPosition(-value, innerComponent.getY());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return innerComponent.getPreferredSize();
    }

    @Override
    protected boolean scrollEvent(KrScrollEvent event) {
        verticalScrollBar.handle(event);
        return true;
    }

    private class LayoutManager implements KrLayout {

        private Vector2 lastResize = new Vector2(0, 0);

        @Override
        public void setGeometry(Rectangle geometry) {
            if (geometry.width == lastResize.x && geometry.height == lastResize.y) {
                return;
            }

            lastResize = new Vector2(geometry.width, geometry.height);

            float childWidth = Math.max(innerComponent.getPreferredSize().x, geometry.width);
            float childHeight = Math.max(innerComponent.getPreferredSize().y, geometry.height);

            verticalScrollBar.setValue(0);
            horizontalScrollBar.setValue(0);

            boolean horizontalScrollRequired = false;
            boolean verticalScrollRequired = false;

            if (childWidth > geometry.getWidth()) {
                horizontalScrollRequired = true;
                float horizontalOverflow = childWidth - geometry.getWidth();
                horizontalScrollBar.setValueRange(new KrRange(0, horizontalOverflow));
            } else {
                horizontalScrollBar.setValueRange(0, 0);
            }

            if (childHeight > geometry.getHeight()) {
                verticalScrollRequired = true;
                float verticalOverflow = childHeight - geometry.getHeight();
                verticalScrollBar.setValueRange(0, verticalOverflow);
            } else {
                verticalScrollBar.setValueRange(0, 0);
            }

            float vScrollbarSize = verticalScrollRequired ? verticalScrollBar.getPreferredSize().x : 0;
            float hScrollbarSize = horizontalScrollRequired ? horizontalScrollBar.getPreferredSize().y : 0;

            if (horizontalScrollRequired) {
                verticalScrollBar.setValueRange(0, verticalScrollBar.getValueRange().getMax() + hScrollbarSize);
            }

            if (verticalScrollRequired) {
                horizontalScrollBar.setValueRange(0, horizontalScrollBar.getValueRange().getMax() + vScrollbarSize);
            }

            innerComponent.setBounds(0, 0, childWidth, childHeight);
            verticalScrollBar.setBounds(geometry.width - vScrollbarSize, 0, vScrollbarSize, geometry.height);
            horizontalScrollBar.setBounds(0, geometry.height - hScrollbarSize, geometry.width - vScrollbarSize, hScrollbarSize);
        }

        @Override
        public Vector2 getMinSize() {
            return innerComponent.getMinSize();
        }

        @Override
        public Vector2 getMaxSize() {
            return innerComponent.getMaxSize();
        }

        @Override
        public Vector2 getPreferredSize() {
            return innerComponent.getPreferredSize();
        }

        @Override
        public void addWidget(KrWidget child, Object layoutConstraint) {
        }

        @Override
        public void removeWidget(KrWidget child) {
        }
    }
}
