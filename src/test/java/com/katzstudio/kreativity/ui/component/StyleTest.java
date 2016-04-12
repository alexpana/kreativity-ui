package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.TestUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.katzstudio.kreativity.ui.TestUtils.getAllWidgets;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * KrWidgetStyle related unit tests
 */
@RunWith(JUnitParamsRunner.class)
public class StyleTest {

    // Sadly, JUnitParamsRunner calls the parameters before calling @BeforeClass
    static {
        TestUtils.initializeToolkit();
    }

    @Test
    @Parameters
    public void testEnsureUniqueStyle(String widgetClass, KrWidget widget) throws Exception {
        Object originalStyle = widget.getStyle();
        widget.ensureUniqueStyle();
        Object newStyle = widget.getStyle();
        assertThat(newStyle, not(equals(originalStyle)));
        assertThat(newStyle, notNullValue());
    }

    private Object parametersForTestEnsureUniqueStyle() {
        return getAllWidgets();
    }
}
