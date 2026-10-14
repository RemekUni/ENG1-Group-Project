package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * The Trigger class extends the hitbox class which defines the size and position of the boxes.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Trigger extends Hitbox {
    public final boolean oneShot;//Whether the action can be done more than once.

    public Trigger(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot) {
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size);
        this.oneShot = oneShot;
    }

    public boolean oneShot(){
        return oneShot;
    }

}


