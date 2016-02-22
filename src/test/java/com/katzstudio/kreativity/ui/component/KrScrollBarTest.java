package com.katzstudio.kreativity.ui.component;

import org.junit.Test;

import static com.katzstudio.kreativity.ui.component.KrScrollBar.mapRange;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrScrollBar}
 */
public class KrScrollBarTest {
    @Test
    public void testMapRange() throws Exception {
        assertThat(mapRange(0f, 0, 1, 10, 20), is(10.0f));
        assertThat(mapRange(0.5f, 0, 1, 10, 20), is(15.0f));
        assertThat(mapRange(1f, 0, 1, 10, 20), is(20.0f));

        assertThat(mapRange(1f, 1, 0, 10, 20), is(10.0f));
        assertThat(mapRange(0, 1, 0, 10, 20), is(20.0f));

        // when min == max, the newMin is returned
        assertThat(mapRange(30, 0, 0, 10, 20), is(10.0f));

        assertThat(mapRange(0, 1, 0, 20, 20), is(20.0f));
    }
}