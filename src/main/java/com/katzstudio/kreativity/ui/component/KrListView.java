package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSelectionMode;
import com.katzstudio.kreativity.ui.component.renderer.KrCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrListViewCellRenderer;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import com.katzstudio.kreativity.ui.model.KrSelection;
import com.katzstudio.kreativity.ui.model.KrSelectionModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrWidget<KrWidgetStyle> {

    private final static int ROW_HEIGHT = 20;

    private final KrAbstractItemModel model;

    private final KrCellRenderer cellRenderer;

    private KrScrollBar verticalScrollBar = new KrScrollBar(VERTICAL);

    private List<KrDoubleClickListener> doubleClickListeners = new ArrayList<>();

    @Getter private KrSelectionModel selectionModel = new KrSelectionModel();

    @Getter private KrSelectionMode selectionMode = KrSelectionMode.EXTENDED;

    public KrListView(KrAbstractItemModel model) {
        this(model, new KrListViewCellRenderer());
    }

    public KrListView(KrAbstractItemModel model, KrCellRenderer renderer) {
        this.model = model;
        this.cellRenderer = renderer;

        model.addListener(this::onModelDataChanged);
        verticalScrollBar.addScrollListener(this::onScroll);
        selectionModel.addSelectionListener(this::onSelectionChanged);

        setStyle(getDefaultToolkit().getSkin().getListViewStyle());
        setLayout(new Layout());
        add(verticalScrollBar);

        onModelDataChanged();
    }

    private void onScroll(float v) {
        invalidate();
    }

    private void onSelectionChanged(KrSelection oldSelection, KrSelection newSelection) {
    }

    public void setSelectionMode(KrSelectionMode newSelectionMode) {
        if (newSelectionMode != selectionMode) {
            selectionMode = newSelectionMode;
            selectionModel.clearSelection();
        }
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);

        if (selectionMode == KrSelectionMode.NONE) {
            return false;
        }

        KrModelIndex itemIndex = findItemIndexAt(screenToLocal(event.getScreenPosition()));
        if (selectionMode == KrSelectionMode.SINGLE) {
            if (event.isCtrlDown() && selectionModel.getCurrentSelection().contains(itemIndex)) {
                selectionModel.setSelection(KrSelection.EMPTY);
            } else {
                selectionModel.setSelection(KrSelection.of(itemIndex));
            }
            return true;
        }

        if (event.isCtrlDown()) {
            if (selectionModel.getCurrentSelection().contains(itemIndex)) {
                selectionModel.remove(itemIndex);
            } else {
                selectionModel.add(itemIndex);
            }
        } else {
            selectionModel.setSelection(KrSelection.of(itemIndex));
        }
        return true;
    }

    @Override
    protected boolean mouseDoubleClickEvent(KrMouseEvent event) {
        super.mouseDoubleClickEvent(event);

        KrModelIndex itemIndex = findItemIndexAt(screenToLocal(event.getScreenPosition()));
        if (itemIndex != null) {
            notifyItemDoubleClicked(itemIndex);
        }
        return true;
    }

    @Override
    protected boolean scrollEvent(KrScrollEvent event) {
        super.scrollEvent(event);
        return verticalScrollBar.scrollEvent(event);
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getListViewStyle()) {
            style = new KrWidgetStyle(style);
        }
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        int cellY = (int) -verticalScrollBar.getCurrentValue();
        int cellHeight = ROW_HEIGHT;
        int cellWidth = (int) getWidth();
        for (int i = 0; i < model.getRowCount(); ++i) {
            KrModelIndex index = new KrModelIndex(i);
            KrWidget item = cellRenderer.getComponent(index, model, selectionModel.getCurrentSelection().contains(new KrModelIndex(i)));
            item.setGeometry(0, cellY, cellWidth, cellHeight);
            item.draw(renderer);
            cellY += cellHeight;
        }
    }

    @Override
    public void validate() {
        super.validate();
        this.verticalScrollBar.setValueRange(0, Math.max(getPreferredHeight() - getHeight(), 0));
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(100, model.getRowCount() * ROW_HEIGHT);
    }

    private void onModelDataChanged() {
        invalidate();
    }

    private KrModelIndex findItemIndexAt(Vector2 position) {
        return findItemIndexAt((int) position.x, (int) position.y);
    }

    private KrModelIndex findItemIndexAt(int x, int y) {
        int index = (int) ((y + verticalScrollBar.getCurrentValue()) / ROW_HEIGHT);
        System.out.println("y = " + y);
        System.out.println("verticalScrollBar.getCurrentValue() = " + verticalScrollBar.getCurrentValue());
        System.out.println("index = " + index);
        return new KrModelIndex(index);
    }

    public void addDoubleClickListener(KrDoubleClickListener listener) {
        doubleClickListeners.add(listener);
    }

    public void removeDoubleClickListener(KrDoubleClickListener listener) {
        doubleClickListeners.remove(listener);
    }

    protected void notifyItemDoubleClicked(KrModelIndex itemIndex) {
        doubleClickListeners.forEach(l -> l.itemDoubleClicked(itemIndex));
    }

    private class Layout implements KrLayout {

        @Override
        public void setGeometry(Rectangle geometry) {
            int width = (int) geometry.getWidth();
            int height = (int) geometry.getHeight();

            float preferredHeight = getPreferredHeight();
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
            return KrListView.this.calculatePreferredSize();
        }

        @Override
        public Vector2 getMaxSize() {
            return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        }

        @Override
        public Vector2 getPreferredSize() {
            return KrListView.this.calculatePreferredSize();
        }

        @Override
        public void addWidget(KrWidget child, Object layoutConstraint) {
        }

        @Override
        public void removeWidget(KrWidget child) {
        }
    }

    public interface KrDoubleClickListener {
        void itemDoubleClicked(KrModelIndex itemIndex);
    }

}
