package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.KrSizePolicyModel;
import com.katzstudio.kreativity.ui.component.KrWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * Layout manager that can place widgets either in the center of the area (stretched) or on
 * the margins.
 */
public class KrBorderLayout implements KrLayout {

    public enum Constraint {
        WEST, NORTH, SOUTH, EAST, CENTER;
    }

    private final Map<KrWidget, Constraint> constraints = new HashMap<>();

    private final int verticalPadding;

    private final int horizontalPadding;

    private KrSizePolicyModel columnSizePolicy;

    private int constraintsMask = 0;

    public KrBorderLayout(int columns) {
        this(0, 0);
    }

    public KrBorderLayout(int verticalPadding, int horizontalPadding) {
        this.verticalPadding = verticalPadding;
        this.horizontalPadding = horizontalPadding;
    }

    @Override
    public void setGeometry(Rectangle geometry) {
    }

    @Override
    public Vector2 getMinSize() {
        return null;
    }

    @Override
    public Vector2 getMaxSize() {
        return null;
    }

    @Override
    public Vector2 getPreferredSize() {
        return null;
    }

    @Override
    public void addWidget(KrWidget child, Object layoutConstraint) {
        if (!(layoutConstraint instanceof Constraint)) {
            throw new IllegalArgumentException("Unknown constraint: " + layoutConstraint);
        }

        Constraint constraint = (Constraint) layoutConstraint;

        if ((constraintsMask & (1 << constraint.ordinal())) != 0) {
            throw new IllegalArgumentException("Constraint already used: " + layoutConstraint);
        }

        constraints.put(child, constraint);
        useConstraint(constraint);
    }

    @Override
    public void removeWidget(KrWidget child) {
        if (!constraints.containsKey(child)) {
            throw new IllegalArgumentException("Widget isn't managed by this layout.");
        }

        clearConstraint(constraints.get(child));
        constraints.remove(child);
    }

    private boolean constraintUsed(Constraint constraint) {
        return (constraintsMask & (1 << constraint.ordinal())) != 0;
    }

    private void useConstraint(Constraint constraint) {
        constraintsMask |= (1 << constraint.ordinal());
    }

    private void clearConstraint(Constraint constraint) {
        if (constraintUsed(constraint)) {
            constraintsMask -= (1 << constraint.ordinal());
        }
    }
}
