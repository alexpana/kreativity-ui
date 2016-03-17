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

/**
 * The {@link KrToolkit} class offers functionality that is global to the UI framework
 * such as changing the cursor, or creating drawables.
 */
@SuppressWarnings("WeakerAccess")
public class KrToolkit {

    private static final Map<Color, Drawable> DRAWABLE_CACHE = new HashMap<>();

    private static KrSkin.Cursor CURRENT_CURSOR;

    private static KrToolkit INSTANCE;

    public KrToolkit getDefault() {
        if (INSTANCE == null) {
            INSTANCE = new KrToolkit();
        }

        return INSTANCE;
    }

    private KrToolkit() {
    }

    public static void setCursor(KrSkin.Cursor cursor) {
        if (cursor == null) {
            cursor = KrSkin.Cursor.POINTER;
        }
        CURRENT_CURSOR = cursor;
        Gdx.input.setCursorImage(cursor.getCursorPixmap(), cursor.getHotSpotX(), cursor.getHotSpotY());
    }

    public static KrSkin.Cursor getCursor() {
        return CURRENT_CURSOR;
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
