package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.base.Strings;
import com.katzstudio.kreativity.ui.component.KrWidget;
import com.katzstudio.kreativity.ui.event.KrEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.render.KrColorBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrWidgetStyle;
import lombok.Getter;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * The tooltip manager registers tooltips for each components, and makes sure the
 * correct tooltip is displayed when necessary.
 */
public class KrTooltipManager {

    @Getter private final KrTooltipWidget tooltipWidget;

    public KrTooltipManager(KrCanvas canvas) {
        canvas.addInputListener(this::onEventDispatched);
        tooltipWidget = new KrTooltipWidget();
        tooltipWidget.setVisible(false);
    }

    private void onEventDispatched(KrWidget widget, KrEvent event) {
        if (event instanceof KrMouseEvent && ((KrMouseEvent) event).getType() == KrMouseEvent.Type.MOVED) {
            if (widget.getTooltipText() != null && !Strings.isNullOrEmpty(widget.getTooltipText())) {
                showTooltip(((KrMouseEvent) event).getScreenPosition(), widget.getTooltipText());
            } else {
                hideTooltip();
            }
        }
    }

    private void showTooltip(Vector2 screenPosition, String tooltipText) {
        tooltipWidget.setPosition(screenPosition.add(0, 20));
        tooltipWidget.setText(tooltipText);
        tooltipWidget.setVisible(true);
    }

    private void hideTooltip() {
        tooltipWidget.setVisible(false);
    }

    private class KrTooltipWidget extends KrWidget<KrWidgetStyle> {
        private String tooltipText;

        KrTooltipWidget() {
            setStyle(KrSkin.instance().getWidgetStyle());
        }

        void setText(String text) {
            tooltipText = text;
            setSize(getPreferredSize());
            setPadding(new KrPadding(10, 10, 10, 10));
        }

        @Override
        protected void drawSelf(KrRenderer renderer) {
            if (tooltipText == null) {
                return;
            }

            renderer.setBrush(new KrColorBrush(0x202020, 0.95f));
            int radius = 3;
            Rectangle textBounds = (new KrFontMetrics()).bounds(getStyle().font, tooltipText);
            Vector2 textPosition = KrAlignmentTool.alignRectangles(textBounds, new Rectangle(0, 0, getWidth(), getHeight()), KrAlignment.MIDDLE_CENTER);

            renderer.fillRoundedRect(0, 0, (int) getWidth(), (int) getHeight(), radius);
            renderer.setPen(new KrPen(1, KrColor.rgb(0xC0C0C0)));
            renderer.drawTextWithShadow(tooltipText, textPosition, new Vector2(0, 1), KrColor.rgb(0x101010));
        }

        @Override
        public Vector2 getPreferredSize() {
            Rectangle textBounds = (new KrFontMetrics()).bounds(getStyle().font, tooltipText);
            return rectangles(textBounds).expand(getPadding()).size();
        }
    }
}
