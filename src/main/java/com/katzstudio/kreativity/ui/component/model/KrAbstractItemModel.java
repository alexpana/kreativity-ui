package com.katzstudio.kreativity.ui.component.model;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Inspired by Qt's QAbstractItemModel, the {@link KrAbstractItemModel} class can be used to
 * represent both table and hierarchical values.
 *
 * @see <a href="http://doc.qt.io/qt-4.8/qabstractitemmodel.html#details">QAbstractItemModel Class</a>
 */
@SuppressWarnings("WeakerAccess")
public class KrAbstractItemModel {

    protected final List<Listener> listeners = new ArrayList<>();

    public Object getValue(KrModelIndex index) {
        return null;
    }

    public void setValue(KrModelIndex index, Object value) {

    }

    public int getColumnCount() {
        return getColumnCount(KrModelIndex.ROOT);
    }

    public int getColumnCount(KrModelIndex index) {
        return 0;
    }

    public int getRowCount() {
        return getColumnCount(KrModelIndex.ROOT);
    }

    public int getRowCount(KrModelIndex index) {
        return 0;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    protected void notifyDataChanged() {
        listeners.forEach(Listener::dataChanged);
    }

    // TODO(alex): add support for adding / removing rows

    @Data
    public static class KrModelIndex {
        public static final KrModelIndex ROOT = new KrModelIndex(0, 0, null);

        @Getter private final int row;

        @Getter private final int column;

        @Getter private final KrModelIndex parentIndex;
    }

    public interface Listener {
        void dataChanged();
    }
}
