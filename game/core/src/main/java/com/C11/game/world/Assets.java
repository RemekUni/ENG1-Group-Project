package com.C11.game.world;

import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    // Game assets
    public static final Texture PLAYER_TEXTURE = new Texture("sprites/tile_0098.png");
    public static final Texture GOOSE_TEXTURE = new Texture("sprites/duck.png");
    public static final Texture ITEM_TEXTURE = new Texture("sprites/baguette.png");
    public static final Texture SANDWICH_TEXTURE = new Texture("sprites/sandwich.png");
    public static final Texture CAN_TEXTURE = new Texture("sprites/can.png");

    // UI
    public static final Texture SLOT_SELECTED_TEXTURE = new Texture("ui/slot-selected.png");
    public static final Texture SLOT_UNSELECTED_TEXTURE = new Texture("ui/slot-unselected.png");


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
        ITEM_TEXTURE.dispose();
        SANDWICH_TEXTURE.dispose();
    }
}