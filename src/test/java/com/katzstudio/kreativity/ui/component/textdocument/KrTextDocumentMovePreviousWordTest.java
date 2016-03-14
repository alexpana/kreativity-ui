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
 * Unit tests for {@link KrTextDocument} related to jumping to the previous word
 */
@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class KrTextDocumentMovePreviousWordTest {

    private static final String TEST_STRING = "xx x   xx";

    private final int caretPosition;

    private final int previousWordPosition;

    @Test
    public void testMovePreviousWord() {
        KrTextDocument document = new KrTextDocument();
        document.setText(TEST_STRING);
        document.setCaretPosition(caretPosition);
        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(previousWordPosition));
    }

    @Parameterized.Parameters(name = "Previous word from {0} in \"" + TEST_STRING + "\" is at: {1}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 7}, {9, 7}
        });
    }
}
