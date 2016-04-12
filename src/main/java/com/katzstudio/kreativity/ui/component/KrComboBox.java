package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import com.katzstudio.kreativity.ui.model.KrValueModel.KrAbstractValueModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrComboBoxStyle;
import com.katzstudio.kreativity.ui.util.KrStrings;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * A {@link KrComboBox} provides a means of presenting a list of
 * options to the user in a way that takes up the minimum amount of screen space.
 * <p>
 * A ComboBox is a selection widget that displays the current item,
 * and can pop up a list of selectable items.
 */
public class KrComboBox<T> extends KrWidget {

    private KrMenu popupMenu;

    private List<T> elements = new ArrayList<>();

    private KrValueModel<T> model;

    public KrComboBox() {
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrComboBox.class));
        popupMenu = new KrMenu();
        popupMenu.addMenuListener(this::onSelectionChange);
        popupMenu.getPopup().setPopupParent(this);

        model = new KrAbstractValueModel<>(null);
    }

    private void onSelectionChange(KrMenu.KrMenuItem selectedItem) {
        @SuppressWarnings("unchecked")
        T selectedValue = ((KrComboBoxMenuItem) selectedItem).value;
        setSelection(selectedValue);
    }

    public void setSelection(T selectedValue) {
        model.setValue(selectedValue);
        text.setString(KrStrings.toString(selectedValue));
    }

    public T getSelection() {
        return model.getValue();
    }

    public void setValues(List<T> values) {
        setSelection(null);

        elements.clear();
        elements.addAll(values);

        popupMenu.clearMenuItems();

        for (T value : values) {
            popupMenu.addMenuItem(new KrComboBoxMenuItem(value));
        }
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(100, 21);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        if (popupMenu.isShowing()) {
            renderer.setBrush(((KrComboBoxStyle) getStyle()).pressedBackground);
        } else {
            renderer.setBrush(((KrComboBoxStyle) getStyle()).background);
        }

        renderer.fillRect(0, 0, getWidth(), getHeight());

        rectangles(0, 0, getWidth(), getHeight()).shrink(getPadding()).value(tmpRect);
        Vector2 pTextPosition = KrAlignmentTool.alignRectangles(text.getBounds(), tmpRect, KrAlignment.MIDDLE_LEFT);

        renderer.setPen(1, getForeground());
        renderer.drawText(text.getString(), pTextPosition);

        Pools.free(pTextPosition);
    }

    @Override
    protected void mousePressedEvent(KrMouseEvent event) {
        if (popupMenu.isShowing()) {
            popupMenu.hide();
        } else {
            showPopupMenu();
        }

        notifyMousePressed(event);
    }

    private void showPopupMenu() {
        Rectangle screenGeometry = getScreenGeometry();
        popupMenu.setPreferredWidth(screenGeometry.width);
        popupMenu.showAt((int) screenGeometry.x, ((int) (screenGeometry.y + screenGeometry.height)) - 1);
    }

    private class KrComboBoxMenuItem extends KrMenu.KrMenuItem {

        private T value;

        public KrComboBoxMenuItem(T value) {
            super(value.toString());
            this.value = value;
        }
    }
}
