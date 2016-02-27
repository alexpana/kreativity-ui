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

    public Rectangles intersect(Rectangle other) {
        Segment horizontalIntersectionProjection = project(horizontalProjection(rectangle), horizontalProjection(other));
        Segment verticalIntersectionProjection = project(verticalProjection(rectangle), verticalProjection(other));
        rectangle = rectangleFromProjections(horizontalIntersectionProjection, verticalIntersectionProjection);
        return this;
    }

    private static Segment verticalProjection(Rectangle rectangle) {
        return new Segment(rectangle.y, rectangle.y + rectangle.height);
    }

    private static Segment horizontalProjection(Rectangle rectangle) {
        return new Segment(rectangle.x, rectangle.x + rectangle.width);
    }

    private static Rectangle rectangleFromProjections(Segment horizontalProjection, Segment verticalProjection) {
        float width = horizontalProjection.getSecond() - horizontalProjection.getFirst();
        float height = verticalProjection.getSecond() - verticalProjection.getFirst();

        if (width == 0 || height == 0) {
            return new Rectangle(0, 0, 0, 0);
        }

        return new Rectangle(horizontalProjection.getFirst(),
                verticalProjection.getFirst(),
                width,
                height);
    }

    private static Segment project(Segment first, Segment second) {
        if (first.getFirst() > second.getFirst()) {
            Segment tmp = first;
            first = second;
            second = tmp;
        }

        if (second.getFirst() > first.getSecond()) {
            return Segment.ZERO;
        }
        return new Segment(second.getFirst(), Math.min(second.getSecond(), first.getSecond()));
    }

    public Rectangle value() {
        return rectangle;
    }

    private static class Segment extends Pair<Float, Float> {
        public static final Segment ZERO = new Segment(0.0f, 0.0f);

        public Segment(Float first, Float second) {
            super(first, second);
        }
    }
}
