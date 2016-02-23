package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextField.TextDocument;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TextDocument} class
 */
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
    public void testSetCaretPosition() throws Exception {
        TextDocument document = new TextDocument();
        document.setText("0123456789");

        document.setCaretPosition(3);
        assertThat(document.getCaretPosition(), is(3));

        document.setCaretPosition(-3);
        assertThat(document.getCaretPosition(), is(0));

        document.setCaretPosition(100);
        assertThat(document.getCaretPosition(), is(10));

    }
}
