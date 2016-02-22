package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Unit tests for {@link AlignmentTool}
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class AlignmentToolTest {

    private final Rectangle innerRectangle;

    private final Rectangle outerRectangle;

    private final KrAlignment alignment;

    private final Vector2 expectedPosition;

    @Test
    public void testAlignment() throws Exception {
        Vector2 actualPosition = AlignmentTool.alignRectangles(innerRectangle, outerRectangle, alignment);
        Assert.assertThat(actualPosition, CoreMatchers.is(expectedPosition));
    }

    // Data provider
    @Parameterized.Parameters(name = "{2}, {3}")
    public static Collection parameters() {
        Rectangle innerA = new Rectangle(40, 40, 50, 50);
        Rectangle outerA = new Rectangle(100, 100, 200, 200);
        Rectangle outerB = new Rectangle(69, 42, 60, 10);

        return Arrays.asList(new Object[][]{
                {innerA, outerB, KrAlignment.TOP_LEFT, new Vector2(69, 42)},
                {innerA, outerA, KrAlignment.TOP_LEFT, new Vector2(100, 100)},
                {innerA, outerA, KrAlignment.TOP_CENTER, new Vector2(175, 100)},
                {innerA, outerA, KrAlignment.TOP_RIGHT, new Vector2(250, 100)},

                {innerA, outerA, KrAlignment.MIDDLE_LEFT, new Vector2(100, 175)},
                {innerA, outerA, KrAlignment.MIDDLE_CENTER, new Vector2(175, 175)},
                {innerA, outerA, KrAlignment.MIDDLE_RIGHT, new Vector2(250, 175)},

                {innerA, outerA, KrAlignment.BOTTOM_LEFT, new Vector2(100, 250)},
                {innerA, outerA, KrAlignment.BOTTOM_CENTER, new Vector2(175, 250)},
                {innerA, outerA, KrAlignment.BOTTOM_RIGHT, new Vector2(250, 250)},
        });
    }
}