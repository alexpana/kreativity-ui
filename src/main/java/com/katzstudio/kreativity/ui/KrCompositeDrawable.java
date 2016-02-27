package com.katzstudio.kreativity.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by alex
 * on 27.03.2015.
 */
public class KrCompositeDrawable extends TextureRegionDrawable {
    private final TextureRegion first;
    private final TextureRegion second;

    public KrCompositeDrawable(TextureRegion first, TextureRegion second) {
        super(second);
        this.first = first;
        this.second = second;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.draw(first, x, y, width, height);
        batch.draw(second, x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX,
                     float scaleY, float rotation) {
        batch.draw(first, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        batch.draw(second, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }
}