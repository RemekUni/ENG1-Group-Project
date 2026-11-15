package com.C11.game.screens;

import com.C11.game.world.Assets;
import com.C11.game.events.*;
import com.C11.game.ui.TimerDisplay;
import com.C11.game.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import java.util.concurrent.ThreadLocalRandom;

import com.C11.game.Main;
import com.C11.game.HUDController;
import com.C11.game.entities.AEntity;
import com.C11.game.entities.EntitiesHandler;
import com.C11.game.entities.Goose;
import com.C11.game.entities.ItemEntity;
import com.C11.game.entities.Player;
import com.C11.game.ui.HUD;
import com.C11.game.ui.Hotbar;
import com.C11.game.ui.PauseMenu;
import com.C11.game.world.GameMap;
import com.C11.game.stats.GameResult;
import com.C11.game.stats.GameStats;
import com.C11.game.inventory.Item;
import com.C11.game.world.SimplifiedMapObject;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;
import java.util.List;

/**
 * Main gameplay screen. It owns the map, camera, viewport, entities and their respective textures.
 * Each frame runs update(delta), then draw(). These are separate to allow for game pausing to only skip updates but keep drawing the paused game screen.
 *
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 * @author james (hjp562@york.ac.uk)
 */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final GameMap gameMap;

    private boolean isPaused;
    private final PauseMenu pauseMenu;
    private HUD hud;
    
    private final TimerDisplay timerDisplay;

    private final Viewport viewport;
    private final Viewport viewportText;
    private final OrthographicCamera camera;
    private final OrthographicCamera cameraText;

    private final float worldWidth;
    private final float worldHeight;

    private EntitiesHandler entitiesHandler = new EntitiesHandler();

    private final Player player;
    private final HUDController playerController;

    private final SpriteBatch spriteBatch;

    private final Skin skin;

    /**
     * Minimum visible area in world units (tiles).
     * ExtendViewport may show more in the visible area on non 16:9 screens.
     */
    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;

    // private final Texture playerTexture;
    // private final Texture gooseTexture;
    // private final Texture itemTexture1;
    // private final Texture sandwichTexture;

    // Hidden events on the map, checked every frame in update()
    private final List<EventTrigger> events = new ArrayList<>();
    private int eventsTriggered = 0;

    private boolean wave1 = true;
    private boolean wave2 = true;
    private boolean wave3 = true;


    /**
     * Loads map and textures.
     * Sets up the camera and viewport.
     * Spawns entities.
     *
     * @param game game provides the shared batch
     */
    public GameScreen(Main game, int gameLengthSeconds) {
        // -- Game, assets, world initialisation --
        this.game = game;
        this.spriteBatch = game.getBatch();
        this.skin = game.getSkin();
        GameStats.reset();
        gameMap = new GameMap("maps/campus.tmx");

        timerDisplay = new TimerDisplay(Assets.TIMER_FONT, gameLengthSeconds);
        timerDisplay.getTimer().start();

        // Load assets. Loaded once and then shared by entities. Disposed in #dispose(). 
        // TODO: See #Assets javadoc
        new Assets();

        // World dimenstions
        worldWidth = gameMap.getWorldWidth();
        worldHeight = gameMap.getWorldHeight();

        // Camera setup
        camera = new OrthographicCamera();
        cameraText = new OrthographicCamera();
        camera.position.set(worldWidth/2f, worldHeight/2f, 0);

        // Viewport
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        int viewport_scale = 20;  // Text viewport must be larger than the usual viewport to ensure that text is a reasonable size and resolution.
        viewportText = new ExtendViewport(worldWidth * viewport_scale, worldHeight * viewport_scale, cameraText);

        // -- Spawn entities --
        SimplifiedMapObject playerObject = null;
        for (SimplifiedMapObject mapObject : gameMap.getSpawnObjects()) {
            if ("player".equals(mapObject.name())) {
                playerObject = mapObject;
                break;
            }
        }

        if (playerObject == null) {
            throw new IllegalStateException("Player spawn was not found.");
        }

        // Spawn player
        player = new Player(Assets.PLAYER_TEXTURE, playerObject.position().x, playerObject.position().y, gameMap, entitiesHandler);
        player.enableCollection();

        for (SimplifiedMapObject mapObject : gameMap.getSpawnObjects()) {
            // Spawn geese
            if ("goose".equals(mapObject.name())) {
                Goose goose;
                boolean leaveMap = mapObject.properties().get("leaveMap", false, Boolean.class);
                boolean itemCollection = mapObject.properties().get("itemCollection", false, Boolean.class);
                if (leaveMap) {
                    goose = new Goose(Assets.GOOSE_TEXTURE, mapObject.position().x, mapObject.position().y, gameMap, entitiesHandler);
                    // TEMP: button to make the stationary goose just created leave the map
                    TextureRegion singleButtonTexture = new TextureRegion(Assets.BUTTON_TEXTURE);
                    LeaveMap leaveMapEvent = new LeaveMap(singleButtonTexture, mapObject.position().x - 3, mapObject.position().y, gameMap, 1.5f, true, goose);
                    events.add(leaveMapEvent);
                } else {
                    goose = new Goose(Assets.GOOSE_TEXTURE, mapObject.position().x, mapObject.position().y, gameMap, player, entitiesHandler);
                }
                if (itemCollection) {
                    goose.enableCollection();
                }
            }
        }

        // Split the sandwich.png so only 1 sandwich shows on screen, not both like in the image
        

        // Spawn in the sandwiches
        for (Vector2 position : gameMap.getSandwichPositions()){
            ItemEntity spoiledSandwich = new ItemEntity(Assets.SPOILED_SANDWICH_ITEM, gameMap, position.x, position.y, entitiesHandler);
            spoiledSandwich.setTriggerAction(() -> Config.runSpoiledSandwichAction(player, gameMap, entitiesHandler));
        }

        // Release the CANS
        for (Vector2 position : gameMap.getCanPositions()){
            ItemEntity can = new ItemEntity(Assets.CAN_ITEM, gameMap, position.x, position.y, entitiesHandler);
            can.setTriggerAction(() -> Config.runCanAction(player));
            // Can can = new Can(singleCanTexture, position.x, position.y, gameMap, 1, player);
        }

        // temp/dev (TODO: remove)
        int baguetteCount = 10;
        for (int i = 0; i < baguetteCount; i++) {
            int randomX = ThreadLocalRandom.current().nextInt((int) 0, (int) worldWidth);
            int randomY = ThreadLocalRandom.current().nextInt((int) 0, (int) worldHeight);

            new ItemEntity(Assets.BAGUETTE_ITEM, gameMap, randomX, randomY, entitiesHandler);
        }
        // ItemEntity itemType1 = new ItemEntity(new Item("baguette", Assets.ITEM_TEXTURE), gameMap, player.getPosition().x, player.getPosition().y + 4, entitiesHandler);

        Hotbar hotbar = new Hotbar(player.getInventory());
        hud = new HUD(hotbar);
        playerController = new HUDController(player, hotbar);
        pauseMenu = new PauseMenu(skin, () -> resumeGame(), () -> Gdx.app.exit());
    }

    // pauses at the start of the game as required
    /** @req FR_PAUSE */
    @Override
    public void show() {
        

        pauseGame();
    }

    // false: do not recentre the camera, it is supposed to follow the player instead
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        viewportText.update(width, height, false);
        followPlayer();
        hud.resize(width, height);
        pauseMenu.resize(width, height);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (isPaused)
                resumeGame();
            else
                pauseGame();
        }
        if (!isPaused)
            update(delta);
        draw();

        if (isPaused)
            pauseMenu.render(delta);

        hud.render(delta);


    }

    private void update(float delta){
        entitiesHandler.updateEntities(delta);


        // iterates through all events then records a gameState counter for them if they fired
        for (EventTrigger event : events) {
            if (event.isTriggerEnabled() && event.getHitbox().overlaps(player.getHitbox()) && event.trigger()) {
                GameStats.recordEventTriggered();
            }
        }

        for (AEntity entity : entitiesHandler.getEntitiesSet().iterator().toArray()) {
            if (entity != player && entity.isTriggerEnabled() && entity.getHitbox().overlaps(player.getHitbox()) && entity.trigger()) {
                GameStats.recordEventTriggered();
                if (entity.isComplete()) {
                    entitiesHandler.removeEntity(entity);
                }
            }
        }


        if (timerDisplay.getTimer().update()) {
            if (timerDisplay.getTimer().getTicks() == 10 && wave1) {
                wave1 = false;
                for (Vector2 position : gameMap.getWaveSpawnPositions("wave1")) {
                    new Goose(Assets.GOOSE_TEXTURE, position.x, position.y, gameMap, player, entitiesHandler);
                }
            }
            if (timerDisplay.getTimer().getTicks() == 20 && wave2) {
                wave2 = false;
                for (Vector2 position : gameMap.getWaveSpawnPositions("wave2")) {
                    new Goose(Assets.GOOSE_TEXTURE, position.x, position.y, gameMap, player, entitiesHandler);
                }
            }
            if (timerDisplay.getTimer().getTicks() == 30 && wave3) {
                wave3 = false;
                for (Vector2 position : gameMap.getWaveSpawnPositions("wave3")) {
                    new Goose(Assets.GOOSE_TEXTURE, position.x, position.y, gameMap, player, entitiesHandler);
                }
            }
            if (timerDisplay.getTimer().getTicks() == timerDisplay.getInitialTime()) {
                timerDisplay.getTimer().pause();  // Call pause() instead of stop() because stop() resets the tick counter and therefore resets the timer display; this is purely an aesthetic thing!
                System.out.println("TIMER OVER TIMER OVER");
                endGame();
            }

        }

        followPlayer();
    }

    // small helper to create GameResult snapshot and show the end screen. should be used whenever anything triggers the end of the game
    private void endGame() {
        GameResult result = GameStats.toResult(timerDisplay.getTimer().getTicks());
        game.showEndScreen(result);
    }

    /** @req FR_PAUSE */
    private void pauseGame() {
        isPaused = true;
        timerDisplay.getTimer().pause();
        pauseMenu.show();
    }

    private void resumeGame() {
        isPaused = false;
        timerDisplay.getTimer().unpause();
        pauseMenu.hide();
        Gdx.input.setInputProcessor(playerController);
    }

    private void followPlayer() {
        // Follows the player's center, updated every frame
        viewport.getCamera().position.set(player.getPosition().x + player.getWidth() / 2,
                player.getPosition().y + player.getHeight() / 2, 0);
        // Clamps the camera so that it stays within the map borders
        viewport.getCamera().position.set(
                MathUtils.clamp(camera.position.x, viewport.getWorldWidth() / 2,
                        worldWidth - viewport.getWorldWidth() / 2),
                MathUtils.clamp(camera.position.y, viewport.getWorldHeight() / 2,
                        worldHeight - viewport.getWorldHeight() / 2),
                0);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        camera.update();

        gameMap.render(camera);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        entitiesHandler.draw(spriteBatch);

        for (EventTrigger event : events) {
            event.draw(spriteBatch);
        }

        spriteBatch.end();

        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        //                                  RENDER ALL TEXT HERE!
        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

        viewportText.apply();
        spriteBatch.setProjectionMatrix(cameraText.combined);

        spriteBatch.begin();

        timerDisplay.draw(spriteBatch, worldWidth / 2 + Config.TIMER_OFFSET_X, worldHeight / 2 + Config.TIMER_OFFSET_Y);

        spriteBatch.end();
    }

    @Override
    public void pause() {
        pauseGame();
    }

    @Override
    public void dispose() {
        gameMap.dispose();
        pauseMenu.dispose();
        timerDisplay.dispose();
    }

}
