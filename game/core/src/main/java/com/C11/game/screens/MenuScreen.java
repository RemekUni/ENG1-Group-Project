package com.C11.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public abstract class MenuScreen extends ScreenAdapter {
    private final Stage stage;
    private final Color bgColor;
    private final Skin skin;

    // fraction of the root table's width
    private static final float BUTTON_WIDTH_FRACTION = 0.3f;
    // in UI viewport units, not world units
    private static final float BUTTON_PADDING = 10f;

    private static final float TITLE_PADDING_BOT = 30f;
    private static final float TITLE_PADDING_TOP = 10f;

    private static final float TITLE_SCALE = 10f;

    private static final float BODY_PADDING_BOT = 10f;

    // matches game view's aspect ratio
    private static final float UI_WIDTH = 1280f;
    private static final float UI_HEIGHT = 720f;

    private final Table root;
    private final Table buttons;
    private final Table body;
    private final Table content;

    protected MenuScreen(Skin skin, String title, Color color) {
        stage = new Stage(new ExtendViewport(UI_WIDTH, UI_HEIGHT));
        bgColor = color;
        this.skin = skin;

        root = new Table();
        body = new Table();
        buttons = new Table();
        content = new Table();

        root.setFillParent(true);
        stage.addActor(root);

        Label titleLabel = new Label(title, skin);
        titleLabel.setFontScale(TITLE_SCALE);

        root.add(titleLabel).padBottom(TITLE_PADDING_BOT).padTop(TITLE_PADDING_TOP).row();
        content.add(body).padBottom(BODY_PADDING_BOT).row();
        content.add(buttons).row();
        root.add(content).expandY();
    }

    protected void addButton(String buttonText, Runnable onClick) {
        TextButton button = new TextButton(buttonText, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.run();
            }
        });

        buttons.add(button).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
    }

    protected void addBodyLabel(String text) {
        Label label = new Label(text, skin);
        body.add(label).row();
    }

    @Override 
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override 
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override 
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override 
    public void dispose() {
        stage.dispose();
    }
}
