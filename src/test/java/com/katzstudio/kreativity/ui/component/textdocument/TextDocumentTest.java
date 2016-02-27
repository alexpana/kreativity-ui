package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextField.TextDocument;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TextDocument} class
 */
@RunWith(JUnitParamsRunner.class)
public class TextDocumentTest {
    @Test
    public void testSetText() {
        TextDocument textDocument = new TextDocument();

        textDocument.setText("you shall not pass!");
        assertThat(textDocument.getText(), is("you shall not pass!"));
    }

    @Test
    public void testCaretNavigation() {
        TextDocument textDocument = new TextDocument();
        textDocument.setText("0123456789");

        textDocument.moveCaretHome();
        assertThat(textDocument.getCaretPosition(), is(0));

        textDocument.moveCaretLeft();
        assertThat(textDocument.getCaretPosition(), is(0));

        textDocument.moveCaretEnd();
        assertThat(textDocument.getCaretPosition(), is(10));

        textDocument.moveCaretLeft();
        assertThat(textDocument.getCaretPosition(), is(9));

        textDocument.moveCaretRight();
        assertThat(textDocument.getCaretPosition(), is(10));

        textDocument.moveCaretRight();
        assertThat(textDocument.getCaretPosition(), is(10));
    }

    @Test
    @Parameters
    public void testSetCaretPosition(Integer caretPosition, Integer expectedPosition) {
        TextDocument document = new TextDocument();
        document.setText("0123456789");

        document.setCaretPosition(caretPosition);
        assertThat(document.getCaretPosition(), is(expectedPosition));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestSetCaretPosition() {
        return new Object[][]{
                {3, 3}, {-3, 0}, {100, 10}
        };
    }

    @Test
    @Parameters
    public void testDeleteCharBeforeCaret(Integer initialCaretPosition,
                                          Integer expectedCaretPosition,
                                          String expectedText) {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setCaretPosition(initialCaretPosition);
        document.deleteCharBeforeCaret();

        assertThat(document.getCaretPosition(), is(expectedCaretPosition));
        assertThat(document.getText(), is(expectedText));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDeleteCharBeforeCaret() {
        return new Object[][]{
                {0, 0, "0123456789"},
                {1, 0, "123456789"},
                {5, 4, "012356789"},
                {9, 8, "012345679"},
        };
    }

    @Test
    @Parameters
    public void testDeleteSelectionBeforeCaret(Integer selectionStart,
                                               Integer selectionEnd,
                                               Integer expectedCaretPosition,
                                               String expectedText) {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(selectionStart, selectionEnd);
        document.deleteCharBeforeCaret();

        assertThat(document.getCaretPosition(), is(expectedCaretPosition));
        assertThat(document.getText(), is(expectedText));
        assertThat(document.hasSelection(), is(false));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDeleteSelectionBeforeCaret() {
        return new Object[][]{
                {0, 3, 0, "3456789"},
                {4, 6, 4, "01236789"},
                {5, 10, 5, "01234"},
                {5, 9, 5, "012349"},
        };
    }

    @Test
    public void testDeselectOnCaretMove() {
        TextDocument document = new TextDocument();
        document.setText("something");
        document.setSelection(2, 4);
        document.moveCaretHome();

        assertThat(document.hasSelection(), is(false));
    }

    @Test
    public void testBeginSelection() {
        TextDocument document = new TextDocument();
        document.setText("something");
        document.setCaretPosition(4);
        document.beginSelection();

        assertThat(document.getSelectionBegin(), is(4));
        assertThat(document.getSelectionEnd(), is(4));
    }

    @Test
    public void testGetSelectedText() {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(2, 6);
        assertThat(document.getSelectedText(), is("2345"));
    }

    @Test
    public void testGetSelectedTextWithReverseSelection() {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(6, 2);
        assertThat(document.getSelectedText(), is("2345"));
    }

    @Test
    public void testInsertAfterSelecting() {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(2, 6);
        document.insertText("something");

        assertThat(document.getText(), is("01something6789"));
    }

    @Test
    public void testInsertAfterSelectingAll() {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(0, 10);
        document.insertText("something");

        assertThat(document.getText(), is("something"));
    }

    @Test
    public void testDeleteReverseSelection() throws Exception {
        TextDocument document = new TextDocument();

        document.setText("0123456789");
        document.setCaretPosition(6);
        document.beginSelection();
        for (int i = 0; i < 4; ++i) {
            document.moveCaretLeft();
        }
        document.deleteCharAfterCaret();

        assertThat(document.getText(), is("016789"));
        assertThat(document.getCaretPosition(), is(2));
        assertThat(document.hasSelection(), is(false));

        document.setText("0123456789");
        document.setCaretPosition(6);
        document.beginSelection();
        for (int i = 0; i < 4; ++i) {
            document.moveCaretLeft();
        }
        document.deleteCharBeforeCaret();

        assertThat(document.getText(), is("016789"));
        assertThat(document.getCaretPosition(), is(2));
        assertThat(document.hasSelection(), is(false));
    }

    @Test
    public void testSetTextRemovesSelectionAndCaretPosition() throws Exception {
        TextDocument document = new TextDocument();
        document.setText("0123456789");
        document.setSelection(6, 2);
        document.setText("something wrong?");

        assertThat(document.hasSelection(), is(false));
        assertThat(document.getCaretPosition(), is(0));
    }
}
