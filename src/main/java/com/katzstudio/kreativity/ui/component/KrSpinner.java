package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;
import static com.katzstudio.kreativity.ui.event.KrMouseEvent.Button.LEFT;
import static com.katzstudio.kreativity.ui.event.KrMouseEvent.Button.RIGHT;

/**
 * Spinner component.
 */
public class KrSpinner extends KrTextField {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private String formattedValue;

    private float modelValue;

    @Getter @Setter private float increment = 0.1f;

    private final List<EditListener> editListeners = new ArrayList<>();

    @Getter @Setter private KrValueModel<Float> spinnerModel = new KrValueModel.Default<>(0.0f);

    private boolean dragStarted;

    private boolean wasDragged = false;

    public KrSpinner() {
        setDefaultStyle(getDefaultToolkit().getSkin().getStyle(KrSpinner.class));
        setCursor(KrCursor.ARROW);
    }

    @Override
    protected void keyPressedEvent(KrKeyEvent event) {
        if (event.getKeycode() == Input.Keys.ENTER || event.getKeycode() == Input.Keys.ESCAPE) {
            validateText();
            notifyEditStopped();
            clearFocus();
            event.accept();
            return;
        }

        super.keyPressedEvent(event);
    }

    @Override
    protected void mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);
        wasDragged = false;

        if (event.getButton() == RIGHT) {
            notifyEditStarted();
            setValue(0);
            notifyEditStopped();
        }

        if (event.getButton() == LEFT) {
            dragStarted = true;
            notifyEditStarted();
        }

        clearFocus();
        event.accept();
    }

    @Override
    protected void mouseMoveEvent(KrMouseEvent event) {
        super.mouseMoveEvent(event);

        if (dragStarted) {
            wasDragged = true;
            incrementValue(Gdx.input.getDeltaX());
        }
        event.accept();
    }

    @Override
    protected void mouseReleasedEvent(KrMouseEvent event) {
        super.mouseReleasedEvent(event);

        dragStarted = false;
        if (!wasDragged && event.getButton() == LEFT) {
            requestFocus();
            textDocument.selectAll();
        } else {
            notifyEditStopped();
        }
        event.accept();
    }

    private void validateText() {
        if (isFloatRepresentation(getText())) {
            setValue(Float.valueOf(getText()));
        } else {
            resetText();
        }
    }

    private void resetText() {
        setText(String.valueOf(spinnerModel.getValue()));
    }

    private void incrementValue(float times) {
        float newValue = spinnerModel.getValue() + increment * times;
        setValue(newValue);
    }

    @SuppressWarnings("deprecation")
    public void setValue(float value) {
        if (spinnerModel.getValue() != value) {
            spinnerModel.setValue(value);
            setText(FORMAT.format(value));
            notifyValueChanged(value);
        }
    }

    private boolean isFloatRepresentation(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Float.valueOf(value);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override
    public void update(float deltaSeconds) {
        if (spinnerModel.getValue() != modelValue) {
            modelValue = spinnerModel.getValue();
            formattedValue = FORMAT.format(modelValue);
        }
        if (!isFocused()) {
            setText(formattedValue);
        }
        super.update(deltaSeconds);
    }

    /**
     * @deprecated please use {@link KrSpinner#setValue} instead.
     */
    @Deprecated
    @Override
    public void setText(String str) {
        super.setText(str);
    }

    public void addEditListener(EditListener editListener) {
        editListeners.add(editListener);
    }

    public void removeEditListener(EditListener editListener) {
        editListeners.remove(editListener);
    }

    private void notifyEditStarted() {
        editListeners.forEach(EditListener::editStarted);
    }

    private void notifyValueChanged(float newValue) {
        editListeners.forEach(listener -> listener.valueChanged(newValue));
    }

    private void notifyEditStopped() {
        editListeners.forEach(EditListener::editStopped);
    }

    public interface EditListener {
        void editStarted();

        void valueChanged(float newValue);

        void editStopped();
    }
}
