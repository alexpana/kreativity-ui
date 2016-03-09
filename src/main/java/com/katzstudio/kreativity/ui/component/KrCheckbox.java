package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrModel;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple checkbox component that can be either checked or unchecked.
 */
public class KrCheckbox extends KrWidget {

    @Getter private boolean isChecked;

    @Setter private Style style;

    @Getter @Setter private String text;

    @Getter @Setter private KrModel<Boolean> model = new KrModel.Empty<>();

    private final List<ValueListener> valueListeners = new ArrayList<>();

    public KrCheckbox() {
        setStyle(KrSkin.instance().getCheckboxStyle());
        setSize(calculatePreferredSize());
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getCheckboxStyle()) {
            style = style.copy();
        }
    }

    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;
            model.setValue(checked);
            notifyValueChanged(checked);
        }
    }

    public void addValueListener(ValueListener valueListener) {
        valueListeners.add(valueListener);
    }

    public void removeValueListener(ValueListener valueListener) {
        valueListeners.remove(valueListener);
    }

    private void notifyValueChanged(boolean newValue) {
        valueListeners.forEach(listener -> listener.valueChanged(newValue));
    }

    @Override
    public Vector2 calculatePreferredSize() {
        // TODO(alex): add support for text
        return new Vector2(14, 15);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        // TODO(alex): add support for text
        renderer.setBrush(new KrDrawableBrush(style.background));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        if (isChecked) {
            renderer.setBrush(new KrDrawableBrush(style.mark));
            renderer.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void update(float deltaSeconds) {
        if (!(model instanceof KrModel.Empty) && model.getValue() != isChecked) {
            isChecked = model.getValue();
        }
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);

        if (event.getButton() == KrMouseEvent.Button.LEFT) {
            event.accept();
            setChecked(!isChecked);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrCheckbox").toString();
    }

    @AllArgsConstructor
    public static class Style {

        public Drawable background;

        public Drawable mark;

        public Style copy() {
            return new Style(background, mark);
        }
    }

    public interface ValueListener {
        void valueChanged(boolean newValue);
    }
}
