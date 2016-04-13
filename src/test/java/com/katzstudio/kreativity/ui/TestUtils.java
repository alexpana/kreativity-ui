package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.backend.KrBackend;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.component.*;
import com.katzstudio.kreativity.ui.model.KrItemModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import java.util.Arrays;
import java.util.Collection;

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
        when(skin.getStyle(KrWidget.class)).thenReturn(createWidgetStyle());
        when(skin.getStyle(KrPanel.class)).thenReturn(createPanelStyle());
        when(skin.getStyle(KrLabel.class)).thenReturn(createLabelStyle());
        when(skin.getStyle(KrTextField.class)).thenReturn(createTextFieldStyle());
        when(skin.getStyle(KrCheckbox.class)).thenReturn(createCheckBoxStyle());
        when(skin.getStyle(KrButton.class)).thenReturn(createButtonStyle());
        when(skin.getStyle(KrSpinner.class)).thenReturn(createSpinnerStyle());
        when(skin.getStyle(KrIconPanel.class)).thenReturn(createWidgetStyle());
        when(skin.getStyle(KrScrollBar.class)).thenReturn(createScrollBarStyle());
        when(skin.getStyle(KrListView.class)).thenReturn(createItemViewStyle());
        when(skin.getStyle(KrTableView.class)).thenReturn(createItemViewStyle());
        when(skin.getStyle(KrComboBox.class)).thenReturn(createComboBoxStyle());
    }

    public static Collection<Object[]> getAllWidgets() {
        KrMenu menu = new KrMenu();
        KrMenu.KrMenuItem menuItem = new KrMenu.KrMenuItem("");
        menu.addMenuItem(menuItem);

        return Arrays.asList(new Object[][]{
                {"KrWidget", new KrWidget()},
                {"KrLabel", new KrLabel("")},
                {"KrButton", new KrButton("")},
                {"KrCheckbox", new KrCheckbox()},
                {"KrIconPanel", new KrIconPanel(KrFontAwesomeGlyph.FONT)},
                {"KrTextField", new KrTextField()},
                {"KrSpinner", new KrSpinner()},
                {"KrListView", new KrListView(mock(KrItemModel.class))},
                {"KrTableView", new KrTableView(mock(KrItemModel.class))},
                {"KrPanel", new KrPanel()},
                {"KrScrollBar", new KrScrollBar(KrOrientation.VERTICAL)},
                {"KrToggleButton", new KrToggleButton("")},
                {"KrPopup", new KrPopup()},
                {"KrMenu", menu},
                {"KrMenuItem", menuItem},
                {"KrComboBox", new KrComboBox<>()},
                {"KrCollapsiblePanel", new KrCollapsiblePanel("")}
        });
    }

    public static KrBackend createMockBackend() {
        KrFontMetrics fontMetricsMock = mock(KrFontMetrics.class);
        when(fontMetricsMock.bounds(any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));
        when(fontMetricsMock.bounds(any(), any(), any())).thenReturn(new Rectangle(0, 0, 100, 10));

        KrBackend backend = mock(KrBackend.class);
        when(backend.getFontMetrics()).thenReturn(fontMetricsMock);
        when(backend.createColorDrawable(any())).thenReturn(mock(Drawable.class));
        when(backend.getRenderer()).thenReturn(mock(KrRenderer.class));
        when(backend.getInputSource()).thenReturn(mock(KrInputSource.class));
        return backend;
    }
}
