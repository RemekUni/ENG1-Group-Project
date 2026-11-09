package com.C11.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.C11.game.world.GameMap;
import com.C11.game.world.Item;

/**
 * The player-controlled sprite. Moves with WASD.
 * Collision is handled by Entity
 *
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class Player extends ACollectorEntity{
    private final Vector2 direction = new Vector2();

    // tiles per second
    private static final float PLAYER_SPEED = 4f;

    private static final float THROW_DISTANCE = 1f;
    private static final float BOOSTED_SPEED = 7f;
    private float boostTimer = 0f;

    // Hitbox covers lower half of the sprite and is 2/3 of its width.
    // This lets the player get between 1 tile spaces more easily and looks better when approaching collision objects such as buildings and trees.
    private static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    private static final float HITBOX_HEIGHT_SCALE = 0.5f;

    private GameMap gameMap;

    /**
     * Creates an entity for the player to control
     *
     * @param playerTexture texture of the player
     * @param startX starting 'x' position of entity's bottom-left corner, in world units
     * @param startY starting 'y' position of entity's bottom-left corner, in world units
     * @param gameMap map used for collision checks
     */
    public Player(Texture playerTexture, float startX, float startY, GameMap gameMap, EntitiesHandler entitiesHandler) {
        super(playerTexture, gameMap, startX, startY, PLAYER_SPEED, HITBOX_WIDTH_SCALE, HITBOX_HEIGHT_SCALE, entitiesHandler);
        this.gameMap = gameMap;
    }

    // Opposite keys cancel out; x and y can both be set for diagonals. Entity.move() normalises direction so diagonal movement is not faster
    // TODO: map keys to actions, not hardcoded
    @Override
    public void update(float delta) {
        if (boostTimer > 0){
            boostTimer-=delta;

            if (boostTimer <=0){
                setSpeed(PLAYER_SPEED); //remove the speed increase once the boost has expired 
            }
        }

        direction.set(0, 0);

        if (Gdx.input.isKeyPressed(Keys.D)) {
            direction.x += 1;
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            direction.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            direction.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            direction.y += 1;
        }

        if (Gdx.input.isKeyPressed(Keys.T)) {
           throwItem(0);
        }

        move(direction, delta);
    }

    public void activateSpeedBoost(float boostTime){
        setSpeed(BOOSTED_SPEED);
        boostTimer += boostTime; // += allows for stacking
    }

    public void throwItem(int inventoryIndex) {
        if (inventory.size == 0) return;

        doThrowLogic(inventory.get(inventoryIndex));
    }

    public void throwItem(Item itemType) {
        if (inventory.size == 0) return;

        doThrowLogic(itemType);
    }

    private void doThrowLogic(Item itemType) {
        Vector2 throwVector = direction.scl(THROW_DISTANCE);

        this.inventory.removeValue(itemType, true);
        new ItemEntity(itemType, gameMap, this.getPosition().x + throwVector.x, this.getPosition().y + throwVector.y, entitiesHandler);
        System.out.println("spawned item at " + (this.getPosition().x + throwVector.x) + ", " + (this.getPosition().y + throwVector.y) + ", player pos is: " + this.getPosition());
    }

    private void startAim() {

    }
}
