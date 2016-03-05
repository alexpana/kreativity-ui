package com.katzstudio.kreativity.ui;

import com.katzstudio.kreativity.ui.component.KrWidget;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The {@link KrFocusManager} class takes care of switching the focus between widgets.
 */
public class KrFocusManager {

    private final KrWidget rootWidget;

    private final List<KrWidget> orderedFocusableWidgets = new LinkedList<>();

    public KrFocusManager(KrWidget rootWidget) {
        this.rootWidget = rootWidget;
        refresh();
    }

    public void refresh() {
        orderedFocusableWidgets.clear();
        Queue<KrWidget> toProcess = new LinkedList<>();
        toProcess.add(rootWidget);

        while (!toProcess.isEmpty()) {
            KrWidget widget = toProcess.poll();
            if (widget.isFocusable()) {
                orderedFocusableWidgets.add(widget);
            }
            widget.getChildren().forEach(toProcess::add);
        }
    }

    public KrWidget nextFocusable(KrWidget currentlyFocused) {
        int focusHolderIndex = orderedFocusableWidgets.indexOf(currentlyFocused);
        int nextFocusHolderIndex = (focusHolderIndex + 1) % orderedFocusableWidgets.size();
        return orderedFocusableWidgets.get(nextFocusHolderIndex);
    }

    public KrWidget previousFocusable(KrWidget currentlyFocused) {
        int focusHolderIndex = orderedFocusableWidgets.indexOf(currentlyFocused);
        int previousIndex = (orderedFocusableWidgets.size() + focusHolderIndex - 1) % orderedFocusableWidgets.size();
        return orderedFocusableWidgets.get(previousIndex);
    }
}
