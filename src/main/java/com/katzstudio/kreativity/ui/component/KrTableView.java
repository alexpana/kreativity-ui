package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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

    public KrTableView(KrItemModel model) {
        this(model, null);
    }

    public KrTableView(KrItemModel model, KrTableColumnModel columnModel) {
        super(model);
        this.model = model;
        this.columnModel = columnModel;
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrTableView.class));
        verticalScrollBar.setVisible(false);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        boolean clipped;
        boolean drawHeader = columnModel != null;
        int columnCount = columnModel != null ? columnModel.getColumnCount() : model.getColumnCount();
        int clipY = 1;

        int x = 0;
        int y = 0;
        int columnWidth = (int) (getWidth() / columnCount);
        int rowHeight = ROW_HEIGHT;

        int gridSize = ((KrItemViewStyle) getStyle()).gridVisible ? 1 : 0;
        Color borderColor = KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BORDER);

        renderer.setBrush(borderColor);
        renderer.fillRoundedRect(0, 0, (int) getWidth(), (int) getHeight(), 3);

        renderer.setBrush(KrToolkit.getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND));
        renderer.fillRoundedRect(1, 1, (int) getWidth() - 2, (int) getHeight() - 2, 3);

        // draw columns
        if (drawHeader) {
            for (int i = 0; i < columnCount; ++i) {
                KrWidget cellWidget = headerRenderer.getComponent(i, columnModel);
                cellWidget.setGeometry(x, y, columnWidth - gridSize, rowHeight - gridSize);
                cellWidget.draw(renderer);
                x += columnWidth;
            }
            renderer.setPen(borderColor);
            renderer.drawLine(0, ROW_HEIGHT, getWidth(), ROW_HEIGHT);
            clipY += ROW_HEIGHT;
        }

        clipped = renderer.beginClip(1, clipY, getWidth() - 1, getHeight() - ROW_HEIGHT - 2);

        // draw elements
        x = 0;
        for (int i = 0; i < columnCount; ++i) {
            y = (int) -verticalScrollBar.getCurrentValue() + (drawHeader ? rowHeight : 0);
            for (int j = 0; j < model.getRowCount(); ++j) {
                KrWidget cellWidget = cellRenderer.getComponent(j, i, null, model, selectionModel.getCurrentSelection().containsRow(j));
                cellWidget.setGeometry(x, y, columnWidth - gridSize, rowHeight - gridSize);
                cellWidget.draw(renderer);
                y += rowHeight;
            }
            x += columnWidth;
        }

        if (clipped) {
            renderer.endClip();
        }

        // draw grid
        if (((KrItemViewStyle) getStyle()).gridVisible) {
            renderer.setPen(1, ((KrItemViewStyle) getStyle()).gridColor);

            for (int i = 1; i < columnCount; ++i) {
                renderer.drawLine(i * columnWidth - 1, 0, i * columnWidth - 1, getHeight());
            }

//            clipped = renderer.beginClip(1, ROW_HEIGHT - 2, getWidth() - 2, getHeight() - ROW_HEIGHT - 2);
//            int rowCount = model.getRowCount();
//            for (int i = 1; i < rowCount + (drawHeader ? 1 : 0); ++i) {
//                int offset = (int) -verticalScrollBar.getCurrentValue();
//                renderer.drawLine(0, i * rowHeight - 1 + offset, getWidth(), i * rowHeight - 1 + offset);
//            }
//            if (clipped) {
//                renderer.endClip();
//            }
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

    // TODO: implement selection type: cell / row

    public interface KrTableColumnModel {

        int getColumnCount();

        String getColumnName(int index);

        int getModelIndex(int index);
    }
}
