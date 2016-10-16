package com.katzstudio.kreativity.ui.layout;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.component.KrWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * A layout similar to {@link KrFlowLayout} which gives all children the same size
 * and uses multiple lines. It's similar to the the way file managers layout the folders
 * and files when using an icon view.
 */
public class KrFileLayout implements KrLayout {

    private List<KrWidget> widgets = new ArrayList<>();

    private Vector2 childSize;

    @Override
    public void setGeometry(Rectangle geometry) {
        int widgetsPerLine = Math.floorDiv((int) geometry.getWidth(), (int) childSize.x);
        int linesRequred = (int) Math.ceil((float) widgets.size() / widgetsPerLine);
        int horizontalSpacing = 0; // TODO: distribute the children horizontally (see windows explorer for reference)

        // we distribute the children disregarding the height of the parent
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
        widgets.add(child);
    }

    @Override
    public void removeWidget(KrWidget child) {
        widgets.remove(child);
    }
}
