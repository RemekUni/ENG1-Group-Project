package com.C11.game.events;

import com.C11.game.entities.Player;
import com.C11.game.hitboxes.EventTrigger;
import com.badlogic.gdx.math.Vector2;

/**
 * A invisible rectangle on the map that triggers once (if oneShot is set to true), the first time the
 * player walks into it. Subclasses define what happens in action().
 *
 * Planned events (not yet implemented):
 * Negative: goose jumpscare, geese steal shoes, abandoned sandwich
 * Positive: golden goose, friendly student
 *
 * @author Remigiusz Pertek (vfc515@york.ac.uk)
 */
public abstract class HiddenEvent extends EventTrigger {
    protected final Player player;

    public HiddenEvent(float startX, float startY, float widthScale, float heightScale, float size, Player player, boolean oneShot) {
        super(widthScale, heightScale, new Vector2(startX, startY), size, oneShot);
        this.player = player;
    }

    /**
     * Call once per frame. returns the trigger() value the first time the player's centre is inside
     * @return true only on the frame the event triggers
     */
    public boolean check() {
        if (!contains(player.getCenterX(), player.getCenterY())) {
            return false;
        }
        return trigger();
    }
}
