package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.model.KrItemModel.KrModelIndex;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A selection represents a collections of indexes from a model.
 */
@EqualsAndHashCode
public class KrSelection implements Iterable<KrModelIndex> {

    public static final KrSelection EMPTY = new KrSelection();

    @Getter private final List<KrModelIndex> selectedIndexes;

    private KrSelection() {
        selectedIndexes = Collections.EMPTY_LIST;
    }

    private KrSelection(KrModelIndex singleIndex) {
        selectedIndexes = Collections.singletonList(singleIndex);
    }

    public KrSelection(List<KrModelIndex> selectedIndexes) {
        this.selectedIndexes = new ArrayList<>(selectedIndexes);
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

    public boolean containsRow(int row) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < selectedIndexes.size(); ++i) {
            if (selectedIndexes.get(i).getRow() == row) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return selectedIndexes.size();
    }

    public KrSelection expand(KrModelIndex index) {
        List<KrModelIndex> newSelection = new ArrayList<>(selectedIndexes);
        newSelection.add(index);
        return new KrSelection(newSelection);
    }

    public KrSelection shrink(KrModelIndex index) {
        List<KrModelIndex> newSelection = selectedIndexes.stream().filter(i -> !Objects.equals(i, index)).collect(Collectors.toList());
        return new KrSelection(newSelection);
    }

    @Override
    public Iterator<KrModelIndex> iterator() {
        return selectedIndexes.iterator();
    }

    @Override
    public String toString() {
        BiFunction<String, KrModelIndex, String> acc = (a, x) -> {
            if (a.length() > 0) {
                a += ", ";
            }
            return a + x.toString();
        };
        return "KrSelection[" + selectedIndexes.stream().reduce("", acc, String::concat) + "]";
    }
}
