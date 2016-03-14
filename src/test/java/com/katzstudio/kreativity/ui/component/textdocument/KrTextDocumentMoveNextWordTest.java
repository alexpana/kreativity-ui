package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextDocument;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrTextDocument} related to finding the next word from
 * the caret position
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class KrTextDocumentMoveNextWordTest {
    private static final String TEST_STRING = "xx x   xx";

    private final int caretPosition;

    private final int nextWordPosition;

    @Test
    public void testMovePreviousWord() {
        KrTextDocument document = new KrTextDocument();
        document.setText(TEST_STRING);
        document.setCaretPosition(caretPosition);
        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(nextWordPosition));
    }

    @Parameterized.Parameters(name = "Previous word from {0} in \"" + TEST_STRING + "\" is at: {1}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {0, 3}, {1, 3}, {2, 3}, {3, 7}, {4, 7}, {5, 7}, {6, 7}, {7, 9}, {8, 9}, {9, 9}
        });
    }
}
