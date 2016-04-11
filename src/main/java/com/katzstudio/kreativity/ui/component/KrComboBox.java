package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import java.util.ArrayList;
import java.util.List;

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
    }

    public void setValues(List<T> values) {
        elements.clear();
        elements.addAll(values);

        popupMenu.clearMenuItems();

        for (T value : values) {
            popupMenu.addMenuItem(new KrMenu.KrMenuItem(value.toString()));
        }
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(100, 21);
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        super.drawSelf(renderer);
    }

    @Override
    protected void notifyMousePressed(KrMouseEvent event) {
        super.notifyMousePressed(event);

        showPopupMenu();

        event.accept();
    }

    private void showPopupMenu() {
        Rectangle screenGeometry = getScreenGeometry();
        popupMenu.setPreferredWidth(screenGeometry.width);
        popupMenu.showAt((int) screenGeometry.x, ((int) (screenGeometry.y + screenGeometry.height)) - 1);
    }
}
