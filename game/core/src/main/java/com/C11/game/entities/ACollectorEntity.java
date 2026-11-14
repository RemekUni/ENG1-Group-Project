package com.C11.game.entities;

import com.C11.game.utils.GameState;
import com.C11.game.world.GameMap;
import com.C11.game.world.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Abstract base class for a collector entity at the given position. Inherits from {@link AEntity}.
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public abstract class ACollectorEntity extends AEntity {
    protected final ObjectMap<String, ObjectSet<ItemEntity>> inventory = new ObjectMap<>();
    private final int MAX_INVENTORY_SIZE = 2;

    private boolean isCollectionEnabled;

    private GameMap gameMap;

    /**
     * Spawn a collector entity at the specified world position with item collection <b>disabled</b> by default - enable with {@link #enableCollection(Array)}.
     * @param entityTexture sprite texture (shared), not disposed by Entity
     * @param gameMap map used for collision checks
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param speed movement speed in tiles per second
     * @param hitboxWidthScale hitbox width as a fraction of the sprite width (centered)
     * @param hitboxHeightScale same as width, but for height (bottom-aligned)
     * @param entitiesArray reference to dynamic array of all entities
     */
    public ACollectorEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, EntitiesHandler entitiesHandler) {
        super(entityTexture, gameMap, startX, startY, speed, hitboxWidthScale, hitboxHeightScale, entitiesHandler);
        this.gameMap = gameMap;

        this.isCollectionEnabled = false;
    }

    public void collectItem(ItemEntity itemEntity) {
        if (!isCollectionEnabled) return; // safe check

        // Remove #ItemEntity from the render list. GC should then dispose it from memory as it will not be referenced anywhere
        itemEntity.collect();
        // Check to see if the item type is unique and there is space in the inventory
        if (!inventory.containsKey(itemEntity.getItem().getName())) {
            if (inventory.size < MAX_INVENTORY_SIZE) {
                inventory.put(itemEntity.getItem().getName(), new ObjectSet<>());
            } 
            else {
                System.out.println("Inventory full! Cannot collect " + itemEntity.getItem().getName() + "!");
                return;
            }
        }

        inventory.get(itemEntity.getItem().getName()).add(itemEntity);
        System.out.println(inventory);

        // Add items picked up stats
        GameState.recordItemPickup();

        // dev/temp
        // for (Item item : inventory) {
        //     System.out.println(this + " | " + item.getName() + " | " + item.getEntityTexture());
        // }
    }

    public boolean isCollectionEnabled() {
        return isCollectionEnabled;
    }

    /**
     * Enable item collection. Use this method overload <b>only if</b> previously already defined an {@link #itemEntities} using {@link #enableCollection(Array)}.
     *
     * @throws IllegalStateException if the item array was not originally initialised via {@link #enableCollection(Array)}.
     * @see #enableCollection(Array)
     */
    public void enableCollection() {
        this.isCollectionEnabled = true;
    }

    /**
     * Disable item collection. Does not remove the reference to {@link #itemEntities}.
     * Call {@link #enableCollection()} to re-enable collection using the existing
     * item array, rather than {@link #enableCollection(Array)}.
     */
    public void disableCollection() {
        this.isCollectionEnabled = false;
    }

    public void throwItem(ItemEntity item) {
        Item itemType = item.getItem();
        new ItemEntity(itemType, gameMap, this.getPosition().x, this.getPosition().y, entitiesHandler);
        inventory.get(itemType.getName()).remove(item);
    }

    public ObjectMap<String, ObjectSet<ItemEntity>> getInventory() {
        return inventory;
    }

    public int getMaxInventorySize() {
        return MAX_INVENTORY_SIZE;
    }
}
