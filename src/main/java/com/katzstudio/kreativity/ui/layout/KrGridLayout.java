package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.component.KrWidget;

import java.util.List;

/**
 * A layout implementation that distributes widgets in a grid.
 * <p>
 * All cells have the same height. Column widths can differ.
 */
public class KrGridLayout implements KrLayout {

    private final int columnCount;

    private final List<KrWidget> widgets = Lists.newArrayList();

    private final int verticalPadding;

    private final int horizontalPadding;

//    private KrSizePolicyModel columnSizePolicy = new KrSizePolicyModel();

    private int constraintsMask = 0;

    public KrGridLayout(int columns) {
        this(columns, 0, 0);
    }

    public KrGridLayout(int columns, int verticalPadding, int horizontalPadding) {
        this.verticalPadding = verticalPadding;
        this.horizontalPadding = horizontalPadding;
        this.columnCount = columns;
    }

    @Override
    public void setGeometry(Rectangle geometry) {
//        int availableWidth = (int) (geometry.getWidth() - horizontalPadding * (columnCount + 1));
//        List<Float> columnSize = columnSizePolicy.getSizes(availableWidth);

        int rowCount = (widgets.size() + 1) / columnCount;
        int cellHeight = (int) (geometry.getHeight() - verticalPadding * (rowCount + 1)) / rowCount;
        int cellWidth = (int) (geometry.getWidth() - horizontalPadding * (columnCount + 1)) / columnCount;

        int cellX = (int) (geometry.getX() + horizontalPadding);
        int cellY = (int) (geometry.getY() + verticalPadding);
        int column = 0;

        for (KrWidget widget : widgets) {
//            int cellWidth = columnSize.get(column).intValue();

            layoutInsideCell(widget, new Rectangle(cellX, cellY, cellWidth, cellHeight));

            cellX += cellWidth + horizontalPadding;
            column += 1;
            if (column == columnCount) {
                column = 0;
                cellX = (int) (geometry.getX() + horizontalPadding);
                cellY += cellHeight + verticalPadding;
            }
        }
    }

    private void layoutInsideCell(KrWidget widget, Rectangle cellBounds) {
        Vector2 widgetPreferredSize = widget.getPreferredSize();

        int widgetWidth = (widgetPreferredSize.x >= cellBounds.width) ? (int) cellBounds.width : (int) widgetPreferredSize.x;
        int widgetHeight = (widgetPreferredSize.y >= cellBounds.height) ? (int) cellBounds.height : (int) widgetPreferredSize.y;

        Vector2 widgetPosition = KrAlignmentTool.alignRectangles(new Rectangle(0, 0, widgetWidth, widgetHeight), cellBounds, KrAlignment.MIDDLE_CENTER);

        widget.setBounds(widgetPosition.x, widgetPosition.y, widgetWidth, widgetHeight);
    }

    @Override
    public Vector2 getMinSize() {
        return null;
    }

    @Override
    public Vector2 getMaxSize() {
        return null;
    }

    @Override
    public Vector2 getPreferredSize() {
        return null;
    }

    @Override
    public void addWidget(KrWidget child, Object layoutConstraint) {
        widgets.add(child);
    }

    @Override
    public void removeWidget(KrWidget child) {
        widgets.remove(child);
    }
}
