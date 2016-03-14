package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextDocument;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test for {@link KrTextDocument}
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class KrTextDocumentCaretPositionAfterInsertTest {

    private final String text;

    private final Integer initialCaretPosition;

    private final String textToInput;

    private final Integer expectedCaretPosition;

    @Test
    public void testCaretPosition() throws Exception {
        KrTextDocument document = new KrTextDocument();
        document.setText(text);
        document.setCaretPosition(initialCaretPosition);
        document.insertText(textToInput);

        assertThat(document.getCaretPosition(), is(expectedCaretPosition));
    }

    @Parameterized.Parameters(name = "Insert \"{2}\" in \"{0}\" at position {1}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {"test", 0, "x", 1},
                {"test", 0, "test", 4},
                {"test", 0, "", 0},
                {"test", 3, "x", 4},
                {"test", 3, "test", 7},
                {"test", 4, "x", 5},
                {"test", 4, "test", 8},
                {"test", 4, "", 4},
        });
    }
}
