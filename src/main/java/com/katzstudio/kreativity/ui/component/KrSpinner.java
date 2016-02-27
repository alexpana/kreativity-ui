package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrContext;
import com.katzstudio.kreativity.ui.KrModel;
import com.katzstudio.kreativity.ui.KrToolkit;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Spinner component.
 */
public class KrSpinner extends TextField {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private float value = 0;

    @Getter
    @Setter
    private float increment = 0.1f;

    private final List<EditListener> editListeners = Lists.newArrayList();

    @Getter
    @Setter
    private KrModel<Float> model;

    public KrSpinner(KrContext uiContext, String text) {
        super(text, uiContext.getSkin());

        KrToolkit.ensureUniqueStyle(this);

        Drawable noedit_background = uiContext.getSkin().getDrawable("spinner.background_noedit");
        Drawable edit_background = uiContext.getSkin().getDrawable("spinner.background_edit");

        getStyle().background = noedit_background;
        getStyle().background.setBottomHeight(3);
        getStyle().background.setLeftWidth(7);

        addListener(createInputListener(uiContext));
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (event.isFocused()) {
                    getStyle().background = edit_background;
                } else {
                    getStyle().background = noedit_background;
                }
                validateText();
            }
        });

        setText("0");
    }

    private InputListener createInputListener(final KrContext uiContext) {
        return new InputListener() {
            private boolean dragStarted;
            private boolean wasDragged = false;

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    validateText();
                    notifyEditStopped();
                    return true;
                }

                if (event.getKeyCode() == Input.Keys.ESCAPE) {
                    validateText();
                    notifyEditStopped();
                    uiContext.getStage().setKeyboardFocus(null);
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                wasDragged = false;

                if (button == Input.Buttons.RIGHT) {
                    notifyEditStarted();
                    setValue(0);
                    notifyEditStopped();
                }

                if (button == Input.Buttons.LEFT) {
                    dragStarted = true;
                    notifyEditStarted();
                }

                uiContext.getStage().setKeyboardFocus(null);
                event.setBubbles(false);
                event.handle();
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragStarted) {
                    wasDragged = true;
                    incrementValue(Gdx.input.getDeltaX());
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragStarted = false;
                if (!wasDragged && button == Input.Buttons.LEFT) {
                    uiContext.getStage().setKeyboardFocus(KrSpinner.this);
                } else {
                    notifyEditStopped();
                }
            }
        };
    }

    private void validateText() {
        if (isFloatRepresentation(getText())) {
            setValue(Float.valueOf(getText()));
        } else {
            resetText();
        }
    }

    private void resetText() {
        setText(String.valueOf(value));
    }

    private void incrementValue(float times) {
        setValue(value + increment * times);
    }

    @SuppressWarnings("deprecation")
    public void setValue(float value) {
        if (this.value != value) {
            this.value = value;
            if (model != null) {
                model.setValue(value);
            }
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

    /**
     * @deprecated please use {@link KrSpinner#setValue} instead.
     */
    @Deprecated
    @Override
    public void setText(String str) {
        super.setText(str);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (model != null && model.getValue() != value) {
            setValue(model.getValue());
        }

        super.draw(batch, parentAlpha);
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
