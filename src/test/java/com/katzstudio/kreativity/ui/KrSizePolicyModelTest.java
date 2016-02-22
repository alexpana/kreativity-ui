package com.katzstudio.kreativity.ui;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link KrSizePolicyModel}
 */
public class KrSizePolicyModelTest {
    @Test
    public void testExactAbsoluteSize() throws Exception {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 0),
                new KrUnifiedSize(30, 0),
                new KrUnifiedSize(15, 0));

        assertThat(model.getSizes(55), Matchers.<Float>containsInRelativeOrder(10.0f, 30.0f, 15.0f));
    }

    @Test
    public void testLessThanAbsoluteSize() throws Exception {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 0),
                new KrUnifiedSize(20, 0),
                new KrUnifiedSize(20, 0));

        assertThat(model.getSizes(10), Matchers.<Float>containsInRelativeOrder(2.0f, 4.0f, 4.0f));
    }

    @Test
    public void testGreaterThanAbsoluteSize() throws Exception {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 0),
                new KrUnifiedSize(30, 0),
                new KrUnifiedSize(15, 0));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(10.0f, 30.0f, 15.0f));
    }

    @Test
    public void testNegativeRelativeValue() {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, -0.5f),
                new KrUnifiedSize(30, 0f));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(10.0f, 30f));
    }

    @Test
    public void testNegativeAbsoluteValue() {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 0f),
                new KrUnifiedSize(-30, 0f));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(10.0f, 0f));
    }

    @Test
    public void testAbsoluteAndRelative() {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 2f),
                new KrUnifiedSize(30, 4f));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(30.0f, 70f));
    }

    @Test
    public void testAbsoluteAndRelativeOneFull() {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(10, 1f),
                new KrUnifiedSize(30, 0f));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(70.0f, 30f));
    }

    @Test
    public void testOnlyRelative() {
        KrSizePolicyModel model = new KrSizePolicyModel(
                new KrUnifiedSize(0, 3f),
                new KrUnifiedSize(0, 2f));

        assertThat(model.getSizes(100), Matchers.<Float>containsInRelativeOrder(60.0f, 40f));
    }
}