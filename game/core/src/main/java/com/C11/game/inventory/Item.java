package com.C11.game.inventory;

import com.badlogic.gdx.graphics.Texture;

/**
 * The text and image representation of an item in the inventory.
 *
 * @req UR_ITEMS
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class Item {

    private String name;
    private Texture texture;

    public Item(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public String toString() {
        return name;
    }
}
