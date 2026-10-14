package com.C11.game.hitboxes;

import com.badlogic.gdx.math.Vector2;

/**
 * The Item class extends Trigger class and defines hidden collectible items.
 */
public abstract class Item extends Trigger{
    public Item(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot){
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size, oneShot);
    }
    public void action(){
        //Collect something, if oneShot true, only once
    }
}
