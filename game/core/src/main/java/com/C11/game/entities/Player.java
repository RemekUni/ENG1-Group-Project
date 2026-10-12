package com.C11.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Player implements Disposable{
    private final Vector2 position;
    private final Vector2 direction;
    private float size;
    private float speed = 4f;
    private Texture playerTexture;

    public Player(float startX, float startY, float unitScale) {
        position = new Vector2(startX, startY);
        direction = new Vector2(0f, 0f);
        playerTexture = new Texture("sprites/tile_0098.png");
        size = playerTexture.getWidth() * unitScale;
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
        position.mulAdd(direction, speed * delta);

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
