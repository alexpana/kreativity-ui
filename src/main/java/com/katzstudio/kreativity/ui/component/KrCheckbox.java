package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrCheckboxStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple checkbox component that can be either checked or unchecked.
 */
public class KrCheckbox extends KrWidget<KrCheckboxStyle> {

    @Getter @Setter private KrValueModel<Boolean> model = new KrValueModel.Default<>(false);

    private final List<ValueListener> valueListeners = new ArrayList<>();

    public KrCheckbox() {
        setStyle(getDefaultToolkit().getSkin().getCheckboxStyle());
        setSize(calculatePreferredSize());
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getCheckboxStyle()) {
            style = new KrCheckboxStyle(style);
        }
    }

    public void setChecked(boolean checked) {
        if (checked != model.getValue()) {
            model.setValue(checked);
            notifyValueChanged(checked);
        }
    }

    @SuppressWarnings("unused")
    public void addValueListener(ValueListener valueListener) {
        valueListeners.add(valueListener);
    }

    @SuppressWarnings("unused")
    public void removeValueListener(ValueListener valueListener) {
        valueListeners.remove(valueListener);
    }

    private void notifyValueChanged(boolean newValue) {
        valueListeners.forEach(listener -> listener.valueChanged(newValue));
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(14, 15);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(new KrDrawableBrush(style.checkboxBackground));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        if (model.getValue()) {
            renderer.setBrush(new KrDrawableBrush(style.mark));
            renderer.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);

        if (event.getButton() == KrMouseEvent.Button.LEFT) {
            event.accept();
            setChecked(!model.getValue());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrCheckbox").toString();
    }

    public interface ValueListener {
        void valueChanged(boolean newValue);
    }
}
