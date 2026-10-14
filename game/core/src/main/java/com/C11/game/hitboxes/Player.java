package com.C11.game.hitboxes;

/**
 * A class that inherits the Hitbox abstract class to allow different functionalities
 * for each hitbox. This box will be around the playable character
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class Player extends Hitbox {
    public Player(float x, float y, float width, float height){
        super(x, y, width, height);
    }
}
