package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * created by Alex
 * on 12-Apr-2015.
 */
@RequiredArgsConstructor
public class KrContext {

    @Getter private final Stage stage;

    @Getter private final Skin skin;
}
