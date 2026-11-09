package com.C11.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class EntitiesHandler {
    // All entities on the map
    private final ObjectSet<AEntity> entities = new ObjectSet<>();

    // Categorised entity sets
    private ObjectSet<ItemEntity> itemEntities = new ObjectSet<>();
    private ObjectSet<Goose> geese = new ObjectSet<>();

    private Player player;

    /**
     * Maintain entities on the map, including updating, drawing, and
     * interatacting entities of specific types.
     */
    public EntitiesHandler() {

    }
    
    /** 
     * Updates all entities on the map. Called once per frame, subclasses decide movement and behaviour here.
     * @param delta seconds since last frame 
     */
    public void updateEntities(float delta) {
        // Update each entity        
        for (AEntity entity : entities) {
            entity.update(delta);

            // Make collectors collect items
            if (entity instanceof ACollectorEntity collector) {
                float cX = entity.getPosition().x;
                float cY = entity.getPosition().y;

                for (ItemEntity itemEntity : itemEntities) {
                    float iX = itemEntity.getPosition().x;
                    float iY = itemEntity.getPosition().y;

                    boolean isInXBounds = iX >= cX - itemEntity.PICKUP_DISTANCE && iX <= cX + entity.getHitbox().getWidth() + itemEntity.PICKUP_DISTANCE;
                    boolean isInYBounds = iY >= cY - itemEntity.PICKUP_DISTANCE && iY <= cY + entity.getHitbox().getHeight() + itemEntity.PICKUP_DISTANCE;

                    // Run through all collectors until catching the first entity that is inbounds
                    if (isInYBounds && isInXBounds) {
                        if (collector.isCollectionEnabled()) {
                            collector.collectItem(itemEntity);
                            System.out.println();
                        }
                    }
                }
            }
        }
    }

    /** 
     * Draw the entityTexture of each entity on the map using the shared spriteBatch.
     * 
     * @param batch the shared spriteBatch
     */
    public void draw(SpriteBatch batch) {
        for (AEntity entity : entities) {
            entity.draw(batch);
        }
    }

    /**
     * Add entity instance to a set of all entities on the map.
     * It is required for it be implemented and called only from the entity's constructor,
     * to ensure it will be added to the live entity's set during its instantiation.
     * @param entity
     */
    public void add(AEntity entity) {
        entities.add(entity);

        // -- Categorise entities --
        // Item
        if (entity instanceof ItemEntity itemEntity) {
            itemEntities.add(itemEntity);
        }
        // Goose
        if (entity instanceof Goose goose) {
            geese.add(goose);
        }
        // Player
        if (entity instanceof Player player) {
            if (this.player != null) return;

            this.player = player;
        }
    }

    /**
     * Update all subcategories like {@link #itemEntities}, accessible using appropriate getter methods like {@link #getItemEntitiesSet()}.
     * <b>Required</b> only when doing changes to the primary {@link #entities} set, accessible via {@link #getEntitiesSet()}.
     */
    public void updateCategories() {
        updateItemEntityCategory();
        updateGooseCategory();

        System.out.println(geese.size);
    }

    private void updateItemEntityCategory() {
        ObjectSet<ItemEntity> itemEntities = new ObjectSet<>();

        for (AEntity entity : entities) {
            if (entity instanceof ItemEntity itemEntity) {
                itemEntities.add(itemEntity);
            }
        }

        this.itemEntities = itemEntities;
    }

    private void updateGooseCategory() {
        ObjectSet<Goose> geese = new ObjectSet<>();

        for (AEntity entity : entities) {
            if (entity instanceof Goose goose) {
                geese.add(goose);
            }
        }

        this.geese = geese;
    }

    /**
     * Get set of all live entities on the map.
     * @return {@type ObjectSet}
     */
    public ObjectSet<AEntity> getEntitiesSet() {
        return entities;
    }

    /**
     * Get set of all live item entities on the map.
     * @return {@type ObjectSet}
     */
    public ObjectSet<ItemEntity> getItemEntitiesSet() {
        return itemEntities;
    }

    /**
     * Get set of all live geese entities on the map.
     * @return {@type ObjectSet}
     */
    public ObjectSet<Goose> getGeeseSet() {
        return geese;
    }
}
