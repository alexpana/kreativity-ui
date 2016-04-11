package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrToolkit;
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
	}

	public void setValues(List<T> values) {
		elements.clear();
		elements.addAll(values);
	}

	@Override
	public Vector2 calculatePreferredSize() {
		return new Vector2(100, 21);
	}

	@Override
	protected void drawSelf(KrRenderer renderer) {
		super.drawSelf(renderer);
	}
}
