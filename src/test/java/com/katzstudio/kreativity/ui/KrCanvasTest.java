package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import org.junit.Before;
import org.junit.Test;

import static com.katzstudio.kreativity.ui.TestObjectFactory.createCanvas;
import static com.katzstudio.kreativity.ui.TestObjectFactory.createWidget;
import static com.katzstudio.kreativity.ui.TestUtils.initializeTestStyles;
import static com.katzstudio.kreativity.ui.TestUtils.initializeToolkit;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link KrCanvas}
 */
public class KrCanvasTest {

    @Before
    public void setUp() throws Exception {
        initializeTestStyles();
        initializeToolkit();
    }

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
    public void testScreenGeometryOneParent() {
        KrWidget parent = createWidget("parent", 50, 50, 100, 100);
        KrWidget child = createWidget("child", 20, 20, 10, 10);

        assertThat(KrCanvas.getScreenGeometry(child), is(new Rectangle(20, 20, 10, 10)));

        parent.add(child);

        assertThat(KrCanvas.getScreenGeometry(child), is(new Rectangle(70, 70, 10, 10)));
    }

    @Test
    public void testFocusRequest() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget widget = new KrWidget();
        canvas.getRootPanel().add(widget);

        assertThat(canvas.requestFocus(widget), is(true));
        assertThat(canvas.getKeyboardFocusHolder(), is(widget));
    }

    @Test
    public void testClearFocus() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget widget = new KrWidget();
        canvas.getRootPanel().add(widget);

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

        canvas.keyPressed(new KrKeyEvent(KrKeyEvent.Type.PRESSED, 120, "x", false, false, false));

        verify(widget).handle(eq(new KrKeyEvent(KrKeyEvent.Type.PRESSED, 120, "x")));
    }

    @Test
    public void testDispatchKeyReleasedEvents() throws Exception {
        KrCanvas canvas = createCanvas();
        KrWidget widget = mock(KrWidget.class);
        when(widget.getCanvas()).thenReturn(canvas);

        canvas.requestFocus(widget);
        reset(widget);
        canvas.keyPressed(new KrKeyEvent(KrKeyEvent.Type.RELEASED, 52, "", false, false, false));

        verify(widget).handle(eq(new KrKeyEvent(KrKeyEvent.Type.RELEASED, 52, "")));
    }

    @Test
    public void testKeyPressWithNoFocus() throws Exception {
        KrCanvas canvas = createCanvas();

        KrKeyEvent keyEvent = new KrKeyEvent(KrKeyEvent.Type.RELEASED, 52, "", false, false, false);
        canvas.keyPressed(keyEvent);

        assertThat(keyEvent.handled(), is(false));
    }

    @Test
    public void testConvertPointToScreen() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget first = new KrWidget();
        KrWidget second = new KrWidget();

        first.add(second);
        canvas.getRootPanel().add(first);

        first.setGeometry(50, 50, 100, 100);

        second.setGeometry(8, 4, 10, 10);

        assertThat(KrCanvas.convertPointToScreen(new Vector2(4, 2), second), is(new Vector2(62, 56)));
    }
}
