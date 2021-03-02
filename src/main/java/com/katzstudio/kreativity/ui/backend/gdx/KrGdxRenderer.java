package com.katzstudio.kreativity.ui.backend.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import com.katzstudio.kreativity.ui.KrColor;
import com.katzstudio.kreativity.ui.render.KrBrush;
import com.katzstudio.kreativity.ui.render.KrColorBrush;
import com.katzstudio.kreativity.ui.render.KrDrawableBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.katzstudio.kreativity.ui.KrToolkit.getDefaultToolkit;

/**
 * {@link KrRenderer} implementation for the libgdx backend
 */
public class KrGdxRenderer extends KrRenderer {

    private final RenderMode spriteBatchRenderMode;

    private final RenderMode lineShapeRenderMode;

    private final RenderMode filledShapeRenderMode;

    private final RenderMode nullRenderMode;

    private final SpriteBatch spriteBatch;

    private final ShapeRenderer shapeRenderer;

    private RenderMode currentRenderMode;

    @Setter @Getter private BitmapFont font;

    private Vector2 translation;

    @Getter private Vector2 viewportSize = new Vector2(0, 0);

    private Color penColor;

    private Color colorBrush;

    private Drawable drawableBrush;

    private BrushType brushType;

    @Getter private float opacity = 1;

    public KrGdxRenderer() {
        spriteBatch = new SpriteBatch(100);
        shapeRenderer = new ShapeRenderer(100);
        shapeRenderer.setAutoShapeType(true);
        translation = new Vector2(0, 0);

        spriteBatchRenderMode = new SpriteBatchRenderMode();
        lineShapeRenderMode = new ShapeRenderMode(ShapeRenderer.ShapeType.Line);
        filledShapeRenderMode = new ShapeRenderMode(ShapeRenderer.ShapeType.Filled);
        nullRenderMode = new NullRenderMode();

        currentRenderMode = nullRenderMode;

        penColor = Color.BLACK;
        colorBrush = KrColor.TRANSPARENT;
        brushType = BrushType.COLOR;
    }

    @Override
    public void beginFrame() {
        currentRenderMode = nullRenderMode;
    }

    @Override
    public void endFrame() {
        flush();
        translate(-translation.x, -translation.y);
    }

    @Override
    public void drawText(String text, float x, float y) {
        Color color = getAlphaMultiplied(penColor);

        ensureSpriteBatchOpen();

        Color originalFontColor = font.getColor();

        font.setColor(color);
        font.draw(spriteBatch, text, x, viewportSize.y - y);

        font.setColor(originalFontColor);

        Pools.get(Color.class).free(color);
    }

    private Color getAlphaMultiplied(Color color) {
        Color multipliedColor = Pools.get(Color.class).obtain();
        multipliedColor.set(color);
        multipliedColor.a *= getOpacity();
        return multipliedColor;
    }

    private void freeColor(Color color) {
        Pools.get(Color.class).free(color);
    }

    @Override
    public void drawTextWithShadow(String text, Vector2 position, Vector2 shadowOffset, Color shadowColor) {
        if (shadowOffset.equals(Vector2.Zero)) {
            drawText(text, position);
            return;
        }

        Color originalFontColor = font.getColor();

        ensureSpriteBatchOpen();

        // render shadow
        Color color = getAlphaMultiplied(shadowColor);
        font.setColor(color);
        font.draw(spriteBatch, text, position.x + shadowOffset.x, viewportSize.y - position.y - shadowOffset.y);
        freeColor(color);

        // render text
        color = getAlphaMultiplied(penColor);
        font.setColor(color);
        font.draw(spriteBatch, text, position.x, viewportSize.y - position.y);
        freeColor(color);

        font.setColor(originalFontColor);
    }

    @Override
    public void drawRect(float x, float y, float w, float h) {
        Color color = getAlphaMultiplied(penColor);

        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);

        drawLineInternal(x, y, x + w - 1, y);
        drawLineInternal(x, y, x, y + h - 1);
        drawLineInternal(x + w - 1, y, x + w - 1, y + h - 1);
        drawLineInternal(x, y + h - 1, x + w - 1, y + h - 1);

        freeColor(color);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        Color color = getAlphaMultiplied(penColor);

        ensureShapeRendererOpen(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        drawLineInternal(x1, y1, x2, y2);

        freeColor(color);
    }

    private void drawLineInternal(float x1, float y1, float x2, float y2) {
        shapeRenderer.line(x1, viewportSize.y - y1, x2 + 1, viewportSize.y - y2 - 1);
    }

    @Override
    public void fillRect(float x, float y, float w, float h) {
        if (brushType == BrushType.DRAWABLE) {
            ensureSpriteBatchOpen();
            spriteBatch.setColor(1, 1, 1, getOpacity());
            drawableBrush.draw(spriteBatch, x, viewportSize.y - y - h, w, h);
        }

        if (brushType == BrushType.COLOR) {
            Color color = getAlphaMultiplied(colorBrush);
            ensureShapeRendererOpen(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, viewportSize.y - y - h, w, h);
            freeColor(color);
        }
    }

    @Override
    public void fillRoundedRect(float x, float y, float w, float h, int cornerRadius) {
        Drawable drawable = getRoundedRectDrawable(cornerRadius);
        Color originalColor = null;

        ensureSpriteBatchOpen();
        if (brushType == BrushType.COLOR) {
            originalColor = spriteBatch.getColor();
            Color color = getAlphaMultiplied(colorBrush);
            spriteBatch.setColor(color);
            freeColor(color);
        }

        drawable.draw(spriteBatch, x, viewportSize.y - y - h, w, h);

        if (originalColor != null) {
            spriteBatch.setColor(originalColor);
        }
    }

    private Drawable getRoundedRectDrawable(int radius) {
        switch (radius) {
            case 1:
            case 2:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_2");
            case 3:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_3");
            case 4:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_4");
            case 5:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_5");
            case 6:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_6");
            default:
                return getDefaultToolkit().getSkin().getDrawable("rounded_rect_2");
        }
    }

    @Override
    public void translate(float x, float y) {
        flush();
        translation.add(x, y);
        spriteBatch.getTransformMatrix().translate(x, -y, 0);
        shapeRenderer.translate(x, -y, 0);
    }

    @Override
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

    @Override
    public void endClip() {
        flush();
        Pools.free(ScissorStack.popScissors());
    }

    @Override
    public void setViewportSize(float width, float height) {
        viewportSize.set(width, height);
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public float setOpacity(float opacity) {
        float oldOpacity = this.opacity;
        this.opacity = opacity;
        return oldOpacity;
    }

    @Override
    public KrPen getPen() {
        return new KrPen(1, penColor);
    }

    @Override
    public void setBrush(KrBrush brush) {
        if (brush instanceof KrDrawableBrush) {
            brushType = BrushType.DRAWABLE;
            drawableBrush = ((KrDrawableBrush) drawableBrush).getDrawable();
        }

        if (brush instanceof KrColorBrush) {
            brushType = BrushType.COLOR;
            colorBrush = ((KrColorBrush) brush).getColor();
        }
    }

    @Override
    public void setBrush(Drawable drawable) {
        brushType = BrushType.DRAWABLE;
        drawableBrush = drawable;
    }

    @Override
    public void setBrush(Color color) {
        brushType = BrushType.COLOR;
        colorBrush = color;
    }

    @Override
    public KrBrush getBrush() {
        return null;
    }

    @Override
    public void setPen(KrPen pen) {
        penColor = pen.getColor();
    }

    @Override
    public void setPen(int size, Color color) {
        penColor = color;
    }

    @Override
    public void popState() {
        // TODO(alex): implement
    }

    @Override
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

    private enum BrushType {
        DRAWABLE, COLOR
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
