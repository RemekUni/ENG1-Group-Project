package com.C11.game.hitboxes;

import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The Trigger class extends the hitbox class which defines the size and position of the boxes.
 * It provides the framework to add actions to each event/item
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class EventTrigger extends Hitbox {
    protected boolean oneShot;//Whether the action can be done more than once.
    protected boolean triggered = false;
    protected Vector2 position;
    protected float size;
    protected Texture texture;

    public EventTrigger(Texture texture, float startX, float startY, GameMap gameMap, float size, boolean oneShot) {
        super(1.0f, 1.0f, new Vector2(startX, startY), size);
        this.texture = texture;
        this.position = new Vector2(startX, startY);
        this.size = size;
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


