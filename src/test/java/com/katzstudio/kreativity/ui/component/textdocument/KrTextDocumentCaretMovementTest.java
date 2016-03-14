package com.katzstudio.kreativity.ui.component.textdocument;

import com.katzstudio.kreativity.ui.component.KrTextDocument;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrTextDocument} related to caret movement
 */
public class KrTextDocumentCaretMovementTest {

    public static final String TEST_STRING = "aaaa bbb cccccc dddd  ee ";

    private KrTextDocument document;

    @Before
    public void setUp() {
        document = new KrTextDocument();
        document.setText(TEST_STRING);
    }

    @Test
    public void testMoveLeft() {
        document.setCaretPosition(2);

        document.moveCaretLeft();
        assertThat(document.getCaretPosition(), is(1));

        document.moveCaretLeft();
        assertThat(document.getCaretPosition(), is(0));

        document.moveCaretLeft();
        assertThat(document.getCaretPosition(), is(0));
    }

    @Test
    public void testMoveRight() {
        document.setCaretPosition(TEST_STRING.length() - 2);

        document.moveCaretRight();
        assertThat(document.getCaretPosition(), is(TEST_STRING.length() - 1));

        document.moveCaretRight();
        assertThat(document.getCaretPosition(), is(TEST_STRING.length()));

        document.moveCaretRight();
        assertThat(document.getCaretPosition(), is(TEST_STRING.length()));
    }

    @Test
    public void testMoveHome() {
        document.setCaretPosition(4);
        document.moveCaretHome();
        assertThat(document.getCaretPosition(), is(0));
    }

    @Test
    public void testMoveEnd() {
        document.setCaretPosition(5);
        document.moveCaretEnd();
        assertThat(document.getCaretPosition(), is(TEST_STRING.length()));
    }

    @Test
    public void testMoveNextWord() {
        //  0     5    9       16     22  25
        //  |aaaa |bbb |cccccc |dddd  |ee |

        document.setCaretPosition(1);

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(5));

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(9));

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(16));

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(22));

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(25));

        document.moveCaretNextWord();
        assertThat(document.getCaretPosition(), is(25));
    }

    @Test
    public void testMovePreviousWord() {
        //  0     5    9       16     22  25
        //  |aaaa |bbb |cccccc |dddd  |ee |

        document.setCaretPosition(100);

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(22));

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(16));

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(9));

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(5));

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(0));

        document.moveCaretPreviousWord();
        assertThat(document.getCaretPosition(), is(0));
    }
}