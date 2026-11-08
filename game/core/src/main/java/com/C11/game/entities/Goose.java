package com.C11.game.entities;

import com.C11.game.items.ItemEntity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.C11.game.world.GameMap;

/**
 * A goose that chases a given target and stops when close.
 *
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class Goose extends CollectorEntity {
    private final Vector2 direction = new Vector2();

    // tiles per second
    private static final float GOOSE_SPEED = 3f;

    // same as player
    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;

    /** Centre to centre distance, in tiles, at which the goose will stop approaching its target */
    private static final float STOP_DISTANCE = 1.5f;

    // Can be any target, not just the player, so later on the geese can chase a distraction too
    // TODO: the goose moving towards something was just a test i made to see how a goose could behave, this can later be made optional or removed entirely
    private final Entity target;

    private boolean leaveMap = false;

    /**
     * Creates a goose that chases a target, collection <b>off</b> by default
     *
     * @param gooseTexture texture of the goose
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     * @param target the entity this goose moves towards
     * @param entitiesArray reference to dynamic array of all entities
     */
    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, Entity target, Array<Entity> entitiesArray) {
        super(gooseTexture, gameMap, startX, startY, GOOSE_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, entitiesArray);
        this.target = target;
    }

    /**
     * Creates a goose that chases a target, collection <b>on</b> by default
     *
     * @param gooseTexture texture of the goose
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     * @param target the entity this goose moves towards
     * @param entitiesArray reference to dynamic array of all entities
     * @param itemsArray dynamic array of all items on the map
     */
    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, Entity target, Array<Entity> entitiesArray, Array<ItemEntity> itemsArray) {
        super(gooseTexture, gameMap, startX, startY, GOOSE_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, entitiesArray, itemsArray);
        this.target = target;
    }

    /**
     * Creates a goose that does not move, collection <b>on</b> by default
     *
     * @param gooseTexture texture of the goose
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     * @param entitiesArray reference to dynamic array of all entities
     * @param itemsArray dynamic array of all items on the map
     */
    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, Array<Entity> entitiesArray, Array<ItemEntity> itemsArray) {
        super(gooseTexture, gameMap, startX, startY, GOOSE_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, entitiesArray, itemsArray);
        this.target = this; // the goose will not move anywhere since it is its own target
    }

    @Override
    public void update(float delta) {
        if (leaveMap) {
            direction.set(-100, 0); // move left to leave the map
            if (getCenterX() < 5) {
                // remove the goose from the game when it leaves the map
                this.remove();
            }
        } 
        else {
            direction.set(this.target.getCenterX(), this.target.getCenterY()).sub(getCenterX(), getCenterY());
        }
        if (direction.len() >= STOP_DISTANCE) {
            move(direction, delta);
        }
    }

    public void leaveMap() {
        this.leaveMap = true;
    }
}
