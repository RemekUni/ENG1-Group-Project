package com.C11.game.inventory;

import com.badlogic.gdx.utils.Array;

public class Inventory {
    private final Array<Item> inventory = new Array<>();

    public Inventory() {

    }

    public Array<Item> getContainer() {
        return inventory;
    }
}
