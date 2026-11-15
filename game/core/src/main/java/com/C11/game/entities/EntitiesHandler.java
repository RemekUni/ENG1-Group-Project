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
    private ObjectSet<AEntity> characters = new ObjectSet<>();

    private Player player;

    // Counts
    private int itemCount = 0;
    private int gooseCount = 0;

    /**
     * Maintain entities on the map, including updating, drawing, and
     * interatacting entities of specific types.
     */
    public EntitiesHandler() {

    }

    /** 
     * Update all entities on the map.
     * @param delta seconds since last frame 
     */
    public void updateEntities(float delta) {
        player.update(delta);
        for (AEntity goose : geese) {
            goose.update(delta);
        }
        for (ItemEntity item : itemEntities) {
            // For an unknown reason, the item entity is sometimes null, so this check is required to prevent a crash
            if (item == null) {
                System.out.println("NULL");
                continue;
            }
            item.update(delta);
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
    public void addEntity(AEntity entity) {
        entities.add(entity);

        // -- Categorise entities --
        // Item
        if (entity instanceof ItemEntity itemEntity) {
            itemEntities.add(itemEntity);
            itemCount++;
        }
        // Goose
        if (entity instanceof Goose goose) {
            geese.add(goose);
            characters.add(goose);
            gooseCount++;
        }
        // Player
        if (entity instanceof Player player) {
            if (this.player != null) return;

            this.player = player;
            characters.add(player);
        }
    }

    public void removeEntity(AEntity entity) {
        entities.remove(entity);

        // -- Categorise entities --
        // Item
        if (entity instanceof ItemEntity itemEntity) {
            itemEntities.remove(itemEntity);
            itemCount--;
        }
        // Goose
        if (entity instanceof Goose goose) {
            geese.remove(goose);
            characters.remove(goose);
            gooseCount--;
        }
        // Player not included as it is a singleton and should not be removed
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

    public ObjectSet<AEntity> getCharactersSet() {
        return characters;
    }

    public Player getPlayer() {
        return player;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getGooseCount() {
        return gooseCount;
    }
}
