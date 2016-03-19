package com.katzstudio.kreativity.ui.model;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

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
        KrAbstractItemModel.KrModelIndex selectedIndex = new KrAbstractItemModel.KrModelIndex(2, 3, null);

        KrSelection selection = new KrSelection(selectedIndex);
        assertThat(selection.size(), is(1));
        assertThat(selection.getSelectedIndexes().get(0), is(selectedIndex));
        assertThat(selection.getSelectedIndexes().contains(selectedIndex), is(true));
    }

    @Test
    public void testMultipleSelection() throws Exception {
        KrAbstractItemModel.KrModelIndex indexA = new KrAbstractItemModel.KrModelIndex(2, 3, null);
        KrAbstractItemModel.KrModelIndex indexB = new KrAbstractItemModel.KrModelIndex(4, 3, null);
        KrAbstractItemModel.KrModelIndex indexC = new KrAbstractItemModel.KrModelIndex(6, 3, null);

        KrSelection selection = new KrSelection(ImmutableList.of(indexA, indexB, indexC));
        assertThat(selection.size(), is(3));
        assertThat(selection.contains(indexA), is(true));
        assertThat(selection.contains(indexB), is(true));
        assertThat(selection.contains(indexC), is(true));
    }
}