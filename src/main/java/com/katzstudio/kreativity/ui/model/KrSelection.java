package com.katzstudio.kreativity.ui.model;

import com.google.common.collect.ImmutableList;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;

/**
 * A selection represents a collections of indexes from a model.
 */
@EqualsAndHashCode
public class KrSelection implements Iterable<KrModelIndex> {

    public static final KrSelection EMPTY = new KrSelection();

    @Getter private final ImmutableList<KrModelIndex> selectedIndexes;

    private KrSelection() {
        selectedIndexes = ImmutableList.of();
    }

    private KrSelection(KrModelIndex singleIndex) {
        selectedIndexes = ImmutableList.of(singleIndex);
    }

    public KrSelection(List<KrModelIndex> selectedIndexes) {
        this.selectedIndexes = ImmutableList.copyOf(selectedIndexes);
    }

    public static KrSelection of(KrModelIndex index) {
        if (index == null) {
            return EMPTY;
        } else {
            return new KrSelection(index);
        }
    }

    public boolean contains(KrModelIndex index) {
        return selectedIndexes.contains(index);
    }

    public int size() {
        return selectedIndexes.size();
    }

    @Override
    public Iterator<KrModelIndex> iterator() {
        return selectedIndexes.iterator();
    }
}
