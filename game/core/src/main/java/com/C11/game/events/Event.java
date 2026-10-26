package com.C11.game.events;

import com.C11.game.hitboxes.Trigger;
import com.badlogic.gdx.math.Vector2;

/**
 * The Events class extends Trigger class and allows hitboxes to be categorized as a event.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Event extends Trigger{
    public Event(float widthScale, float heightScale, Vector2 position, float size, boolean oneShot){
        super(widthScale, heightScale, position, size, oneShot);
    }
}
