package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrRenderer;
import org.junit.Test;

import static com.katzstudio.kreativity.ui.TestObjectFactory.createCanvas;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link KrWidget}
 */
public class KrWidgetTest {
    @Test
    public void testAddWidget() throws Exception {
        KrWidget parentWidget = new KrWidget("parent");
        KrWidget childWidget = new KrWidget("child");

        parentWidget.add(childWidget);

        assertThat(childWidget.getParent(), is(parentWidget));
        assertThat(parentWidget.getChildren().size(), is(1));
        assertThat(parentWidget.getChildren().get(0), is(childWidget));
    }

    @Test
    public void testTopLevelAncestor() throws Exception {
        KrWidget root = new KrWidget();
        KrWidget firstChild = new KrWidget();
        KrWidget secondChild = new KrWidget();

        assertThat(root.getTopLevelAncestor(), is(root));
        assertThat(firstChild.getTopLevelAncestor(), is(firstChild));
        assertThat(secondChild.getTopLevelAncestor(), is(secondChild));

        root.add(firstChild);
        assertThat(root.getTopLevelAncestor(), is(root));
        assertThat(firstChild.getTopLevelAncestor(), is(root));
        assertThat(secondChild.getTopLevelAncestor(), is(secondChild));

        firstChild.add(secondChild);
        assertThat(root.getTopLevelAncestor(), is(root));
        assertThat(firstChild.getTopLevelAncestor(), is(root));
        assertThat(secondChild.getTopLevelAncestor(), is(root));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildAlreadyParented() throws Exception {
        KrWidget root = new KrWidget();
        KrWidget firstChild = new KrWidget();
        KrWidget secondChild = new KrWidget();

        firstChild.add(secondChild);

        root.add(secondChild);
    }

    @Test
    public void testGetCanvas() throws Exception {
        KrWidget widget = new KrWidget();

        assertThat(widget.getCanvas(), is((KrCanvas) null));

        KrCanvas canvas = new KrCanvas(mock(KrRenderer.class), 100, 100);
        canvas.getRootComponent().add(widget);
        assertThat(widget.getCanvas(), is(canvas));
    }

    @Test
    public void testRemoveFocusedChild() throws Exception {
        KrCanvas canvas = createCanvas();

        KrWidget parent = new KrWidget();
        KrWidget child = new KrWidget();

        parent.add(child);
        canvas.getRootComponent().add(parent);

        assertThat(child.requestFocus(), is(true));
        assertThat(child.isFocused(), is(true));

        parent.remove(child);
        assertThat(child.isFocused(), is(false));
    }
}
