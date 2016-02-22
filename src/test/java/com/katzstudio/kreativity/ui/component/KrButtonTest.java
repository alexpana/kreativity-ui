package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrRenderer;
import com.katzstudio.kreativity.ui.KreativitySkin;
import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import org.junit.Before;
import org.junit.Test;

import static com.katzstudio.kreativity.ui.TestObjectFactory.createButtonStyle;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link KrButton}
 */
public class KrButtonTest {

    private KrButton button;

    private KrButton.Style buttonStyle;

    private KrRenderer renderer;

    @Before
    public void setUp() {
        buttonStyle = createButtonStyle();
        KreativitySkin.instance().setButtonStyle(buttonStyle);

        button = new KrButton("button");
        renderer = mock(KrRenderer.class);
    }

    @Test
    public void testDrawSimple() throws Exception {
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
        verify(renderer).renderText(eq("button"), any(Float.class), any(Float.class));
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
        verifyRendererCalledWithDrawable(buttonStyle.backgroundHovered);
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
        verifyRendererCalledWithDrawable(buttonStyle.backgroundHovered);
    }

    @Test
    public void testClickEvent() throws Exception {
        KrButton.Listener listener = mock(KrButton.Listener.class);
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

        button.handle(new KrExitEvent());
        button.drawSelf(renderer);
        verifyRendererCalledWithDrawable(buttonStyle.backgroundNormal);
    }

    public void verifyRendererCalledWithDrawable(Drawable drawable) {
        verify(renderer).renderDrawable(eq(drawable), any(Float.class), any(Float.class), any(Float.class), any(Float.class));
    }
}
