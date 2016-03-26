package com.katzstudio.kreativity.ui.backend.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.backend.KrBackend;
import com.katzstudio.kreativity.ui.backend.KrInputSource;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import static com.katzstudio.kreativity.ui.libgdx.KrLibGdxCursorHelper.systemCursor;

/**
 * This backed works with the libgdx Lwjgl3 backend.
 */
public class KrLwjgl3Backend implements KrBackend {

    private final KrLwjgl3Renderer renderer;

    private final KrLwjgl3FontMetrics fontMetrics;


    private final KrLwjgl3InputSource inputSource;

    private final Clipboard clipboard;

    private KrCursor currentCursor;

    public KrLwjgl3Backend() {
        renderer = new KrLwjgl3Renderer();
        fontMetrics = new KrLwjgl3FontMetrics();
        inputSource = new KrLwjgl3InputSource();
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
}
