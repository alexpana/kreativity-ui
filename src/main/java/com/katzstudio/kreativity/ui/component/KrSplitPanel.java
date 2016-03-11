package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrColor;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.event.listener.KrMouseListener;
import com.katzstudio.kreativity.ui.layout.KrLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

/**
 * Container panel that splits it's area between children. Areas can be resized.
 */
public class KrSplitPanel extends KrWidget {

    private static final int SEPARATOR_SIZE = 1;

    private final List<KrWidget> separatorList = Lists.newArrayList();

    private final List<Cell> cells = Lists.newArrayList();

    private final CellSizePolicyModel cellSizePolicyModel = new CellSizePolicyModel();

    public KrSplitPanel() {
        setLayout(new LayoutManager());
    }

    public void add(KrWidget widget, KrUnifiedSize preferredSize) {
        add(cells.size(), widget, preferredSize);
    }

    public void add(int index, KrWidget widget, KrUnifiedSize preferredSize) {
        Cell cell = new Cell(widget, preferredSize);
        cells.add(index, cell);
        add(widget);
        generateSeparators();
    }

    private void generateSeparators() {
        separatorList.forEach(this::remove);
        separatorList.clear();

        for (int i = 1; i < cells.size(); ++i) {
            final Cell topCell = cells.get(i - 1);
            final Cell bottomCell = cells.get(i);

            KrPanel separator = new KrPanel();
            separator.ensureUniqueStyle();
            ((KrPanel.Style) separator.getStyle()).background = KrToolkit.createColorDrawable(KrColor.rgb(0xff0000));

            separator.addMouseListener(new KrMouseListener.KrMouseAdapter() {
                private float deltaY = 0;
                private boolean dragging = false;
                private float topCellOffset = 0;
                private float bottomCellOffset = 0;

                private float maxDelta;
                private float minDelta;

                @Override
                public void mousePressed(KrMouseEvent event) {
                    deltaY = 0;
                    topCellOffset = topCell.getOffset();
                    bottomCellOffset = bottomCell.getOffset();

                    minDelta = Math.min(0, topCell.getPreferredHeight() - topCell.getHeight());
                    maxDelta = Math.max(0, bottomCell.getHeight() - bottomCell.getPreferredHeight());
                    dragging = true;
                }

                @Override
                public void mouseReleased(KrMouseEvent event) {
                    dragging = false;
                }

                @Override
                public void mouseMoved(KrMouseEvent event) {
                    if (dragging) {
                        deltaY += event.getDeltaMove().y;
                        int actualDelta = (int) Math.max(minDelta, Math.min(maxDelta, deltaY));
                        topCell.setOffset(topCellOffset + actualDelta);
                        bottomCell.setOffset(bottomCellOffset - actualDelta);
                        validate();
                    }
                }
            });

            separatorList.add(separator);
            add(separator);
        }

        validate();
    }

    @Override
    public void remove(KrWidget child) {
        super.remove(child);

        removeCell(findCellForWidget(child));
    }

    private void removeCell(Cell cell) {
        if (cell == null) {
            return;
        }

        cells.remove(cell);
        remove(cell.getComponent());
        generateSeparators();
    }

    private Cell findCellForWidget(KrWidget widget) {
        for (Cell cell : cells) {
            if (cell.getComponent().equals(widget)) {
                return cell;
            }
        }
        return null;
    }

    private final class LayoutManager implements KrLayout {

        @Override
        public void setGeometry(Rectangle geometry) {
            float availableSize = geometry.getHeight() - separatorList.size() * SEPARATOR_SIZE;
            List<Float> sizes = cellSizePolicyModel.getSizes(availableSize);

            float cellWidth = geometry.getWidth();
            float topOffset = 0;
            int cellIndex = 0;
            for (Cell cell : cells) {
                final int cellHeight = (int) (sizes.get(cellIndex) + cell.getOffset());

                cell.getComponent().setBounds(0, topOffset, cellWidth, cellHeight);

                if (cellIndex < separatorList.size()) {
                    separatorList.get(cellIndex).setBounds(0, topOffset + cellHeight - 1, cellWidth, SEPARATOR_SIZE + 2);
                }
                cellIndex += 1;
                topOffset += cellHeight + SEPARATOR_SIZE;
            }
        }

        @Override
        public Vector2 getMinSize() {
            float minWidth = cells.stream().map(cell -> cell.component.getMinWidth()).max(Float::compare).get();
            float minHeight = cells.stream().map(cell -> cell.component.getMinHeight()).reduce(0f, Float::sum);

            return new Vector2(minWidth, minHeight + (cells.size() - 1) * SEPARATOR_SIZE);
        }

        @Override
        public Vector2 getMaxSize() {
            return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        }

        @Override
        public Vector2 getPreferredSize() {
            float prefWidth = cells.stream().map(cell -> cell.component.getPreferredWidth()).max(Float::compare).get();
            float prefHeight = cells.stream().map(cell -> cell.component.getPreferredHeight()).reduce(0f, Float::sum);

            return new Vector2(prefWidth, prefHeight + (cells.size() - 1) * SEPARATOR_SIZE);
        }

        @Override
        public void addWidget(KrWidget child, Object layoutConstraint) {
        }

        @Override
        public void removeWidget(KrWidget child) {
        }
    }

    private final class CellSizePolicyModel extends KrSizePolicyModel {
        @Override
        public Stream<KrUnifiedSize> stream() {
            return cells.stream().map(Cell::getPreferredSize);
        }
    }

    private final static class Cell {

        @Getter private final KrWidget component;

        // Used internally when dragging to keep track of the drag offset
        @Getter @Setter private float offset;

        @Getter @Setter private KrUnifiedSize preferredSize;

        private Cell(KrWidget component, KrUnifiedSize preferredSize) {
            this.component = component;
            this.preferredSize = preferredSize;
        }

        public float getPreferredHeight() {
            return preferredSize.getAbsolute();
        }

        public float getHeight() {
            return component.getHeight();
        }
    }
}
