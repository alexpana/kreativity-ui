package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.component.renderer.KrCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrDefaultCellRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrDefaultTableHeaderRenderer;
import com.katzstudio.kreativity.ui.component.renderer.KrTableHeaderRenderer;
import com.katzstudio.kreativity.ui.model.KrItemModel;
import com.katzstudio.kreativity.ui.model.KrItemModel.KrModelIndex;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrItemViewStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A {@link KrTableView} widget displays data stored in a {@link KrItemModel}
 * using a table.
 */
public class KrTableView extends KrAbstractItemView {

    private static final int ROW_HEIGHT = 20;

    @Getter @Setter private KrItemModel model;

    @Getter @Setter private KrTableColumnModel columnModel;

    @Getter @Setter private KrCellRenderer cellRenderer = new KrDefaultCellRenderer();

    @Getter @Setter private KrTableHeaderRenderer headerRenderer = new KrDefaultTableHeaderRenderer();

    private KrSizePolicyModel columnSizePolicy;

    public KrTableView(KrItemModel model) {
        this(model, null);
    }

    public KrTableView(KrItemModel model, KrTableColumnModel columnModel) {
        super(model);
        this.model = model;
        this.columnModel = columnModel;
        int columnCount = columnModel != null ? columnModel.getColumnCount() : model.getColumnCount();
        columnSizePolicy = new KrSizePolicyModel(columnCount);
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrTableView.class));
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        boolean clipped;
        boolean drawHeader = columnModel != null;
        int columnCount = columnModel != null ? columnModel.getColumnCount() : model.getColumnCount();
        int clipY = 1;

        int x = 0;
        int y = 0;
        int rowHeight = ROW_HEIGHT;

        Color borderColor = KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER);

        List<Integer> columnSizes = columnSizePolicy.getIntSizes(getWidth());

        renderer.setBrush(borderColor);
        renderer.fillRoundedRect(0, 0, (int) getWidth(), (int) getHeight(), 3);

        renderer.setBrush(KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_LIGHT));
        renderer.fillRoundedRect(1, 1, (int) getWidth() - 2, (int) getHeight() - 2, 3);

        // draw columns
        if (drawHeader) {
            for (int i = 0; i < columnCount; ++i) {
                KrWidget cellWidget = headerRenderer.getComponent(i, columnModel);
                cellWidget.setGeometry(x, y, columnSizes.get(i), rowHeight);
                cellWidget.draw(renderer);
                x += columnSizes.get(i);
            }
            renderer.setPen(borderColor);
            renderer.drawLine(0, ROW_HEIGHT, getWidth(), ROW_HEIGHT);
            clipY += ROW_HEIGHT;
        }

        clipped = renderer.beginClip(1, clipY, getWidth() - 2, getHeight() - ROW_HEIGHT - 2);

        // draw elements
        x = 0;
        for (int i = 0; i < columnCount; ++i) {
            y = (int) -verticalScrollBar.getCurrentValue() + (drawHeader ? rowHeight : 0);
            for (int j = 0; j < model.getRowCount(); ++j) {
                KrWidget cellWidget = cellRenderer.getComponent(j, i, null, model, selectionModel.getCurrentSelection().containsRow(j));
                cellWidget.setGeometry(x, y, columnSizes.get(i), rowHeight);
                cellWidget.draw(renderer);
                y += rowHeight;
            }
            x += columnSizes.get(i);
        }

        if (clipped) {
            renderer.endClip();
        }

        // draw grid
        if (((KrItemViewStyle) getStyle()).gridVisible) {
            renderer.setPen(1, ((KrItemViewStyle) getStyle()).gridColor);

            x = 0;
            for (int i = 0; i < columnCount - 1; ++i) {
                x += columnSizes.get(i) - 1;
                renderer.drawLine(x, 0, x, getHeight());
            }
        }
    }

    public KrModelIndex findItemIndexAt(int x, int y) {
        if (columnModel != null) {
            y -= ROW_HEIGHT;
        }
        int index = (int) ((y + verticalScrollBar.getCurrentValue()) / ROW_HEIGHT);
        return new KrModelIndex(index);
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(100, model.getRowCount() * ROW_HEIGHT + (columnModel != null ? ROW_HEIGHT : 0));
    }

    @Override
    protected void getScrollBarGeometry(Rectangle scrollbarGeometry) {
        int scrollBarWidth = (int) verticalScrollBar.getPreferredWidth();
        scrollbarGeometry.set(getWidth() - scrollBarWidth - 2, ROW_HEIGHT + 2, scrollBarWidth, getHeight() - ROW_HEIGHT - 4);
    }

    // TODO: implement selection type: cell / row

    public interface KrTableColumnModel {

        int getColumnCount();

        String getColumnName(int index);

        int getModelIndex(int index);
    }
}
