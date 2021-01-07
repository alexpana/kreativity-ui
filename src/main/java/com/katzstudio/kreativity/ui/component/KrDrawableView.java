package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrRectangles.rectangles;

/**
 * A view that renders a drawable.
 */
public class KrDrawableView extends KrWidget {

    @Getter @Setter private Drawable drawable;

    @Getter @Setter private boolean keepAspect = false;

    private final float aspect;

    public KrDrawableView(Drawable drawable) {
        this.drawable = drawable;
        this.aspect = drawable.getMinWidth() / drawable.getMinHeight();
        setSize(calculatePreferredSize());
    }

    @Override
    public Vector2 calculatePreferredSize() {
        float width = drawable.getMinWidth();
        float height = drawable.getMinHeight();

        Rectangle bounds = tmpRect.set(0,0, width, height);
        return rectangles(bounds).expand(getPadding()).size();
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.setBrush(drawable);
        renderer.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void setGeometry(float x, float y, float width, float height) {
        if (this.x == x && this.y == y && this.width == width && this.height == height) {
            return;
        }

        if (keepAspect) {
            if (width < height) {
                height = width / aspect;
            } else if (height < width) {
                width = height * aspect;
            }
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        invalidate();
    }
}
