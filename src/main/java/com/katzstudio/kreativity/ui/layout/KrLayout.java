package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;

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
}
