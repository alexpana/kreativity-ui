package com.katzstudio.kreativity.ui.component;

import com.google.common.base.Strings;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A text document holds a text string and operates on this text via a caret.
 * The caret can be moved left or right. Selecting text is done by starting a
 * selection from the caret position, and then moving the caret to the selection
 * end position.
 * <p>
 * Text can be inserted at the caret position, or existing text can be removed
 * before or after the caret. Removing text in either direction will delete the
 * selected text. Inserting text while a selection is active will replace the
 * selection
 */
public class KrTextDocument {

    @Getter private String text = "";

    @Getter private int caretPosition = 0;

    @Getter private int selectionBegin = 0;

    @Getter private int selectionEnd = 0;

    private boolean isSelecting = false;

    private final List<TextListener> textListeners = new ArrayList<>();

    public void setText(String text) {
        updateTextValue(text);
        this.caretPosition = 0;
        clearSelection();
    }

    private void updateTextValue(String text) {
        text = Strings.nullToEmpty(text);
        if (!text.equals(this.text)) {
            String oldValue = this.text;
            notifyTextChanged(oldValue, text);
            this.text = text;
        }
    }

    public void insertText(String text) {
        deleteSelection();
        updateTextValue(this.text.substring(0, caretPosition) + text + this.text.substring(caretPosition));
        caretPosition += text.length();
    }

    public void deleteSelection() {
        if (!hasSelection()) {
            return;
        }

        int selectionLow = Math.min(selectionBegin, selectionEnd);
        int selectionHigh = Math.max(selectionBegin, selectionEnd);

        updateTextValue(text.substring(0, selectionLow) + text.substring(selectionHigh));
        caretPosition = selectionLow;
        clearSelection();
    }

    public boolean hasSelection() {
        return selectionBegin != selectionEnd;
    }

    public void clearSelection() {
        selectionBegin = selectionEnd = 0;
    }

    public void deleteCharBeforeCaret() {
        if (hasSelection()) {
            deleteSelection();
            return;
        }

        if (caretPosition > 0) {
            updateTextValue(text.substring(0, caretPosition - 1) + text.substring(caretPosition));
            caretPosition = caretPosition - 1;
        }
    }

    public void deleteCharAfterCaret() {
        if (hasSelection()) {
            deleteSelection();
            return;
        }

        if (caretPosition < text.length()) {
            updateTextValue(text.substring(0, caretPosition) + text.substring(caretPosition + 1));
        }
    }

    public void moveCaretLeft() {
        caretPosition = Math.max(0, caretPosition - 1);
        syncSelectionFromCursor();
    }

    public void moveCaretRight() {
        caretPosition = Math.min(text.length(), caretPosition + 1);
        syncSelectionFromCursor();
    }

    public void moveCaretHome() {
        caretPosition = 0;
        syncSelectionFromCursor();
    }

    public void moveCaretEnd() {
        caretPosition = text.length();
        syncSelectionFromCursor();
    }

    public void moveCaretNextWord() {
        caretPosition = findNextWord(caretPosition, text);
        syncSelectionFromCursor();
    }

    public void moveCaretPreviousWord() {
        caretPosition = findPreviousWord(caretPosition, text);
        syncSelectionFromCursor();
    }

    public void beginSelection() {
        selectionBegin = selectionEnd = caretPosition;
        isSelecting = true;
    }

    public void endSelection() {
        isSelecting = false;
    }

    public void setSelection(int begin, int end) {
        int min = Math.min(begin, end);
        int max = Math.max(begin, end);

        begin = Math.max(0, min);
        end = Math.min(text.length(), max);

        selectionBegin = begin;
        selectionEnd = end;
        caretPosition = selectionEnd;
    }

    /**
     * Called whenever the caret position changes, to make sure the end of the selection is at
     * the caret position. If no selection is taking place, this method does nothing.
     */
    private void syncSelectionFromCursor() {
        if (isSelecting) {
            selectionEnd = caretPosition;
        } else {
            clearSelection();
        }
    }

    public void setCaretPosition(int caretPosition) {
        this.caretPosition = Math.max(0, Math.min(caretPosition, text.length()));
    }

    public String getTextBeforeCaret() {
        return text.substring(0, caretPosition);
    }

    @SuppressWarnings("unused")
    public void undo() {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("unused")
    public void redo() {
        throw new UnsupportedOperationException("not implemented");
    }

    private static int findNextWord(int startPosition, String text) {
        boolean foundWhitespace = false;
        boolean done = false;
        while (startPosition < text.length() && !done) {
            if (text.charAt(startPosition) != ' ' && foundWhitespace) {
                done = true;
            } else {
                foundWhitespace = text.charAt(startPosition) == ' ';
                startPosition += 1;
            }
        }
        return startPosition;
    }

    private static int findPreviousWord(int startPosition, String text) {
        boolean foundNonWhitespace = false;
        boolean done = false;
        while (startPosition > 0 && !done) {
            if (text.charAt(startPosition - 1) == ' ' && foundNonWhitespace) {
                done = true;
            } else {
                foundNonWhitespace = text.charAt(startPosition - 1) != ' ';
                startPosition -= 1;
            }
        }
        return startPosition;
    }

    public void selectAll() {
        setSelection(0, text.length());
    }

    public String getSelectedText() {
        if (selectionBegin > selectionEnd) {
            return text.substring(selectionEnd, selectionBegin);
        }
        return text.substring(selectionBegin, selectionEnd);
    }

    public void addTextListener(TextListener textListener) {
        textListeners.add(textListener);
    }

    public void removeTextListener(TextListener textListener) {
        textListeners.remove(textListener);
    }

    private void notifyTextChanged(String oldValue, String newValue) {
        textListeners.forEach(listener -> listener.textChanged(oldValue, newValue));
    }

    public interface TextListener {
        void textChanged(String oldValue, String newValue);
    }
}
