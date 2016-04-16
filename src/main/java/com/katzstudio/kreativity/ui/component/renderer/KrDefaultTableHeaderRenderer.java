package com.katzstudio.kreativity.ui.component.renderer;

import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrTableView;
import com.katzstudio.kreativity.ui.component.KrWidget;

/**
 * Default implementation for {@link KrTableHeaderRenderer}
 */
public class KrDefaultTableHeaderRenderer implements KrTableHeaderRenderer {
    private final KrLabel label;

    public KrDefaultTableHeaderRenderer() {
        label = new KrLabel("");
        label.ensureUniqueStyle();
    }

    @Override
    public KrWidget getComponent(int column, KrTableView.KrTableColumnModel columnModel) {
        label.setText(columnModel.getColumnName(column));
        return label;
    }
}
