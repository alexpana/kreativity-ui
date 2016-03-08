package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * A horizontal scroll bar
 */
public class KrHorizontalScrollBar extends KrScrollBar {
    @Override
    protected Rectangle getThumbGeometry() {
        return null;
    }

    @Override
    protected float getTrackLength() {
        return 0;
    }
}
