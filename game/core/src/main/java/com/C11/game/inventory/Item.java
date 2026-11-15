package com.C11.game.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @req UR_INVENTORY
 * 
 */
public class Item {

    private String name;
    private TextureRegion textureRegion;

    public Item(String name, Texture texture) {
        this(name, new TextureRegion(texture));
    }

    public Item(String name, TextureRegion textureRegion) {
        this.name = name;
        this.textureRegion = textureRegion;
    }

    public String getName() {
        return name;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public String toString() {
        return name;
    }
}
