package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Unit tests for {@link Rectangles}
 */
public class RectanglesTest {

    @Test
    public void testShrinkWithPadding() {
        KrPadding padding = new KrPadding(6, 4, 2, 4);
        Rectangle rectangle = new Rectangle(50, 50, 100, 100);

        Rectangle result = Rectangles.rectangles(rectangle).shrink(padding).value();
        Assert.assertThat(result, is(new Rectangle(54, 56, 92, 92)));
    }

    @Test
    public void testExpandWithPadding() {
        KrPadding padding = new KrPadding(6, 4, 2, 4);
        Rectangle rectangle = new Rectangle(50, 50, 100, 100);

        Rectangle result = Rectangles.rectangles(rectangle).expand(padding).value();
        Assert.assertThat(result, is(new Rectangle(46, 44, 108, 108)));
    }
}