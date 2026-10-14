package com.C11.game.ui;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


public class PauseMenu implements Disposable {
    private final Stage stage;
    private final Skin skin;

    private static final float DIM_ALPHA = 0.5f;

    private static final float BUTTON_WIDTH_FRACTION = 0.3f;
    private static final float BUTTON_PADDING = 10f;

    // matches game view's aspect ratio
    private static final float UI_WIDTH = 1280f;
    private static final float UI_HEIGHT = 720f;

    public PauseMenu(Runnable onResume) {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ExtendViewport(UI_WIDTH, UI_HEIGHT));

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", 0f, 0f, 0f, DIM_ALPHA));
        stage.addActor(root);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onResume.run();
            }   
        });
        root.add(resumeButton).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
    }

    public void render(float delta) {
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override 
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
    
}
