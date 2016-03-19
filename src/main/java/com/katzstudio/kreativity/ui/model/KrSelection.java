package com.katzstudio.kreativity.ui.model;

import com.google.common.collect.ImmutableList;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * A selection represents a collections of indexes from a model.
 */
@EqualsAndHashCode
public class KrSelection {

    public static final KrSelection EMPTY = new KrSelection();

    @Getter private final ImmutableList<KrModelIndex> selectedIndexes;

    private KrSelection() {
        selectedIndexes = ImmutableList.of();
    }

    public KrSelection(KrModelIndex singleIndex) {
        selectedIndexes = ImmutableList.of(singleIndex);
    }

    public KrSelection(List<KrModelIndex> selectedIndexes) {
        this.selectedIndexes = ImmutableList.copyOf(selectedIndexes);
    }

    public boolean contains(KrModelIndex index) {
        return selectedIndexes.contains(index);
    }

    public int size() {
        return selectedIndexes.size();
    }
}
