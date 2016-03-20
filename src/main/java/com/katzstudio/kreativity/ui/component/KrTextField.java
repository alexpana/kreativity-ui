package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import com.katzstudio.kreativity.ui.render.KrColorBrush;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.katzstudio.kreativity.ui.KrFontMetrics.metrics;
import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * The {@link KrTextField} class provides a widget that can display and edit plain text.
 */
public class KrTextField extends KrWidget {

    public static final int CARET_HEIGHT = 14;

    public static final int CARET_TOP_OFFSET = 3;

    @Setter private Style style;

    protected final KrTextDocument textDocument;

    @Getter @Setter private KrValueModel<String> model = new KrValueModel.Default<>("");

    private int textOffset;

    public KrTextField() {
        textDocument = new KrTextDocument();
        textDocument.addTextListener(this::onDocumentTextChanged);
        setFocusable(true);
        setStyle(KrSkin.instance().getTextFieldStyle());
        setPadding(new KrPadding(1, 4, 4, 4));
    }

    private void onDocumentTextChanged(String oldValue, String newValue) {
        model.setValue(newValue);
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == KrSkin.instance().getTextFieldStyle()) {
            style = style.copy();
        }
    }

    @Override
    public Object getStyle() {
        return style;
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        super.mousePressedEvent(event);
        return requestFocus();
    }

    @Override
    protected boolean keyPressedEvent(KrKeyEvent event) {
        super.keyPressedEvent(event);

        if (event.getKeycode() == ESCAPE) {
            getCanvas().clearFocus();
        }

        if (event.getKeycode() == Input.Keys.A && event.isCtrlDown()) {
            textDocument.selectAll();
            return true;
        }

        if (event.getKeycode() == Input.Keys.C && event.isCtrlDown() && textDocument.hasSelection()) {
            getCanvas().getClipboard().setContents(textDocument.getSelectedText());
            return true;
        }

        if (event.getKeycode() == Input.Keys.V && event.isCtrlDown()) {
            textDocument.insertText(getCanvas().getClipboard().getContents());
            return true;
        }

        if (!event.getValue().equals("")) {
            textDocument.insertText(event.getValue());
        }

        if (event.isShiftDown() && !textDocument.hasSelection()) {
            textDocument.beginSelection();
        }

        if (!event.isShiftDown()) {
            textDocument.endSelection();
        }

        if (event.getKeycode() == Input.Keys.BACKSPACE) {
            textDocument.deleteCharBeforeCaret();
        }

        if (event.getKeycode() == Input.Keys.FORWARD_DEL) {
            textDocument.deleteCharAfterCaret();
        }

        if (event.getKeycode() == Input.Keys.LEFT) {
            if (event.isCtrlDown()) {
                textDocument.moveCaretPreviousWord();
            } else {
                textDocument.moveCaretLeft();
            }
        }

        if (event.getKeycode() == Input.Keys.RIGHT) {
            if (event.isCtrlDown()) {
                textDocument.moveCaretNextWord();
            } else {
                textDocument.moveCaretRight();
            }
        }

        if (event.getKeycode() == Input.Keys.HOME || event.getKeycode() == Input.Keys.NUMPAD_7) {
            textDocument.moveCaretHome();
        }

        if (event.getKeycode() == Input.Keys.END || event.getKeycode() == Input.Keys.NUMPAD_1) {
            textDocument.moveCaretEnd();
        }

        return true;
    }

    @Override
    protected boolean focusGainedEvent(KrFocusEvent event) {
        super.focusGainedEvent(event);
        model.ephemeralChangesBegin();
        return true;
    }

    @Override
    protected boolean focusLostEvent(KrFocusEvent event) {
        super.focusLostEvent(event);
        model.ephemeralChangesEnd();
        textDocument.clearSelection();
        return true;
    }

    @Override
    public Vector2 calculatePreferredSize() {
        return new Vector2(80, 21);
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrTextField").toString();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        recalculateTextOffset();

        Rectangle innerGeometry = new Rectangle(0, 0, getWidth(), getHeight());

        boolean componentClip = renderer.beginClip(innerGeometry);

        // render background
        renderer.setBrush(new KrDrawableBrush(getBackgroundDrawable()));
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Rectangle innerViewport = rectangles(innerGeometry).shrink(getPadding()).value();
        boolean viewportClip = renderer.beginClip(innerViewport);

        String text = textDocument.getText();
        KrFontMetrics metrics = metrics(style.font);

        Rectangle textBounds = metrics.bounds(text);
        Vector2 textPosition = KrAlignmentTool.alignRectangles(textBounds, innerViewport, KrAlignment.MIDDLE_LEFT);
        textPosition.x = getPadding().left - textOffset;
        textPosition.y += 1;

        // render selection
        if (isFocused() && textDocument.hasSelection()) {
            Rectangle selectionRect = getSelectionRect(textPosition.x);
            renderer.setBrush(new KrColorBrush(style.selectionColor));
            renderer.fillRect(selectionRect);
        }

        // render text
        renderer.drawText(text, textPosition);

        // render caret
        if (isFocused()) {
            int caretPosition = textDocument.getCaretPosition();
            float caretX = textPosition.x + metrics.bounds(text.substring(0, caretPosition)).getWidth();
            renderer.setPen(new KrPen(1, style.caretColor));
            renderer.drawLine(caretX, CARET_TOP_OFFSET, caretX, CARET_TOP_OFFSET + CARET_HEIGHT);
        }

        if (viewportClip) {
            renderer.endClip();
        }

        if (componentClip) {
            renderer.endClip();
        }
    }

    @Override
    public void update(float deltaSeconds) {
        // sync model value with document value when needed
        if (!model.getValue().equals(getText())) {
            setText(model.getValue());
        }
        super.update(deltaSeconds);
    }

    private Drawable getBackgroundDrawable() {
        Drawable background = style.backgroundNormal;
        if (isFocused()) {
            background = style.backgroundFocused;
        }
        return background;
    }

    private Rectangle getSelectionRect(float textPositionX) {

        KrFontMetrics metrics = metrics(style.font);
        String text = textDocument.getText();

        float selectionStartX = textPositionX + metrics.bounds(text.substring(0, textDocument.getSelectionBegin())).getWidth();
        float selectionEndX = textPositionX + metrics.bounds(text.substring(0, textDocument.getSelectionEnd())).getWidth();
        float selectionWidth = selectionEndX - selectionStartX;

        return new Rectangle(
                selectionStartX,
                CARET_TOP_OFFSET,
                selectionWidth + 1,
                CARET_HEIGHT);
    }

    private void recalculateTextOffset() {
        KrFontMetrics metrics = metrics(style.font);
        float textPositionX = getX() + getPadding().left - textOffset;
        float caretXPosition = textPositionX + metrics.bounds(textDocument.getTextBeforeCaret()).width;
        float textWidth = metrics.bounds(textDocument.getText()).width;

        Rectangle innerViewport = rectangles(getGeometry()).shrink(getPadding()).value();
        if (innerViewport.getWidth() > textWidth) {
            textOffset = 0;
            return;
        }

        if (caretXPosition <= innerViewport.x) {
            textOffset -= innerViewport.x - caretXPosition;
        }

        if (caretXPosition >= innerViewport.x + innerViewport.width) {
            textOffset += caretXPosition - (innerViewport.x + innerViewport.width) + 1;
        }
    }

    public void setText(String text) {
        textDocument.setText(text);
    }

    public String getText() {
        return textDocument.getText();
    }

    @AllArgsConstructor
    public static class Style {

        public Drawable backgroundNormal;

        public Drawable backgroundHovered;

        public Drawable backgroundFocused;

        public BitmapFont font;

        public Color foregroundColor;

        public Color caretColor;

        public Color selectionColor;

        public Style copy() {
            return new Style(backgroundNormal, backgroundHovered, backgroundFocused, font, foregroundColor, caretColor, selectionColor);
        }
    }
}
