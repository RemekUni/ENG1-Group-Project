package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * A class that inherits the Hitbox abstract class to allow different functionalities
 * for each hitbox. This class will represent a hidden item the player can pick up.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class HiddenItem extends Item {
    public HiddenItem(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot){
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size, oneShot);
    }

    @Override
    public void action(){
        //Collect an item
        System.out.println("Item collected!");
    }
}
