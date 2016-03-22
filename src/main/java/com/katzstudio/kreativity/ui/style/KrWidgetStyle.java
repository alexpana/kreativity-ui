package com.katzstudio.kreativity.ui.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.KrPadding;

/**
 * Default style class with properties common to all widgets
 */
public class KrWidgetStyle {

    public Drawable background;

    public Color foregroundColor;

    public BitmapFont font;

    public Vector2 textShadowOffset;

    public Color textShadowColor;

    public Drawable icon;

    public Color selectionColor;

    public KrPadding padding;

    public KrCursor cursor;

    public KrWidgetStyle() {
    }

    public KrWidgetStyle(KrWidgetStyle other) {
        this.background = other.background;
        this.foregroundColor = other.foregroundColor;
        this.font = other.font;
        this.textShadowOffset = other.textShadowOffset;
        this.textShadowColor = other.textShadowColor;
        this.icon = other.icon;
        this.selectionColor = other.selectionColor;
        this.padding = other.padding;
        this.cursor = other.cursor;
    }

    public KrWidgetStyle copy() {
        return new KrWidgetStyle(this);
    }
}
