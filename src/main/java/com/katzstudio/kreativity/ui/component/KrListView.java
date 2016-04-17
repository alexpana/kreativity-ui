package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.component.renderer.KrCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrDefaultCellRenderer;
import com.katzstudio.kreativity.ui.model.KrItemModel;
import com.katzstudio.kreativity.ui.model.KrItemModel.KrModelIndex;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrAbstractItemView {

    public KrListView(KrItemModel model) {
        this(model, new KrDefaultCellRenderer());
    }

    public KrListView(KrItemModel model, KrCellRenderer renderer) {
        super(model, renderer);
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrListView.class));
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {

        Color borderColor = KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER);
        renderer.setBrush(borderColor);
        renderer.fillRoundedRect(0, 0, (int) getWidth(), (int) getHeight(), 3);

        renderer.setBrush(KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));
        renderer.fillRoundedRect(1, 1, (int) getWidth() - 2, (int) getHeight() - 2, 3);

        boolean clipped = renderer.beginClip(1, 1, getWidth() - 2, getHeight() - 2);

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

        if (clipped) {
            renderer.endClip();
        }
    }

    @Override
    protected void getScrollBarGeometry(Rectangle scrollbarGeometry) {
        int scrollBarWidth = (int) verticalScrollBar.getPreferredWidth();
        scrollbarGeometry.set(getWidth() - scrollBarWidth - 2, 2, scrollBarWidth, getHeight() - 4);
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
