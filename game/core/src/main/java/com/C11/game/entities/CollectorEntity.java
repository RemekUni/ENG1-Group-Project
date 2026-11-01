package com.C11.game.entities;

import com.C11.game.ItemType;
import com.C11.game.items.ItemEntity;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Creates a collector entity at the given position. Inherits from {@link Entity}.
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
// TODO: The default collection state in constructor implementation is really unintuitive: rewrite to be a separate method instead.
public abstract class CollectorEntity extends Entity {
    private final Array<ItemType> inventory = new Array<>();

    private boolean isCollectionEnabled;
    private Array<ItemEntity> itemsArray;

    /**
     * Spawn a collector entity at the specified world position with collection toggled <b>on</b> by default.
     * @param entityTexture sprite texture (shared), not disposed by Entity
     * @param gameMap map used for collision checks
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param speed movement speed in tiles per second
     * @param hitboxWidthScale hitbox width as a fraction of the sprite width (centered)
     * @param hitboxHeightScale same as width, but for height (bottom-aligned)
     * @param entitiesArray reference to dynamic array of all entities
     * @param itemsArray dynamic array of all items on the map
     */
    public CollectorEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, Array<Entity> entitiesArray, Array<ItemEntity> itemsArray) {
        super(entityTexture, gameMap, startX, startY, speed, hitboxWidthScale, hitboxHeightScale, entitiesArray);

        this.isCollectionEnabled = true;
        this.itemsArray = itemsArray;
    }

    /**
     * Spawn a collector entity at the specified world position with collection toggled <b>off</b> by default.
     * @param entityTexture sprite texture (shared), not disposed by Entity
     * @param gameMap map used for collision checks
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param speed movement speed in tiles per second
     * @param hitboxWidthScale hitbox width as a fraction of the sprite width (centered)
     * @param hitboxHeightScale same as width, but for height (bottom-aligned)
     * @param entitiesArray reference to dynamic array of all entities
     */
    public CollectorEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, Array<Entity> entitiesArray) {
        super(entityTexture, gameMap, startX, startY, speed, hitboxWidthScale, hitboxHeightScale, entitiesArray);

        this.isCollectionEnabled = false;
        // ..itemsArray initialisation can be ommitted as it will not be called unless collection is enabled afterwards, through #setCollectionOn which requires itemsArray to be initialised.s
    }

    public void collectItem(ItemEntity itemEntity) {
        if (!isCollectionEnabled) return;

        // Remove #ItemEntity from the render list. GC should then dispose it from memory as it will not be referenced anywhere
        itemsArray.removeValue(itemEntity, true);
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

    public void setCollectionOn(Array<ItemEntity> itemsArray) {
        // In case collection will be toggled on it is required to have itemsArray initialised, for the items to be collectable
        this.itemsArray = itemsArray;

        this.isCollectionEnabled = true;
    }

    public void setCollectionOff() {
        this.isCollectionEnabled = false;
    }
}
