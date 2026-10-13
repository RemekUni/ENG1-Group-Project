package com.C11.game.entities;

import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    private final Vector2 position;
    private float size;
    private float speed;
    private Texture entityTexture;

    private final GameMap gameMap;
    private final Rectangle bounds;

    private final float hitboxWidth;
    private final float hitboxHeight;
    private final float hitboxOffsetX;

    public Entity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale) {
        this.entityTexture = entityTexture;
        this.gameMap = gameMap;
        this.speed = speed;

        position = new Vector2(startX, startY);
        size = entityTexture.getWidth() * gameMap.getUnitScale();

        hitboxWidth = size * hitboxWidthScale;
        hitboxHeight = size * hitboxHeightScale;
        hitboxOffsetX = (size - hitboxWidth) / 2f;

        bounds = new Rectangle();
    }

    protected void move(Vector2 direction, float delta) {
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

    public abstract void update(float delta);

    private boolean isColliding() {
        bounds.set(position.x + hitboxOffsetX, position.y, hitboxWidth, hitboxHeight);
        return gameMap.isBlocked(bounds);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(entityTexture, position.x, position.y, size, size);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getCenterX() {
        return position.x + size / 2f;
    }

    public float getCenterY() {
        return position.y + size / 2f;
    }

    public float getWidth() {
        return size;
    }

    public float getHeight() {
        return size;
    }
    
}
