package com.katzstudio.kreativity.ui;

/**
 * Simple structure for hold the padding of a component.
 */
public class KrPadding {

    public float left;

    public float right;

    public float top;

    public float bottom;

    public KrPadding(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public KrPadding(float vertical, float horizontal) {
        this.top = this.bottom = vertical;
        this.left = this.right = horizontal;
    }

    public KrPadding(float all) {
        this.left = this.right = this.top = this.bottom = all;
    }

    public float getHorizontalPadding() {
        return left + right;
    }

    public float getVerticalPadding() {
        return top + bottom;
    }
}
