package com.katzstudio.kreativity.ui;

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

public class KrRenderer {

    private final SpriteBatch spriteBatch;

    private BitmapFont font;

    private Color foregroundColor;

    @Getter @Setter private Vector2 viewportSize;

    private Vector2 translation;

    private Vector2 textShadowOffset;

    private Color textShadowColor;

    private final ShapeRenderer shapeRenderer;

    private boolean isSpriteBatchOpen = false;

    public KrRenderer() {
        spriteBatch = new SpriteBatch(100);
        shapeRenderer = new ShapeRenderer(100);
        shapeRenderer.setAutoShapeType(true);
        translation = Vector2.Zero;
        textShadowOffset = Vector2.Zero;
        textShadowColor = Color.BLACK;
    }

    public void beginFrame() {
        ensureSpriteBatchOpen();
    }

    public void endFrame() {
        ensureClosed();
    }

    public void renderText(String text, Vector2 position) {
        renderText(text, position.x, position.y);
    }

    public void renderText(String text, float x, float y) {
        ensureSpriteBatchOpen();
        if (textShadowOffset.x != 0 || textShadowOffset.y != 0) {
            font.setColor(textShadowColor);
            font.draw(spriteBatch, text, x + textShadowOffset.x, viewportSize.y - y - textShadowOffset.y);
        }

        font.setColor(foregroundColor);
        font.draw(spriteBatch, text, x, viewportSize.y - y);
    }

    public void renderDrawable(Drawable drawable, float x, float y, float w, float h) {
        ensureSpriteBatchOpen();
        drawable.draw(spriteBatch, x, viewportSize.y - y - h, w, h);
    }

    public void renderLine(float x1, float y1, float x2, float y2) {
        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(foregroundColor);
        shapeRenderer.line(x1, viewportSize.y - y1, x2, viewportSize.y - y2);
        shapeRenderer.end();
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void setForeground(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void translate(float x, float y) {
        this.translation.add(x, y);
    }

    public void setTextShadow(Vector2 shadowOffset, Color shadowColor) {
        this.textShadowOffset = shadowOffset;
        this.textShadowColor = shadowColor;
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
