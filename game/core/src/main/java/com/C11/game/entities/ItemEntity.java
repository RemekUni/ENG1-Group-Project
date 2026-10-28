package com.C11.game.entities;

import java.util.Random;

import com.C11.game.ItemType;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Class for spawning in an item entity on the map with the specified attributes. Inherits from {@link Entity}.
 * 
 * @req UR_INVENTORY
 * 
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class ItemEntity extends Entity {

    // -- Item entity rewritable attributes --
    public static final float PICKUP_DISTANCE = 0.5f;
    public static final float IDLE_ANIMATION_SPEED = 0.1f;

    private final ItemType ITEM;

    private Array<Entity> entities;

    private float lifetime = 0f;

    private final Vector2 direction = new Vector2(0, 0);
    
    private final float ANIMATION_RANDOM_SHIFT = new Random().nextFloat(2);

    /**
     * Spawn an item of the specified type at the specified world position.
     * @param itemType item type.
     * @param gameMap map, used for collision checks
     * @param x starting 'x' position of entity's bottom-left corner, in world units
     * @param y starting 'y' position of entity's bottom-left corner, in world units
     * @param entitiesArray reference to dynamic array of all entities
     * 
     */
    public ItemEntity(ItemType itemType, GameMap gameMap, float x, float y, Array<Entity> entities) {
        super(itemType.getEntityTexture(), gameMap, x, y, IDLE_ANIMATION_SPEED, 0, 0, entities);

        this.entities = entities;
        this.ITEM = itemType;
    }

    // Simple sin item animation
    private void idleAnimate(Vector2 pos, float delta) {
        lifetime += delta;
        final float periodFrequency = 1.5f;

        direction.y = MathUtils.sin((lifetime + ANIMATION_RANDOM_SHIFT) * periodFrequency); 
    }

    private CollectorEntity getCollisionCollectorEntity() {
        float x = this.getPosition().x;
        float y = this.getPosition().y;
            
        for (Entity entity : entities) {
            float entityX = entity.getPosition().x;

            float entityY = entity.getPosition().y;

            boolean isInXBounds = x >= entityX - PICKUP_DISTANCE && x <= entityX + entity.getHitbox().getWidth() + PICKUP_DISTANCE;
            boolean isInYBounds = y >= entityY - PICKUP_DISTANCE && y <= entityY + entity.getHitbox().getHeight() + PICKUP_DISTANCE;

            // Run through all collectors until catching the first entity that is inbounds
            if (isInYBounds && isInXBounds) {
                if (entity instanceof CollectorEntity collector) return collector;
            }
        }

        return null;
    }

    @Override
    public void update(float delta) {
        // Idle animated position
        idleAnimate(direction, delta);

        CollectorEntity collisionCollectorEntity = getCollisionCollectorEntity();

        // If collided entity is collector, make it collect the item
        if (collisionCollectorEntity != null) {
            collisionCollectorEntity.collectItem(this);
        }

        // Animate the item by making use of #Entity.move()
        move(direction, delta);
    }

    public ItemType getItem() {
        return ITEM;
    }
}