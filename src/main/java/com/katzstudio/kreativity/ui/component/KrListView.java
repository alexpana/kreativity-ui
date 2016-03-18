package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.renderer.KrListViewCellRenderer;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import lombok.AllArgsConstructor;

import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrWidget {

    private Style style;

    private final KrAbstractItemModel model;

    private final KrPanel innerPanel;

    private final Renderer renderer;

    private int listGeometryOffset = 0;

    private KrScrollBar verticalScrollBar = new KrScrollBar(VERTICAL);

    public KrListView(KrAbstractItemModel model) {
        this(model, new KrListViewCellRenderer());
    }

    public KrListView(KrAbstractItemModel model, Renderer renderer) {
        this.model = model;
        this.renderer = renderer;
        this.innerPanel = new KrPanel(new KrFlowLayout(VERTICAL));

        model.addListener(this::onModelDataChanged);
        verticalScrollBar.addScrollListener(this::onScroll);

        setLayout(new Layout());
        add(innerPanel);
        add(verticalScrollBar);

        onModelDataChanged();
    }

    private void onScroll(float v) {
        listGeometryOffset = (int) v;
        invalidate();
    }

    @Override
    protected boolean scrollEvent(KrScrollEvent event) {
        return verticalScrollBar.scrollEvent(event);
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getListViewStyle()) {
            style = style.copy();
        }
    }

    private void onModelDataChanged() {
        innerPanel.clear();

        for (int i = 0; i < model.getRowCount(); ++i) {
            KrModelIndex index = new KrModelIndex(i);
            KrWidget itemComponent = renderer.getComponent(index, model, false);
            innerPanel.add(itemComponent);
        }

        invalidate();
    }

    public interface Renderer {
        KrWidget getComponent(KrModelIndex index, KrAbstractItemModel model, boolean isSelected);
    }

    private class Layout implements KrLayout {

        @Override
        public void setGeometry(Rectangle geometry) {
            int width = (int) geometry.getWidth();
            int height = (int) geometry.getHeight();

            float preferredHeight = innerPanel.getPreferredHeight();
            innerPanel.setGeometry(0, -listGeometryOffset, width, preferredHeight);

            int requiredScrollSize = (int) (preferredHeight - geometry.getHeight());

            if (requiredScrollSize > 0) {
                int scrollBarWidth = (int) verticalScrollBar.getPreferredWidth();
                verticalScrollBar.setGeometry(width - scrollBarWidth, 0, scrollBarWidth, height);
                verticalScrollBar.setValueRange(0, requiredScrollSize);
            } else {
                verticalScrollBar.setSize(0, 0);
                verticalScrollBar.setValueRange(0, 0);
            }
        }

        @Override
        public Vector2 getMinSize() {
            return innerPanel.getMinSize();
        }

        @Override
        public Vector2 getMaxSize() {
            return innerPanel.getMaxSize();
        }

        @Override
        public Vector2 getPreferredSize() {
            return innerPanel.getPreferredSize();
        }

        @Override
        public void addWidget(KrWidget child, Object layoutConstraint) {
        }

        @Override
        public void removeWidget(KrWidget child) {
        }
    }

    @AllArgsConstructor
    static public class Style {
        public final Drawable background;

        public Style copy() {
            return new Style(background);
        }
    }
}
