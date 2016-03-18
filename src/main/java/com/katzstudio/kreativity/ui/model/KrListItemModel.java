package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.component.KrListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link KrListItemModel} is a convenience class that's specialized for {@link KrListView}
 */
public class KrListItemModel<T> extends KrAbstractItemModel {

    private final List<T> items = new ArrayList<>();

    public KrListItemModel() {
    }

    public KrListItemModel(List<T> items) {
        this.items.addAll(items);
    }

    @Override
    public Object getValue(KrModelIndex index) {
        // completely ignore parent index / column
        return items.get(index.getRow());
    }

    @Override
    public void setValue(KrModelIndex index, Object value) {
        items.set(index.getRow(), (T) value);
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
