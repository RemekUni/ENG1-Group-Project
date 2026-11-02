package com.C11.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.C11.game.Main;
import com.C11.game.entities.Entity;
import com.C11.game.entities.Goose;
import com.C11.game.entities.ItemEntity;
import com.C11.game.entities.Player;
import com.C11.game.events.HiddenEvent;
import com.C11.game.ui.PauseMenu;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;
import java.util.List;
import com.C11.game.events.HiddenEvent;
import com.C11.game.ItemType;

/**
 * Main gameplay screen. It owns the map, camera, viewport, entities and their respective textures.
 * Each frame runs update(delta), then draw(). These are separate to allow for game pausing to only skip updates but keep drawing the paused game screen.
 * 
 * @req UR_MAP UR_CHARACTER UR_GEESE
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final GameMap gameMap;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;
    private final Player player;
    private final Goose goose;
    private final Goose goose2;

    private Array<Entity> entities = new Array<>();
    private Array<ItemEntity> itemEntities = new Array<>();;
    
    private final float worldWidth;
    private final float worldHeight;

    /**
     * Minimum visible area in world units (tiles).
     * ExtendViewport may show more in the visible area on non 16:9 screens.
     */
    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;

    // Loaded once and then shared by entities. Disposed in dispose().
    private final Texture playerTexture;
    private final Texture gooseTexture;
    private final Texture itemTexture1; // TODO: Use one secitoned texture sprite for multiple item types


    private boolean isPaused;

    private final PauseMenu pauseMenu;

    // Hidden events on the map, checked every frame in update()
    private final List<HiddenEvent> events = new ArrayList<>();
    private int eventsTriggered = 0; // for FR_EVENT_COUNTER

    /**
     * Loads map and textures.
     * Sets up the camera and viewport.
     * Spawns entities.
     * 
     * @param game game provides the shared batch
     */
    public GameScreen(Main game) {
        // TODO: read spawn positions from the Tiled spawn layer and use them to initialise entities
        this.game = game;
        this.spriteBatch = game.getBatch();
        gameMap = new GameMap("maps/campus.tmx");

        playerTexture = new Texture("sprites/tile_0098.png");
        gooseTexture = new Texture("sprites/duck.png");
        itemTexture1 = new Texture("sprites/item-prototype.png");

        worldWidth = gameMap.getWorldWidth();
        worldHeight = gameMap.getWorldHeight();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);

        camera.position.set(worldWidth/2f, worldHeight/2f, 0);


        player = new Player(playerTexture, worldWidth/2, worldHeight/2, gameMap, entities, itemEntities);
        goose = new Goose(gooseTexture, worldWidth/2 - 10, worldHeight/2, gameMap, player, entities);
        goose2 = new Goose(gooseTexture, worldWidth/2 + 10, worldHeight/2, gameMap, player, entities, itemEntities);
        
        events.add(new HiddenEvent(worldWidth/2 + 3, worldHeight/2, 2f, 2f, 1f, player, true) {
            @Override
            public void action() {
                System.out.println("Hidden event triggered!");
            }
        });

        // temp/dev
        int itemCount = 50;
        for (int i = 0; i < itemCount; i++) {
            int randomX = ThreadLocalRandom.current().nextInt((int) 0, (int) worldWidth);
            int randomY = ThreadLocalRandom.current().nextInt((int) 0, (int) worldHeight);


            ItemEntity itemType1 = new ItemEntity(new ItemType("baguette", itemTexture1), gameMap, randomX, randomY, entities);
            itemEntities.add(itemType1);
        }

        pauseMenu = new PauseMenu(() -> resumeGame(), () -> Gdx.app.exit());
    }

    @Override 
    public void show() {
        pauseGame();
    }

    // false: do not recentre the camera, it is supposed to follow the player instead
    @Override 
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        followPlayer();
        pauseMenu.resize(width, height);
    }

    /** @req FR_PAUSE */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (isPaused) resumeGame();
            else pauseGame();
        } else if (Gdx.input.isKeyJustPressed(Keys.P)) {
            game.showEndScreen();
        }
        if (!isPaused) update(delta);
        draw();

        if (isPaused) pauseMenu.render(delta);

    }

    /** @req FR_EVENT_COUNTER FR_TRIGGER_EVENT */
    private void update(float delta){
        player.update(delta);
        goose.update(delta);
        goose2.update(delta);
        // TODO: test event only, replace with real events
        for (HiddenEvent event : events) {
            if (event.check()) {
                eventsTriggered++;
            }
        }

        // Items
        for (ItemEntity item : itemEntities) {
            item.update(delta);
        }

        followPlayer();
    }

    /** @req FR_PAUSE */
    private void pauseGame() {
        isPaused = true;
        pauseMenu.show();
    }

    /** @req FR_PAUSE */
    private void resumeGame() {
        isPaused = false;
        pauseMenu.hide();
    }

    private void followPlayer() {
        // Follows the player's center, updated every frame
        viewport.getCamera().position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, 0);
        // Clamps the camera so that it stays within the map borders
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
        goose.draw(spriteBatch);
        goose2.draw(spriteBatch);
        // Items
        for (ItemEntity item : itemEntities) {
            item.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    @Override
    public void pause() {
        pauseGame();
    }

    @Override
    public void dispose() {
        gameMap.dispose();
        playerTexture.dispose();
        gooseTexture.dispose();
        pauseMenu.dispose();
        itemTexture1.dispose();
    }
    
}
