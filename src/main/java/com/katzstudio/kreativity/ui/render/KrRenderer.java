package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The {@link KrRenderer} class takes care of rendering various parts of the interface.
 * <p>
 * It can render geometric shapes, images and text. All UI components call
 * the renderer to paint their graphical representation
 */
public class KrRenderer {

    private final RenderMode spriteBatchRenderMode;

    private final RenderMode lineShapeRenderMode;

    private final RenderMode filledShapeRenderMode;

    private final RenderMode nullRenderMode;

    private final SpriteBatch spriteBatch;

    private final ShapeRenderer shapeRenderer;

    private RenderMode currentRenderMode;

    @Setter private BitmapFont font;

    private Vector2 translation;

    @Getter private Vector2 viewportSize;

    @Getter @Setter private KrBrush brush;

    @Getter @Setter private KrPen pen;

    public KrRenderer() {
        spriteBatch = new SpriteBatch(100);
        shapeRenderer = new ShapeRenderer(100);
        shapeRenderer.setAutoShapeType(true);
        translation = Vector2.Zero;

        spriteBatchRenderMode = new SpriteBatchRenderMode();
        lineShapeRenderMode = new ShapeRenderMode(ShapeRenderer.ShapeType.Line);
        filledShapeRenderMode = new ShapeRenderMode(ShapeRenderer.ShapeType.Filled);
        nullRenderMode = new NullRenderMode();

        currentRenderMode = nullRenderMode;

        pen = new KrPen(1.0f, Color.BLACK);
        brush = new KrColorBrush(KrColor.TRANSPARENT);
    }

    public void beginFrame() {
        currentRenderMode = nullRenderMode;
    }

    public void endFrame() {
        flush();
        translate(-translation.x, -translation.y);
    }

    public void drawText(String text, Vector2 position) {
        drawText(text, position.x, position.y);
    }

    public void drawText(String text, float x, float y) {
        ensureSpriteBatchOpen();

        Color originalFontColor = font.getColor();

        font.setColor(pen.getColor());
        font.draw(spriteBatch, text, x, viewportSize.y - y);

        font.setColor(originalFontColor);
    }

    public void drawTextWithShadow(String text, Vector2 position, Vector2 shadowOffset, Color shadowColor) {
        if (shadowOffset.equals(Vector2.Zero)) {
            drawText(text, position);
            return;
        }

        Color originalFontColor = font.getColor();

        ensureSpriteBatchOpen();

        // render shadow
        font.setColor(shadowColor);
        font.draw(spriteBatch, text, position.x + shadowOffset.x, viewportSize.y - position.y - shadowOffset.y);

        // render text
        font.setColor(pen.getColor());
        font.draw(spriteBatch, text, position.x, viewportSize.y - position.y);

        font.setColor(originalFontColor);
    }

    public void drawRect(Rectangle rectangle) {
        drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void drawRect(float x, float y, float w, float h) {
        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(pen.getColor());

        drawLineInternal(x, y, x + w - 1, y);
        drawLineInternal(x, y, x, y + h - 1);
        drawLineInternal(x + w - 1, y, x + w - 1, y + h - 1);
        drawLineInternal(x, y + h - 1, x + w - 1, y + h - 1);

        shapeRenderer.end();
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(pen.getColor());
        drawLineInternal(x1, y1, x2, y2);
        shapeRenderer.end();
    }

    private void drawLineInternal(float x1, float y1, float x2, float y2) {
        shapeRenderer.line(x1, viewportSize.y - y1, x2 + 1, viewportSize.y - y2 - 1);
    }

    public void fillRect(Rectangle rectangle) {
        fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void fillRect(float x, float y, float w, float h) {
        if (brush instanceof KrDrawableBrush) {
            ensureSpriteBatchOpen();
            KrDrawableBrush drawableBrush = (KrDrawableBrush) brush;
            drawableBrush.getDrawable().draw(spriteBatch, x, viewportSize.y - y - h, w, h);
        }

        if (brush instanceof KrColorBrush) {
            KrColorBrush colorBrush = (KrColorBrush) brush;

            ensureShapeRendererOpen(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(colorBrush.getColor());
            shapeRenderer.rect(x, viewportSize.y - y - h, w, h);
        }
    }

    public void translate(float x, float y) {
        translation.add(x, y);
        spriteBatch.getTransformMatrix().translate(x, -y, 0);
        shapeRenderer.translate(x, -y, 0);
    }

    public boolean beginClip(Rectangle rectangle) {
        return beginClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public boolean beginClip(float x, float y, float width, float height) {
        flush();
        Rectangle clipRectangle = Pools.obtain(Rectangle.class);
        clipRectangle.set(x + translation.x, viewportSize.y - y - height - translation.y, width, height);
        if (ScissorStack.pushScissors(clipRectangle)) {
            return true;
        }
        Pools.free(clipRectangle);
        return false;

    }

    public void endClip() {
        flush();
        Pools.free(ScissorStack.popScissors());
    }

    public void setViewportSize(float width, float height) {
        viewportSize.set(width, height);
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void popState() {
        // TODO(alex): implement
    }

    public void pushState() {
        // TODO(alex): implement
    }

    private void ensureSpriteBatchOpen() {
        if (currentRenderMode != spriteBatchRenderMode) {
            currentRenderMode.end();
            currentRenderMode = spriteBatchRenderMode;
            currentRenderMode.begin();
        }
    }

    private void ensureShapeRendererOpen(ShapeRenderer.ShapeType shapeType) {
        RenderMode requestedRenderMode = shapeType == ShapeRenderer.ShapeType.Line ? lineShapeRenderMode : filledShapeRenderMode;
        if (currentRenderMode != requestedRenderMode) {
            currentRenderMode.end();
            currentRenderMode = requestedRenderMode;
            currentRenderMode.begin();
        }
    }

    private void flush() {
        currentRenderMode.end();
        currentRenderMode = nullRenderMode;
    }

    private interface RenderMode {
        void begin();

        void end();
    }

    private class SpriteBatchRenderMode implements RenderMode {
        @Override
        public void begin() {
            spriteBatch.begin();
        }

        @Override
        public void end() {
            spriteBatch.end();
        }
    }

    @RequiredArgsConstructor
    private class ShapeRenderMode implements RenderMode {
        public final ShapeRenderer.ShapeType shapeType;

        @Override
        public void begin() {
            shapeRenderer.begin(shapeType);
        }

        @Override
        public void end() {
            shapeRenderer.end();
        }
    }

    private class NullRenderMode implements RenderMode {
        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }
    }
}
