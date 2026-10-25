package com.C11.game;

import java.util.Random;

import com.C11.game.entities.Entity;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


// TODO: Make own abstract class for items that will inherit from Entity
public class Item extends Entity {

    // private Vector2 pos;
    private float lifetime = 0f;

    private final Vector2 direction = new Vector2();

    // Entity can pick up items in 1 unit distance
    private static final float PICKUP_DISTANCE = 0.5f;

    private Array<Entity> collectorEntities;
    private Array<Item> itemsArray;

    private float randomAnimationShift;

    // collectorEntities should be an ArrayList to allow for a variable number of entities during the runtime
    public Item(Texture entityTexture, GameMap gameMap, float startX, float startY, Array<Entity> collectorEntities, Array<Item> itemsArray) {
        // TODO: Consider refactoring to omit #gameMap as item may not be in need to be checked for out of bounds
        super(entityTexture, gameMap, startX, startY, 0.1f, 0, 0);

        this.collectorEntities = collectorEntities;
        this.itemsArray = itemsArray;

        this.randomAnimationShift = new Random().nextFloat(2);

        // pos = new Vector2(startX, startY);
    }

    private void idleAnimate(Vector2 pos, float delta) {
        lifetime += delta;
        float animationSpeed = 2f;

        direction.y = MathUtils.sin((lifetime + randomAnimationShift) * animationSpeed); 
    }

    private boolean isCollidingEntity() {
        for (Entity entity : collectorEntities) {
            float x = this.getPosition().x;
            float entityX = entity.getPosition().x;

            float y = this.getPosition().y;
            float entityY = entity.getPosition().y;

            boolean isInXBounds = x >= entityX - PICKUP_DISTANCE && x <= entityX + entity.getHitboxWidth() + PICKUP_DISTANCE;
            boolean isInYBounds = y >= entityY - PICKUP_DISTANCE && y <= entityY + entity.getHitboxHeight() + PICKUP_DISTANCE;

            if (isInYBounds && isInXBounds) {
                return isInXBounds && isInYBounds;
            }
        }

        return false;
    }


    @Override
    public void update(float delta) {
        direction.set(0, 0);

        // Idle animated position
        idleAnimate(direction, delta);

        boolean pickupItem = isCollidingEntity();
        // TODO: Memory leak - item is still in memory after removal from the render list
        if (pickupItem) itemsArray.removeValue(this, true);


        move(direction, delta);
    }
}