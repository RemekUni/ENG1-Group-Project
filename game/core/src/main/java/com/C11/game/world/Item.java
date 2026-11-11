package com.C11.game.world;

import com.badlogic.gdx.graphics.Texture;

/**
 * @req UR_INVENTORY
 * 
 */
public class Item {

    private String name;
    private Texture entityTexture;

    public Item(String name, Texture entityTexture) {
        this.name = name;
        this.entityTexture = entityTexture;
    }

    public String getName() {
        return name;
    }

    public Texture getEntityTexture() {
        return entityTexture;
    }

    public String toString() {
        return name;
    }
}
