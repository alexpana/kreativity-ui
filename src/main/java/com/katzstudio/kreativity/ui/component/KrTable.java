package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.KrToolkit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;

/**
 * A Table component
 */
public class KrTable extends Table {

    private final static Integer SCROLLBAR_WIDTH = 5;

    private final static int ROW_PADDING = 0;

    private final static int COLUMN_PADDING = 0;

    private final static int ROW_HEIGHT = 18;

    private final Model model;

    private final List<Cell> cells = Lists.newArrayList();

    private final KrScrollBar scrollBar;

    private final List<Listener> listeners = Lists.newArrayList();

    private final KrWidget selectionHighlight = null;

    private final KrWidget headerBackground = null;

    private Integer selectionIndex = -1;

    @Getter
    private Renderer renderer;

    public KrTable(KrContext uiContext, Model model) {
        this.model = model;

        renderer = new DefaultRenderer(uiContext);
//        selectionHighlight = new KrWidget(KreativitySkin.getColor(SELECTION_BACKGROUND));
//        headerBackground = new KrWidget(KreativitySkin.getColor(BACKGROUND_DARK));
        scrollBar = createScrollBar();

        cells.clear();
        cells.addAll(generateCells());
        reloadComponents();

        setTouchable(Touchable.enabled);
        setClip(true);

        addListener(createInputListener(uiContext));
    }

    private KrScrollBar createScrollBar() {
        KrScrollBar scrollBar = new KrScrollBar();
        scrollBar.setScrollAmount(ROW_HEIGHT + ROW_PADDING);
        scrollBar.setMaxValue(getOverflowHeight());
        scrollBar.addScrollListener(newScrollValue -> layout());
        return scrollBar;
    }

    public void setRenderer(Renderer newRenderer) {
        this.renderer = newRenderer;
        cells.clear();
        cells.addAll(generateCells());

        reloadComponents();
    }

    private InputListener createInputListener(final KrContext uiContext) {
        return new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                uiContext.getStage().setScrollFocus(scrollBar);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                uiContext.getStage().setKeyboardFocus(KrTable.this);
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.DOWN) {
                    setSelectedRow(selectionIndex + 1);
                }
                if (event.getKeyCode() == Input.Keys.UP) {
                    setSelectedRow(selectionIndex - 1);
                }
                return true;
            }
        };
    }

    private List<Cell> generateCells() {
        List<Cell> cells = Lists.newArrayListWithCapacity(model.getColumnCount() * model.getRowCount());

        for (int columnIndex = 0; columnIndex < model.getColumnCount(); ++columnIndex) {
            for (int rowIndex = 0; rowIndex <= model.getRowCount(); ++rowIndex) {
                Object cellValue = rowIndex == 0 ? model.getColumnName(columnIndex) : model.getValueAt(rowIndex - 1, columnIndex);
                Actor cellComponent = renderer.getComponentFor(this, rowIndex, columnIndex, cellValue);
                if (rowIndex > 0) {
                    KrToolkit.addInputListener(cellComponent, createCellInputListener(rowIndex, columnIndex));
                }
                cells.add(new Cell(rowIndex, columnIndex, cellComponent));
            }
        }

        return cells;
    }

    private void reloadComponents() {
        clearChildren();

//        add(selectionHighlight);

        cells.stream().filter(cell -> cell.getRow() > 0).forEach(cell -> add(cell.getComponent()));

//        add(headerBackground);

        cells.stream().filter(cell -> cell.getRow() == 0).forEach(cell -> add(cell.getComponent()));

        add(scrollBar);
    }

    // TODO: find a way to use a single listener for all cells
    private InputListener createCellInputListener(final int finalRowIndex, final int finalColumnIndex) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setSelectedRow(finalRowIndex);
                notifyCellClicked(finalRowIndex, finalColumnIndex, button);
                event.setBubbles(true);
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.setBubbles(true);
                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                event.setBubbles(true);
            }
        };
    }

    public void setSelectedRow(int newSelection) {
        selectionIndex = clamp(newSelection, 1, model.getRowCount());
        ensureRowVisible(selectionIndex);
        layout();
        notifySelectionChanged(newSelection);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        layout();
    }

    public void ensureRowVisible(int rowIndex) {
        float bottom = rowIndex * (ROW_HEIGHT + ROW_PADDING) + ROW_HEIGHT;

        float rowBottom = getHeight() - bottom + getScrollOffset();
        float rowTop = rowBottom + ROW_HEIGHT;

        if (rowBottom < 0) {
            scrollBar.setCurrentValue(scrollBar.getCurrentValue() - rowBottom);
        }


        float viewportTop = getHeight() - ROW_HEIGHT - ROW_PADDING;
        if (rowTop > viewportTop) {
            scrollBar.setCurrentValue(scrollBar.getCurrentValue() + (viewportTop - rowTop));
        }
    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public float getPrefHeight() {
        return (model.getRowCount() + 1) * (ROW_HEIGHT + ROW_PADDING);
    }

    @Override
    public float getMaxHeight() {
        return 10000;
    }

    @Override
    public void layout() {
        List<Float> columnWidths = getColumnWidths();

        cells.stream().forEach(cell -> layoutCell(cell, columnWidths));

        // layout the selection highlight
        if (selectionIndex > -1) {
            int top = selectionIndex * (ROW_HEIGHT + ROW_PADDING) + ROW_HEIGHT;
            int width = getRowWidth();
            if (requiresScrollBar()) {
                width -= SCROLLBAR_WIDTH + 1;
            }
//            selectionHighlight.setBounds(0, getHeight() - top + getScrollOffset(), width, ROW_HEIGHT);
//            selectionHighlight.setVisible(true);
        } else {
//            selectionHighlight.setVisible(false);
        }

        // layout the header background
        headerBackground.setBounds(0, getHeight() - ROW_HEIGHT - ROW_PADDING, getWidth(), ROW_HEIGHT + ROW_PADDING);

        // layout the scroll bar
        if (requiresScrollBar()) {
            scrollBar.setBounds(getRowWidth() - SCROLLBAR_WIDTH, 0, SCROLLBAR_WIDTH, getHeight() - ROW_HEIGHT - 1);
            scrollBar.setVisible(true);
        } else {
            scrollBar.setVisible(false);
        }
    }

    private List<Float> getColumnWidths() {
        return model.getColumnSizePolicyModel().getSizes(getWidth());
    }

    private int getRowWidth() {
        return (int) getWidth();
    }

    private void layoutCell(Cell cell, List<Float> columnWidths) {
        int left = 0;
        for (int i = 0; i < cell.getColumn(); ++i) {
            left += columnWidths.get(i);
        }

        float scrollOffset = getScrollOffset();
        if (cell.getRow() == 0) {
            scrollOffset = 0;
        }

        int top = (int) (cell.getRow() * (ROW_HEIGHT + ROW_PADDING) + ROW_HEIGHT - scrollOffset);
        int addPadding = cell.getColumn() == model.getColumnCount() - 1 ? 0 : 1;

        float width = columnWidths.get(cell.getColumn()) - COLUMN_PADDING * addPadding;

        if (cell.getRow() > 0 && cell.getColumn() == model.getColumnCount() - 1 && requiresScrollBar()) {
            width -= SCROLLBAR_WIDTH + 4;
        }

        cell.getComponent().setBounds(left, getHeight() - top, width, ROW_HEIGHT);
    }

    private float getOverflowHeight() {
        float totalContentHeight = model.getRowCount() * (ROW_HEIGHT + ROW_PADDING);
        float rowAreaHeight = getHeight() - ROW_HEIGHT - ROW_PADDING;
        return totalContentHeight - rowAreaHeight;
    }

    private boolean requiresScrollBar() {
        return getOverflowHeight() > 0;
    }

    private float getScrollOffset() {
        if (requiresScrollBar()) {
            return scrollBar.getCurrentValue();
        } else {
            return 0;
        }
    }

    @Override
    protected void sizeChanged() {
        scrollBar.setMaxValue(getOverflowHeight());
        layout();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyCellClicked(int row, int column, int button) {
        listeners.stream()
                .forEach(listener -> listener.cellClicked(row, column, button));
    }

    private void notifySelectionChanged(int newSelection) {
        listeners.stream()
                .forEach(listener -> listener.selectionChanged(newSelection));
    }

    @RequiredArgsConstructor
    private final class Cell {
        @Getter
        private final int row;

        @Getter
        private final int column;

        @Getter
        private final Actor component;
    }

    /**
     * Data model for the {@link KrTable} ui component.
     */
    public interface Model {
        int getColumnCount();

        int getRowCount();

        Object getValueAt(int row, int column);

        String getColumnName(int column);

        KrSizePolicyModel getColumnSizePolicyModel();
    }

    /**
     * Event listener for {@link KrTable} specific events.
     */
    public interface Listener {
        void cellClicked(int row, int column, int button);

        void selectionChanged(int newSelection);
    }

    public interface Renderer {
        Actor getComponentFor(KrTable table, int row, int column, Object value);
    }

    public static class DefaultRenderer implements Renderer {

        private final Label.LabelStyle cellStyle;

        private final KrContext uiContext;

        public DefaultRenderer(KrContext uiContext) {
            this.uiContext = uiContext;

            cellStyle = new Label.LabelStyle(uiContext.getSkin().get(Label.LabelStyle.class));
            cellStyle.background = KrToolkit.createColorDrawable(new Color(0x00000000));
            cellStyle.background.setLeftWidth(4);
        }

        @Override
        public Actor getComponentFor(KrTable table, int row, int column, Object value) {
            final Label cellComponent = new Label(value.toString(), uiContext.getSkin());
            cellComponent.setStyle(cellStyle);
            return cellComponent;
        }
    }
}
