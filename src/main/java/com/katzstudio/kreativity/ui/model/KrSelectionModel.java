package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.component.KrListView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A selection model for a {@link KrListView} widget.
 * <p>
 * The selection model determines what selections are possible (multiple, single, etc)
 * and what items are currently selected.
 */
public class KrSelectionModel {

    private final List<KrListSelectionListener> listeners = new ArrayList<>();

    @Getter private KrSelection currentSelection = KrSelection.EMPTY;

    public KrSelectionModel() {
    }

    public void setSelection(KrSelection newSelection) {
        if (!Objects.equals(currentSelection, newSelection)) {
            KrSelection oldSelection = currentSelection;
            currentSelection = newSelection;
            notifySelectionChanged(oldSelection, newSelection);
        }
    }

    public void clearSelection() {
        setSelection(KrSelection.EMPTY);
    }

    public void addSelectionListener(KrListSelectionListener listener) {
        listeners.add(listener);
    }

    public void removeSelectionListener(KrListSelectionListener listener) {
        listeners.remove(listener);
    }

    protected void notifySelectionChanged(KrSelection oldSelection, KrSelection newSelection) {
        listeners.forEach(l -> l.selectionChanged(oldSelection, newSelection));
    }

    public interface KrListSelectionListener {
        void selectionChanged(KrSelection oldSelection, KrSelection newSelection);
    }
}
