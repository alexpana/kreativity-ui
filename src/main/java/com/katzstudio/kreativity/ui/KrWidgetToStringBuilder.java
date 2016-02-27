package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;

/**
 * Helper class for building widget string representations
 */
public class KrWidgetToStringBuilder {
    private String name = "Unnamed";
    private String type = "KrWidget";
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private boolean visible = true;
    private boolean enabled = true;

    private KrWidgetToStringBuilder() {
    }

    public static KrWidgetToStringBuilder builder() {
        return new KrWidgetToStringBuilder();
    }

    public KrWidgetToStringBuilder name(String name) {
        this.name = name == null ? "Unnamed" : name;
        return this;
    }

    public KrWidgetToStringBuilder type(String type) {
        this.type = type == null ? "KrWidget" : type;
        return this;
    }

    public KrWidgetToStringBuilder bounds(Rectangle bounds) {
        this.bounds = bounds;
        return this;
    }

    public KrWidgetToStringBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public KrWidgetToStringBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String toString() {
        return String.format("%s(\"%s\", bounds: %s, visible: %s, enabled: %s)", type, name, rectangleToString(bounds), boolToString(visible), boolToString(enabled));
    }

    private static String rectangleToString(Rectangle rectangle) {
        return String.format("[%s, %s, %s, %s]", (int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
    }

    private String boolToString(boolean b) {
        return b ? "T" : "F";
    }
}
