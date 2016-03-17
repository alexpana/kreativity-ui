package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.katzstudio.kreativity.ui.component.KrButton;
import com.katzstudio.kreativity.ui.component.KrCheckbox;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrPanel;
import com.katzstudio.kreativity.ui.component.KrTextField;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Factory for various test objects
 */
public class TestObjectFactory {

    private TestObjectFactory() {
    }

    public static BitmapFont createBitmapFont() {
        BitmapFont bitmapFontMock = mock(BitmapFont.class);
        when(bitmapFontMock.getBounds(any())).then(invocation -> {
            BitmapFont.TextBounds bounds = new BitmapFont.TextBounds();
            bounds.width = ((String) invocation.getArguments()[0]).length() * 10;
            bounds.height = 12;
            return bounds;
        });
        return bitmapFontMock;
    }

    public static KrPanel.Style createPanelStyle() {
        return new KrPanel.Style(mock(Drawable.class));
    }

    public static KrLabel.Style createLabelStyle() {
        return new KrLabel.Style(mock(Drawable.class),
                createBitmapFont(),
                Color.BLACK);
    }

    public static KrButton.Style createButtonStyle() {
        return new KrButton.Style(mock(Drawable.class),
                mock(Drawable.class),
                mock(Drawable.class),
                createBitmapFont(),
                Color.BLACK,
                Vector2.Zero,
                Color.BLACK);
    }

    public static KrTextField.Style createTextFieldStyle() {
        return new KrTextField.Style(
                mock(Drawable.class),
                mock(Drawable.class),
                mock(Drawable.class),
                createBitmapFont(),
                Color.BLACK,
                Color.BLACK,
                Color.BLACK);
    }

    public static KrCheckbox.Style createCheckBoxStyle() {
        return new KrCheckbox.Style(
                mock(Drawable.class),
                mock(Drawable.class));
    }

    public static KrCanvas createCanvas() {
        return new KrCanvas(mock(KrRenderer.class), mock(Clipboard.class), 100, 100);
    }

    public static KrWidget createWidget(String name, int x, int y, int width, int height) {
        KrWidget widgetA = new KrWidget(name);
        widgetA.setGeometry(x, y, width, height);
        return widgetA;
    }
}
