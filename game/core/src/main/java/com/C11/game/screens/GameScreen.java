package com.C11.game.screens;

import com.C11.game.ui.TimerDisplay;
import com.C11.game.utils.GameResult;
import com.C11.game.utils.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;
import java.util.List;

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
    private final Viewport VIEWPORT;
    private final Viewport VIEWPORT_TEXT;
    private final OrthographicCamera CAMERA;
    private final OrthographicCamera CAMERA_TEXT;
    private final SpriteBatch SPRITE_BATCH;
    private final Player PLAYER;
    private final Goose GOOSE;
    private final Goose GOOSE2;

    private final GameState gameState;

    private Array<Entity> entities = new Array<>();
    private Array<ItemEntity> itemEntities = new Array<>();;

    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    /**
     * Minimum visible area in world units (tiles).
     * ExtendViewport may show more in the visible area on non 16:9 screens.
     */
    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;

    // Loaded once and then shared by entities. Disposed in dispose().
    private final Texture playerTexture;
    private final Texture gooseTexture;
    private final Texture itemTexture1; // TODO: Use one sectioned texture sprite for multiple item types


    private boolean isPaused;

    private final PauseMenu pauseMenu;
    private final TimerDisplay TIMER_DISPLAY;
    private final int GAME_LENGTH;

    // Hidden events on the map, checked every frame in update()
    private final List<HiddenEvent> EVENTS = new ArrayList<>();

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
        this.SPRITE_BATCH = game.getBatch();
        this.gameState = new GameState();
        gameMap = new GameMap("maps/campus.tmx");
        GAME_LENGTH = 30;
        TIMER_DISPLAY = new TimerDisplay("SuperNormal.ttf", GAME_LENGTH);
        TIMER_DISPLAY.getTimer().start();

        playerTexture = new Texture("sprites/tile_0098.png");
        gooseTexture = new Texture("sprites/duck.png");
        itemTexture1 = new Texture("sprites/item-prototype.png");

        WORLD_WIDTH = gameMap.getWorldWidth();
        WORLD_HEIGHT = gameMap.getWorldHeight();

        CAMERA = new OrthographicCamera();
        VIEWPORT = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, CAMERA);
        CAMERA_TEXT = new OrthographicCamera();
        // worldWidth is multiplied by an arbitrary number to better facilitate text.
        VIEWPORT_TEXT = new ExtendViewport(WORLD_WIDTH * 20, WORLD_HEIGHT * 20, CAMERA_TEXT);

        CAMERA.position.set(WORLD_WIDTH /2f, WORLD_HEIGHT /2f, 0);


        PLAYER = new Player(playerTexture, WORLD_WIDTH /2, WORLD_HEIGHT /2, gameMap, entities, itemEntities);
        GOOSE = new Goose(gooseTexture, WORLD_WIDTH /2 - 10, WORLD_HEIGHT /2, gameMap, PLAYER, entities);
        GOOSE2 = new Goose(gooseTexture, WORLD_WIDTH /2 + 10, WORLD_HEIGHT /2, gameMap, PLAYER, entities, itemEntities);

        EVENTS.add(new HiddenEvent(WORLD_WIDTH /2 + 3, WORLD_HEIGHT /2, 2f, 2f, 1f, PLAYER, true) {
            @Override
            public void action() {
                System.out.println("Hidden event triggered!");
            }
        });

        // temp/dev
        int itemCount = 50;
        for (int i = 0; i < itemCount; i++) {
            int randomX = ThreadLocalRandom.current().nextInt((int) 0, (int) WORLD_WIDTH);
            int randomY = ThreadLocalRandom.current().nextInt((int) 0, (int) WORLD_HEIGHT);


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
        VIEWPORT.update(width, height, false);
        VIEWPORT_TEXT.update(width, height, false);
        followPlayer();
        pauseMenu.resize(width, height);
    }

    /** @req FR_PAUSE */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (isPaused) resumeGame();
            else pauseGame();
        }
        if (!isPaused) update(delta);
        draw();

        if (isPaused) pauseMenu.render(delta);

    }

    /** @req FR_EVENT_COUNTER FR_TRIGGER_EVENT */
    private void update(float delta) {
        GOOSE.update(delta);
        GOOSE2.update(delta);
        PLAYER.update(delta);
        // TODO: test event only, replace with real events
        for (HiddenEvent event : EVENTS) {
            if (event.check()) {
                gameState.recordEventTriggered();
            }
        }

        // Items
        for (ItemEntity item : itemEntities) {
            item.update(delta);
        }

        if (TIMER_DISPLAY.getTimer().update()) {
            if (TIMER_DISPLAY.getTimer().getTicks() == TIMER_DISPLAY.getInitialTime()) {
                TIMER_DISPLAY.getTimer().pause();  // Call pause() instead of stop() because stop() resets the tick counter and therefore resets the timer display; this is purely an aesthetic thing!
                System.out.println("TIMER OVER TIMER OVER");
                endGame();
            }
        }

        followPlayer();
    }

    private void endGame() {
        GameResult result = gameState.toResult(TIMER_DISPLAY.getTimer().getTicks());
        game.showEndScreen(result);
    }

    /** @req FR_PAUSE */
    private void pauseGame() {
        isPaused = true;
        TIMER_DISPLAY.getTimer().pause();
        pauseMenu.show();
    }

    /** @req FR_PAUSE */
    private void resumeGame() {
        isPaused = false;
        TIMER_DISPLAY.getTimer().unpause();
        pauseMenu.hide();
    }

    private void followPlayer() {
        // Follows the player's center, updated every frame
        VIEWPORT.getCamera().position.set(PLAYER.getPosition().x + PLAYER.getWidth() / 2, PLAYER.getPosition().y + PLAYER.getHeight() / 2, 0);
        // Clamps the camera so that it stays within the map borders
        VIEWPORT.getCamera().position.set(MathUtils.clamp(CAMERA.position.x, VIEWPORT.getWorldWidth() / 2, WORLD_WIDTH - VIEWPORT.getWorldWidth() / 2), MathUtils.clamp(CAMERA.position.y, VIEWPORT.getWorldHeight() / 2, WORLD_HEIGHT - VIEWPORT.getWorldHeight() / 2), 0);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        VIEWPORT.apply();
        CAMERA.update();

        gameMap.render(CAMERA);

        SPRITE_BATCH.setProjectionMatrix(VIEWPORT.getCamera().combined);
        SPRITE_BATCH.begin();

        PLAYER.draw(SPRITE_BATCH);
        GOOSE.draw(SPRITE_BATCH);
        GOOSE2.draw(SPRITE_BATCH);
        // Items
        for (ItemEntity item : itemEntities) {
            item.draw(SPRITE_BATCH);
        }

        SPRITE_BATCH.end();


        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        //                                  RENDER ALL TEXT HERE!
        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

        VIEWPORT_TEXT.apply();
        SPRITE_BATCH.setProjectionMatrix(VIEWPORT_TEXT.getCamera().combined);

        SPRITE_BATCH.begin();

        TIMER_DISPLAY.draw(SPRITE_BATCH, WORLD_WIDTH / 2 - 100, WORLD_HEIGHT / 2 + 350);

        SPRITE_BATCH.end();

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
