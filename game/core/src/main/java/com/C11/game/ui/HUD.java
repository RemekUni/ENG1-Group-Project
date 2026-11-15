package com.C11.game.ui;

import com.C11.game.entities.Player;
import com.C11.game.world.Inventory;
import com.C11.game.world.Item;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class HUD extends Stage {

    public HUD(Inventory inventory) {
        super(new FitViewport(16, 9));

        Hotbar hotbar = new Hotbar(inventory);
        hotbar.setFillParent(true); // TODO: fills whole stage, but would be more intuitive to have it the size to fit its actors

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
