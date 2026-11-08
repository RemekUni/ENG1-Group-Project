package com.C11.game.screens;

import com.C11.game.events.*;
import com.C11.game.ui.TimerDisplay;
import com.C11.game.utils.GameResult;
import com.C11.game.utils.GameState;
import com.C11.game.hitboxes.EventTrigger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import java.util.concurrent.ThreadLocalRandom;

import com.C11.game.Main;
import com.C11.game.entities.Entity;
import com.C11.game.entities.Goose;
import com.C11.game.items.ItemEntity;
import com.C11.game.entities.Player;
import com.C11.game.ui.PauseMenu;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 * @author james (hjp562@york.ac.uk)
 */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final GameMap gameMap;
    private final Viewport viewport;
    private final Viewport viewportText;
    private final OrthographicCamera camera;
    private final OrthographicCamera cameraText;
    private final SpriteBatch spriteBatch;
    private final Player player;
    private final Array<Goose> geese = new Array<>();

    private final GameState gameState;

    private final Array<Entity> entities = new Array<>();
    private final Array<ItemEntity> baguettes = new Array<>();;

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
    private final Texture baguetteTexture; // TODO: Use one secitoned texture sprite for multiple item types
    private final Texture sandwichTexture;
    private final Texture canTexture;

    private boolean isPaused;

    private final PauseMenu pauseMenu;
    private final TimerDisplay timerDisplay;
    static private final int GAME_LENGTH = 30;

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
        // TODO: read spawn positions from the Tiled spawn layer and use them to initialise entities
        this.game = game;
        this.spriteBatch = game.getBatch();
        this.gameState = new GameState();
        gameMap = new GameMap("maps/campus.tmx");
        timerDisplay = new TimerDisplay("SuperNormal.ttf", GAME_LENGTH);
        timerDisplay.getTimer().start();

        playerTexture = new Texture("sprites/tile_0098.png");
        gooseTexture = new Texture("sprites/duck.png");
        baguetteTexture = new Texture("sprites/baguette.png");
        sandwichTexture = new Texture("sprites/sandwich.png");
        canTexture = new Texture("sprites/can.png");

        worldWidth = gameMap.getWorldWidth();
        worldHeight = gameMap.getWorldHeight();

        camera = new OrthographicCamera();
        cameraText = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        viewportText = new ExtendViewport(worldWidth * 20, worldHeight * 20, cameraText);

        camera.position.set(worldWidth/2f, worldHeight/2f, 0);


        player = new Player(playerTexture, worldWidth/2, worldHeight/2, gameMap, entities, baguettes);
        Goose goose = new Goose(gooseTexture, worldWidth /2 - 10, worldHeight /2, gameMap, player, entities);
        Goose goose2 = new Goose(gooseTexture, worldWidth/2 + 10, worldHeight/2, gameMap, player, entities, baguettes);
        Goose goose3 = new Goose(gooseTexture, worldWidth/2 + 3, worldHeight/2, gameMap, entities, baguettes); // stationary goose

        //Add initial 2 geese to the gameMap by adding to the array
        geese.add(goose);
        geese.add(goose2);
        geese.add(goose3);

        //Split the sandwich.png so only 1 sandwich shows on screen, not both like in the image
        int sandwichWidth = sandwichTexture.getWidth() /2;
        int sandwichHeight = sandwichTexture.getHeight();
        TextureRegion singleSandwichTexture = new TextureRegion(sandwichTexture, 0 ,0 , sandwichWidth, sandwichHeight);
        
        //Spawn in the sandwiches
        for (Vector2 position : gameMap.getSandwichPositions()){
            AbandonedSandwich sandwich = new AbandonedSandwich(singleSandwichTexture, position.x, position.y, gameMap, 1.5f, player, gooseTexture, entities, geese);
            events.add(sandwich);
        }

        //Release the CANS
        TextureRegion singleCanTexture = new TextureRegion(canTexture);
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

            ItemEntity baguette = new ItemEntity(new ItemType("baguette", baguetteTexture), gameMap, randomX, randomY, entities);
            baguettes.add(baguette);
        }

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
            if (isPaused) resumeGame();
            else pauseGame();
        }
        if (!isPaused) update(delta);
        draw();

        if (isPaused) pauseMenu.render(delta);

    }

    private void update(float delta){
        player.update(delta);
        for (Goose goose : geese) {
            goose.update(delta);
        }

        //This checks if player touches any event item (sandwich or can) and calls trigger which runs the event
        for (EventTrigger event : events){
            if (!event.isComplete() && event.overlaps(player.getHitbox())){
                event.trigger();
                eventsTriggered++; //for eventual scoring 
            }
        }

        // baguettes (TODO: remove/refactor)
        for (ItemEntity baguette : baguettes) {
            baguette.update(delta);
        }

        if (timerDisplay.getTimer().update()) {
            if (timerDisplay.getTimer().getTicks() == timerDisplay.getInitialTime()) {
                timerDisplay.getTimer().pause();  // Call pause() instead of stop() because stop() resets the tick counter and therefore resets the timer display; this is purely an aesthetic thing!
                System.out.println("TIMER OVER TIMER OVER");
                endGame();
            }
            // TEMPORARY TEST CODE: make the stationary goose leave the map after 5 seconds
            if (timerDisplay.getTimer().getTicks() == 5) {
                System.out.println("goose leaving map");
                geese.get(2).leaveMap(); // make the stationary goose leave the map after 5 seconds 
                // I don't understand why geese are stored in both entitiesArray and geese, but this code only removes the goose from entitiesArray
            }
        }

        followPlayer();
    }

    // small helper to create GameResult snapshot and show the end screen. should be used whenever anything triggers the end of the game
    private void endGame() {
        GameResult result = gameState.toResult(timerDisplay.getTimer().getTicks());
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

        // player.draw(spriteBatch);
        // for (Goose goose : geese) {
        //    goose.draw(spriteBatch);
        // }

        for (Entity entity : entities) {
            entity.draw(spriteBatch);
        }

        // baguettes (TODO: remove/refactor)
        for (ItemEntity item : baguettes) {
            item.draw(spriteBatch);
        }

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
        playerTexture.dispose();
        gooseTexture.dispose();
        pauseMenu.dispose();
        baguetteTexture.dispose();
        sandwichTexture.dispose();
        canTexture.dispose();
        timerDisplay.dispose();
    }

}
