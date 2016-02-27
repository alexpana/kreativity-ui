package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import org.junit.Test;

import static com.katzstudio.kreativity.ui.TestObjectFactory.createCanvas;
import static com.katzstudio.kreativity.ui.TestObjectFactory.createWidget;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link KrCanvas}
 */
public class KrCanvasTest {

    @Test
    public void testFindWidgetSimple() {
        KrWidget widgetA = createWidget("Widget A", 0, 0, 50, 100);

        KrWidget widgetA1 = createWidget("Widget A1", 0, 0, 50, 50);

        KrWidget widgetA2 = createWidget("Widget A2", 0, 50, 50, 50);

        // widget A is split horizontally between A1 and A2
        widgetA.add(widgetA1);
        widgetA.add(widgetA2);

        KrWidget widgetB = createWidget("Widget B", 50, 0, 50, 100);

        KrWidget rootWidget = createWidget("root", 0, 0, 200, 200);

        // root widget is split vertically into A and B
        rootWidget.add(widgetA);
        rootWidget.add(widgetB);

        assertThat(KrCanvas.findWidgetAt(rootWidget, 30, 30), is(widgetA1));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 30, 70), is(widgetA2));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 60, 70), is(widgetB));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 150, 10), is(rootWidget));
    }

    @Test
    public void testFindWidgetOverlapping() {
        KrWidget rootWidget = createWidget("root", 0, 0, 100, 100);

        KrWidget bottomWidget = createWidget("bottom", 10, 10, 50, 50);

        KrWidget topWidget = createWidget("top", 20, 10, 50, 50);

        rootWidget.add(bottomWidget);
        rootWidget.add(topWidget);

        assertThat(KrCanvas.findWidgetAt(rootWidget, 30, 30), is(topWidget));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 80, 30), is(rootWidget));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 15, 30), is(bottomWidget));
        assertThat(KrCanvas.findWidgetAt(rootWidget, 65, 30), is(topWidget));
    }

    @Test
    public void testScreenBoundsOneParent() {
        KrWidget parent = createWidget("parent", 50, 50, 100, 100);
        KrWidget child = createWidget("child", 20, 20, 10, 10);

        assertThat(KrCanvas.getScreenBounds(child), is(new Rectangle(20, 20, 10, 10)));

        parent.add(child);

        assertThat(KrCanvas.getScreenBounds(child), is(new Rectangle(70, 70, 10, 10)));
    }

    @Test
    public void testFocusRequest() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget widget = new KrWidget();
        canvas.getRootComponent().add(widget);

        assertThat(canvas.requestFocus(widget), is(true));
        assertThat(canvas.getKeyboardFocusHolder(), is(widget));
    }

    @Test
    public void testClearFocus() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget widget = new KrWidget();
        canvas.getRootComponent().add(widget);

        canvas.requestFocus(widget);
        assertThat(canvas.getKeyboardFocusHolder(), is(widget));
        assertThat(widget.isFocused(), is(true));

        canvas.requestFocus(null);
        assertThat(canvas.getKeyboardFocusHolder(), is((KrWidget) null));
        assertThat(widget.isFocused(), is(false));
    }

    @Test
    public void testWidgetsReceiveFocusEvents() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget widgetA = mock(KrWidget.class);
        when(widgetA.getCanvas()).thenReturn(canvas);

        KrWidget widgetB = mock(KrWidget.class);
        when(widgetB.getCanvas()).thenReturn(canvas);

        canvas.requestFocus(widgetA);

        verify(widgetA).handle(eq(new KrFocusEvent(KrFocusEvent.Type.FOCUS_GAINED)));

        canvas.requestFocus(widgetB);
        verify(widgetA).handle(eq(new KrFocusEvent(KrFocusEvent.Type.FOCUS_LOST)));
        verify(widgetB).handle(eq(new KrFocusEvent(KrFocusEvent.Type.FOCUS_GAINED)));
    }

    @Test
    public void testDispatchKeyPressedEvents() throws Exception {
        KrCanvas canvas = createCanvas();
        KrWidget widget = mock(KrWidget.class);
        when(widget.getCanvas()).thenReturn(canvas);

        canvas.requestFocus(widget);
        reset(widget);

        canvas.keyDown(120);
        canvas.keyTyped('x');

        verify(widget).handle(eq(new KrKeyEvent(KrKeyEvent.Type.PRESSED, 120, "x")));
    }

    @Test
    public void testDispatchKeyReleasedEvents() throws Exception {
        KrCanvas canvas = createCanvas();
        KrWidget widget = mock(KrWidget.class);
        when(widget.getCanvas()).thenReturn(canvas);

        canvas.requestFocus(widget);
        reset(widget);

        canvas.keyUp(52);

        verify(widget).handle(eq(new KrKeyEvent(KrKeyEvent.Type.RELEASED, 52, "x")));
    }

    @Test
    public void testKeyPressWithNoFocus() throws Exception {
        KrCanvas canvas = createCanvas();

        assertThat(canvas.keyDown(32), is(false));
    }
}
