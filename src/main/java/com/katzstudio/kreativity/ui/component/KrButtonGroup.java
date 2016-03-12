package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.HORIZONTAL;

/**
 * A button group contains a list of horizontal buttons, aligned and glued together. Only one button may be
 * toggled at any time.
 */
public class KrButtonGroup extends KrPanel {

    private final List<KrToggleButton> toggleButtons = new ArrayList<>();

    private KrToggleButton currentlyCheckedButton;

    private Style style;

    public KrButtonGroup(KrToggleButton... buttons) {

        style = KrSkin.instance().getButtonGroupStyle();

        toggleButtons.addAll(Arrays.asList(buttons));

        setLayout(new KrFlowLayout(HORIZONTAL, 0, 0));

        toggleButtons.forEach(this::add);

        toggleButtons.forEach(button -> button.addToggleListener(isChecked -> {
            if (!isChecked) {
                currentlyCheckedButton = null;
                return;
            }
            if (currentlyCheckedButton != null) {
                currentlyCheckedButton.setChecked(false);
            }
            currentlyCheckedButton = button;
        }));

        applyStyle();
    }

    private void applyStyle() {
        if (toggleButtons.size() == 1) {
            toggleButtons.get(0).setStyle(style.singleButtonStyle);
        } else {
            toggleButtons.get(0).setStyle(style.firstButtonStyle);
            toggleButtons.get(toggleButtons.size() - 1).setStyle(style.lastButtonStyle);

            for (int i = 1; i < toggleButtons.size() - 1; ++i) {
                toggleButtons.get(i).setStyle(style.middleButtonStyle);
            }
        }
    }

    @Override
    public Object getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        applyStyle();
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getButtonGroupStyle()) {
            style = style.copy();
        }
    }

    @AllArgsConstructor
    public static class Style {

        public KrButton.Style singleButtonStyle;

        public KrButton.Style firstButtonStyle;

        public KrButton.Style middleButtonStyle;

        public KrButton.Style lastButtonStyle;

        public Style copy() {
            return new Style(singleButtonStyle, firstButtonStyle, middleButtonStyle, lastButtonStyle);
        }
    }
}
