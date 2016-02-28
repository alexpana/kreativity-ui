package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrUnifiedSize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

/**
 * Container panel that splits it's area between children. Areas can be resized.
 */
public class KrSplitPanel extends Table {

    private static final int SEPARATOR_SIZE = 1;

    private final List<KrWidget> separatorList = Lists.newArrayList();

    private final List<Cell> cells = Lists.newArrayList();

    private final CellSizePolicyModel cellSizePolicyModel = new CellSizePolicyModel();

    public KrSplitPanel() {
    }

    public void add(Actor child, KrUnifiedSize preferredSize) {
        add(cells.size(), child, preferredSize);
    }

    public void add(int index, Actor child, KrUnifiedSize preferredSize) {
        Cell cell = new Cell(child, preferredSize);
        cells.add(index, cell);
        add(child);
        generateSeparators();
    }

    private void generateSeparators() {
//        separatorList.forEach(this::removeActor);
//        separatorList.clear();

        for (int i = 1; i < cells.size(); ++i) {
            final Cell topCell = cells.get(i - 1);
            final Cell bottomCell = cells.get(i);

//            KrWidget separator = new KrWidget(KrColor.TRANSPARENT);
//            separator.addListener(new InputListener() {
//                private float deltaY = 0;
//
//                private float topCellOffset = 0;
//                private float bottomCellOffset = 0;
//
//                private float maxDelta;
//                private float minDelta;
//
//                private KrSkin.Cursor previousCursor;
//
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    deltaY = 0;
//                    topCellOffset = topCell.getOffset();
//                    bottomCellOffset = bottomCell.getOffset();
//
//                    minDelta = Math.min(0, topCell.getPreferredHeight() - topCell.getHeight());
//                    maxDelta = Math.max(0, bottomCell.getHeight() - bottomCell.getPreferredHeight());
//
//                    return true;
//                }
//
//                @Override
//                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                    if (previousCursor == null) {
//                        previousCursor = KrToolkit.getCursor();
//                        KrToolkit.setCursor(KrSkin.Cursor.RESIZE_V);
//                    }
//                }
//
//                @Override
//                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                    KrToolkit.setCursor(previousCursor);
//                    previousCursor = null;
//                }
//
//                @Override
//                public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                    deltaY += Gdx.input.getDeltaY();
//
//                    int actualDelta = (int) Math.max(minDelta, Math.min(maxDelta, deltaY));
//
//                    topCell.setOffset(topCellOffset + actualDelta);
//                    bottomCell.setOffset(bottomCellOffset - actualDelta);
//                    layout();
//                }
//            });

//            separatorList.add(separator);
//            add(separator);
        }

        layout();
    }

    public void remove(Actor child) {
        removeCell(findCellFromActor(child));
    }

    private void removeCell(Cell cell) {
        cells.remove(cell);
        removeActor(cell.getComponent());
        generateSeparators();
    }

    private Cell findCellFromActor(Actor child) {
        for (Cell cell : cells) {
            if (cell.getComponent().equals(child)) {
                return cell;
            }
        }
        return null;
    }

    @Override
    public void layout() {
        float availableSize = getHeight() - separatorList.size() * SEPARATOR_SIZE;
        List<Float> sizes = cellSizePolicyModel.getSizes(availableSize);

        float topOffset = 0;
        int cellIndex = 0;
        for (Cell cell : cells) {
            final int cellHeight = (int) (sizes.get(cellIndex) + cell.getOffset());

            cell.getComponent().setBounds(0, (int) (getHeight() - topOffset - cellHeight - 0.5), getWidth(), cellHeight);

            if (cellIndex < separatorList.size()) {
                separatorList.get(cellIndex).setBounds(0, getHeight() - topOffset - cellHeight - SEPARATOR_SIZE - 1, getWidth(), SEPARATOR_SIZE + 2);
            }
            cellIndex += 1;
            topOffset += cellHeight + SEPARATOR_SIZE + 0.5;
        }
    }

    @Override
    protected void sizeChanged() {
        layout();
    }

    private final class CellSizePolicyModel extends KrSizePolicyModel {
        @Override
        public Stream<KrUnifiedSize> stream() {
            return cells.stream().map(Cell::getPreferredSize);
        }
    }

    private final static class Cell {

        @Getter
        private final Actor component;

        @Getter
        @Setter
        private float offset;

        @Getter
        @Setter
        private KrUnifiedSize preferredSize;

        private Cell(Actor component, KrUnifiedSize preferredSize) {
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

    @RequiredArgsConstructor
    private final static class Separator {
        @Getter
        private final KrWidget component;

        @Getter
        private final Cell topCell;

        @Getter
        private final Cell bottomCell;
    }
}
