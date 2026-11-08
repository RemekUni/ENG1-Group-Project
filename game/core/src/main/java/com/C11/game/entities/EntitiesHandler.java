package com.C11.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectSet;

public class EntitiesHandler {
    private final ObjectSet<AEntity> entities = new ObjectSet<>();

    private ObjectSet<ItemEntity> itemEntities = new ObjectSet<>();
    private ObjectSet<Goose> geese = new ObjectSet<>();

    private Player player;

    public EntitiesHandler() {

    }

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
                        }
                    }
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (AEntity entity : entities) {
            entity.draw(batch);
        }
    }

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
            if (this.player == null) return;

            this.player = player;
        }
    }

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

    public ObjectSet<AEntity> getEntitiesSet() {
        return entities;
    }

    public ObjectSet<ItemEntity> getItemEntitiesSet() {
        return itemEntities;
    }

    public ObjectSet<Goose> getGeeseSet() {
        return geese;
    }
}
