package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * A Hitbox class that creates a rectangle around an area on the map and the sprite that
 * can be used to trigger events in the Trigger Class or for collisions.
 * It extends the Rectangle Class as the required "overlaps()" method is already defined.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class Hitbox extends Rectangle {
    private final float offsetX;
    private final float offsetY;
    private final float width;
    private final float height;
    
    /**
     * Main Hitbox constructor for actually creating the hitbox rectangle
     * 
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     * @param offsetX the horizontal distance from position to the hitbox's bottom left corner (in world units)
     * @param offsetY the vertical distance from position to the hitbox's bottom left corner (in world units)
     * @param position the owner's bottom left position, hitbox is placed at position + offset
     */
    public Hitbox(float width, float height, float offsetX, float offsetY, Vector2 position) {
        super(position.x + offsetX, position.y + offsetY, width, height);
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * This constructor is used for calculating width and height from scales and forces 0 offset.
     * This constructor should be used for things like trigger zones and hidden events where the hitbox does not need
     * offsets because these are fixed zones on the map where the position is the corner, so they have no owner to offset from.
     * 
     * @param widthScale horizontal scale of the hitbox.
     * @param heightScale vertical scale of the hitbox.
     * @param position the location of the hitbox in the map.
     * @param size base length in world units that the scales are multiplied by
     */
    public Hitbox(float widthScale, float heightScale, Vector2 position, float size) {
        this(size * widthScale, size * heightScale, 0f, 0f, position);
    }

    /**
     * This method for hitboxes is for sprites that have scaled down sizes for hitboxes and require an
     * offset to center align the hitbox along the x-axis, with no 'y' offset as we want it bottom aligned to the feet
     * of the sprite.
     * 
     * @param size the size of the sprite in world units
     * @param widthScale horizontal scale of the hitbox.
     * @param heightScale vertical scale of the hitbox.
     * @param position the bottom left position of the sprite
     * 
     * @return the entity's hitbox to be used for collisions and keeping the hitbox on its owner.
     */
    public static Hitbox forSprite(float size, float widthScale, float heightScale, Vector2 position) {
        float width = size * widthScale;
        float height = size * heightScale;
        float offsetX = (size - width) / 2f;
        return new Hitbox(width, height, offsetX, 0f, position);
    }

    /**
     * Follow method for sticking a hitbox on a moving object like entities
     * 
     * @param position the owner's current position
     */
    public void follow(Vector2 position) {
        setPosition(position.x + offsetX, position.y + offsetY);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
