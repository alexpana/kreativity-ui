package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The {@link KrRectangles} class offers functionality for manipulating {@link Rectangle} objects.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KrRectangles {

    private Rectangle rectangle;

    public static KrRectangles rectangles(Rectangle rectangle) {
        return new KrRectangles(rectangle);
    }

    public static KrRectangles rectangles(Vector2 size) {
        return new KrRectangles(new Rectangle(0, 0, size.x, size.y));
    }

    public KrRectangles shrink(KrPadding padding) {
        rectangle.x += padding.left;
        rectangle.y += padding.top;
        rectangle.width -= padding.getHorizontalPadding();
        rectangle.height -= padding.getVerticalPadding();
        return this;
    }

    public KrRectangles expand(KrPadding padding) {
        rectangle.x -= padding.left;
        rectangle.y -= padding.top;
        rectangle.width += padding.getHorizontalPadding();
        rectangle.height += padding.getVerticalPadding();
        return this;
    }

    public KrRectangles intersect(Rectangle other) {
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

    private static class Segment extends KrPair<Float, Float> {
        public static final Segment ZERO = new Segment(0.0f, 0.0f);

        public Segment(Float first, Float second) {
            super(first, second);
        }
    }
}
