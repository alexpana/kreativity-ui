package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrButtonStyle;
import org.junit.Before;
import org.junit.Test;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;
import static com.katzstudio.kreativity.ui.TestObjectFactory.createButtonStyle;
import static com.katzstudio.kreativity.ui.TestUtils.initializeToolkit;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link KrButton}
 */
public class KrButtonTest {

    private KrButton button;

    private KrButtonStyle buttonStyle;

    private KrRenderer renderer;

    @Before
    public void setUp() {
        initializeToolkit();

        buttonStyle = createButtonStyle();
        when(getDefaultToolkit().getSkin().getButtonStyle()).thenReturn(buttonStyle);

        button = new KrButton("button");
        renderer = mock(KrRenderer.class);
    }

    @Test
    public void testDrawSimple() throws Exception {
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
        verify(renderer).drawTextWithShadow(eq("button"), any(Vector2.class), eq(Vector2.Zero), eq(Color.BLACK));
    }

    @Test
    public void testDrawPressed() throws Exception {
        button.handle(new KrMouseEvent(KrMouseEvent.Type.PRESSED, KrMouseEvent.Button.LEFT, null, null));
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundArmed);
    }

    @Test
    public void testDrawHovered() throws Exception {
        button.handle(new KrEnterEvent());
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
    }

    @Test
    public void testDrawAfterExit() {
        button.handle(new KrEnterEvent());
        button.handle(new KrMouseEvent(KrMouseEvent.Type.PRESSED, KrMouseEvent.Button.LEFT, null, null));
        button.handle(new KrExitEvent());

        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
    }

    @Test
    public void testDrawAfterClick() throws Exception {
        button.handle(new KrEnterEvent());
        button.handle(new KrMouseEvent(KrMouseEvent.Type.PRESSED, KrMouseEvent.Button.LEFT, null, null));
        button.handle(new KrMouseEvent(KrMouseEvent.Type.RELEASED, KrMouseEvent.Button.LEFT, null, null));

        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
    }

    @Test
    public void testClickEvent() throws Exception {
        KrButton.KrButtonListener listener = mock(KrButton.KrButtonListener.class);
        button.addListener(listener);
        button.handle(new KrMouseEvent(KrMouseEvent.Type.PRESSED, KrMouseEvent.Button.LEFT, null, null));
        button.handle(new KrMouseEvent(KrMouseEvent.Type.RELEASED, KrMouseEvent.Button.LEFT, null, null));
        verify(listener).clicked();
    }

    @Test
    public void testMouseLeaveWhileArmed() throws Exception {
        button.handle(new KrMouseEvent(KrMouseEvent.Type.PRESSED, KrMouseEvent.Button.LEFT, null, null));
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundArmed);
        reset(renderer);

        button.handle(new KrExitEvent());
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
    }

    public void verifyRendererCalledWithDrawable(Drawable drawable) {
        verify(renderer).setBrush(eq(new KrDrawableBrush(drawable)));
        verify(renderer).fillRect(any(Float.class), any(Float.class), any(Float.class), any(Float.class));
    }
}
