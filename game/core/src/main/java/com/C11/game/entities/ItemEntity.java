package com.C11.game.entities;

import java.util.Random;

import com.C11.game.world.GameMap;
import com.C11.game.world.Item;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for spawning in an item entity on the map with the specified attributes. Inherits from {@link AEntity}.
 * This is for the baguette item ONLY for now.
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class ItemEntity extends AEntity {

    // -- Item entity rewritable attributes --
    public float PICKUP_DISTANCE = 0.1f;
    public static float IDLE_ANIMATION_SPEED = 0.1f;

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
        super(itemType.getEntityTexture(), gameMap, x, y, IDLE_ANIMATION_SPEED, 0, 0, entitiesHandler);

        this.ITEM = itemType;
    }

    public void collect() {
        entitiesHandler.getEntitiesSet().remove(this);
        entitiesHandler.updateCategories();
    }

    // Simple sin item animation
    private void idleAnimate(Vector2 pos, float delta) {
        lifetime += delta;
        final float periodFrequency = 1.5f;

        direction.y = MathUtils.sin((lifetime + ANIMATION_RANDOM_SHIFT) * periodFrequency);
    }

    @Override
    public void update(float delta) {
        // Idle animated position
        idleAnimate(direction, delta);

        // Animate the item by making use of #Entity.move()
        move(direction, delta);
    }

    public Item getItem() {
        return ITEM;
    }
}
