package com.C11.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.math.Rectangle;

public class Player implements Disposable{
    private final Vector2 position;
    private final Vector2 direction;
    private float size;
    private float speed = 4f;
    private Texture playerTexture;

    private final GameMap gameMap;
    private final Rectangle bounds;

    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;

    private final float hitboxWidth;
    private final float hitboxHeight;
    private final float hitboxOffsetX;

    public Player(float startX, float startY, GameMap gameMap) {
        this.gameMap = gameMap;
        position = new Vector2(startX, startY);
        direction = new Vector2(0f, 0f);
        playerTexture = new Texture("sprites/tile_0098.png");
        size = playerTexture.getWidth() * gameMap.getUnitScale();

        hitboxWidth = size * HITBOX_WIDTH_SCALE;
        hitboxHeight = size * HITBOX_HEIGHT_SCALE;
        hitboxOffsetX = (size - hitboxWidth) / 2f;

        bounds = new Rectangle();
    }

    private boolean isColliding() {
        bounds.set(position.x + hitboxOffsetX, position.y, hitboxWidth, hitboxHeight);
        return gameMap.isBlocked(bounds);
    }

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

        direction.nor();
        
        float dx = direction.x * speed * delta;
        float dy = direction.y * speed * delta;

        position.x += dx;
        if (isColliding()) {
            position.x -=dx;
        }

        position.y += dy;
        if (isColliding()) {
            position.y -=dy;
        }

    }

    public void draw(SpriteBatch batch) {
        batch.draw(playerTexture, position.x, position.y, size, size);
    }

    public Vector2 getVector2Position() {
        return position;
    }

    public float getWidth() {
        return size;
    }

    public float getHeight() {
        return size;
    }

    @Override 
    public void dispose() {
        playerTexture.dispose();
    }

    
}
