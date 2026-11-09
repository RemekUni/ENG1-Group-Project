package com.C11.game.entities;

import com.C11.game.hitboxes.Hitbox;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract base class for anything that moves on the map (like player or geese). Handles position, drawing, and collision-aware movement.
 * Subclasses decide which way to move in update() and then call move().
 * Entity does not own its texture, so it does not dispose it, the creator does
 * 
 * @req UR_BOUNDARIES
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public abstract class AEntity {
    private final Vector2 position;
    // private final Vector2 direction = new Vector2();

    private float size;
    private float speed;
    private Texture entityTexture;

    private final GameMap gameMap;

    private final Hitbox hitbox;

    protected EntitiesHandler entitiesHandler;

    /**
     * Creates an entity at the given position.
     * 
     * @param entityTexture sprite texture (shared), not disposed by Entity
     * @param gameMap map used for collision checks
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param speed movement speed in tiles per second
     * @param hitboxWidthScale hitbox width as a fraction of the sprite width (centered)
     * @param hitboxHeightScale same as width, but for height (bottom-aligned)
     * @param entitiesArray reference to dynamic array of all entities
     */
    public AEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, EntitiesHandler entitiesHandler) {
        this.entityTexture = entityTexture;
        this.gameMap = gameMap;
        this.speed = speed;
        this.entitiesHandler = entitiesHandler;

        position = new Vector2(startX, startY);

        // This is assuming all entities are square. will need to be changed if we add non-square entities
        size = entityTexture.getWidth() * gameMap.getUnitScale();

        hitbox = Hitbox.forSprite(size, hitboxWidthScale, hitboxHeightScale, position);

        // Add to Entities set
        entitiesHandler.add(this);
        entitiesHandler.updateCategories();
    }

    /**
     * Moves entity in the given direction at its current speed, stopping at map collisions.
     * X and Y are resolved separately so entities slide against walls instead of sticking (just makes movement smoother in general)
     * 
     * Note: normalises direction in-place, which means the callers vector will get changed
     * 
     * @req FR_PLAYER_BOUNDARY FR_GEESE_BOUNDARY
     * 
     * @param direction vector direction for entity to move
     * @param delta time in seconds from last frame
     */
    protected void move(Vector2 direction, float delta) {
        direction.nor();
        
        float dx = direction.x * speed * delta;
        float dy = direction.y * speed * delta;

        position.x += dx;
        if (isColliding()) {
            position.x -=dx;
        }

        position.y += dy;
        if (isColliding()) {
            position.y -=dy;
        }

        hitbox.follow(position);
    }

    /** 
     * Called once per frame, subclasses decide movement and behaviour here.
     * @param delta seconds since last frame 
     */
    public abstract void update(float delta);

    // Check for hitbox collisions on the map after first snapping the hitbox to its entity
    private boolean isColliding() {
        hitbox.follow(position);
        return gameMap.isBlocked(hitbox);
    }

    /** 
     * Draw the entityTexture using the shared spriteBatch
     * 
     * @param batch the shared spriteBatch
     */
    public void draw(SpriteBatch batch) {
        batch.draw(entityTexture, position.x, position.y, size, size);
    }

    /** @return current bottom-left vector position in world units */
    public Vector2 getPosition() {
        return position;
    }

    /** @return centre x of the sprite in world units */
    public float getCenterX() {
        return position.x + size / 2f;
    }

    /** @return centre y of the sprite in world units */
    public float getCenterY() {
        return position.y + size / 2f;
    }

    /** @return entity texture width in world units */
    public float getWidth() {
        return size;
    }

    /** @return entity texture height in world units */
    public float getHeight() {
        return size;
    }

    /** @return the entity's collision hitbox in world units, updated by move(). Treat as read-only */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /** @return the entity's current rate of speed in world unites  */
    public float getSpeed(){
        return speed;
    }
    /** @param speed set the speed to the desired speed */
    public void setSpeed(float speed){
        this.speed = speed;
    }
}
