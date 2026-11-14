package com.C11.game.events;

import com.C11.game.entities.Player;
import com.C11.game.entities.Goose;
import com.C11.game.hitboxes.EventTrigger;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An event where the player can make a goose leave the map.
 * The method will call the leaveMap() method on the goose.
 *
 * @author ethandavis128 (xxb522@york.ac.uk)
 */
public class LeaveMap extends EventTrigger {
    private final Goose targetGoose;

    public LeaveMap(TextureRegion texture, float startX, float startY, GameMap gameMap, float size, boolean oneShot, Goose goose) {
        super(texture, startX, startY, gameMap, size, oneShot);
        this.targetGoose = goose;
    }
    
    @Override 
    public void action() {
        targetGoose.leaveMap();
    }
}
