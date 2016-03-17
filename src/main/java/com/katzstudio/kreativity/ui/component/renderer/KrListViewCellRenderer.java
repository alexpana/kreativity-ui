package com.katzstudio.kreativity.ui.component.renderer;

import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrListView;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;

/**
 * A simple list view renderer implementation
 */
public class KrListViewCellRenderer implements KrListView.Renderer {
    @Override
    public KrWidget getComponent(KrAbstractItemModel.KrModelIndex index, KrAbstractItemModel model, boolean isSelected) {
        return new KrLabel(model.getValue(index).toString());
    }
}
