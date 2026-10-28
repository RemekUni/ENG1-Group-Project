package com.C11.game.items;

import com.C11.game.hitboxes.EventTrigger;
import com.badlogic.gdx.math.Vector2;

/**
 * The Events class extends Trigger class and allows hitboxes to be categorized as an item.
 *
 *  @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Item extends EventTrigger {
    public Item(float widthScale, float heightScale, Vector2 position, float size, boolean oneShot){
        super(widthScale, heightScale, position, size, oneShot);
    }
}
