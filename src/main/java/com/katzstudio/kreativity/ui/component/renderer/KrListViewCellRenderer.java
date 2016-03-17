package com.katzstudio.kreativity.ui.component.renderer;

import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrListView;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;
import static com.katzstudio.kreativity.ui.KrToolkit.getDrawable;

/**
 * A simple list view renderer implementation
 */
public class KrListViewCellRenderer implements KrListView.Renderer {
    @Override
    public KrWidget getComponent(KrAbstractItemModel.KrModelIndex index, KrAbstractItemModel model, boolean isSelected) {
        KrLabel label = new KrLabel(model.getValue(index).toString());
        
        if (isSelected) {
            label.ensureUniqueStyle();
            KrSkin skin = KrSkin.instance();
            ((KrLabel.Style) label.getStyle()).background = getDrawable(skin.getColor(SELECTION_BACKGROUND));
        }
        return label;
    }
}
