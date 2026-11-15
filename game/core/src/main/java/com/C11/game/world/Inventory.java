package com.C11.game.world;

import com.badlogic.gdx.utils.Array;

public class Inventory {
    private final Array<Item> inventory = new Array<>();
    public final int MAX_INVENTORY_SIZE = 2;

    public Inventory() {

    }

    public Array<Item> getContainer() {
        return inventory;
    }

    // public int getMaxInventorySize() {
    //     return MAX_INVENTORY_SIZE;
    // }
}
