package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrAlignment;
import com.katzstudio.kreativity.ui.KrAlignmentTool;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.event.KrFocusEvent;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import com.katzstudio.kreativity.ui.model.KrValueModel;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.style.KrTextFieldStyle;
import com.katzstudio.kreativity.ui.util.ReturnsPooledObject;
import lombok.Getter;
import lombok.Setter;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;
import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * The {@link KrTextField} class provides a widget that can display and edit plain text.
 */
public class KrTextField extends KrWidget<KrTextFieldStyle> {

    public static final int CARET_HEIGHT = 14;

    public static final int CARET_TOP_OFFSET = 3;

    protected final KrTextDocument textDocument;

    @Getter @Setter private KrValueModel<String> model = new KrValueModel.Default<>("");

    private int textOffset;

    public KrTextField() {
        textDocument = new KrTextDocument();
        textDocument.addTextListener(this::onDocumentTextChanged);
        setFocusable(true);
        setStyle(getDefaultToolkit().getSkin().getTextFieldStyle());
        setCursor(KrCursor.IBEAM);
    }

    private void onDocumentTextChanged(String oldValue, String newValue) {
        model.setValue(newValue);
    }

    @Override
    public void ensureUniqueStyle() {
        if (style == getDefaultToolkit().getSkin().getTextFieldStyle()) {
            style = new KrTextFieldStyle(style);
        }
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
            getDefaultToolkit().writeToClipboard(textDocument.getSelectedText());
            return true;
        }

        if (event.getKeycode() == Input.Keys.V && event.isCtrlDown()) {
            textDocument.insertText(getDefaultToolkit().readFromClipboard());
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

        Rectangle innerGeometry = Pools.obtain(Rectangle.class).set(0, 0, getWidth(), getHeight());

        boolean componentClip = renderer.beginClip(innerGeometry);

        // render checkboxBackground
        renderer.setBrush(getBackgroundDrawable());
        renderer.fillRect(0, 0, getWidth(), getHeight());

        Rectangle innerViewport = rectangles(innerGeometry).shrink(getPadding()).value();
        boolean viewportClip = renderer.beginClip(innerViewport);

        String text = textDocument.getText();
        KrFontMetrics metrics = getDefaultToolkit().fontMetrics();

        Rectangle textBounds = metrics.bounds(style.font, text, tmpRect);
        Vector2 textPosition = KrAlignmentTool.alignRectangles(textBounds, innerViewport, KrAlignment.MIDDLE_LEFT);
        textPosition.x = getPadding().left - textOffset;
        textPosition.y += 1;

        // render selection
        if (isFocused() && textDocument.hasSelection()) {
            Rectangle selectionRect = getSelectionRect(textPosition.x);
            renderer.setBrush(style.selectionColor);
            renderer.fillRect(selectionRect);
            Pools.free(selectionRect);
        }

        // render text
        renderer.drawText(text, textPosition);

        // render caret
        if (isFocused()) {
            int caretPosition = textDocument.getCaretPosition();
            float caretX = textPosition.x + metrics.bounds(style.font, text.substring(0, caretPosition), tmpRect).getWidth();
            renderer.setPen(1, style.caretColor);
            renderer.drawLine(caretX, CARET_TOP_OFFSET, caretX, CARET_TOP_OFFSET + CARET_HEIGHT);
        }

        if (viewportClip) {
            renderer.endClip();
        }

        if (componentClip) {
            renderer.endClip();
        }

        Pools.free(textPosition);
        Pools.free(innerViewport);
        Pools.free(innerGeometry);
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

    @ReturnsPooledObject
    private Rectangle getSelectionRect(float textPositionX) {

        KrFontMetrics metrics = getDefaultToolkit().fontMetrics();
        String text = textDocument.getText();

        float selectionStartX = textPositionX + metrics.bounds(style.font, text.substring(0, textDocument.getSelectionBegin()), tmpRect).getWidth();
        float selectionEndX = textPositionX + metrics.bounds(style.font, text.substring(0, textDocument.getSelectionEnd()), tmpRect).getWidth();
        float selectionWidth = selectionEndX - selectionStartX;

        return Pools.obtain(Rectangle.class).set(
                selectionStartX,
                CARET_TOP_OFFSET,
                selectionWidth + 1,
                CARET_HEIGHT);
    }

    private void recalculateTextOffset() {
        KrFontMetrics metrics = getDefaultToolkit().fontMetrics();
        float textPositionX = getX() + getPadding().left - textOffset;
        float caretXPosition = textPositionX + metrics.bounds(style.font, textDocument.getTextBeforeCaret(), tmpRect).width;
        float textWidth = metrics.bounds(style.font, textDocument.getText(), tmpRect).width;

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

        Pools.free(innerViewport);
    }

    public void setText(String text) {
        textDocument.setText(text);
    }

    public String getText() {
        return textDocument.getText();
    }

}
