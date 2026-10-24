package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * An abstract Hitbox class that creates a rectangle around an area on the map and the sprite that
 * can be used to trigger events in the Trigger Class.
 * It extends the Rectangle Class as the required "overlaps()" method is already defined.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Hitbox extends Rectangle {
    /**
     *
     * @param HITBOX_WIDTH_SCALE horizontal scale of the hitbox.
     * @param HITBOX_HEIGHT_SCALE vertical scale of the hitbox.
     * @param position the location of the hitbox in the map.
     */
    public Hitbox(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size) {
        super(position.x, position.y, size * HITBOX_WIDTH_SCALE, size * HITBOX_HEIGHT_SCALE);
    }
}

