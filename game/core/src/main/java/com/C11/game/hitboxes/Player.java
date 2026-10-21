package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * A class that inherits the Hitbox abstract class to allow different functionalities
 * for each hitbox. This box will be around the playable character
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class Player extends Hitbox {
    public Player (float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size) {
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size);
    }
}
