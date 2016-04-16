package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The {@link KrRenderer} takes care of rendering various parts of the interface.
 * <p>
 * It can render geometric shapes, images and text. All UI components call
 * the renderer to paint their graphical representation
 */
@SuppressWarnings("WeakerAccess")
public abstract class KrRenderer {

    public abstract void beginFrame();

    public abstract void endFrame();

    public void drawText(String text, Vector2 position) {
        drawText(text, position.x, position.y);
    }

    public abstract void drawText(String text, float x, float y);

    public abstract void drawTextWithShadow(String text, Vector2 position, Vector2 shadowOffset, Color shadowColor);

    public void drawRect(Rectangle rectangle) {
        drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public abstract void drawRect(float x, float y, float w, float h);

    public abstract void drawLine(float x1, float y1, float x2, float y2);

    public void fillRect(Rectangle rectangle) {
        fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public abstract void fillRect(float x, float y, float w, float h);

    public void fillRoundedRect(Rectangle geometry, int radius) {
        fillRoundedRect(geometry.x, geometry.y, geometry.width, geometry.height, radius);
    }

    public abstract void fillRoundedRect(float x, float y, float w, float h, int cornerRadius);

    public abstract void translate(float x, float y);

    public boolean beginClip(Rectangle rectangle) {
        return beginClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public abstract boolean beginClip(float x, float y, float width, float height);

    public abstract void endClip();

    public abstract void setViewportSize(float width, float height);

    public abstract float setOpacity(float opacity);

    public abstract float getOpacity();

    public abstract void popState();

    public abstract void pushState();

    public abstract Vector2 getViewportSize();

    public abstract KrBrush getBrush();

    public abstract KrPen getPen();

    public abstract BitmapFont getFont();

    public abstract void setFont(BitmapFont font);

    public abstract void setBrush(KrBrush brush);

    public abstract void setBrush(Drawable drawable);

    public abstract void setBrush(Color color);

    public abstract void setPen(KrPen pen);

    public abstract void setPen(int size, Color color);

    public void setPen(Color color) {
        setPen(1, color);
    }
}
