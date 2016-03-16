package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.katzstudio.kreativity.ui.component.KrWidget;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link KrStackLayout}
 */
public class KrStackLayoutTest {
    @Test
    public void testLayout() throws Exception {
        KrWidget widgetA = new KrWidget();
        KrWidget widgetB = new KrWidget();
        KrWidget widgetC = new KrWidget();

        KrStackLayout stackLayout = new KrStackLayout();
        stackLayout.addWidget(widgetA, null);
        stackLayout.addWidget(widgetB, null);
        stackLayout.addWidget(widgetC, null);

        Rectangle layoutGeometry = new Rectangle(10, 10, 100, 100);
        stackLayout.setGeometry(layoutGeometry);

        assertThat(widgetA.getGeometry(), is(layoutGeometry));
        assertThat(widgetB.getGeometry(), is(layoutGeometry));
        assertThat(widgetC.getGeometry(), is(layoutGeometry));
    }
}