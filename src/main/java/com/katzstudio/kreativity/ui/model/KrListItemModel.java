package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.component.KrListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link KrListItemModel} is a convenience class that's specialized for {@link KrListView}
 */
public class KrListItemModel<T> extends KrItemModel<T> {

    private final List<T> items = new ArrayList<>();

    public KrListItemModel() {
    }

    public KrListItemModel(List<T> items) {
        this.items.addAll(items);
    }

    @Override
    public T getValue(int row, int column, KrModelIndex parentIndex) {
        // completely ignore parent index / column
        return items.get(row);
    }

    @Override
    public void setValue(KrModelIndex index, T value) {
        items.set(index.getRow(), value);
        notifyDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getColumnCount(KrModelIndex index) {
        return getColumnCount();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getRowCount(KrModelIndex index) {
        return getRowCount();
    }
}
