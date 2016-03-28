package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.katzstudio.kreativity.ui.animation.KrAnimations;
import com.katzstudio.kreativity.ui.backend.KrBackend;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import com.katzstudio.kreativity.ui.util.KrUpdateListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link KrToolkit} offers functionality that is global to the UI framework
 * such as changing the cursor, or creating drawables.
 */
public class KrToolkit {

    private static KrToolkit INSTANCE;

    private final KrBackend backend;

    private final Map<Color, Drawable> drawableCache = new HashMap<>();

    private final List<KrUpdateListener> updateListeners = new ArrayList<>();

    private final KrAnimations animations;

    private KrCanvas canvas;

    @Getter private KrSkin skin;

    public static KrToolkit getDefaultToolkit() {
        return INSTANCE;
    }

    public static void initialize(KrBackend backend) {
        INSTANCE = new KrToolkit(backend);
    }

    public static void initialize(KrBackend backend, KrSkin skin) {
        INSTANCE = new KrToolkit(backend, skin);
    }

    private KrToolkit(KrBackend backend) {
        this.backend = backend;
        this.skin = new KrSkin(this);
        this.animations = new KrAnimations();
    }

    private KrToolkit(KrBackend backend, KrSkin skin) {
        this.backend = backend;
        this.skin = skin;
        this.animations = new KrAnimations();
    }

    public KrCanvas createCanvas() {
        if (canvas == null) {
            canvas = new KrCanvas(getInputSource(), getRenderer(), backend.getScreenWidth(), backend.getScreenHeight());
        }
        return canvas;
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

    public static KrAnimations animations() {
        return getDefaultToolkit().animations;
    }

    public void registerUpdateListener(KrUpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    public void unregisterUpdateListener(KrUpdateListener updateListener) {
        updateListeners.remove(updateListener);
    }

    public void update(float deltaSeconds) {
        if (canvas != null) {
            canvas.update(deltaSeconds);
        }
        new ArrayList<>(updateListeners).forEach(l -> l.update(deltaSeconds));
        animations.update(deltaSeconds);
    }

    private Drawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }
}
