package com.katzstudio.kreativity.ui;

import static com.katzstudio.kreativity.ui.TestObjectFactory.*;

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
}
