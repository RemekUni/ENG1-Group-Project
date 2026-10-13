package com.C11.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.C11.game.world.GameMap;

public class Player extends Entity{
    private final Vector2 direction = new Vector2();

    private static final float PLAYER_SPEED = 4f;

    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;

    public Player(Texture playerTexture, float startX, float startY, GameMap gameMap) {
        super(playerTexture, gameMap, startX, startY, PLAYER_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE);
    }

    @Override 
    public void update(float delta) {
        direction.set(0, 0);
        
        if (Gdx.input.isKeyPressed(Keys.D)) {
            direction.x += 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.A)) {
            direction.x -= 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.S)) {
            direction.y -= 1;
        } 
        if (Gdx.input.isKeyPressed(Keys.W)) {
            direction.y += 1;
        }

        move(direction, delta);

    }
    
}
