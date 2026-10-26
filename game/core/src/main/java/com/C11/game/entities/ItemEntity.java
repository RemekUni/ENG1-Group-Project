package com.C11.game.entities;

import java.util.Random;

import com.C11.game.ItemType;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


// TODO: Make own abstract class for items that will inherit from Entity
public class ItemEntity extends Entity {

    private float lifetime = 0f;

    private final Vector2 direction = new Vector2();

    // Entity can pick up items in 1 unit distance
    private static final float PICKUP_DISTANCE = 0.5f;
    
    private static final float IDLE_ANIMATION_SPEED = 0.5f;
    private final float animationRandomShift = new Random().nextFloat(2);

    private Array<Entity> collectorEntities;
    private Array<ItemEntity> itemsArray;


    // collectorEntities should be an ArrayList to allow for a variable number of entities during the runtime
    public ItemEntity(ItemType itemType, float x, float y, Array<Entity> collectorEntities, Array<ItemEntity> itemsArray) {
        // TODO: Resolve redundant gameMap bounds logic; current bypass: empty gameMap instantiation - TMX map instantiations fails but eliminates the dependency
        super(itemType.getEntityTexture(), new GameMap(""), x, y, IDLE_ANIMATION_SPEED, 0, 0);

        this.collectorEntities = collectorEntities;
        this.itemsArray = itemsArray;
    }

    private void idleAnimate(Vector2 pos, float delta) {
        lifetime += delta;
        final float periodFrequency = 2f;

        direction.y = MathUtils.sin((lifetime + animationRandomShift) * periodFrequency); 
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
        // Delete item entity from the reference list. GC should then dispose it from memory as it will not be referenced anywhere
        if (pickupItem) itemsArray.removeValue(this, true);

        move(direction, delta);
    }
}