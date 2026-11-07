package com.C11.game.entities;

import com.C11.game.ItemType;
import com.C11.game.world.EntitiesHandler;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Creates a collector entity at the given position. Inherits from {@link Entity}.
 * 
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public abstract class CollectorEntity extends Entity {
    protected final Array<ItemType> inventory = new Array<>();
    private int inventorySize = 2;

    private boolean isCollectionEnabled;
    private Array<ItemEntity> itemEntities;

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
    public CollectorEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, EntitiesHandler entitiesHandler) {
        super(entityTexture, gameMap, startX, startY, speed, hitboxWidthScale, hitboxHeightScale, entitiesHandler);
        this.gameMap = gameMap;

        this.isCollectionEnabled = false;
        this.itemEntities = entitiesHandler.getItemEntities();
    }
    
    public void collectItem(ItemEntity itemEntity) {
        if (!isCollectionEnabled) return; 

        // Remove #ItemEntity from the render list. GC should then dispose it from memory as it will not be referenced anywhere
        itemEntities.removeValue(itemEntity, true);
        // Add item to entity's inventory
        inventory.add(itemEntity.getItem());

        // dev/temp
        for (ItemType item : inventory) {
            System.out.println(this + " | " + item.getName() + " | " + item.getEntityTexture());
        }
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
        if (itemEntities == null) {
            itemEntities = new Array<>();
            throw new IllegalStateException(
                "Item array is not initialised. An empty array has been created as a fallback to prevent a crash. "
                + "However, this may cause unexpected behaviour. Please provide the existing item array via "
                + "enableCollection(Array) to ensure correct behaviour."
            );
        }

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

    public void throwItem(ItemType itemType) {
        new ItemEntity(itemType, gameMap, this.getPosition().x, this.getPosition().y, entitiesHandler);
        inventory.removeValue(itemType, true);
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
    }
}
