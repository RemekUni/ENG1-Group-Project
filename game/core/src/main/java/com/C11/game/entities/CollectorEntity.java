package com.C11.game.entities;

import com.C11.game.ItemType;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class CollectorEntity extends Entity {
    private final Array<ItemType> inventory = new Array<>();

    private boolean isCollectionEnabled;
    private Array<ItemEntity> itemsArray;

    public CollectorEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, Array<Entity> entitiesArray, Array<ItemEntity> itemsArray) {
        super(entityTexture, gameMap, startX, startY, speed, hitboxWidthScale, hitboxHeightScale, entitiesArray);

        this.isCollectionEnabled = true;
        this.itemsArray = itemsArray;
    }

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
