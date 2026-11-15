package com.C11.game.entities;

import com.C11.game.Config;
import com.C11.game.hitboxes.Hitbox;
import com.C11.game.world.GameMap;
import com.C11.game.stats.GameStats;
import com.C11.game.inventory.Inventory;
import com.C11.game.inventory.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.MathUtils;

/**
 * Abstract base class for anything that moves on the map (like player or geese). Handles position, drawing, and collision-aware movement.
 * Subclasses decide which way to move in update() and then call move().
 * Entity does not own its texture, so it does not dispose it, the creator does
 * 
 * @req UR_BOUNDARIES
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public abstract class AEntity {
    private static final long PICKUP_COOLDOWN_MS = 1000;

    // Texture
    private Texture entityTexture;

    // -- Attributes --
    private final Vector2 position;
    private float size;
    private float speed;
    private final Hitbox hitbox;
    private boolean isCollectionEnabled;
    private long pickupBlockedUntil = 0;

    // Inventory
    private Inventory inventory;
    protected Array<Item> inventoryContainer;

    // -- Handlers--
    private final GameMap gameMap;
    protected EntitiesHandler entitiesHandler;

    /**
     * Creates an entity at the given position.
     * 
     * @param entityTexture sprite texture (shared), not disposed by Entity
     * @param gameMap map used for collision checks
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param speed movement speed in tiles per second
     * @param hitboxWidthScale hitbox width as a fraction of the sprite width (centered)
     * @param hitboxHeightScale same as width, but for height (bottom-aligned)
     * @param entitiesHandler handler that tracks all entities
     */
    public AEntity(Texture entityTexture, GameMap gameMap, float startX, float startY, float speed, float hitboxWidthScale, float hitboxHeightScale, EntitiesHandler entitiesHandler) {
        this.entityTexture = entityTexture;
        // -- Attributes --
        this.speed = speed;
        this.isCollectionEnabled = false;

        this.gameMap = gameMap;
        this.entitiesHandler = entitiesHandler;

        position = new Vector2(startX, startY);

        // This is assuming all entities are square. will need to be changed if we add non-square entities
        size = entityTexture.getWidth() * gameMap.getUnitScale();

        hitbox = Hitbox.forSprite(size, hitboxWidthScale, hitboxHeightScale, position);

        // Check if the spawn location is clear, if not, move the entity by a small random float between -1 and 1
        while (!isSpawnClear()) {
            position.x += MathUtils.random(-1f, 1f);
            position.y += MathUtils.random(-1f, 1f);
        }

        // Add to Entities set
        entitiesHandler.add(this);
    }

    /**
     * Moves entity in the given direction at its current speed, stopping at map collisions.
     * X and Y are resolved separately so entities slide against walls instead of sticking (just makes movement smoother in general)
     * 
     * Note: normalises direction in-place, which means the callers vector will get changed
     * 
     * @req FR_PLAYER_BOUNDARY FR_GEESE_BOUNDARY
     * 
     * @param direction vector direction for entity to move
     * @param delta time in seconds from last frame
     * @param checkCollisions whether to check for collisions
     */
    protected void move(Vector2 direction, float delta, boolean checkCollisions) {
        direction.nor();
        
        float dx = direction.x * speed * delta;
        float dy = direction.y * speed * delta;

        position.x += dx;
        if (checkCollisions && isColliding()) {
            position.x -=dx;
        }

        position.y += dy;
        if (checkCollisions && isColliding()) {
            position.y -=dy;
        }

        hitbox.follow(position);
    }

    /** 
     * Called once per frame, subclasses decide movement and behaviour here.
     * @param delta seconds since last frame 
     */
    public abstract void update(float delta);

    // Check for hitbox collisions on the map after first snapping the hitbox to its entity
    private boolean isColliding() {
        hitbox.follow(position);
        for (AEntity entity : entitiesHandler.getCharactersSet()) {
            if (entity != this && hitbox.overlaps(entity.getHitbox())) {
                return true;
            }
        }   
        return gameMap.isBlocked(hitbox);
    }

    private boolean isSpawnClear() {
        hitbox.follow(position);
        return !gameMap.isBlocked(getHitbox());
    }

    public void collectItem(ItemEntity itemEntity) {
        if (!isCollectionEnabled || TimeUtils.millis() < pickupBlockedUntil) return; // safe check

        // Inventory full: leave the item in the world
        if (inventoryContainer.size >= Config.MAX_INVENTORY_SIZE) {
            System.out.println("Inventory full! Cannot collect " + itemEntity.getItem().getName() + "!");
            return;
        }

        // Remove #ItemEntity from the render list. GC should then dispose it from memory as it will not be referenced anywhere
        itemEntity.collect();
        inventoryContainer.add(itemEntity.getItem());

        System.out.println(inventoryContainer);

        // Add items picked up stats
        GameStats.recordItemPickup();

        // dev/temp
        // for (Item item : inventory) {
        //     System.out.println(this + " | " + item.getName() + " | " + item.getEntityTexture());
        // }
    }

    public boolean isCollectionEnabled() {
        return isCollectionEnabled;
    }

    /**
     * Enable item collection. Creates the inventory on first call.
     */
    public void enableCollection() {
        if (inventory == null) {
            this.inventory = new Inventory();
            this.inventoryContainer = this.inventory.getContainer();
        }
        this.isCollectionEnabled = true;
    }

    /**
     * Disable item collection.
     * Call {@link #enableCollection()} to re-enable collection.
     */
    public void disableCollection() {
        this.isCollectionEnabled = false;
    }

    /**
     * Drops an item from the inventory into the world at this entity's position.
     * Pickup is blocked for a short cooldown so the item is not instantly re-collected.
     *
     * @param item the inventory item to throw, ignored if null or not in the inventory
     */
    public void throwItem(Item item) {
        if (!isCollectionEnabled || item == null || !inventoryContainer.removeValue(item, true)) return;
        new ItemEntity(item, gameMap, this.getPosition().x, this.getPosition().y, entitiesHandler);
        pickupBlockedUntil = TimeUtils.millis() + PICKUP_COOLDOWN_MS;
    }

    public void useItem(int index) {
        if (!isCollectionEnabled || index < 0 || index >= inventoryContainer.size) return;
        inventoryContainer.removeIndex(index);
        GameStats.recordItemUsed();
    }

    /** @return the inventory, or null if {@link #enableCollection()} was never called */
    public Inventory getInventory() {
        return inventory;
    }

    /** 
     * Draw the entityTexture using the shared spriteBatch
     * 
     * @param batch the shared spriteBatch
     */
    public void draw(SpriteBatch batch) {
        batch.draw(entityTexture, position.x, position.y, size, size);
    }

    /** @return current bottom-left vector position in world units */
    public Vector2 getPosition() {
        return position;
    }

    /** @return centre x of the sprite in world units */
    public float getCenterX() {
        return position.x + size / 2f;
    }

    /** @return centre y of the sprite in world units */
    public float getCenterY() {
        return position.y + size / 2f;
    }

    /** @return entity texture width in world units */
    public float getWidth() {
        return size;
    }

    /** @return entity texture height in world units */
    public float getHeight() {
        return size;
    }

    /** @return the entity's collision hitbox in world units, updated by move(). Treat as read-only */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /** @return the entity's current rate of speed in world unites  */
    public float getSpeed(){
        return speed;
    }
    /** @param speed set the speed to the desired speed */
    public void setSpeed(float speed){
        this.speed = speed;
    }
}