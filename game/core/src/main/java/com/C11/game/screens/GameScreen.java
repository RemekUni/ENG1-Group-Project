package com.C11.game.screens;

import com.C11.game.world.Assets;
// import com.C11.game.utils.GameState;
import com.C11.game.events.*;
import com.C11.game.ui.TimerDisplay;
import com.C11.game.utils.GameResult;
import com.C11.game.utils.GameState;
import com.C11.game.hitboxes.EventTrigger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import java.util.concurrent.ThreadLocalRandom;

import com.C11.game.Main;
import com.C11.game.entities.EntitiesHandler;
import com.C11.game.entities.Goose;
import com.C11.game.entities.ItemEntity;
import com.C11.game.entities.Player;
import com.C11.game.ui.PauseMenu;
import com.C11.game.world.GameMap;
import com.C11.game.world.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    // private final GameState gameState;

    private boolean isPaused;
    private final PauseMenu pauseMenu;
    static private final int GAME_LENGTH = 30;
    private final TimerDisplay timerDisplay;

    private final Viewport viewport;
    private final Viewport viewportText;
    private final OrthographicCamera camera;
    private final OrthographicCamera cameraText;

    private final float worldWidth;
    private final float worldHeight;

    private EntitiesHandler entitiesHandler = new EntitiesHandler();

    private final Player player;

    private final SpriteBatch spriteBatch;

    

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

    /**
     * Loads map and textures.
     * Sets up the camera and viewport.
     * Spawns entities.
     *
     * @param game game provides the shared batch
     */
    public GameScreen(Main game) {
        // TODO: read spawn positions from the Tiled spawn layer and use them to
        // initialise entities
        
        // -- Game, assets, world initialisation --
        this.game = game;
        this.spriteBatch = game.getBatch();
        // this.gameState = new GameState();
        gameMap = new GameMap("maps/campus.tmx");
        timerDisplay = new TimerDisplay(Assets.TIMER_FONT, GAME_LENGTH);
        timerDisplay.getTimer().start();

        // Load assets. Loaded once and then shared by entities. Disposed in #dispose().
        // TODO: Use one secitoned texture sprite for multiple item types
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
        viewportText = new ExtendViewport(worldWidth * 20, worldHeight * 20, cameraText);

        // -- Spawn entities --
        // Spawn player
        player = new Player(Assets.PLAYER_TEXTURE, worldWidth / 2, worldHeight / 2, gameMap, entitiesHandler);
        player.enableCollection();
        
        // Spawn *2* geese
        new Goose(Assets.GOOSE_TEXTURE, worldWidth / 2 - 10, worldHeight / 2, gameMap, player, entitiesHandler);
        final Goose goose2 = new Goose(Assets.GOOSE_TEXTURE, worldWidth / 2 + 10, worldHeight / 2, gameMap, player, entitiesHandler);
        goose2.enableCollection();
       

        //Split the sandwich.png so only 1 sandwich shows on screen, not both like in the image
        int sandwichWidth = Assets.SANDWICH_TEXTURE.getWidth() /2;
        int sandwichHeight = Assets.SANDWICH_TEXTURE.getHeight();
        TextureRegion singleSandwichTexture = new TextureRegion(Assets.SANDWICH_TEXTURE, 0 ,0 , sandwichWidth, sandwichHeight);
        
        //Spawn in the sandwiches
        for (Vector2 position : gameMap.getSandwichPositions()){
            AbandonedSandwich sandwich = new AbandonedSandwich(singleSandwichTexture, position.x, position.y, gameMap, 1.5f, player, Assets.GOOSE_TEXTURE, entitiesHandler);
            events.add(sandwich);
        }

        //Release the CANS
        TextureRegion singleCanTexture = new TextureRegion(Assets.CAN_TEXTURE);
        for (Vector2 position : gameMap.getCanPositions()){
            Can can = new Can(singleCanTexture, position.x, position.y, gameMap, 1, player);
            events.add(can);
        }

        // temp/dev (TODO: remove/refactor)
        int baguetteCount = 50;
        for (int i = 0; i < baguetteCount; i++) {
            int randomX = ThreadLocalRandom.current().nextInt((int) 0, (int) worldWidth);
            int randomY = ThreadLocalRandom.current().nextInt((int) 0, (int) worldHeight);
            //TODO rename all referances to "item" to bagguette 

            ItemEntity itemType1 = new ItemEntity(new Item("baguette", Assets.ITEM_TEXTURE), gameMap, randomX, randomY, entitiesHandler);
        }
        // ItemEntity itemType1 = new ItemEntity(new Item("baguette", Assets.ITEM_TEXTURE), gameMap, player.getPosition().x, player.getPosition().y + 4, entitiesHandler);


        pauseMenu = new PauseMenu(() -> resumeGame(), () -> Gdx.app.exit());
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

    }

    private void update(float delta){
        entitiesHandler.updateEntities(delta);

        // iterates through all events then records a gameState counter for them if they fired
        for (EventTrigger event : events) {
            if (event.overlaps(player.getHitbox()) && event.trigger()) {
                GameState.recordEventTriggered();
            }
        }


        if (timerDisplay.getTimer().update()) {
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
        GameResult result = GameState.toResult(timerDisplay.getTimer().getTicks());
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

        timerDisplay.draw(spriteBatch, worldWidth / 2 - 100, worldHeight / 2 + 350);

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
        Assets.dispose();
    }

}
