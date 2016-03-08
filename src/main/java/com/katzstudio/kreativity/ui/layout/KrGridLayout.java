package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A layout implementation that distributes widgets in a grid.
 * <p>
 * All cells have the same height. Column widths can differ.
 */
public class KrGridLayout implements KrLayout {

    private final int columnCount;

    private final List<KrWidget> widgets = Lists.newArrayList();

    private final Map<KrWidget, Constraint> constraints = new HashMap<>();

    private final int verticalPadding;

    private final int horizontalPadding;

    @Getter private KrSizePolicyModel columnSizePolicy;

    private boolean isAdjusting = false;

    public KrGridLayout(int columns) {
        this(columns, 0, 0);
    }

    public KrGridLayout(int columns, int horizontalPadding, int verticalPadding) {
        this.verticalPadding = verticalPadding;
        this.horizontalPadding = horizontalPadding;
        this.columnCount = columns;
        this.columnSizePolicy = new KrSizePolicyModel(columns);
    }

    public void setColumnSizePolicy(KrSizePolicyModel sizePolicy) {
        int elementCount = sizePolicy.getCount();
        if (elementCount != columnCount) {
            throw new IllegalArgumentException(
                    String.format("Size policy has wrong number of elements. Expected %d but got %d.",
                            columnCount,
                            elementCount));
        }

        this.columnSizePolicy = sizePolicy;
    }

    @Override
    public void setGeometry(Rectangle geometry) {
        if (isAdjusting) {
            return;
        }

        isAdjusting = true;
        int rowCount = getRowCount();
        int cellHeight = (int) (geometry.getHeight() - verticalPadding * (rowCount + 1)) / rowCount;

        List<Float> sizes = columnSizePolicy.getSizes(geometry.getWidth() - horizontalPadding * (rowCount + 1));

        int cellX = (int) (geometry.getX() + horizontalPadding);
        int cellY = (int) (geometry.getY() + verticalPadding);
        int column = 0;

        for (KrWidget widget : widgets) {
            float cellWidth = sizes.get(column);
            layoutInsideCell(widget, new Rectangle(cellX, cellY, cellWidth, cellHeight));

            cellX += cellWidth + horizontalPadding;
            column += 1;
            if (column == columnCount) {
                column = 0;
                cellX = (int) (geometry.getX() + horizontalPadding);
                cellY += cellHeight + verticalPadding;
            }
        }
        isAdjusting = false;
    }

    private int getRowCount() {
        return (int) (widgets.size() / columnCount + 0.5);
    }

    private void layoutInsideCell(KrWidget widget, Rectangle cellBounds) {
        Constraint constraint = constraints.get(widget);
        Vector2 widgetPreferredSize = widget.getPreferredSize();

        int widgetWidth = (widgetPreferredSize.x >= cellBounds.width) ? (int) cellBounds.width : (int) widgetPreferredSize.x;
        int widgetHeight = (widgetPreferredSize.y >= cellBounds.height) ? (int) cellBounds.height : (int) widgetPreferredSize.y;

        if (constraint.stretchHorizontal) {
            widgetWidth = (int) cellBounds.width;
        }

        if (constraint.stretchVertical) {
            widgetHeight = (int) cellBounds.height;
        }

        Vector2 widgetPosition = KrAlignmentTool.alignRectangles(new Rectangle(0, 0, widgetWidth, widgetHeight), cellBounds, constraint.alignment);

        widget.setBounds((int) widgetPosition.x, (int) widgetPosition.y, widgetWidth, widgetHeight);
    }

    @Override
    public Vector2 getMinSize() {
        float cellWidth = widgets.stream().map(widget -> widget.getMinSize().x).max(Float::compare).orElse(0.0f);
        float cellHeight = widgets.stream().map(widget -> widget.getMinSize().y).max(Float::compare).orElse(0.0f);
        float rowCount = getRowCount();

        return new Vector2(columnCount * cellWidth + (columnCount + 1) * horizontalPadding,
                rowCount * cellHeight + (rowCount + 1) * verticalPadding);
    }

    @Override
    public Vector2 getMaxSize() {
        return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Vector2 getPreferredSize() {
        float cellWidth = widgets.stream().map(widget -> widget.getPreferredSize().x).max(Float::compare).orElse(0.0f);
        float cellHeight = widgets.stream().map(widget -> widget.getPreferredSize().y).max(Float::compare).orElse(0.0f);
        float rowCount = getRowCount();

        return new Vector2(columnCount * cellWidth + (columnCount + 1) * horizontalPadding,
                rowCount * cellHeight + (rowCount + 1) * verticalPadding);
    }

    @Override
    public void addWidget(KrWidget child, Object layoutConstraint) {
        if (layoutConstraint == null) {
            layoutConstraint = Constraint.DEFAULT;
        }

        if (!(layoutConstraint instanceof Constraint)) {
            throw new IllegalArgumentException("Unrecognized constraint class");
        }

        constraints.put(child, (Constraint) layoutConstraint);
        widgets.add(child);
    }

    @Override
    public void removeWidget(KrWidget child) {
        widgets.remove(child);
        constraints.remove(child);
    }

    @RequiredArgsConstructor
    public static class Constraint {

        public static final Constraint DEFAULT = new Constraint(KrAlignment.MIDDLE_CENTER, false, false);

        @Getter private final KrAlignment alignment;

        @Getter final boolean stretchHorizontal;

        @Getter final boolean stretchVertical;
    }
}
