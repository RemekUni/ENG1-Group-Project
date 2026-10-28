package com.C11.game.events;

import com.C11.game.hitboxes.EventTrigger;
import com.badlogic.gdx.math.Vector2;

/**
 * An event where the player collects an abandoned sandwich and increases number of geese chasing.
 */
public class AbandonedSandwich extends EventTrigger {

    public AbandonedSandwich(float widthScale, float heightScale, Vector2 position, float size, boolean oneShot) {
        super(widthScale, heightScale, position, size, oneShot);


    }
    @Override
    public void action() {

    }
}
