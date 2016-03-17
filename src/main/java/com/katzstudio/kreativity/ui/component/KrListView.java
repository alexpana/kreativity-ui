package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.component.model.KrAbstractItemModel.KrModelIndex;
import lombok.AllArgsConstructor;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrWidget {

    private Style style;

    private final KrAbstractItemModel model;

    public KrListView(KrAbstractItemModel model) {
        this.model = model;
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getListViewStyle()) {
            style = style.copy();
        }
    }

    public interface Renderer {
        KrWidget getComponent(KrModelIndex index, KrAbstractItemModel model, boolean isSelected);
    }

    @AllArgsConstructor
    static public class Style {
        public final Drawable background;

        public Style copy() {
            return new Style(background);
        }
    }
}
