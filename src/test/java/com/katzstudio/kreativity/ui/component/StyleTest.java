package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.TestUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * KrWidgetStyle related unit tests
 */
@RunWith(JUnitParamsRunner.class)
public class StyleTest {

    // Sadly, JUnitParamsRunner calls the parameters before calling @BeforeClass
    static {
        TestUtils.initializeTestStyles();
        TestUtils.initializeToolkit();
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
