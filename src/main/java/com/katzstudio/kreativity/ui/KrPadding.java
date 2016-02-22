package com.katzstudio.kreativity.ui;

import lombok.AllArgsConstructor;

/**
 * Simple structure for hold the padding of a component.
 */
@AllArgsConstructor
public class KrPadding {

    public float left;

    public float right;

    public float top;

    public float bottom;

    public float getHorizontalPadding() {
        return left + right;
    }

    public float getVerticalPadding() {
        return top + bottom;
    }
}
