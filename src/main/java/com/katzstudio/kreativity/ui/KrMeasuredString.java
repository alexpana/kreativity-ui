package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * {@link String} wrapper that caches font metrics.
 */
public class KrMeasuredString {

    @Getter private String string;

    @Getter private BitmapFont font;

    private Rectangle bounds;

    public KrMeasuredString() {
        this("", getDefaultToolkit().getSkin().getDefaultFont());
    }

    public KrMeasuredString(String string) {
        this(string, getDefaultToolkit().getSkin().getDefaultFont());
    }

    public KrMeasuredString(String string, BitmapFont font) {
        this.string = string;
        this.font = font;
        updateBounds();
    }

    public void setString(String string) {
        if (!this.string.equals(string)) {
            this.string = string;
            updateBounds();
        }
    }

    public void setFont(BitmapFont font) {
        if (!this.font.equals(font)) {
            this.font = font;
            updateBounds();
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getBounds(Rectangle bounds) {
        bounds.set(this.bounds);
        return bounds;
    }

    private void updateBounds() {
        if (string.isEmpty()) {
            bounds = new Rectangle(0, 0, 0, 0);
            return;
        }
        getDefaultToolkit().fontMetrics().bounds(font, string, bounds);
    }
}
