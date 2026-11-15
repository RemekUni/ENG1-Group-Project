package com.C11.game.ui;

import com.C11.game.world.Assets;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class HUD extends Stage {
    private ImageButton pauseButton;

    public HUD(Hotbar hotbar, Runnable onPause) {
        super(new FitViewport(16, 9));

        hotbar.setFillParent(true);

        Table buttons = new Table();
        buttons.setFillParent(true);
        buttons.top().left();
        buttons.add(createPauseButton(onPause)).size(1.25f, 1.25f);

        addActor(hotbar);
        addActor(buttons);
    }

    /**
     * private helper to create the pause button
     * 
     * @param onPause runnable on click
     * @return ImageButton pause button
     */
    private ImageButton createPauseButton(Runnable onPause) {
        pauseButton = new ImageButton(new TextureRegionDrawable(Assets.PAUSE_ICON));
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPause.run();
            }
        });
        return pauseButton;
    }

    /**
     * sets the visibility of the pause button
     * 
     * @param visible true (show), false (hide)
     */
    public void setPauseButtonVisible(boolean visible) {
        pauseButton.setVisible(visible);
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
