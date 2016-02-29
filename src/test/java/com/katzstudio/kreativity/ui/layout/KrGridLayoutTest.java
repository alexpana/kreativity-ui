package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link KrGridLayout}
 */
public class KrGridLayoutTest {
    @Test
    public void test2x2FullExpandNoSpacing() {
        KrGridLayout gridLayout = new KrGridLayout(2);

        KrWidget widgetA1 = new KrWidget();
        KrWidget widgetA2 = new KrWidget();
        KrWidget widgetB1 = new KrWidget();
        KrWidget widgetB2 = new KrWidget();

        gridLayout.addWidget(widgetA1, null);
        gridLayout.addWidget(widgetA2, null);
        gridLayout.addWidget(widgetB1, null);
        gridLayout.addWidget(widgetB2, null);

        gridLayout.setGeometry(new Rectangle(0, 0, 200, 200));

        assertThat(widgetA1.getGeometry(), is(new Rectangle(0, 0, 100, 100)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(100, 0, 100, 100)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(0, 100, 100, 100)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(100, 100, 100, 100)));
    }

    @Test
    public void test2x2FullExpandNoSpacingOffCenter() {
        KrGridLayout gridLayout = new KrGridLayout(2);

        KrWidget widgetA1 = new KrWidget();
        KrWidget widgetA2 = new KrWidget();
        KrWidget widgetB1 = new KrWidget();
        KrWidget widgetB2 = new KrWidget();

        gridLayout.addWidget(widgetA1, null);
        gridLayout.addWidget(widgetA2, null);
        gridLayout.addWidget(widgetB1, null);
        gridLayout.addWidget(widgetB2, null);

        gridLayout.setGeometry(new Rectangle(50, 50, 200, 200));

        assertThat(widgetA1.getGeometry(), is(new Rectangle(50, 50, 100, 100)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(150, 50, 100, 100)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(50, 150, 100, 100)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(150, 150, 100, 100)));
    }

    @Test
    public void test2x2PreferredSizeNoSpacing() {
        KrGridLayout gridLayout = new KrGridLayout(2);

        // wide and short
        KrWidget widgetA1 = new KrWidget();
        widgetA1.setPreferredSize(new Vector2(200, 50));

        // narrow and short
        KrWidget widgetA2 = new KrWidget();
        widgetA2.setPreferredSize(new Vector2(50, 50));

        // narrow and tall
        KrWidget widgetB1 = new KrWidget();
        widgetB1.setPreferredSize(new Vector2(50, 200));

        // wide and tall
        KrWidget widgetB2 = new KrWidget();
        widgetB2.setPreferredSize(new Vector2(200, 200));

        gridLayout.addWidget(widgetA1, null);
        gridLayout.addWidget(widgetA2, null);
        gridLayout.addWidget(widgetB1, null);
        gridLayout.addWidget(widgetB2, null);

        gridLayout.setGeometry(new Rectangle(0, 0, 200, 200));

        assertThat(widgetA1.getGeometry(), is(new Rectangle(0, 25, 100, 50)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(125, 25, 50, 50)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(25, 100, 50, 100)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(100, 100, 100, 100)));
    }

    @Test
    public void test2x2FullExpandWithSpacing() {
        KrGridLayout gridLayout = new KrGridLayout(2, 10, 10);

        KrWidget widgetA1 = new KrWidget();
        KrWidget widgetA2 = new KrWidget();
        KrWidget widgetB1 = new KrWidget();
        KrWidget widgetB2 = new KrWidget();

        gridLayout.addWidget(widgetA1, null);
        gridLayout.addWidget(widgetA2, null);
        gridLayout.addWidget(widgetB1, null);
        gridLayout.addWidget(widgetB2, null);

        gridLayout.setGeometry(new Rectangle(0, 0, 100, 100));

        assertThat(widgetA1.getGeometry(), is(new Rectangle(10, 10, 35, 35)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(55, 10, 35, 35)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(10, 55, 35, 35)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(55, 55, 35, 35)));
    }

    @Test
    public void test2x2PreferredWithNoSpacing() {
        KrGridLayout gridLayout = new KrGridLayout(2, 10, 10);

        // wide and short
        KrWidget widgetA1 = new KrWidget();
        widgetA1.setPreferredSize(new Vector2(200, 50));

        // narrow and short
        KrWidget widgetA2 = new KrWidget();
        widgetA2.setPreferredSize(new Vector2(50, 50));

        // narrow and tall
        KrWidget widgetB1 = new KrWidget();
        widgetB1.setPreferredSize(new Vector2(50, 200));

        // wide and tall
        KrWidget widgetB2 = new KrWidget();
        widgetB2.setPreferredSize(new Vector2(200, 200));

        gridLayout.addWidget(widgetA1, null);
        gridLayout.addWidget(widgetA2, null);
        gridLayout.addWidget(widgetB1, null);
        gridLayout.addWidget(widgetB2, null);

        gridLayout.setGeometry(new Rectangle(0, 0, 200, 200));

        assertThat(widgetA1.getGeometry(), is(new Rectangle(10, 27.5f, 85, 50)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(122.5f, 27.5f, 50.0f, 50.0f)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(27.5f, 105, 50, 85)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(105, 105, 85, 85)));
    }
}