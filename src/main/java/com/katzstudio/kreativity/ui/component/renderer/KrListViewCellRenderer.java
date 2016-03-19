package com.katzstudio.kreativity.ui.component.renderer;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrListView;
import com.katzstudio.kreativity.ui.component.KrListView.KrItemDelegate;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.model.KrAbstractItemModel;

import static com.katzstudio.kreativity.ui.KrSkin.ColorKey.SELECTION_BACKGROUND;
import static com.katzstudio.kreativity.ui.KrToolkit.getDrawable;

/**
 * A simple list view renderer implementation
 */
public class KrListViewCellRenderer implements KrListView.Renderer {
    @Override
    public KrItemDelegate getComponent(KrAbstractItemModel.KrModelIndex index, KrAbstractItemModel model) {
        return new ItemDelegate(model.getValue(index).toString());
    }

    private static class ItemDelegate extends KrItemDelegate {

        private final KrLabel label;

        private final Drawable unselectedBackground;

        private final Drawable selectedBackground;

        public ItemDelegate(String value) {
            label = new KrLabel(value);
            label.ensureUniqueStyle();
            label.setPadding(new KrPadding(4, 4, 4, 4));
            unselectedBackground = ((KrLabel.Style) label.getStyle()).background;
            selectedBackground = getDrawable(KrSkin.instance().getColor(SELECTION_BACKGROUND));
        }

        @Override
        public void setSelected(boolean selected) {
            if (selected) {
                ((KrLabel.Style) label.getStyle()).background = selectedBackground;
            } else {
                ((KrLabel.Style) label.getStyle()).background = unselectedBackground;
            }
        }

        @Override
        public KrWidget getWidget() {
            return label;
        }
    }
}
