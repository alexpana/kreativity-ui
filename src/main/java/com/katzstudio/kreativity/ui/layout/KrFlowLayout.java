package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import com.katzstudio.kreativity.ui.component.KrWidget;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.HORIZONTAL;
import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.VERTICAL;

/**
 * A
 */
@RequiredArgsConstructor
public class KrFlowLayout implements KrLayout {

    public enum Direction {
        HORIZONTAL, VERTICAL
    }

    private final Direction direction;

    private final int horizontalPadding;

    private final int verticalPadding;

    private final List<KrWidget> widgets = new ArrayList<>();

    public KrFlowLayout() {
        this(Direction.HORIZONTAL, 0, 0);
    }

    public KrFlowLayout(Direction direction) {
        this(direction, 0, 0);
    }

    @Override
    public void setGeometry(Rectangle geometry) {
        KrSizePolicyModel sizePolicyModel;

        if (direction == HORIZONTAL) {
            sizePolicyModel = new KrSizePolicyModel(widgets.stream()
                    .map(w -> new KrUnifiedSize(w.getPreferredSize().x, 1))
                    .collect(Collectors.toList()));
        } else {
            sizePolicyModel = new KrSizePolicyModel(widgets.stream()
                    .map(w -> new KrUnifiedSize(w.getPreferredSize().y, 1))
                    .collect(Collectors.toList()));
        }

        float widgetAvailableSpace = direction == HORIZONTAL ?
                geometry.getWidth() - (getCols() + 1) * horizontalPadding :
                geometry.getHeight() - (getRows() + 1) * verticalPadding;

        List<Float> sizes = sizePolicyModel.getSizes(widgetAvailableSpace);

        // TODO(alex): move this to the KrSizePolicyModel class
        int totalUsedSize = 0;
        for (int i = 0; i < sizes.size(); ++i) {
            double value = sizes.get(i);
            int floor = (int) Math.floor(value);
            totalUsedSize += floor;
            sizes.set(i, (float) floor);
        }
        int lastElementIndex = sizes.size() - 1;
        sizes.set(lastElementIndex, sizes.get(lastElementIndex) + widgetAvailableSpace - totalUsedSize);

        float cellX = horizontalPadding;
        float cellY = verticalPadding;
        float cellHeight = 0;
        float cellWidth = 0;
        int widgetIndex = 0;

        if (direction == HORIZONTAL) {
            cellHeight = geometry.getHeight() - 2 * verticalPadding;
        } else {
            cellWidth = geometry.getWidth() - 2 * horizontalPadding;
        }

        for (KrWidget widget : widgets) {
            if (direction == HORIZONTAL) {
                cellWidth = sizes.get(widgetIndex);
            } else {
                cellHeight = sizes.get(widgetIndex);
            }

            layoutInsideCell(widget, new Rectangle(cellX, cellY, cellWidth, cellHeight));

            widgetIndex += 1;
            if (direction == HORIZONTAL) {
                cellX += cellWidth + horizontalPadding;
            } else {
                cellY += cellHeight + verticalPadding;
            }
        }
    }

    private void layoutInsideCell(KrWidget widget, Rectangle bounds) {
        // do the actual layout based on the widget constraints
        widget.setBounds(bounds);
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

        if (direction == HORIZONTAL) {
            widgetHorizontalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.x).reduce(Float::sum).orElse(0.0f);
            widgetVerticalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.y).max(Float::compare).orElse(0.0f);
        } else {
            widgetHorizontalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.x).max(Float::compare).orElse(0.0f);
            widgetVerticalSize = widgets.stream().map(widgetSizeFunction).map(v -> v.y).reduce(Float::sum).orElse(0.0f);
        }

        return new Vector2(totalHorizontalPadding + widgetHorizontalSize, totalVerticalPadding + widgetVerticalSize);
    }

    private int getRows() {
        return direction == VERTICAL ? widgets.size() : 1;
    }

    private int getCols() {
        return direction == HORIZONTAL ? widgets.size() : 1;
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
