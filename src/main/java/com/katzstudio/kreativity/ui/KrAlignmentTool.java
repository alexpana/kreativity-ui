package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.util.ReturnsPooledObject;

/**
 * Helper class for manipulating widget geometries (rectangles).
 */
public class KrAlignmentTool {
    private KrAlignmentTool() {
    }

    @ReturnsPooledObject
    public static Vector2 alignRectangles(Rectangle inner, Rectangle outer, KrAlignment alignment) {
        return alignRectangles(inner.x, inner.y, inner.width, inner.height, outer.x, outer.y, outer.width, outer.height, alignment);
    }

    @ReturnsPooledObject
    public static Vector2 alignRectangles(float innerX, float innerY, float innerW, float innerH,
                                          float outerX, float outerY, float outerW, float outerH,
                                          KrAlignment alignment) {
        Vector2 alignedPosition = Pools.obtain(Vector2.class);

        switch (alignment.getVertical()) {
            case TOP:
                alignedPosition.y = outerY;
                break;
            case MIDDLE:
                alignedPosition.y = outerY + (outerH - innerH) / 2;
                break;
            case BOTTOM:
                alignedPosition.y = outerY + outerH - innerH;
                break;
        }
        switch (alignment.getHorizontal()) {
            case LEFT:
                alignedPosition.x = outerX;
                break;
            case CENTER:
                alignedPosition.x = outerX + (outerW - innerW) / 2;
                break;
            case RIGHT:
                alignedPosition.x = outerX + outerW - innerW;
                break;
        }

        return alignedPosition;
    }
}
