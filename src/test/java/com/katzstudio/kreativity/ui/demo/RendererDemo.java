package com.katzstudio.kreativity.ui.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.katzstudio.kreativity.ui.render.KrColorBrush;
import com.katzstudio.kreativity.ui.render.KrPen;
import com.katzstudio.kreativity.ui.render.KrRenderer;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;

/**
 */
public class RendererDemo extends Game {

    private KrRenderer renderer;

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 500;
        config.height = 500;
        config.fullscreen = false;
        config.vSyncEnabled = true;
        config.title = "Kreativity Renderer Demo";
        new LwjglApplication(new RendererDemo(), config);
    }

    @Override
    public void create() {
        gl.glEnable(GL_BLEND);
        gl.glDepthMask(true);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        renderer = new KrRenderer();
    }

    @Override
    public void render() {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderer.beginFrame();

        renderer.setBrush(new KrColorBrush(0x9090ff));
        renderer.setPen(new KrPen(1, Color.WHITE));

        // draw clipped rectangle
//        renderer.beginClip(3, 3, 5, 5);
//        renderer.fillRect(0, 0, 50, 50);
//        renderer.endClip();
//        renderer.fillRect(10, 10, 10, 10);

        // translate & draw clipped rectangle
        renderer.translate(20, 10);
        renderer.beginClip(0, 0, 20, 20);
        renderer.fillRect(0, 0, 100, 100);
        renderer.endClip();
//        renderer.fillRect(10, 10, 10, 10);


        renderer.endFrame();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setViewportSize(new Vector2(width, height));
    }
}
