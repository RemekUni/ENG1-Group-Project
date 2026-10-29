package com.C11.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.C11.game.world.GameMap;

/**
 * The player-controlled sprite. Moves with WASD.
 * Collision is handled by Entity
 * 
 * @req UR_CHARACTER FR_PLAYER_BOUNDARY
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class Player extends CollectorEntity{
    private final Vector2 direction = new Vector2();

    // tiles per second
    private static final float PLAYER_SPEED = 4f;

    // Hitbox covers lower half of the sprite and is 2/3 of its width.
    // This lets the player get between 1 tile spaces more easily and looks better when approaching collision objects such as buildings and trees. 
    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;

    /**
     * Creates an entity for the player to control
     * 
     * @param playerTexture texture of the player
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     */
    public Player(Texture playerTexture, float startX, float startY, GameMap gameMap, Array<Entity> entitiesArray) {
        super(playerTexture, gameMap, startX, startY, PLAYER_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, entitiesArray);
    }

    // Opposite keys cancel out; x and y can both be set for diagonals. Entity.move() normalises direction so diagonal movement is not faster
    // TODO: map keys to actions, not hardcoded
    @Override 
    public void update(float delta) {
        direction.set(0, 0);
        
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction.x += 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction.x -= 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction.y -= 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            direction.y += 1;
        }

        move(direction, delta);
    }

    public void throwItem() {

    }

    private void startAim() {

    }
}
