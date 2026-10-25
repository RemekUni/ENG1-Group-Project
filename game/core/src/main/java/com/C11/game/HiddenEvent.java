package com.C11.game;

import com.C11.game.entities.Player;

/**
 * A invisible rectangle on the map that triggers once, the first time the
 * player walks into it. Subclasses define what happens in onHit().
 *
 * Planned events (not yet implemented):
 * Negative: goose jumpscare, geese steal shoes, abandoned sandwich
 * Positive: golden goose, friendly student
 *
 * @author Remigiusz Pertek (vfc515@york.ac.uk)
 */
public abstract class HiddenEvent extends Hitbox {
    private boolean triggered = false;
    protected final Player player;

    public HiddenEvent(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
    }

    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Call once per frame. Fires onHit() the first time the player's centre is inside
     * @return true only on the fram the event triggers
     */
    public boolean check() {
        if (!triggered && contains(player.getCenterX(), player.getCenterY())) {
            triggered = true;
            onHit();
            return true;
        }
        return false;
    }
}