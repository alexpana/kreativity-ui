package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * The {@link Rectangles} class offers functionality for manipulating {@link Rectangle} objects.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Rectangles {
    private final Rectangle baseRectangle;

    public static Rectangles rectangles(Rectangle rectangle) {
        return new Rectangles(rectangle);
    }

    public Rectangles expand(KrPadding padding) {
        return new Rectangles(new Rectangle(baseRectangle.x,
                baseRectangle.y,
                baseRectangle.width + padding.getHorizontalPadding(),
                baseRectangle.height + padding.getVerticalPadding()));
    }

    public Rectangle value() {
        return baseRectangle;
    }

    public void expand(Rectangle other, KrAlignment alignment) {

    }

    public void shrink(Rectangle other) {

    }
}
