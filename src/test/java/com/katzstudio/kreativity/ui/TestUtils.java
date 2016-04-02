package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.backend.KrBackend;

import static com.katzstudio.kreativity.ui.TestObjectFactory.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Functions used by tests
 */
public class TestUtils {
    public static void initializeToolkit() {
        KrBackend backend = createMockBackend();
        KrToolkit.initialize(backend, mock(KrSkin.class));
        initializeTestStyles();
    }

    private static void initializeTestStyles() {
        KrSkin skin = KrToolkit.getDefaultToolkit().getSkin();
        when(skin.getWidgetStyle()).thenReturn(createWidgetStyle());
        when(skin.getPanelStyle()).thenReturn(createPanelStyle());
        when(skin.getLabelStyle()).thenReturn(createLabelStyle());
        when(skin.getButtonStyle()).thenReturn(createButtonStyle());
        when(skin.getTextFieldStyle()).thenReturn(createTextFieldStyle());
        when(skin.getCheckboxStyle()).thenReturn(createCheckBoxStyle());
        when(skin.getSpinnerStyle()).thenReturn(createSpinnerStyle());
        when(skin.getListViewStyle()).thenReturn(createItemViewStyle());
        when(skin.getHorizontalScrollBarStyle()).thenReturn(createHorizontalScrollBarStyle());
        when(skin.getVerticalScrollBarStyle()).thenReturn(createVerticalScrollBarStyle());
    }

    public static KrBackend createMockBackend() {
        KrFontMetrics fontMetricsMock = mock(KrFontMetrics.class);
        when(fontMetricsMock.bounds(any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));
        when(fontMetricsMock.bounds(any(), any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));

        KrBackend backend = mock(KrBackend.class);
        when(backend.getFontMetrics()).thenReturn(fontMetricsMock);
        when(backend.createColorDrawable(any())).thenReturn(mock(Drawable.class));
        return backend;
    }
}
