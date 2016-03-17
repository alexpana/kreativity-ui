package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.renderer.KrListViewCellRenderer;
import com.katzstudio.kreativity.ui.layout.KrFlowLayout;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import lombok.AllArgsConstructor;

import static com.katzstudio.kreativity.ui.layout.KrFlowLayout.Direction.VERTICAL;

/**
 * A list widget displays items in a vertical list. Items can be selected.
 */
public class KrListView extends KrWidget {

    private Style style;

    private final KrAbstractItemModel model;

    private final KrPanel innerPanel;

    private final Renderer renderer;

    public KrListView(KrAbstractItemModel model) {
        this(model, new KrListViewCellRenderer());
    }

    public KrListView(KrAbstractItemModel model, Renderer renderer) {
        this.model = model;
        this.renderer = renderer;
        this.innerPanel = new KrPanel(new KrFlowLayout(VERTICAL));

        this.model.addListener(this::onModelDataChanged);
        add(innerPanel);
    }

    private void onModelDataChanged() {
        innerPanel.clear();

        for (int i = 0; i < model.getRowCount(); ++i) {
            KrModelIndex index = new KrModelIndex(i);
            KrWidget itemComponent = renderer.getComponent(index, model, false);
            innerPanel.add(itemComponent);
        }

        invalidate();
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
