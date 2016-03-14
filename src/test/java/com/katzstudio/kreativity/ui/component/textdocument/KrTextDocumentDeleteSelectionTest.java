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
 * Unit tests for {@link KrTextDocument} related to removing various selection configurations
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class KrTextDocumentDeleteSelectionTest {

    private final String text;

    private final int selectionBegin;

    private final int selectionEnd;

    private final String expectedResult;

    @Test
    public void testDeleteSelection() {
        KrTextDocument document = new KrTextDocument();

        document.setText(text);
        document.setSelection(selectionBegin, selectionEnd);
        document.deleteSelection();

        assertThat(document.getText(), is(expectedResult));
    }

    @Parameterized.Parameters(name = "\"{0}\"[{1}, {2}] -> \"{3}\"")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                // 012|34567|89
                {"0123456789", 3, 8, "01289"},
                // 012|34567|89
                {"0123456789", 8, 3, "01289"},
                // |01234567|89
                {"0123456789", 0, 8, "89"},
                // 01234567|89|
                {"0123456789", 8, 10, "01234567"},
                // |0123456789|
                {"0123456789", 0, 10, ""},
                // 01234567||89
                {"0123456789", 8, 8, "0123456789"},
                // |0123456789|
                {"0123456789", -12, 100, ""},
        });
    }
}
