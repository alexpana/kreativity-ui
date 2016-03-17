package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.CENTER;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.EAST;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.NORTH;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.SOUTH;
import static com.katzstudio.kreativity.ui.layout.KrBorderLayout.Constraint.WEST;

/**
 * Layout manager that can place widgets either in the center of the area (stretched) or on
 * the margins.
 */
public class KrBorderLayout implements KrLayout {

    public enum Constraint {
        WEST, NORTH, SOUTH, EAST, CENTER
    }

    private final Map<Constraint, KrWidget> constraints = new HashMap<>();

    private final int verticalPadding;

    private final int horizontalPadding;

    private int constraintsMask = 0;

    public KrBorderLayout() {
        this(0, 0);
    }

    public KrBorderLayout(int verticalPadding, int horizontalPadding) {
        this.verticalPadding = verticalPadding;
        this.horizontalPadding = horizontalPadding;
    }

    @Override
    public void setGeometry(Rectangle geometry) {
        // NORTH
        KrWidget northWidget = constraints.get(NORTH);
        float northWidgetHeight = northWidget != null ? northWidget.getPreferredSize().y : 0;
        Rectangle northCell = new Rectangle(horizontalPadding, verticalPadding, geometry.getWidth() - 2 * horizontalPadding, northWidgetHeight);

        // SOUTH
        KrWidget southWidget = constraints.get(Constraint.SOUTH);
        float southWidgetHeight = southWidget != null ? southWidget.getPreferredSize().y : 0;
        Rectangle southCell = new Rectangle(horizontalPadding, verticalPadding, geometry.getWidth() - 2 * horizontalPadding, southWidgetHeight);

        float centerHeight = geometry.getHeight() - 4 * verticalPadding - southCell.height - northCell.height;

        // WEST
        KrWidget westWidget = constraints.get(Constraint.WEST);
        float westWidgetWidth = westWidget != null ? westWidget.getPreferredSize().x : 0;
        Rectangle westCell = new Rectangle(horizontalPadding,
                verticalPadding * 2 + northCell.getHeight(),
                westWidgetWidth,
                centerHeight);

        // EAST
        KrWidget eastWidget = constraints.get(Constraint.EAST);
        float eastWidgetWidth = eastWidget != null ? eastWidget.getPreferredSize().x : 0;
        Rectangle eastCell = new Rectangle(geometry.getWidth() - horizontalPadding - eastWidgetWidth,
                verticalPadding * 2 + northWidgetHeight,
                eastWidgetWidth,
                centerHeight);

        // CENTER
        KrWidget centerWidget = constraints.get(Constraint.CENTER);
        Rectangle centerCell = new Rectangle(
                horizontalPadding * 2 + westWidgetWidth,
                verticalPadding * 2 + northWidgetHeight,
                geometry.getWidth() - 4 * horizontalPadding - westWidgetWidth - eastWidgetWidth,
                centerHeight);

        southCell.y = 3 * verticalPadding + northWidgetHeight + centerHeight;

        layoutInsideCell(northWidget, northCell);
        layoutInsideCell(westWidget, westCell);
        layoutInsideCell(centerWidget, centerCell);
        layoutInsideCell(eastWidget, eastCell);
        layoutInsideCell(southWidget, southCell);
    }

    private void layoutInsideCell(KrWidget widget, Rectangle cellBounds) {
        if (widget != null) {
            widget.setGeometry(cellBounds);
        }
    }

    @Override
    public Vector2 getMinSize() {
        float minWidth = max(minSizeOf(NORTH).x, minSizeOf(WEST).x + minSizeOf(CENTER).x + minSizeOf(EAST).x + 2 * verticalPadding, minSizeOf(SOUTH).x) + 2 * horizontalPadding;
        float minHeight = minSizeOf(NORTH).y + max(minSizeOf(WEST).y, minSizeOf(CENTER).y, minSizeOf(EAST).y) + minSizeOf(SOUTH).y + 4 * verticalPadding;
        return new Vector2(minWidth, minHeight);
    }

    @Override
    public Vector2 getMaxSize() {
        return new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Vector2 getPreferredSize() {
        float prefWidth = max(prefSizeOf(NORTH).x, prefSizeOf(WEST).x + prefSizeOf(CENTER).x + prefSizeOf(EAST).x + 2 * verticalPadding, prefSizeOf(SOUTH).x) + 2 * verticalPadding;
        float prefHeight = prefSizeOf(NORTH).y + max(prefSizeOf(WEST).y, prefSizeOf(CENTER).y, prefSizeOf(EAST).y) + prefSizeOf(SOUTH).y + 4 * verticalPadding;
        return new Vector2(prefWidth, prefHeight);
    }

    private Vector2 minSizeOf(Constraint constraint) {
        if (constraints.containsKey(constraint)) {
            return constraints.get(constraint).getMinSize();
        } else {
            return new Vector2(0, 0);
        }
    }

    private Vector2 prefSizeOf(Constraint constraint) {
        if (constraints.containsKey(constraint)) {
            return constraints.get(constraint).getPreferredSize();
        } else {
            return new Vector2(0, 0);
        }
    }

    private float max(float a, float b, float c) {
        return Math.max(a, Math.max(b, c));
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

        constraints.put(constraint, child);
        useConstraint(constraint);
    }

    @Override
    public void removeWidget(KrWidget child) {
        Constraint constraint = getConstraintFor(child);
        if (constraint == null) {
            throw new IllegalArgumentException("Widget isn't managed by this layout.");
        }

        clearConstraint(constraint);
        constraints.remove(constraint);
    }

    private Constraint getConstraintFor(KrWidget child) {
        for (Map.Entry<Constraint, KrWidget> entry : constraints.entrySet()) {
            if (entry.getValue() == child) {
                return entry.getKey();
            }
        }
        return null;
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
