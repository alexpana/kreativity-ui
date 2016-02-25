package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextField.TextDocument;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TextDocument}
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class TextDocumentCaretPositionAfterDeleteSelectionTest {

    private final String initialText;

    private final Integer selectionBegin;

    private final Integer selectionEnd;

    private final Integer expectedCaretPosition;

    @Test
    public void testCaretPositionAfterDeleteSelection() throws Exception {
        TextDocument document = new TextDocument();
        document.setText(initialText);
        document.setSelection(selectionBegin, selectionEnd);

        document.deleteSelection();

        assertThat(document.getCaretPosition(), is(expectedCaretPosition));
    }

    @Parameterized.Parameters(name = "Delete from \"{0}\" range ({1},{2})")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {"0123456789", 2, 5, 2},
                {"0123456789", 5, 2, 2},
                {"0123456789", 0, 4, 0},
                {"0123456789", 9, 7, 7},
        });
    }
}
