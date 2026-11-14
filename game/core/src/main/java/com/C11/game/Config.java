package com.C11.game;

/**
 * Global constants shared across the game. Everything is static and final,
 * so nothing here can change while the game is running.
 * Do not put textures, fonts or other libGDX objects here.
 * Speeds are in tiles per second, distances in tiles (world units).
 *
 * @author Remigiusz Pertek (vfc515@york.ac.uk)
 */
public final class Config {
    private Config() {} // prevents anyone creating an instance

    // ---- Window ----
    public static final String WINDOW_TITLE = "Goose-Game-C11";

    // ---- Game session ----
    public static final int MIN_GAME_MINUTES = 1;
    public static final int MAX_GAME_MINUTES = 5;
    public static final int GAME_MINUTES_STEP = 1;
    public static final float TIMER_TICK_SECONDS = 1f;

    // ---- Camera and view ----
    // Minimum visible area in tiles. May show more on non 16:9 screens
    public static final float VIEW_WIDTH = 16f;
    public static final float VIEW_HEIGHT = 9f;
    // Text viewport must be larger than the world viewport so text stays sharp
    public static final int TEXT_VIEWPORT_SCALE = 20;
    public static final float TIMER_OFFSET_X = -100f;
    public static final float TIMER_OFFSET_Y = 350f;

    // ---- Entities ----
    public static final float PLAYER_SPEED = 4f;
    public static final float PLAYER_BOOSTED_SPEED = 7f;
    public static final float PLAYER_THROW_DISTANCE = 1f;
    public static final int MAX_INVENTORY_SIZE = 2;

    public static final float GOOSE_SPEED = 3f;
    // Centre to centre distance at which a goose stops approaching its target
    public static final float GOOSE_STOP_DISTANCE = 1.5f;

    // Hitbox covers the lower half of the sprite and 2/3 of its width (player and goose)
    public static final float HITBOX_WIDTH_SCALE = 1f / 1.5f;
    public static final float HITBOX_HEIGHT_SCALE = 0.5f;

    public static final float ITEM_PICKUP_DISTANCE = 1f;
    public static final float ITEM_IDLE_ANIMATION_SPEED = 0.1f;
    public static final float ITEM_IDLE_ANIMATION_FREQUENCY = 1.5f;

    // ---- Events ----
    public static final float CAN_BOOST_DURATION = 30f; // seconds
    public static final float CAN_SIZE = 1f;
    public static final float SANDWICH_SIZE = 1.5f;
    public static final int SANDWICH_GEESE_COUNT = 8;
    public static final float SANDWICH_SPAWN_RADIUS = 10f;

    // ---- Map (Tiled layer names, must match campus.tmx) ----
    public static final String COLLISION_LAYER = "collision";
    public static final String SANDWICH_LAYER = "sandwiches";
    public static final String CAN_LAYER = "cans";

    // ---- UI (viewport units, not world units) ----
    public static final float UI_WIDTH = 1280f;
    public static final float UI_HEIGHT = 720f;
    public static final float BUTTON_PADDING = 10f;
    // Fraction of the root table's width
    public static final float MENU_BUTTON_WIDTH_FRACTION = 0.4f;
    public static final float PAUSE_BUTTON_WIDTH_FRACTION = 0.3f;
    public static final float TITLE_PADDING_TOP = 10f;
    public static final float TITLE_PADDING_BOTTOM = 30f;
    public static final float BODY_PADDING_BOTTOM = 10f;
    // How much the pause menu dims the game (0 = none, 1 = fully black)
    public static final float PAUSE_DIM_ALPHA = 0.5f;

    // ---- Fonts ----
    public static final int TITLE_FONT_SIZE = 96;
    public static final int REGULAR_FONT_SIZE = 48;
    public static final int TIMER_FONT_SIZE = 48;
    public static final float FONT_OVERSAMPLE = 3f;
}