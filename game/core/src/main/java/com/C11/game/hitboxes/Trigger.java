package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * The Trigger class extends the hitbox class which defines the size and position of the boxes.
 * It provides the framework to add actions to each event/item
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Trigger extends Hitbox {
    private final boolean oneShot;//Whether the action can be done more than once.
    private boolean triggered = false;

    public Trigger(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot) {
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size);
        this.oneShot = oneShot;
    }
    public abstract void action(); //Defined in the subclasses.

    /**
     * If oneShot and hitbox has been triggered, call the action, set triggered to true, and return true.
     * 
     * @return true if action ran
     */
    public boolean trigger(){
        if (triggered && oneShot) return false;
        action();
        triggered = true;
        return true;
    }

    /**
     * Method decides if action is done forever or a player can activate it again.
     * @return boolean for if the action is completed (true) or can happen again (false).
     */
    public boolean isComplete(){
        return triggered && oneShot;
    }
}


