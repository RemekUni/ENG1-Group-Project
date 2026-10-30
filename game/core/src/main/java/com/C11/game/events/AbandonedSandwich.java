package com.C11.game.events;

import com.C11.game.hitboxes.EventTrigger;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * An event where the player collects an abandoned sandwich and increases number of geese chasing.
 * The method will new geese to follow the player.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class AbandonedSandwich extends EventTrigger {

    public AbandonedSandwich(TextureRegion sandwichTexture, float startX, float startY, GameMap gameMap, float size) {
        super(sandwichTexture, startX, startY, gameMap, size, true); //You can only do this event once
    }

    /**
     * If a collision between player hitbox and sandwich hitbox, spawn more new geese to follow the player.
     */
    @Override
    public void action() {
        System.out.println("Abandoned sandwich triggered, more geese start following");
        //toDo: Add the goose spawn logic and alert player here...
    }
}
