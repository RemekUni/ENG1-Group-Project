package com.C11.game.items;

import com.badlogic.gdx.math.Vector2;

/**
 * A class that inherits the Hitbox abstract class to allow different functionalities
 * for each hitbox. This class will represent a hidden item the player can pick up.
 * 
 * @req FR_ITEMS
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class HiddenItem extends Item {
    public HiddenItem(float widthScale, float heightScale, Vector2 position, float size, boolean oneShot){
        super(widthScale, heightScale, position, size, oneShot);
    }

    @Override
    public void action(){
        //Collect an item
        System.out.println("Item collected!");
    }
}
