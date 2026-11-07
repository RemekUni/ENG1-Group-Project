package com.C11.game.events;

import com.C11.game.entities.Player;
import com.C11.game.hitboxes.EventTrigger;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This event causes the player's speed to increase for a set amount of time
 *
 * @author james (hjp562@york.ac.uk)
 */
public class Can extends EventTrigger {
    private static final float BOOST_DURATION = 30f;

    private final Player player;

    /**
     * Create a can at the specified coordinates 
     * @param canTexture texture of the can
     * @param startX 
     * @param startY 
     * @param gameMap 
     * @param size size of can in world units 
     * @param player
     */
    public Can(TextureRegion canTexture, float startX, float startY, GameMap gameMap, float size, Player player) {
        super(canTexture, startX, startY, gameMap, size, true);
        this.player = player;
    }

    /**
     * Adds BOOST_DURATION to player's timer, and increase their boost
     */
    @Override
    public void action() {
        player.activateSpeedBoost(BOOST_DURATION);
    }
}
