package com.katzstudio.kreativity.ui.math;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.katzstudio.kreativity.ui.math.KrRange.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * Unit tests for {@link KrRangeTest}
 */
@RunWith(JUnitParamsRunner.class)
public class KrRangeTest {

    public static final double TOLERATED_ERROR = 0.00001;

    @Test
    @Parameters
    public void testConstructor(float min, float max) throws Exception {
        KrRange range = new KrRange(min, max);
        assertThat(range.getMin(), lessThanOrEqualTo(range.getMax()));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestConstructor() {
        return new Object[][]{
                {0, 0}, {10, 0}, {-10, 0}, {0, 10}, {0, -10}, {-100, -200}, {-200, -100}, {100, 100}, {-20, -20}
        };
    }

    @Test
    @Parameters
    public void testMapRange(float value, KrRange sourceRange, KrRange targetRange, float expectedValue) throws Exception {
        assertThat((double) map(value, sourceRange, targetRange), closeTo(expectedValue, TOLERATED_ERROR));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestMapRange() {
        return new Object[][]{
                {5, new KrRange(0, 10), new KrRange(20, 40), 30},
                {0, new KrRange(0, 0), new KrRange(100, 400), 100},
                {-1, new KrRange(-2, 2), new KrRange(100, 400), 175},
                {-2, new KrRange(-2, 2), new KrRange(100, 400), 100},
                {-3, new KrRange(-2, 2), new KrRange(100, 400), 25},
                {-4, new KrRange(-2, 2), new KrRange(100, 400), -50},
                {1, new KrRange(-2, 2), new KrRange(100, 400), 325},
                {2, new KrRange(-2, 2), new KrRange(100, 400), 400},
                {3, new KrRange(-2, 2), new KrRange(100, 400), 475},
                {4, new KrRange(-2, 2), new KrRange(100, 400), 550},
        };
    }

    @Test
    @Parameters
    public void testClamp(float value, KrRange range, float expected) throws Exception {
        assertThat((double) range.clamp(value), closeTo(expected, TOLERATED_ERROR));
    }

    private Object parametersForTestClamp() {
        return new Object[][]{
                {10, new KrRange(-20, 20), 10},
                {10, new KrRange(-20, 10), 10},
                {10, new KrRange(-20, 0), 0},
                {10, new KrRange(-20, -20), -20},
                {10, new KrRange(10, 20), 10},
                {10, new KrRange(30, 40), 30},
        };
    }
}