package com.katzstudio.kreativity.ui.listener;

import com.katzstudio.kreativity.ui.component.KrWidget;

/**
 * Listener for {@link KrWidget} specific events like adding / removing children or property changes.
 */
public interface KrWidgetListener {
    void childAdded(KrWidget child);

    void childRemoved(KrWidget child);

    void propertyChanged(String propertyName, Object oldValue, Object newValue);

    void invalidated();

    class KrAbstractWidgetListener implements KrWidgetListener {

        @Override
        public void childAdded(KrWidget child) {
        }

        @Override
        public void childRemoved(KrWidget child) {
        }

        @Override
        public void propertyChanged(String propertyName, Object oldValue, Object newValue) {
        }

        @Override
        public void invalidated() {
        }
    }
}
