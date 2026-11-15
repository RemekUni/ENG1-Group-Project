package com.C11.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.C11.game.Config;
import com.C11.game.world.GameMap;

/**
 * A goose that chases a given target and stops when close.
 *
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class Goose extends AEntity {
    private final Vector2 direction = new Vector2();

    private boolean leavingMap = false;

    // Can be any target, not just the player, so later on the geese can chase a distraction too
    // TODO: the goose moving towards something was just a test i made to see how a goose could behave, this can later be made optional or removed entirely
    private AEntity target;

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
    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, AEntity target, EntitiesHandler entitiesHandler) {
        super(gooseTexture, gameMap, startX, startY, Config.GOOSE_SPEED, Config.HITBOX_WIDTH_SCALE, Config.HITBOX_HEIGHT_SCALE, entitiesHandler);
        this.target = target;
    }

    /**
     * Alternate constructor for a goose that chases itself (i.e. doesn't move)
     * 
     * @param gooseTexture texture of the goose
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     * @param entitiesArray reference to dynamic array of all entities
     */
    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, EntitiesHandler entitiesHandler) {
        super(gooseTexture, gameMap, startX, startY, Config.GOOSE_SPEED, Config.HITBOX_WIDTH_SCALE, Config.HITBOX_HEIGHT_SCALE, entitiesHandler);
        this.target = this;
    }

    @Override
    public void update(float delta) {
        boolean collide = true; // When the goose is leaving the map, it will not collide with anything
        // If leavingMap is true, this will make it move left instead of towards the target
        if (leavingMap) {
            collide = false;
            direction.set(-100, 0);
            if (getCenterX() < 5) {
                // remove the goose from the game when it reaches the edge of the map
                entitiesHandler.removeEntity(this);
            }
        } 
        else {
            direction.set(this.target.getCenterX(), this.target.getCenterY()).sub(getCenterX(), getCenterY());
        }
        
        if (direction.len() >= Config.GOOSE_STOP_DISTANCE) {
            move(direction, delta, collide);
        }
    }

    public void setTarget(AEntity newTarget) {
        this.target = newTarget;
    }

    public void setStaionary() {
        setTarget(this);
    }

    /**
     * Makes the goose leave the map with no collision checks
     */
    public void leaveMap() {
        leavingMap = true;
    }
}
