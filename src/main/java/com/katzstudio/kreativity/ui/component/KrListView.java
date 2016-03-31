package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.renderer.KrCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrDefaultCellRenderer;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrItemViewStyle;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrAbstractItemView<KrItemViewStyle> {

    public KrListView(KrAbstractItemModel model) {
        this(model, new KrDefaultCellRenderer());
    }

    public KrListView(KrAbstractItemModel model, KrCellRenderer renderer) {
        super(model, renderer);
        setStyle(getDefaultToolkit().getSkin().getListViewStyle());
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getListViewStyle()) {
            style = new KrItemViewStyle(style);
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
    public Vector2 calculatePreferredSize() {
        return new Vector2(100, model.getRowCount() * ROW_HEIGHT);
    }

    @Override
    public KrModelIndex findItemIndexAt(int x, int y) {
        int index = (int) ((y + verticalScrollBar.getCurrentValue()) / ROW_HEIGHT);
        return new KrModelIndex(index);
    }
}
