package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrCanvas;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrRectangles;
import com.katzstudio.kreativity.ui.KrToolkit;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.CENTER;

/**
 * The KrPopup is show above the rest of the widgets, on the overlay panel.
 */
public class KrPopup extends KrWidget {

    private Vector2 tmpVec = new Vector2();

    private List<KrPopupListener> listeners = new ArrayList<>();

    /**
     * The invoker is the widget that requested the popup to show.
     * When the invoker receives focus, the popup remains visible.
     */
    @Setter private KrWidget invoker;

    public KrPopup() {
        setLayout(new KrBorderLayout(0, 0));
        setDefaultStyle(KrToolkit.getDefaultToolkit().getSkin().getStyle(KrWidget.class));
        setPadding(new KrPadding(0));
        setVisible(false);
    }

    public void setContentWidget(KrWidget contentWidget) {
        removeAll();
        add(contentWidget, CENTER);
        setSize(KrRectangles.rectangles(contentWidget.getPreferredSize()).expand(getPadding()).size(tmpVec));
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
        notifyPopupShown();
    }

    public void hide() {
        setVisible(false);
        notifyPopupHidden();
    }

    @Override
    protected void focusLostEvent(KrFocusEvent event) {
        super.focusLostEvent(event);

        if (!KrCanvas.isAncestor(event.getNewFocusHolder(), this) && !(invoker != null && event.getNewFocusHolder() == invoker)) {
            hide();
            event.accept();
        }
    }

    @Override
    protected void keyPressedEvent(KrKeyEvent event) {
        super.keyPressedEvent(event);
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
