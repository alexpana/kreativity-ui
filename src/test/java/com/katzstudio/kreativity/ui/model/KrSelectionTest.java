package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.model.KrAbstractItemModel.KrModelIndex;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrSelection}
 */
public class KrSelectionTest {

    @Test
    public void testEmptySelection() throws Exception {
        KrSelection selection = KrSelection.EMPTY;
        assertThat(selection.getSelectedIndexes().isEmpty(), is(true));
        assertThat(selection.size(), is(0));
    }

    @Test
    public void testSingleSelection() throws Exception {
        KrModelIndex selectedIndex = new KrModelIndex(2, 3, null);

        KrSelection selection = KrSelection.of(selectedIndex);
        assertThat(selection.size(), is(1));
        assertThat(selection.getSelectedIndexes().get(0), is(selectedIndex));
        assertThat(selection.getSelectedIndexes().contains(selectedIndex), is(true));
    }

    @Test
    public void testMultipleSelection() throws Exception {
        KrModelIndex indexA = new KrModelIndex(2, 3, null);
        KrModelIndex indexB = new KrModelIndex(4, 3, null);
        KrModelIndex indexC = new KrModelIndex(6, 3, null);

        KrSelection selection = new KrSelection(Arrays.asList(indexA, indexB, indexC));
        assertThat(selection.size(), is(3));
        assertThat(selection.contains(indexA), is(true));
        assertThat(selection.contains(indexB), is(true));
        assertThat(selection.contains(indexC), is(true));
    }

    @Test
    public void testExpandSelection() throws Exception {

        KrSelection selection = new KrSelection(Arrays.asList(new KrModelIndex(0), new KrModelIndex(1)));

        KrModelIndex newIndex = new KrModelIndex(2);
        KrSelection newSelection = selection.expand(newIndex);

        assertThat(selection.size(), is(2));
        assertThat(newSelection.size(), is(3));
        assertThat(newSelection.contains(newIndex), is(true));
    }

    @Test
    public void testRemoveSelection() throws Exception {
        KrModelIndex removedIndex = new KrModelIndex(2);
        KrSelection selection = new KrSelection(Arrays.asList(new KrModelIndex(0), new KrModelIndex(1), removedIndex));

        KrSelection newSelection = selection.shrink(removedIndex);

        assertThat(selection.size(), is(3));
        assertThat(newSelection.size(), is(2));
        assertThat(newSelection.contains(removedIndex), is(false));
    }
}