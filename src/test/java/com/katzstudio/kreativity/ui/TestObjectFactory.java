package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.*;

import static org.mockito.Mockito.mock;

/**
 * Factory for various test objects
 */
public class TestObjectFactory {

    private TestObjectFactory() {
    }

    public static BitmapFont createBitmapFont() {
        return mock(BitmapFont.class);
    }

    public static KrPanelStyle createPanelStyle() {
        KrPanelStyle panelStyle = new KrPanelStyle();
        panelStyle.background = mock(Drawable.class);
        return panelStyle;
    }

    public static KrLabelStyle createLabelStyle() {
        KrLabelStyle labelStyle = new KrLabelStyle();
        labelStyle.background = mock(Drawable.class);
        labelStyle.font = createBitmapFont();
        labelStyle.foregroundColor = Color.BLACK;
        return labelStyle;
    }

    public static KrButtonStyle createButtonStyle() {
        KrButtonStyle buttonStyle = new KrButtonStyle();
        buttonStyle.background = mock(Drawable.class);
        buttonStyle.backgroundNormal = mock(Drawable.class);
        buttonStyle.backgroundHovered = mock(Drawable.class);
        buttonStyle.backgroundArmed = mock(Drawable.class);
        buttonStyle.font = createBitmapFont();
        buttonStyle.foregroundColor = Color.BLACK;
        buttonStyle.textShadowColor = Color.BLACK;
        buttonStyle.textShadowOffset = Vector2.Zero;
        return buttonStyle;
    }

    public static KrTextFieldStyle createTextFieldStyle() {
        KrTextFieldStyle textFieldStyle = new KrTextFieldStyle();
        textFieldStyle.background = mock(Drawable.class);
        textFieldStyle.backgroundNormal = mock(Drawable.class);
        textFieldStyle.backgroundFocused = mock(Drawable.class);
        textFieldStyle.backgroundHovered = mock(Drawable.class);
        textFieldStyle.font = createBitmapFont();
        textFieldStyle.foregroundColor = Color.BLACK;
        textFieldStyle.caretColor = Color.BLACK;
        textFieldStyle.textShadowColor = Color.BLACK;
        return textFieldStyle;
    }

    public static KrCheckboxStyle createCheckBoxStyle() {
        KrCheckboxStyle checkboxStyle = new KrCheckboxStyle();
        checkboxStyle.mark = mock(Drawable.class);
        checkboxStyle.checkboxBackground = mock(Drawable.class);
        return checkboxStyle;
    }

    public static KrCanvas createCanvas() {
        return new KrCanvas(mock(KrRenderer.class), mock(Clipboard.class), 100, 100);
    }

    public static KrWidget createWidget(String name, int x, int y, int width, int height) {
        KrWidget widget = new KrPanel();
        widget.setName(name);
        widget.setGeometry(x, y, width, height);
        return widget;
    }
}
