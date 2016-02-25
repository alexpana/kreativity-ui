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
    public void testSetText() throws Exception {
        TextDocument textDocument = new TextDocument();

        textDocument.setText("you shall not pass!");
        assertThat(textDocument.getText(), is("you shall not pass!"));
    }

    @Test
    public void testCaretNavigation() throws Exception {
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
    public void testSetCaretPosition(Integer caretPosition, Integer expectedPosition) throws Exception {
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

    // TODO(alex): implement tests for undo / redo actions
}
