package com.katzstudio.kreativity.ui.component;

import com.katzstudio.kreativity.ui.model.KrValueModel;

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
}
