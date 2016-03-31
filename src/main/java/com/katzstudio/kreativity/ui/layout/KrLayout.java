package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;

import static java.lang.Float.MAX_VALUE;

/**
 * A layout that computes the minimum, maximum and preferred size of a {@link KrWidget} based on it's children,
 * and distributes it's children according to the available space.
 */
public interface KrLayout {

    void setGeometry(Rectangle geometry);

    Vector2 getMinSize();

    Vector2 getMaxSize();

    Vector2 getPreferredSize();

    void addWidget(KrWidget child, Object layoutConstraint);

    void removeWidget(KrWidget child);

    abstract class KrAbstractLayout implements KrLayout {
        @Override
        public void setGeometry(Rectangle geometry) {
        }

        @Override
        public Vector2 getMinSize() {
            return getPreferredSize();
        }

        @Override
        public Vector2 getMaxSize() {
            return new Vector2(MAX_VALUE, MAX_VALUE);
        }

        @Override
        public Vector2 getPreferredSize() {
            return null;
        }

        @Override
        public void addWidget(KrWidget child, Object layoutConstraint) {
        }

        @Override
        public void removeWidget(KrWidget child) {
        }
    }
}
