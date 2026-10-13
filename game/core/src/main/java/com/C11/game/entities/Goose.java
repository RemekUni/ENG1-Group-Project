package com.C11.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.C11.game.world.GameMap;

public class Goose extends Entity {
    private final Vector2 direction = new Vector2();

    private static final float GOOSE_SPEED = 3f;

    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;
    private static final float STOP_DISTANCE = 1.5f;

    private final Entity target;

    public Goose(Texture gooseTexture, float startX, float startY, GameMap gameMap, Entity target) {
        super(gooseTexture, gameMap, startX, startY, GOOSE_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE);
        this.target = target;
    }

    @Override 
    public void update(float delta) {
        direction.set(this.target.getCenterX(), this.target.getCenterY()).sub(getCenterX(), getCenterY());

        if (direction.len() >= STOP_DISTANCE) {
            move(direction, delta);
        }
    }
}
