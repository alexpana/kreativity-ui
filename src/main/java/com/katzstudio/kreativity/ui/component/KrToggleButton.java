package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.event.KrEnterEvent;
import com.katzstudio.kreativity.ui.event.KrExitEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A toggle button is a button who remains pressed after clicking (is checked). Clicking the button again
 * unchecks the button, bringing it to its original state.
 */
public class KrToggleButton extends KrButton {

    @Getter private boolean isChecked = false;

    private final List<KrToggleButtonListener> listeners = new ArrayList<>();

    public KrToggleButton(String text) {
        super(text);
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);
        setState(State.ARMED);
        return true;
    }

    @Override
    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        isChecked = !isChecked;
        if (!isChecked) {
            setState(State.HOVERED);
        }
        notifyToggled();
        notifyClicked();
        notifyMouseReleased(event);
        return true;
    }


    @Override
    protected boolean enterEvent(KrEnterEvent event) {
        super.enterEvent(event);
        if (isChecked) {
            setState(State.ARMED);
        } else {
            setState(State.HOVERED);
        }
        return true;
    }

    @Override
    protected boolean exitEvent(KrExitEvent event) {
        super.exitEvent(event);
        if (isChecked) {
            setState(State.ARMED);
        } else {
            setState(State.NORMAL);
        }
        return true;
    }

    public void setChecked(boolean isChecked) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked;

            if (isChecked) {
                setState(State.ARMED);
            } else {
                setState(State.NORMAL);
            }

            notifyToggled();
        }
    }

    public void addToggleListener(KrToggleButtonListener listener) {
        listeners.add(listener);
    }

    public void removeToggleListener(KrToggleButtonListener listener) {
        listeners.remove(listener);
    }

    protected void notifyToggled() {
        listeners.forEach(listener -> listener.toggled(isChecked));
    }


    public interface KrToggleButtonListener {
        void toggled(boolean isChecked);
    }
}
