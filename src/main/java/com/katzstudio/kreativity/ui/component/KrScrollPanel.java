package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.math.KrRange;
import lombok.Getter;

import static com.katzstudio.kreativity.ui.KrOrientation.HORIZONTAL;
import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;

/**
 * A panel component that displays scrollBars when it's size is smaller then its children.
 */
public class KrScrollPanel extends KrPanel {

    @Getter private final KrScrollBar verticalScrollBar;

    @Getter private final KrScrollBar horizontalScrollBar;

    @Getter private final KrWidget innerComponent;

    public KrScrollPanel(KrWidget innerComponent) {
        this.verticalScrollBar = new KrScrollBar(VERTICAL);
        this.horizontalScrollBar = new KrScrollBar(HORIZONTAL);
        this.innerComponent = innerComponent;

        setLayout(new KrScrollPanelLayout());
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
    protected void scrollEvent(KrScrollEvent event) {
        verticalScrollBar.handle(event);
        event.accept();
    }

    private class KrScrollPanelLayout implements KrLayout {

        private Vector2 lastGeometrySize = new Vector2(0, 0);

        @Override
        public void setGeometry(Rectangle geometry) {
            if (geometry.width == lastGeometrySize.x && geometry.height == lastGeometrySize.y) {
                return;
            }

            Vector2 geometrySize = new Vector2(geometry.width, geometry.height);
            lastGeometrySize = geometrySize;
            verticalScrollBar.setValueRange(0, 0);
            horizontalScrollBar.setValueRange(0, 0);

            // initial sizes
            float childWidth = Math.max(innerComponent.getPreferredSize().x, geometry.width);
            float childHeight = Math.max(innerComponent.getPreferredSize().y, geometry.height);

            float vPrefSize = verticalScrollBar.getPreferredSize().x;
            float hPrefSize = horizontalScrollBar.getPreferredSize().y;

            boolean horizontalScrollRequired = childWidth > geometrySize.x;
            boolean verticalScrollRequired = childHeight > geometrySize.y;

            float vScrollBarSize = verticalScrollRequired ? vPrefSize : 0;
            float hScrollBarSize = horizontalScrollRequired ? hPrefSize : 0;

            if (horizontalScrollRequired) {
                float overflow = childWidth - geometry.getWidth() + (verticalScrollRequired ? vScrollBarSize : 0);
                horizontalScrollBar.setValueRange(new KrRange(0, overflow));
                horizontalScrollBar.setVisible(true);
            } else {
                horizontalScrollBar.setVisible(false);
            }

            if (verticalScrollRequired) {
                float overflow = childHeight - geometry.getHeight() + (horizontalScrollRequired ? hScrollBarSize : 0);
                verticalScrollBar.setValueRange(0, overflow);
                verticalScrollBar.setVisible(true);
            } else {
                verticalScrollBar.setVisible(false);
            }

            innerComponent.setGeometry(0, 0, childWidth, childHeight);
            verticalScrollBar.setGeometry(geometry.width - vScrollBarSize - 2, 1, vScrollBarSize, geometry.height - 2);
            horizontalScrollBar.setGeometry(0, geometry.height - hScrollBarSize, geometry.width - vScrollBarSize, hScrollBarSize);
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
