package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;

import static com.katzstudio.kreativity.ui.TestObjectFactory.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Functions used by tests
 */
public class TestUtils {
    public static void initializeTestStyles() {
        KrSkin.instance().setWidgetStyle(createWidgetStyle());
        KrSkin.instance().setPanelStyle(createPanelStyle());
        KrSkin.instance().setLabelStyle(createLabelStyle());
        KrSkin.instance().setButtonStyle(createButtonStyle());
        KrSkin.instance().setTextFieldStyle(createTextFieldStyle());
        KrSkin.instance().setCheckboxStyle(createCheckBoxStyle());
        KrSkin.instance().setSpinnerStyle(createSpinnerStyle());
        KrSkin.instance().setListViewStyle(createWidgetStyle());
        KrSkin.instance().setHorizontalScrollBarStyle(createHorizontalScrollBarStyle());
        KrSkin.instance().setVerticalScrollBarStyle(createVerticalScrollBarStyle());
    }

    public static void initializeToolkit() {
        KrFontMetrics fontMetricsMock = mock(KrFontMetrics.class);
        when(fontMetricsMock.bounds(any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));

        KrToolkit toolkit = mock(KrToolkit.class);
        when(toolkit.fontMetrics()).thenReturn(fontMetricsMock);

        KrToolkit.setDefault(toolkit);
    }
}
