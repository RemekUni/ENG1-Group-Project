package com.C11.game.entities;

import java.util.Random;

import com.C11.game.Config;
import com.C11.game.world.GameMap;
import com.C11.game.inventory.Item;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Class for spawning in an item entity on the map with the specified attributes. Inherits from {@link AEntity}.
 *
 * @req FR_ITEMS
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 * @author Ethan Davis (xxb522@york.ac.uk)
 */
public class ItemEntity extends AEntity {

    private final Item ITEM;

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
    public ItemEntity(Item itemType, GameMap gameMap, float x, float y, EntitiesHandler entitiesHandler) {
        super(itemType.getTexture(), gameMap, x, y, Config.ITEM_IDLE_ANIMATION_SPEED, 0, 0, entitiesHandler);

        this.ITEM = itemType;
    }

    /**
     * Remove this entity from the set of entities on map.
     */
    public void collect() {
        entitiesHandler.removeEntity(this);
        System.out.println("Collected " + ITEM.getName() + "!");
    }

    /**
     * Checks if item is within the collection range of an entity eligible to collect items
     *
     * @return null if no entity in range, otherwise the first entity found to be in range
     */
    public AEntity collectionCollisionDetection() {
        for (AEntity entity : entitiesHandler.getCharactersSet()) {
            double iX = (double) this.getCenterX();
            double iY = (double) this.getCenterY();

            if (entity.isCollectionEnabled()) {
                double cX = (double) entity.getCenterX();
                double cY = (double) entity.getCenterY();

                // Check if distance between centers is less than or equal to the pickup distance
                boolean isInBounds = Math.sqrt(Math.pow(iX - cX, 2) + Math.pow(iY - cY, 2)) <= Config.ITEM_PICKUP_DISTANCE;

                // Run through all collectors until catching the first entity that is inbounds
                if (isInBounds) {
                    return entity;
                }
            }
        }
        return null;
    }

    // Simple sin item animation
    private void idleAnimate(Vector2 pos, float delta) {
        lifetime += delta;


        direction.y = MathUtils.sin((lifetime + ANIMATION_RANDOM_SHIFT) * Config.ITEM_IDLE_ANIMATION_FREQUENCY);
    }

    @Override
    public void update(float delta) {
        // Check if any collectors are in range to collect the item
        AEntity foundCollector = collectionCollisionDetection();
        if (foundCollector != null) {
            foundCollector.collectItem(this);
        }
        else {
            // Idle animated position
            idleAnimate(direction, delta);

            // Animate the item by making use of #Entity.move()
            move(direction, delta, false);
        }
    }

    public Item getItem() {
        return ITEM;
    }

    public String toString() {
        return ITEM.getName() + "@" + Integer.toHexString(hashCode());
    }
}
