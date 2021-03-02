package com.katzstudio.kreativity.ui.backend.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Clipboard;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.backend.KrBackend;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * This backend works with libgdx.
 */
public class KrGdxBackend implements KrBackend {

    private final Map<Color, Drawable> drawableCache = new HashMap<>();

    private final KrGdxRenderer renderer;

    private final KrGdxFontMetrics fontMetrics;

    private final KrGdxInputSource inputSource;

    private final Clipboard clipboard;

    private KrCursor currentCursor;

    public KrGdxBackend() {
        renderer = new KrGdxRenderer();
        fontMetrics = new KrGdxFontMetrics();
        inputSource = new KrGdxInputSource();
        clipboard = Gdx.app.getClipboard();
    }

    @Override
    public KrRenderer getRenderer() {
        return renderer;
    }

    @Override
    public KrInputSource getInputSource() {
        return inputSource;
    }

    @Override
    public KrFontMetrics getFontMetrics() {
        return fontMetrics;
    }

    @Override
    public void setCursor(KrCursor cursor) {
        if (cursor == null) {
            cursor = KrCursor.ARROW;
        }
        currentCursor = cursor;
        Gdx.graphics.setSystemCursor(systemCursor(cursor));
    }

    @Override
    public KrCursor getCursor() {
        return currentCursor;
    }

    @Override
    public void writeToClipboard(String value) {
        clipboard.setContents(value);
    }

    @Override
    public String readFromClipboard() {
        return clipboard.getContents();
    }

    @Override
    public int getScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return Gdx.graphics.getHeight();
    }

    @Override
    public Drawable createColorDrawable(Color color) {
        if (!drawableCache.containsKey(color)) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();
            Drawable colorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
            drawableCache.put(color, colorDrawable);
        }
        return drawableCache.get(color);
    }

    private static Cursor.SystemCursor systemCursor(KrCursor cursor) {
        switch (cursor) {
            case ARROW:
                return Cursor.SystemCursor.Arrow;
            case IBEAM:
                return Cursor.SystemCursor.Ibeam;
            case CROSSHAIR:
                return Cursor.SystemCursor.Crosshair;
            case HAND:
                return Cursor.SystemCursor.Hand;
            case HORIZONTAL_RESIZE:
                return Cursor.SystemCursor.HorizontalResize;
            case VERTICAL_RESIZE:
                return Cursor.SystemCursor.VerticalResize;
            default:
                return Cursor.SystemCursor.Arrow;
        }
    }
}
