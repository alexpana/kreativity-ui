package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrRectangles;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.CENTER;

/**
 * The KrPopup is show above the rest of the widgets, on the overlay panel.
 */
public class KrPopup extends KrWidget {

    private Vector2 tmpVec = new Vector2();

    private List<KrPopupListener> listeners = new ArrayList<>();

    public KrPopup() {
        setLayout(new KrBorderLayout(0, 0));
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrWidget.class));
        setPadding(new KrPadding(0));
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

    @Override
    protected void focusLostEvent(KrFocusEvent event) {
        super.focusLostEvent(event);
        hide();
        event.accept();
    }

    public void show(float x, float y) {
        setVisible(true);
        setPosition(x, y);
        if (this.getParent() == null) {
            KrToolkit.getDefaultToolkit().getCanvas().getOverlayPanel().add(this);
        }
        requestFocus();
        notifyPopupShown();
    }

    public void hide() {
        this.setVisible(false);
        notifyPopupHidden();
    }

    @Override
    protected void keyPressedEvent(KrKeyEvent event) {
        if (event.getKeycode() == Input.Keys.ESCAPE) {
            hide();
        }
    }

    public void addListener(KrPopupListener popupListener) {
        listeners.add(popupListener);
    }

    public void removeListener(KrPopupListener popupListener) {
        listeners.remove(popupListener);
    }

    protected void notifyPopupShown() {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < listeners.size(); ++i) {
            listeners.get(i).popupShown();
        }
    }

    protected void notifyPopupHidden() {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < listeners.size(); ++i) {
            listeners.get(i).popupHidden();
        }
    }

    public interface KrPopupListener {
        void popupShown();

        void popupHidden();
    }
}
