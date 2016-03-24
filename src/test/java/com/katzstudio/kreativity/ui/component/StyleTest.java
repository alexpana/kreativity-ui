package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.TestUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.katzstudio.kreativity.ui.TestObjectFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * KrWidgetStyle related unit tests
 */
@RunWith(JUnitParamsRunner.class)
public class StyleTest {

    // Sadly, JUnitParamsRunner calls the parameters before calling @BeforeClass
    static {
        TestUtils.initializeTestStyles();

        KrFontMetrics fontMetricsMock = mock(KrFontMetrics.class);
        when(fontMetricsMock.bounds(any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));
        KrToolkit toolkit = mock(KrToolkit.class);
        when(toolkit.fontMetrics()).thenReturn(fontMetricsMock);
        KrToolkit.setDefault(toolkit);
    }

    @Test
    @Parameters
    public void testEnsureUniqueStyle(KrWidget widget) throws Exception {
        Object originalStyle = widget.getStyle();
        widget.ensureUniqueStyle();
        Object newStyle = widget.getStyle();
        assertThat(newStyle, not(equals(originalStyle)));
    }

    private Object parametersForTestEnsureUniqueStyle() {

        return new Object[][]{
                {new KrPanel()},
                {new KrLabel("")},
                {new KrTextField()},
                {new KrCheckbox()},
                {new KrButton("")},
                {new KrSpinner()}
        };
    }
}
