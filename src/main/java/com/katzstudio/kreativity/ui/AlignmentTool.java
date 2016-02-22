package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Helper class for manipulating widget geometries (rectangles).
 */
public class AlignmentTool {
    private AlignmentTool() {
    }

    public static Vector2 alignRectangles(Rectangle inner, Rectangle outer, KrAlignment alignment) {
        Vector2 alignedPosition = new Vector2();

        switch (alignment.getVertical()) {
            case TOP:
                alignedPosition.y = outer.y;
                break;
            case MIDDLE:
                alignedPosition.y = outer.y + (outer.height - inner.height) / 2;
                break;
            case BOTTOM:
                alignedPosition.y = outer.y + outer.height - inner.height;
                break;
        }
        switch (alignment.getHorizontal()) {
            case LEFT:
                alignedPosition.x = outer.x;
                break;
            case CENTER:
                alignedPosition.x = outer.x + (outer.width - inner.width) / 2;
                break;
            case RIGHT:
                alignedPosition.x = outer.x + outer.width - inner.width;
                break;
        }

        return alignedPosition;
    }
}
