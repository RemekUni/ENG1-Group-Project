package com.C11.game.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    public Item(String name, TextureRegion textureRegion) {
        this.name = name;
        this.texture = textureRegion.getTexture();
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
