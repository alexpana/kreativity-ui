package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.katzstudio.kreativity.ui.backend.KrBackend;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link KrToolkit} offers functionality that is global to the UI framework
 * such as changing the cursor, or creating drawables.
 */
public class KrToolkit {

    private static KrToolkit INSTANCE;

    private final KrBackend backend;

    private final Map<Color, Drawable> drawableCache = new HashMap<>();

    public static KrToolkit getDefaultToolkit() {
        return INSTANCE;
    }

    public static void initialize(KrBackend backend) {
        INSTANCE = new KrToolkit(backend);
    }

    private KrToolkit(KrBackend backend) {
        this.backend = backend;
    }

    public KrCanvas createCanvas() {
        return new KrCanvas(getInputSource(), getRenderer(), backend.getScreenWidth(), backend.getScreenHeight());
    }

    public KrFontMetrics fontMetrics() {
        return backend.getFontMetrics();
    }

    public void setCursor(KrCursor cursor) {
        backend.setCursor(cursor);
    }

    public KrCursor getCursor() {
        return backend.getCursor();
    }

    public void writeToClipboard(String value) {
        backend.writeToClipboard(value);
    }

    public String readFromClipboard() {
        return backend.readFromClipboard();
    }

    public Drawable getDrawable(Color color) {
        if (!drawableCache.containsKey(color)) {
            drawableCache.put(color, createColorDrawable(color));
        }
        return drawableCache.get(color);
    }

    public KrInputSource getInputSource() {
        return backend.getInputSource();
    }

    public KrRenderer getRenderer() {
        return backend.getRenderer();
    }

    private Drawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }
}
