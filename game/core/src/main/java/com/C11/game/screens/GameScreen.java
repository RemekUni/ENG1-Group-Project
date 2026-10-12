package com.C11.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.C11.game.Main;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final GameMap gameMap;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;

    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;

    public GameScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.getBatch();
        gameMap = new GameMap("maps/campus.tmx");

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);

        camera.position.set(gameMap.getWorldWidth()/2f, gameMap.getWorldHeight()/2f, 0);

    }

    @Override 
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta){

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        camera.update();

        gameMap.render(camera);

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        gameMap.dispose();
    }
    
}
