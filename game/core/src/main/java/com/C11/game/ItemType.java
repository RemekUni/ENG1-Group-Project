package com.C11.game;

import com.badlogic.gdx.graphics.Texture;

public class ItemType {

    private String name;
    private Texture entityTexture;

    public ItemType(String name, Texture entityTexture) {
        this.name = name;
        this.entityTexture = entityTexture;
    }

    public String getName() {
        return name;
    }

    public Texture getEntityTexture() {
        return entityTexture;
    }

}
