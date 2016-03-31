package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSelectionMode;
import com.katzstudio.kreativity.ui.component.renderer.KrCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrDefaultCellRenderer;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.KrScrollEvent;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrSelection;
import com.katzstudio.kreativity.ui.model.KrSelectionModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrItemViewStyle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;

/**
 * Base class for item view widgets such as lists and tables
 */
public abstract class KrAbstractItemView<S extends KrItemViewStyle> extends KrWidget<S> {

    protected final static int ROW_HEIGHT = 20;

    protected final KrAbstractItemModel model;

    protected final KrCellRenderer cellRenderer;

    protected KrScrollBar verticalScrollBar = new KrScrollBar(VERTICAL);

    protected List<KrDoubleClickListener> doubleClickListeners = new ArrayList<>();

    @Getter protected KrSelectionModel selectionModel = new KrSelectionModel();

    @Getter protected KrSelectionMode selectionMode = KrSelectionMode.EXTENDED;

    public KrAbstractItemView(KrAbstractItemModel model) {
        this(model, new KrDefaultCellRenderer());
    }

    public KrAbstractItemView(KrAbstractItemModel model, KrCellRenderer renderer) {
        this.model = model;
        this.cellRenderer = renderer;

        model.addListener(this::onModelDataChanged);
        verticalScrollBar.addScrollListener(this::onScroll);
        selectionModel.addSelectionListener(this::onSelectionChanged);

        setLayout(new KrInternalListViewLayout());
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

        KrAbstractItemModel.KrModelIndex itemIndex = findItemIndexAt(screenToLocal(event.getScreenPosition()));
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

        KrAbstractItemModel.KrModelIndex itemIndex = findItemIndexAt(screenToLocal(event.getScreenPosition()));
        notifyItemDoubleClicked(itemIndex);
        return true;
    }

    @Override
    protected boolean scrollEvent(KrScrollEvent event) {
        super.scrollEvent(event);
        return verticalScrollBar.scrollEvent(event);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
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

    public KrAbstractItemModel.KrModelIndex findItemIndexAt(Vector2 position) {
        return findItemIndexAt((int) position.x, (int) position.y);
    }

    abstract public KrAbstractItemModel.KrModelIndex findItemIndexAt(int x, int y);

    public void addDoubleClickListener(KrDoubleClickListener listener) {
        doubleClickListeners.add(listener);
    }

    public void removeDoubleClickListener(KrDoubleClickListener listener) {
        doubleClickListeners.remove(listener);
    }

    protected void notifyItemDoubleClicked(KrAbstractItemModel.KrModelIndex itemIndex) {
        doubleClickListeners.forEach(l -> l.itemDoubleClicked(itemIndex));
    }

    private class KrInternalListViewLayout extends KrLayout.KrAbstractLayout {

        @SuppressWarnings("Duplicates")
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
        public Vector2 getPreferredSize() {
            return calculatePreferredSize();
        }
    }

    public interface KrDoubleClickListener {
        void itemDoubleClicked(KrAbstractItemModel.KrModelIndex itemIndex);
    }
}
