package com.C11.game.world;

import com.C11.game.inventory.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Static references to all assets used in the game.
 * TODO: Because all assets are statically loaded once, it is impossible to load them in again afterwards. Use enum instead.
 */
public final class Assets {



    // Game assets
    public static final Texture PLAYER_TEXTURE = new Texture("sprites/tile_0098.png");
    public static final Texture GOOSE_TEXTURE = new Texture("sprites/duck.png");
    public static final Texture BAGUETTE_TEXTURE = new Texture("sprites/baguette.png");
    public static final Texture SPOILED_SANDWICH_TEXTURE = new Texture("sprites/sandwich.png");
    public static final Texture CAN_TEXTURE = new Texture("sprites/can.png");
    public static final Texture BUTTON_TEXTURE = new Texture("sprites/button.png");

    // UI
    public static final Texture SLOT_SELECTED_TEXTURE = new Texture("ui/slot-selected.png");
    public static final Texture SLOT_UNSELECTED_TEXTURE = new Texture("ui/slot-unselected.png");

    // Items
    public static final Item BAGUETTE_ITEM = new Item("Baguette", BAGUETTE_TEXTURE);
    public static final Item CAN_ITEM = new Item("Can", CAN_TEXTURE);


    public static final Item SPOILED_SANDWICH_ITEM = new Item("Spoiled Sandwich", new TextureRegion(SPOILED_SANDWICH_TEXTURE, 0, 0, SPOILED_SANDWICH_TEXTURE.getWidth() / 2, SPOILED_SANDWICH_TEXTURE.getHeight()));


    // public static final String PICKUP = "sounds/.wav";
    // public static final String GAME_OVER = "sounds/.wav";

    // public static final Array<Sound> GOOSE1 = 
    // public static final Array<Sound> GOOSE2 = 
    // public static final Array<Sound> GOOSE3 = 
    // public static final Array<Sound> GOOSE4 = 
    // public static final Array<Sound> GOOSE5 =
    // public static final Array<Sound> GOOSE6 =
    // public static final Array<Sound> GOOSE7 =


    public static final String TIMER_FONT = "SuperNormal.ttf";

    public static void dispose() {
        PLAYER_TEXTURE.dispose();
        GOOSE_TEXTURE.dispose();
        BAGUETTE_TEXTURE.dispose();
        SPOILED_SANDWICH_TEXTURE.dispose();
        BUTTON_TEXTURE.dispose();
    }
}