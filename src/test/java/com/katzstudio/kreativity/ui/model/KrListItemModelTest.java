package com.katzstudio.kreativity.ui.model;

import com.katzstudio.kreativity.ui.model.KrItemModel.KrModelIndex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrListItemModel}
 */
public class KrListItemModelTest {

    @Test
    public void testColumnCount() {
        KrListItemModel<Object> model = new KrListItemModel<>();
        assertThat(model.getColumnCount(), is(1));
    }


    @Test
    public void testEmptyModelRowCount() throws Exception {
        KrListItemModel<Integer> model = new KrListItemModel<>();
        assertThat(model.getRowCount(), is(0));
    }

    @Test
    public void testRowCount() {
        List<Integer> list = Arrays.asList(10, 20, 30, 40);
        KrListItemModel<Integer> model = new KrListItemModel<>(list);
        assertThat(model.getRowCount(), is(list.size()));
    }

    @Test
    public void testGetValue() throws Exception {
        List<Integer> list = Arrays.asList(10, 20, 30, 40);
        KrListItemModel<Integer> model = new KrListItemModel<>(list);

        assertThat(model.getValue(new KrModelIndex(0)), is(10));
        assertThat(model.getValue(new KrModelIndex(1)), is(20));
        assertThat(model.getValue(new KrModelIndex(2)), is(30));
        assertThat(model.getValue(new KrModelIndex(3)), is(40));
    }
}