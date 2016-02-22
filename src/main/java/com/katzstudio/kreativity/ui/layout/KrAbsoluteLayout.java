package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;

/**
 * A layout that allows components to position and size themselves.
 */
public class KrAbsoluteLayout extends KrLayout {

    public KrAbsoluteLayout(KrWidget parentComponent) {
        super(parentComponent);
    }

    @Override
    public void doLayout() {
        // nothing to do, components are positioned and sized from the outside
    }

    @Override
    public Vector2 getMinSize() {
        return new Vector2(0, 0);
    }

    @Override
    public Vector2 getMaxSize() {
        return new Vector2(1000, 1000);
    }

    @Override
    public Vector2 getPreferredSize() {
        // TODO(alex): iterate through components and get their bounding box.
        return getMaxSize();
    }
}
