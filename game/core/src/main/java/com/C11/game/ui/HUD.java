package com.C11.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class HUD extends Stage {

    public HUD(Hotbar hotbar) {
        super(new FitViewport(16, 9));

        hotbar.setFillParent(true);

        addActor(hotbar);
    }

    public void render(float delta) {
        getViewport().apply();
        act(delta);
        draw();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
