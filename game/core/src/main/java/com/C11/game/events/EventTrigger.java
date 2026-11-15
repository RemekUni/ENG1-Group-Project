package com.C11.game.events;

import com.C11.game.hitboxes.Hitbox;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * The Trigger class extends the hitbox class which defines the size and position of the boxes.
 * It provides the framework to add actions to each event/item
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class EventTrigger {
    protected boolean oneShot;//Whether the action can be done more than once.
    protected boolean triggered = false;
    protected final Vector2 position;
    protected final Hitbox hitbox;
    protected float size;
    protected TextureRegion textureRegion;

    private boolean isTriggerEnabled;

    /**
     * Constructor for a standalone trigger placed on the map
     * @param textureRegion a texture region for the sprite/asset you are using.
     * @param startX bottom left x coordinate of the asset
     * @param startY bottom left y coordinate of the asset
     * @param gameMap the gameMap
     * @param size how big the asset should be
     * @param oneShot if event can happen multiple timea
     */
    public EventTrigger(TextureRegion textureRegion, float startX, float startY, GameMap gameMap, float size, boolean oneShot) {
        this.textureRegion = textureRegion;
        this.position = new Vector2(startX, startY);
        this.size = size;
        this.oneShot = oneShot;
        this.hitbox = new Hitbox(1.0f, 1.0f, position, size);
        this.isTriggerEnabled = true;
    }

    /**
     * Constructor for owners that move (entities). The hitbox is created here, owners reposition it with hitbox.follow(position).
     * The trigger starts disabled.
     * @param startX bottom left x coordinate
     * @param startY bottom left y coordinate
     * @param size sprite size in world units
     * @param hitboxWidthScale hitbox width as a fraction of size
     * @param hitboxHeightScale hitbox height as a fraction of size
     * @param oneShot if event can happen multiple times
     */
    protected EventTrigger(float startX, float startY, float size, float hitboxWidthScale, float hitboxHeightScale, boolean oneShot) {
        this.position = new Vector2(startX, startY);
        this.size = size;
        this.oneShot = oneShot;
        this.hitbox = Hitbox.forSprite(size, hitboxWidthScale, hitboxHeightScale, position);
        this.isTriggerEnabled = false;
    }

    
    /**
     * Action that happens when the event was triggered. Overwrite in subclass to specify action.
     */
    public abstract void action(); //Defined in the subclasses.

    /**
     * If oneShot and hitbox has been triggered, call the action, set triggered to true, and return true.
     *
     * @return true if action ran
     */
    public boolean trigger(){
        if (triggered && oneShot) return false;
        action();
        triggered = true;
        return true;
    }

    /**
     * Method decides if action is done forever or a player can activate it again.
     * @return boolean for if the action is completed (true) or can happen again (false).
     */
    public boolean isComplete(){
        return triggered && oneShot;
    }

    public void draw(SpriteBatch batch) {
        if (!isComplete() && textureRegion != null) {
            batch.draw(textureRegion, position.x, position.y, size, size);
        }
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public boolean isTriggerEnabled() {
        return isTriggerEnabled;
    }

    public void setTriggerEnabled(boolean enabled) {
        this.isTriggerEnabled = enabled;
    }
}



