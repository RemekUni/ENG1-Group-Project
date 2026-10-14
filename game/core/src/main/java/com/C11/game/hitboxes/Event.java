package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * The Events class extends Trigger class and allows hitboxes to be categorized as a event.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Event extends Trigger{
    public Event(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot){
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size, oneShot);
    }
}
