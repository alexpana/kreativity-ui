package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.renderer.KrListViewCellRenderer;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private List<KrItemDelegate> delegateList = new ArrayList<>();

    private int selectionIndex = -1;

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
    protected boolean mousePressedEvent(KrMouseEvent event) {
        int newSelectionIndex = findItemIndexAt(screenToLocal(event.getScreenPosition()));
        if (newSelectionIndex != selectionIndex) {
            if (selectionIndex != -1) {
                delegateList.get(selectionIndex).setSelected(false);
            }
            selectionIndex = newSelectionIndex;
            delegateList.get(selectionIndex).setSelected(true);
        }
        return true;
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
        delegateList.clear();

        for (int i = 0; i < model.getRowCount(); ++i) {
            KrModelIndex index = new KrModelIndex(i);
            KrItemDelegate itemDelegate = renderer.getComponent(index, model);
            delegateList.add(itemDelegate);
            innerPanel.add(itemDelegate.getWidget());
        }

        invalidate();
    }

    private int findItemIndexAt(Vector2 position) {
        return findItemIndexAt((int) position.x, (int) position.y);
    }

    private int findItemIndexAt(int x, int y) {
        int itemIndex = 0;
        for (KrItemDelegate delegate : delegateList) {
            if (delegate.getWidget().getGeometry().contains(x, y + listGeometryOffset)) {
                return itemIndex;
            }
            itemIndex += 1;
        }
        return -1;
    }


    public interface Renderer {
        KrItemDelegate getComponent(KrModelIndex index, KrAbstractItemModel model);
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

    /**
     * The item delegate wraps the {@link KrWidget} displayed in the list view.
     * The delegate can alter the widget in various ways, depending on what the list view
     * requests. For instance, it can change the widget's appearance when selected.
     */
    static public abstract class KrItemDelegate {

        /**
         * Sets whether or not the underlying widget is selected or not
         *
         * @param selected the selection status of the widget
         */
        abstract public void setSelected(boolean selected);

        /**
         * Returns the widget that's used by the list view to display an item.
         *
         * @return the widget managed by this delegate.
         */
        abstract public KrWidget getWidget();
    }

    @AllArgsConstructor
    static public class Style {
        public final Drawable background;

        public Style copy() {
            return new Style(background);
        }
    }
}
