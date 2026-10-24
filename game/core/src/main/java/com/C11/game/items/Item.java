package com.C11.game.items;

import com.C11.game.hitboxes.Trigger;
import com.badlogic.gdx.math.Vector2;

/**
 * The Events class extends Trigger class and allows hitboxes to be categorized as an item.
 *
 *  @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Item extends Trigger{
    public Item(float HITBOX_WIDTH_SCALE, float HITBOX_HEIGHT_SCALE, Vector2 position, float size, boolean oneShot){
        super(HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, position, size, oneShot);
    }
}
