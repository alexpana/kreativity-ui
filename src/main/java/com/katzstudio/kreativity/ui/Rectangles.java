package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The {@link Rectangles} class offers functionality for manipulating {@link Rectangle} objects.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Rectangles {

    private Rectangle rectangle;

    public static Rectangles rectangles(Rectangle rectangle) {
        return new Rectangles(rectangle);
    }

    public static Rectangles rectangles(Vector2 size) {
        return new Rectangles(new Rectangle(0, 0, size.x, size.y));
    }

    public Rectangles shrink(KrPadding padding) {
        rectangle.x += padding.left;
        rectangle.y += padding.top;
        rectangle.width -= padding.getHorizontalPadding();
        rectangle.height -= padding.getVerticalPadding();
        return this;
    }

    public Rectangles expand(KrPadding padding) {
        rectangle.x -= padding.left;
        rectangle.y -= padding.top;
        rectangle.width += padding.getHorizontalPadding();
        rectangle.height += padding.getVerticalPadding();
        return this;
    }

    public Rectangle value() {
        return rectangle;
    }
}
