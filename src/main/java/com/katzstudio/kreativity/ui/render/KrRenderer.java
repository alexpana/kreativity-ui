package com.katzstudio.kreativity.ui.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The {@link KrRenderer} takes care of rendering various parts of the interface.
 * <p>
 * It can render geometric shapes, images and text. All UI components call
 * the renderer to paint their graphical representation
 */
public interface KrRenderer {

    void beginFrame();

    void endFrame();

    void drawText(String text, Vector2 position);

    void drawText(String text, float x, float y);

    void drawTextWithShadow(String text, Vector2 position, Vector2 shadowOffset, Color shadowColor);

    void drawRect(Rectangle rectangle);

    void drawRect(float x, float y, float w, float h);

    void drawLine(float x1, float y1, float x2, float y2);

    void fillRect(Rectangle rectangle);

    void fillRect(float x, float y, float w, float h);

    void fillRoundedRect(Rectangle geometry, int radius);

    void fillRoundedRect(int x, int y, int w, int h, int cornerRadius);

    void translate(float x, float y);

    boolean beginClip(Rectangle rectangle);

    boolean beginClip(float x, float y, float width, float height);

    void endClip();

    void setViewportSize(float width, float height);

    float setOpacity(float opacity);

    float getOpacity();

    void popState();

    void pushState();

    Vector2 getViewportSize();

    KrBrush getBrush();

    KrPen getPen();

    void setFont(com.badlogic.gdx.graphics.g2d.BitmapFont font);

    void setBrush(KrBrush brush);

    void setBrush(Drawable drawable);

    void setBrush(Color color);

    void setPen(KrPen pen);

    void setPen(int size, Color color);
}
