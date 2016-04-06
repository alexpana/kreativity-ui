package com.katzstudio.kreativity.ui.icon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;

/**
 * {@link KrIcon} implementation that uses a {@link Drawable}
 * to draw the icon.
 */
public class KrDrawableIcon implements KrIcon {

    @Getter private final Vector2 size;

    private final Drawable drawable;

    public KrDrawableIcon(Drawable drawable, int width, int height) {
        this.drawable = drawable;
        this.size = new Vector2(width, height);
    }

    @Override
    public void draw(KrRenderer renderer, int x, int y) {
        renderer.setBrush(drawable);
        renderer.fillRect(x, y, size.x, size.y);
    }
}
