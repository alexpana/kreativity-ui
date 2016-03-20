package com.katzstudio.kreativity.ui.libgdx;

import com.badlogic.gdx.graphics.Cursor;
import com.katzstudio.kreativity.ui.KrCursor;

/**
 * Helper class for interfacing libgdx
 */
public class KrLibGdxCursorHelper {
    public static Cursor.SystemCursor systemCursor(KrCursor cursor) {
        switch (cursor) {
            case ARROW:
                return Cursor.SystemCursor.Arrow;
            case IBEAM:
                return Cursor.SystemCursor.Ibeam;
            case CROSSHAIR:
                return Cursor.SystemCursor.Crosshair;
            case HAND:
                return Cursor.SystemCursor.Hand;
            case HORIZONTAL_RESIZE:
                return Cursor.SystemCursor.HorizontalResize;
            case VERTICAL_RESIZE:
                return Cursor.SystemCursor.VerticalResize;
            default:
                return Cursor.SystemCursor.Arrow;
        }
    }
}
