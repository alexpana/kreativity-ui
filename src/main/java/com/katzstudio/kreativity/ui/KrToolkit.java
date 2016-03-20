package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;
import java.util.Map;

import static com.katzstudio.kreativity.ui.libgdx.KrLibGdxCursorHelper.systemCursor;

/**
 * The {@link KrToolkit} class offers functionality that is global to the UI framework
 * such as changing the cursor, or creating drawables.
 */
@SuppressWarnings("WeakerAccess")
public class KrToolkit {

    private static final Map<Color, Drawable> DRAWABLE_CACHE = new HashMap<>();

    private static KrToolkit INSTANCE;

    private KrCursor currentCursor;

    private KrFontMetrics fontMetrics = new KrFontMetrics();

    public static KrToolkit getDefaultToolkit() {
        if (INSTANCE == null) {
            INSTANCE = new KrToolkit();
        }

        return INSTANCE;
    }

    public static void setDefault(KrToolkit toolkit) {
        INSTANCE = toolkit;
    }

    private KrToolkit() {
    }

    public KrFontMetrics fontMetrics() {
        return fontMetrics;
    }

    public void setCursor(KrCursor cursor) {
        if (cursor == null) {
            cursor = KrCursor.ARROW;
        }
        currentCursor = cursor;
        Gdx.graphics.setSystemCursor(systemCursor(cursor));
    }

    public KrCursor getCursor() {
        return currentCursor;
    }

    public static Drawable getDrawable(Color color) {
        if (!DRAWABLE_CACHE.containsKey(color)) {
            DRAWABLE_CACHE.put(color, createColorDrawable(color));
        }
        return DRAWABLE_CACHE.get(color);
    }

    private static Drawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }
}
