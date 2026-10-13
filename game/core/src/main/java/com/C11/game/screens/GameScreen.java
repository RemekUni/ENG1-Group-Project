package com.C11.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.C11.game.Main;
import com.C11.game.entities.Player;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final GameMap gameMap;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;
    private final Player player;
    
    private final float worldWidth;
    private final float worldHeight;

    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;

    private final Texture playerTexture;

    public GameScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.getBatch();
        gameMap = new GameMap("maps/campus.tmx");

        playerTexture = new Texture("sprites/tile_0098.png");

        worldWidth = gameMap.getWorldWidth();
        worldHeight = gameMap.getWorldHeight();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);

        camera.position.set(worldWidth/2f, worldHeight/2f, 0);

        player = new Player(playerTexture, worldWidth/2, worldHeight/2, gameMap);

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
        player.update(delta);
        viewport.getCamera().position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, 0);
        viewport.getCamera().position.set(MathUtils.clamp(camera.position.x, viewport.getWorldWidth() / 2, worldWidth - viewport.getWorldWidth() / 2), MathUtils.clamp(camera.position.y, viewport.getWorldHeight() / 2, worldHeight - viewport.getWorldHeight() / 2), 0);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        camera.update();

        gameMap.render(camera);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        player.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        gameMap.dispose();
        playerTexture.dispose();
    }
    
}
