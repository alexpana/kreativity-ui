package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link KrRenderer} class takes care of rendering various parts of the interface.
 * <p>
 * It can render geometric shapes, images and text. All UI components call
 * the renderer to paint their graphical representation
 */
public class KrRenderer {

    private final SpriteBatch spriteBatch;

    private final ShapeRenderer shapeRenderer;

    @Setter private BitmapFont font;

    private Vector2 translation;

    private boolean isSpriteBatchOpen = false;

    @Getter @Setter private Vector2 viewportSize;

    @Getter @Setter private KrBrush brush;

    @Getter @Setter private KrPen pen;

    // TODO(alex): Add support for brush / pen to be used when rendering shapes

    public KrRenderer() {
        spriteBatch = new SpriteBatch(100);
        shapeRenderer = new ShapeRenderer(100);
        shapeRenderer.setAutoShapeType(true);
        translation = Vector2.Zero;
    }

    public void beginFrame() {
        ensureSpriteBatchOpen();
    }

    public void endFrame() {
        ensureClosed();
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

        drawLineInternal(x, y, x + w, y);
        drawLineInternal(x, y, x, y + h);
        drawLineInternal(x + w, y, x + w, y + h);
        drawLineInternal(x, y + h, x + w, y + h);

        shapeRenderer.end();
    }


    public void drawLine(float x1, float y1, float x2, float y2) {
        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(pen.getColor());
        drawLineInternal(x1, y1, x2, y2);
        shapeRenderer.end();
    }

    private void drawLineInternal(float x1, float y1, float x2, float y2) {
        shapeRenderer.line(x1, viewportSize.y - y1, x2, viewportSize.y - y2);
    }

    public void fillRect(Rectangle rectangle) {
        fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void fillRect(float x, float y, float w, float h) {
        if (brush instanceof KrDrawableBrush) {
            KrDrawableBrush drawableBrush = (KrDrawableBrush) brush;
            ensureSpriteBatchOpen();
            drawableBrush.getDrawable().draw(spriteBatch, x, viewportSize.y - y - h, w, h);
        }

        if (brush instanceof KrColorBrush) {
            KrColorBrush colorBrush = (KrColorBrush) brush;

            ensureShapeRendererOpen(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(colorBrush.getColor());
            shapeRenderer.rect(x, viewportSize.y - y, w, h);
            shapeRenderer.end();
        }
    }

    public void renderDrawable(Drawable drawable, float x, float y, float w, float h) {
        ensureSpriteBatchOpen();
        drawable.draw(spriteBatch, x, viewportSize.y - y - h, w, h);
    }

    public void renderRectangle(float x1, float y1, float w, float h) {
        // TODO(alex): implement
    }

    public void translate(float x, float y) {
        this.translation.add(x, y);
    }

    public boolean beginClip(float x, float y, float width, float height) {
        spriteBatch.flush();
        Rectangle clipRectangle = Pools.obtain(Rectangle.class);
        clipRectangle.set(x, viewportSize.y - y - height, width, height);
        if (ScissorStack.pushScissors(clipRectangle)) {
            return true;
        }
        Pools.free(clipRectangle);
        return false;

    }

    public void endClip() {
        spriteBatch.flush();
        Pools.free(ScissorStack.popScissors());
    }

    // TODO(alex): implement pushState() / popState()

    private void ensureSpriteBatchOpen() {
        if (!isSpriteBatchOpen) {
            shapeRenderer.end();
            spriteBatch.begin();
            isSpriteBatchOpen = true;
        }
    }

    private void ensureShapeRendererOpen(ShapeRenderer.ShapeType shapeType) {
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            shapeRenderer.begin(shapeType);
            isSpriteBatchOpen = false;
        }
    }

    private void ensureClosed() {
        if (isSpriteBatchOpen) {
            spriteBatch.end();
        } else {
            shapeRenderer.end();
        }
    }
}
