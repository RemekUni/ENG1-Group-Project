package com.C11.game.utils;

/**
 * This class is responsible for storing data during gameplay.
 * To be called on whenever a counter needs to go up
 */
public class GameState {
    private static int eventsTriggered;
    private static int itemsUsed;
    private static int itemsPickedUp;

    /**
     * Resets all counters to 0. Should be used when a new game starts.
     * Becomes imperitive once multiple games start getting run within the same session
     */
    public GameState() {
        eventsTriggered = 0;
        itemsUsed = 0;
        itemsPickedUp = 0;
    }

    // methods to be called when incrementing specific counters

    public static void recordEventTriggered() {
        eventsTriggered++;
    }

    public static void recordItemUsed() {
        itemsUsed++;
    }

    public static void recordItemPickup() {
        itemsPickedUp++;
    }

    // getters for counters

    public static int getEventsTriggered() {
        return eventsTriggered;
    }

    public static int getItemsUsed() {
        return itemsUsed;
    }

    public static int getItemsPickedUp() {
        return itemsPickedUp;
    }

    public static GameResult toResult(int elapsedSeconds) {
        return new GameResult(elapsedSeconds, eventsTriggered, itemsUsed, itemsPickedUp);
    }
}
