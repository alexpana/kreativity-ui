package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrOrientation;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import com.katzstudio.kreativity.ui.component.KrWidget;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.katzstudio.kreativity.ui.KrOrientation.HORIZONTAL;
import static com.katzstudio.kreativity.ui.KrOrientation.VERTICAL;

/**
 * A flow layout is used to arrange widgets in a row or a column.
 * <p>
 * A vertical flow layout distributes all widgets in a single column.
 * All widgets have the same width, but preferred heights are taken
 * into consideration.
 * <p>
 * A horizontal flow layout distributes all widgets in a single row.
 * All widgets have the same height, but preferred widths are taken
 * into consideration.
 */
@RequiredArgsConstructor
public class KrFlowLayout implements KrLayout {

    private final KrOrientation orientation;

    private final int horizontalPadding;

    private final int verticalPadding;

    private final List<KrWidget> widgets = new ArrayList<>();

    @SuppressWarnings("unused")
    public KrFlowLayout() {
        this(HORIZONTAL, 0, 0);
    }

    public KrFlowLayout(KrOrientation direction) {
        this(direction, 0, 0);
    }

    @Override
    public void setGeometry(Rectangle geometry) {
        KrSizePolicyModel sizePolicyModel;

        if (orientation == HORIZONTAL) {
            sizePolicyModel = new KrSizePolicyModel(widgets.stream()
                    .map(w -> new KrUnifiedSize(w.getPreferredSize().x, 1))
                    .collect(Collectors.toList()));
        } else {
            sizePolicyModel = new KrSizePolicyModel(widgets.stream()
                    .map(w -> new KrUnifiedSize(w.getPreferredSize().y, 1))
                    .collect(Collectors.toList()));
        }

        float widgetAvailableSpace = orientation == HORIZONTAL ?
                geometry.getWidth() - (getCols() + 1) * horizontalPadding :
                geometry.getHeight() - (getRows() + 1) * verticalPadding;

        List<Integer> sizes = sizePolicyModel.getIntSizes(widgetAvailableSpace);

        float cellX = horizontalPadding;
        float cellY = verticalPadding;
        float cellHeight = 0;
        float cellWidth = 0;
        int widgetIndex = 0;

        if (orientation == HORIZONTAL) {
            cellHeight = geometry.getHeight() - 2 * verticalPadding;
        } else {
            cellWidth = geometry.getWidth() - 2 * horizontalPadding;
        }

        for (KrWidget widget : widgets) {
            if (orientation == HORIZONTAL) {
                cellWidth = sizes.get(widgetIndex);
            } else {
                cellHeight = sizes.get(widgetIndex);
            }

            layoutInsideCell(widget, new Rectangle(cellX, cellY, cellWidth, cellHeight));

            widgetIndex += 1;
            if (orientation == HORIZONTAL) {
                cellX += cellWidth + horizontalPadding;
            } else {
                cellY += cellHeight + verticalPadding;
            }
        }
    }

    private void layoutInsideCell(KrWidget widget, Rectangle bounds) {
        widget.setGeometry(bounds);
    }

    @Override
    public Vector2 getMinSize() {
        return getSize(KrWidget::getMinSize);
    }

    @Override
    public Vector2 getMaxSize() {
        return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Vector2 getPreferredSize() {
        return getSize(KrWidget::getPreferredSize);
    }

    private Vector2 getSize(Function<KrWidget, Vector2> widgetSizeFunction) {
        if (widgets.size() == 0) {
            return new Vector2(0, 0);
        }

        float totalHorizontalPadding = horizontalPadding * (getCols() + 1);
        float totalVerticalPadding = verticalPadding * (getRows() + 1);

        float widgetHorizontalSize;
        float widgetVerticalSize;

        if (orientation == HORIZONTAL) {
            widgetHorizontalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.x).reduce(Float::sum).orElse(0.0f);
            widgetVerticalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.y).max(Float::compare).orElse(0.0f);
        } else {
            widgetHorizontalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.x).max(Float::compare).orElse(0.0f);
            widgetVerticalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.y).reduce(Float::sum).orElse(0.0f);
        }

        return new Vector2(totalHorizontalPadding + widgetHorizontalSize, totalVerticalPadding + widgetVerticalSize);
    }

    private int getRows() {
        return orientation == VERTICAL ? widgets.size() : 1;
    }

    private int getCols() {
        return orientation == HORIZONTAL ? widgets.size() : 1;
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
