package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;
import lombok.RequiredArgsConstructor;

/**
 * A layout that computes the minimum, maximum and preferred size of a {@link KrWidget} based on it's children,
 * and distributes it's children according to the available space.
 */
@RequiredArgsConstructor
public abstract class KrLayout {

    private final KrWidget parentComponent;

    public abstract void doLayout();

    public abstract Vector2 getMinSize();

    public abstract Vector2 getMaxSize();

    public abstract Vector2 getPreferredSize();
}
