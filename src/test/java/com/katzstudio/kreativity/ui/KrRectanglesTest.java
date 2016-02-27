package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link KrRectangles}
 */
@RunWith(JUnitParamsRunner.class)
public class KrRectanglesTest {

    @Test
    public void testShrinkWithPadding() {
        KrPadding padding = new KrPadding(6, 4, 2, 4);
        Rectangle rectangle = new Rectangle(50, 50, 100, 100);

        Rectangle result = KrRectangles.rectangles(rectangle).shrink(padding).value();
        assertThat(result, is(new Rectangle(54, 56, 92, 92)));
    }

    @Test
    public void testExpandWithPadding() {
        KrPadding padding = new KrPadding(6, 4, 2, 4);
        Rectangle rectangle = new Rectangle(50, 50, 100, 100);

        Rectangle result = KrRectangles.rectangles(rectangle).expand(padding).value();
        assertThat(result, is(new Rectangle(46, 44, 108, 108)));
    }

    @Test
    @Parameters(method = "parametersForIntersectionTests")
    public void testIntersection(Rectangle r1, Rectangle r2, Rectangle expectedResult) {
        assertThat(KrRectangles.rectangles(r1).intersect(r2).value(), is(expectedResult));

        assertThat(KrRectangles.rectangles(r2).intersect(r1).value(), is(expectedResult));
    }

    @Test
    @Parameters(method = "parametersForIntersectionTests")
    public void testIntersectionIsCommutative(Rectangle r1, Rectangle r2, Rectangle expectedResult) {
        assertThat(KrRectangles.rectangles(r2).intersect(r1).value(), is(KrRectangles.rectangles(r1).intersect(r2).value()));
    }


    @SuppressWarnings("unused")
    private Object parametersForIntersectionTests() {
        Rectangle baseRect = new Rectangle(100, 200, 300, 200);
        return new Object[][]{
                {baseRect, new Rectangle(75, 175, 150, 75), new Rectangle(100, 200, 125, 50)},
                {baseRect, new Rectangle(250, 150, 25, 100), new Rectangle(250, 200, 25, 50)},
                {baseRect, new Rectangle(275, 125, 25, 25), new Rectangle(0, 0, 0, 0)},
                {baseRect, new Rectangle(350, 200, 75, 50), new Rectangle(350, 200, 50, 50)},
                {baseRect, new Rectangle(525, 150, 75, 50), new Rectangle(0, 0, 0, 0)},
                {baseRect, new Rectangle(-25, 150, 50, 50), new Rectangle(0, 0, 0, 0)},
                {baseRect, new Rectangle(50, 275, 500, 50), new Rectangle(100, 275, 300, 50)},
                {baseRect, new Rectangle(300, 275, 50, 50), new Rectangle(300, 275, 50, 50)},
                {baseRect, new Rectangle(75, 300, 75, 50), new Rectangle(100, 300, 50, 50)},
                {baseRect, new Rectangle(175, 325, 50, 75), new Rectangle(175, 325, 50, 75)},
                {baseRect, new Rectangle(75, 375, 50, 50), new Rectangle(100, 375, 25, 25)},
                {baseRect, new Rectangle(125, 425, 50, 50), new Rectangle(0, 0, 0, 0)},
                {baseRect, new Rectangle(250, 350, 50, 100), new Rectangle(250, 350, 50, 50)},
                {baseRect, new Rectangle(350, 350, 100, 100), new Rectangle(350, 350, 50, 50)},
                {baseRect, new Rectangle(325, 150, 50, 450), new Rectangle(325, 200, 50, 200)},
        };
    }
}