package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrLabel;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.layout.KrBorderLayout;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import com.katzstudio.kreativity.ui.util.KrStrings;
import com.katzstudio.kreativity.ui.util.KrTimer;
import com.katzstudio.kreativity.ui.util.KrUpdateListener;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrToolkit.animations;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * The tooltip manager registers tooltips for each components, and makes sure the
 * correct tooltip is displayed when necessary.
 */

public class KrTooltipManager implements KrUpdateListener {

    @Getter @Setter private float tooltipDelay = 0.5f;

    @Getter private final KrTooltipWidget tooltipWidget;

    private boolean tooltipReady = false;

    private boolean tooltipForced = false;

    private final KrTimer delayTimer;

    public KrTooltipManager(KrCanvas canvas) {
        canvas.addInputListener(this::onEventDispatched);
        tooltipWidget = new KrTooltipWidget();
        tooltipWidget.setVisible(false);
        canvas.getTooltipPanel().add(tooltipWidget);

        delayTimer = new KrTimer(tooltipDelay, this::onDelayFinished);
    }

    /**
     * Forces the tooltip manager to display a tooltip containing the tooltip widget
     * regardless of the currently hovered widget.
     *
     * @param tooltipWidget the widget to be displayed inside the tooltip
     */
    public void showCustomTooltip(KrWidget tooltipWidget) {
        tooltipForced = true;
        prepareTooltip(tooltipWidget);
        showTooltip();
    }

    /**
     * Stops forcing the tooltip manager to display a custom tooltip
     */
    public void stopShowingCustomTooltip() {
        tooltipForced = false;
        hideTooltip();
    }

    private void onDelayFinished() {
        if (tooltipReady && !tooltipForced) {
            showTooltip();
        }
    }

    private void showTooltip() {
        tooltipWidget.setOpacity(0);
        tooltipWidget.setVisible(true);
        animations().setOpacity(tooltipWidget, 1);
    }

    private void hideTooltip() {
        tooltipWidget.setVisible(false);
        tooltipReady = false;
    }

    private void onEventDispatched(KrWidget widget, KrEvent event) {
        if (event instanceof KrMouseEvent && ((KrMouseEvent) event).getType() == KrMouseEvent.Type.MOVED) {
            updateTooltipPosition(((KrMouseEvent) event).getScreenPosition());

            if (tooltipForced) {
                return;
            }

            hideTooltip();

            if (hasTooltipText(widget)) {
                prepareTooltip(widget.getTooltipText());
            } else if (hasTooltipWidget(widget)) {
                prepareTooltip(widget.getTooltipWidget());
            }

            delayTimer.restart();
        }
    }

    private boolean hasTooltipText(KrWidget widget) {
        return widget.getTooltipText() != null && !KrStrings.isNullOrEmpty(widget.getTooltipText());
    }

    private boolean hasTooltipWidget(KrWidget widget) {
        return widget.getTooltipWidget() != null;
    }

    private void prepareTooltip(String tooltipText) {
        tooltipWidget.setText(tooltipText);
        tooltipReady = true;
    }

    private void prepareTooltip(KrWidget tooltip) {
        tooltipWidget.setWidget(tooltip);
        tooltipReady = true;
    }

    private void updateTooltipPosition(Vector2 screenPosition) {
        tooltipWidget.setPosition(screenPosition.x, screenPosition.y + 20);
    }

    @Override
    public void update(float deltaSeconds) {
        // pass
    }

    private class KrTooltipWidget extends KrWidget<KrWidgetStyle> {

        public final Color background = new Color(0x202020ff);

        private final KrLabel textLabel;

        KrTooltipWidget() {
            textLabel = new KrLabel("");
            textLabel.setTextAlignment(KrAlignment.MIDDLE_CENTER);

            setStyle(getDefaultToolkit().getSkin().getWidgetStyle());

            setPadding(new KrPadding(6, 6, 6, 6));
            setLayout(new KrBorderLayout());
            add(textLabel);
        }

        @Override
        public void setText(String text) {
            super.setText(text);
            if (!(getChild(0) == textLabel)) {
                removeAll();
                add(textLabel);
            }
            textLabel.setText(text);
            setSize(getPreferredSize());
        }

        void setWidget(KrWidget tooltipWidget) {
            if (!(getChild(0) == tooltipWidget)) {
                removeAll();
                add(tooltipWidget);
            }
            setSize(getPreferredSize());
        }

        @Override
        protected void drawSelf(KrRenderer renderer) {
            renderer.setBrush(background);
            renderer.setOpacity(getOpacity() * 0.95f);
            int radius = 3;
            renderer.fillRoundedRect(0, 0, (int) getWidth(), (int) getHeight(), radius);
        }
    }
}
