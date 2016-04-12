package com.katzstudio.kreativity.ui.component.renderer;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.model.KrItemModel;
import com.katzstudio.kreativity.ui.model.KrItemModel.KrModelIndex;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * A simple list view renderer implementation
 */
public class KrDefaultCellRenderer implements KrCellRenderer {

    private final KrLabel label;

    private final Drawable unselectedBackground;

    private final Drawable selectedBackground;

    public KrDefaultCellRenderer() {
        label = new KrLabel("");
        label.ensureUniqueStyle();
        label.setPadding(new KrPadding(4, 4, 4, 4));
        unselectedBackground = label.getStyle().background;
        selectedBackground = getDefaultToolkit().getDrawable(getDefaultToolkit().getSkin().getColor(SELECTION_BACKGROUND));
    }

    @Override
    public KrWidget getComponent(KrModelIndex index, KrItemModel model, boolean isSelected) {
        return getComponent(index.getRow(), index.getColumn(), index.getParentIndex(), model, isSelected);
    }

    @Override
    public KrWidget getComponent(int row, int col, KrModelIndex parent, KrItemModel model, boolean isSelected) {
        label.setText(model.getValue(row, col, parent).toString());
        if (isSelected) {
            label.getStyle().background = selectedBackground;
        } else {
            label.getStyle().background = unselectedBackground;
        }

        return label;
    }

}
