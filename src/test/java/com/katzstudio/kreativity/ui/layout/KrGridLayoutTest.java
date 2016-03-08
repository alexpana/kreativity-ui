package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.layout.KrGridLayout.Constraint;
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

        assertThat(widgetA1.getGeometry(), is(new Rectangle(10, 27f, 85, 50)));
        assertThat(widgetA2.getGeometry(), is(new Rectangle(122f, 27f, 50, 50)));
        assertThat(widgetB1.getGeometry(), is(new Rectangle(27f, 105, 50, 85)));
        assertThat(widgetB2.getGeometry(), is(new Rectangle(105, 105, 85, 85)));
    }

    @Test
    public void testOneColumn() throws Exception {
        KrGridLayout gridLayout = new KrGridLayout(1);

        KrWidget widget1 = new KrWidget();
        KrWidget widget2 = new KrWidget();
        KrWidget widget3 = new KrWidget();

        gridLayout.addWidget(widget1, new Constraint(KrAlignment.MIDDLE_CENTER, true, true));
        gridLayout.addWidget(widget2, new Constraint(KrAlignment.MIDDLE_CENTER, true, true));
        gridLayout.addWidget(widget3, new Constraint(KrAlignment.MIDDLE_CENTER, true, true));

        gridLayout.setGeometry(new Rectangle(0, 0, 30, 90));

        assertThat(widget1.getGeometry(), is(new Rectangle(0, 0, 30, 30)));
        assertThat(widget2.getGeometry(), is(new Rectangle(0, 30, 30, 30)));
        assertThat(widget3.getGeometry(), is(new Rectangle(0, 60, 30, 30)));
    }
}