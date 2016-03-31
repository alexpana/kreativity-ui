package com.katzstudio.kreativity.ui.component.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrTableView;
import com.katzstudio.kreativity.ui.component.KrWidget;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * Default implementation for {@link KrTableHeaderRenderer}
 */
public class KrDefaultTableHeaderRenderer implements KrTableHeaderRenderer {
    private final KrLabel label;

    public KrDefaultTableHeaderRenderer() {
        label = new KrLabel("");
        label.ensureUniqueStyle();
        Color backgroundColor = getDefaultToolkit().getSkin().getColor(KrSkin.ColorKey.BACKGROUND_DARK);
        Drawable background = getDefaultToolkit().getDrawable(backgroundColor);
        label.setBackground(background);
    }

    @Override
    public KrWidget getComponent(int column, KrTableView.KrTableColumnModel columnModel) {
        label.setText(columnModel.getColumnName(column));
        return label;
    }
}
