package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Base class for ui tests.
 */
public class AbstractTest extends Game {

    private static LwjglApplication application;

    public void startup() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1000;
        config.height = 800;
        config.fullscreen = false;
        config.vSyncEnabled = true;
        config.title = "Kreativity UI Demo";
        application = new LwjglApplication(this, config);
    }

    @Override
    public void create() {
    }
}
