package com.C11.game.inventory;

import com.C11.game.world.Assets;
import com.badlogic.gdx.utils.Array;

public class Inventory {
    private final Array<Item> inventory = new Array<>();

    public Inventory() {
        inventory.add(new Item("baguette", Assets.ITEM_TEXTURE));
        inventory.add(new Item("baguette", Assets.ITEM_TEXTURE));
    }
    public Array<Item> getContainer() {
        return inventory;
    }
}
