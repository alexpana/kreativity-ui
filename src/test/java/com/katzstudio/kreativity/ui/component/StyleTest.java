package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.KrSkin;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.katzstudio.kreativity.ui.TestObjectFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Style related unit tests
 */
@RunWith(JUnitParamsRunner.class)
public class StyleTest {

    // Sadly, JUnitParamsRunner calls the parameters before calling @BeforeClass
    static {
        KrSkin.instance().setPanelStyle(createPanelStyle());
        KrSkin.instance().setLabelStyle(createLabelStyle());
        KrSkin.instance().setButtonStyle(createButtonStyle());
        KrSkin.instance().setTextFieldStyle(createTextFieldStyle());
        KrSkin.instance().setCheckboxStyle(createCheckBoxStyle());
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
