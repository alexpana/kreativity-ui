package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrRectangles;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;

import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.CENTER;

/**
 * The KrPopup is show above the rest of the widgets, on the overlay panel.
 */
public class KrPopup extends KrWidget<KrWidgetStyle> {

    private Vector2 tmpVec = new Vector2();

    public KrPopup() {
        setLayout(new KrBorderLayout(0, 0));
        setStyle(KrToolkit.getDefaultToolkit().getSkin().getWidgetStyle());
        setPadding(new KrPadding(0));
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrToolkit.getDefaultToolkit().getSkin().getWidgetStyle()) {
            style = new KrWidgetStyle(style);
        }
    }

    public void setContentWidget(KrWidget contentWidget) {
        removeAll();
        add(contentWidget, CENTER);
        setSize(KrRectangles.rectangles(contentWidget.getPreferredSize()).expand(getPadding()).size(tmpVec));
    }

    public void show() {
        show(getX(), getY());
    }

    public void show(Vector2 screenPosition) {
        show(screenPosition.x, screenPosition.y);
    }

    public void show(float x, float y) {
        setVisible(true);
        setPosition(x, y);
        if (this.getParent() == null) {
            KrToolkit.getDefaultToolkit().getCanvas().getOverlayPanel().add(this);
        }
        requestFocus();
    }

    @Override
    protected void focusLostEvent(KrFocusEvent event) {
        super.focusLostEvent(event);
        hide();
        event.accept();
    }

    public void hide() {
        this.setVisible(false);
    }
}
