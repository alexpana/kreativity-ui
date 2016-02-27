package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.katzstudio.kreativity.ui.component.KrButton;
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

    public static KrButton.Style createButtonStyle() {
        return new KrButton.Style(mock(Drawable.class),
                mock(Drawable.class),
                mock(Drawable.class),
                createBitmapFont(),
                Color.BLACK,
                Vector2.Zero,
                Color.BLACK);
    }

    public static KrCanvas createCanvas() {
        return new KrCanvas(mock(KrRenderer.class), mock(Clipboard.class), 100, 100);
    }

    public static KrWidget createWidget(String name, int x, int y, int width, int height) {
        KrWidget widgetA = new KrWidget(name);
        widgetA.setBounds(x, y, width, height);
        return widgetA;
    }
}
