package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.style.KrButtonGroupStyle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrOrientation.HORIZONTAL;

/**
 * A button group contains a list of horizontal buttons, aligned and glued together. Only one button may be
 * toggled at any time.
 */
public class KrButtonGroup extends KrWidget<KrButtonGroupStyle> {

    private final List<KrToggleButton> toggleButtons = new ArrayList<>();

    private KrToggleButton currentlyCheckedButton;

    @Getter private boolean allowUnCheck = true;

    private boolean isAdjusting = false;

    public KrButtonGroup(KrToggleButton... buttons) {

        style = KrSkin.instance().getButtonGroupStyle();

        toggleButtons.addAll(Arrays.asList(buttons));

        setLayout(new KrFlowLayout(HORIZONTAL, 0, 0));

        toggleButtons.forEach(this::add);

        toggleButtons.forEach(button -> button.addToggleListener(isChecked -> buttonToggled(button)));

        applyStyle();
    }

    private void buttonToggled(KrToggleButton button) {
        if (isAdjusting) {
            return;
        }

        if (button.isChecked()) {
            if (button != currentlyCheckedButton) {
                if (currentlyCheckedButton != null) {
                    isAdjusting = true;
                    currentlyCheckedButton.setChecked(false);
                    isAdjusting = false;
                }
                currentlyCheckedButton = button;
            }
        } else {
            // unchecked
            if (allowUnCheck) {
                currentlyCheckedButton = null;
            } else {
                currentlyCheckedButton.setChecked(true);
            }
        }
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

    public void setAllowUnCheck(boolean allowUnCheck) {
        this.allowUnCheck = allowUnCheck;

        if (!allowUnCheck && currentlyCheckedButton == null) {
            toggleButtons.get(0).setChecked(true);
        }
    }

    @Override
    public void setStyle(KrButtonGroupStyle style) {
        this.style = style;
        applyStyle();
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getButtonGroupStyle()) {
            style = new KrButtonGroupStyle(style);
        }
    }

}
