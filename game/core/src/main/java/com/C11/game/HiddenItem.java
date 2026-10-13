package com.C11.game;

/**
 * A class that inherits the Hitbox abstract class to allow different functionalities
 * for each hitbox. This class will represent a hidden item the player can pick up.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public class HiddenItem extends Hitbox{
    public HiddenItem(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    @Override
    public void onHit(){
        //Collect an item
    }
}
