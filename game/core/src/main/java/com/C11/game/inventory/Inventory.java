package com.C11.game.inventory;

import com.C11.game.world.Assets;
import com.badlogic.gdx.utils.Array;

/**
 * Contains an array of items to be used as an inventory.
 *
 * @req FR_INVENTORY
 *
 * @author @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class Inventory {
    private final Array<Item> inventory = new Array<>();

    public Inventory() {
        inventory.add(new Item("baguette", Assets.BAGUETTE_TEXTURE));
        inventory.add(new Item("baguette", Assets.BAGUETTE_TEXTURE));
    }
    public Array<Item> getContainer() {
        return inventory;
    }
}
