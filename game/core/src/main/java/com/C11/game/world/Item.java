package com.C11.game.world;

import com.badlogic.gdx.graphics.Texture;

/**
 * @req UR_INVENTORY
 * 
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
