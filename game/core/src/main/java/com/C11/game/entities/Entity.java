package com.C11.game.entities;

import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Base class for anything that moves on the map (like player or geese). Handles position, drawing, and collision-aware movement.
 * Subclasses decide which way to move in update() and then call move().
 * Entity does not own its texture, so it does not dispose it, the creator does
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public abstract class Entity {
    private final Vector2 position;
    private float size;
    private float speed;
    private Texture entityTexture;

    private final GameMap gameMap;
    private final Rectangle bounds;

    private final float hitboxWidth;
    private final float hitboxHeight;
    private final float hitboxOffsetX;

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
     */
    public Entity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale) {
        this.entityTexture = entityTexture;
        this.gameMap = gameMap;
        this.speed = speed;

        position = new Vector2(startX, startY);

        // This is assuming all entities are square. will need to be changed if we add non-square entities
        size = entityTexture.getWidth() * gameMap.getUnitScale();

        hitboxWidth = size * hitboxWidthScale;
        hitboxHeight = size * hitboxHeightScale;
        hitboxOffsetX = (size - hitboxWidth) / 2f;

        bounds = new Rectangle();
    }

    /**
     * Moves entity in the given direction at its current speed, stopping at map collisions.
     * X and Y are resolved separately so entities slide against walls instead of sticking (just makes movement smoother in general)
     * 
     * Note: normalises direction in-place, which means the callers vector will get changed
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
    }

    /** 
     * Called once per frame, subclasses decide movement and behaviour here.
     * @param delta seconds since last frame 
     */
    public abstract void update(float delta);

    // Hitbox is centered horizontally and aligned to the bottom of the sprite
    private boolean isColliding() {
        bounds.set(position.x + hitboxOffsetX, position.y, hitboxWidth, hitboxHeight);
        return gameMap.isBlocked(bounds);
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
    
}
