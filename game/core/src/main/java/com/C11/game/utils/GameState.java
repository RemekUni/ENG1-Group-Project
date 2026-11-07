package com.C11.game.utils;

/**
 * This class is responsible for storing data during gameplay.
 * To be called on whenever a counter needs to go up
 */
public class GameState {
    private int eventsTriggered;
    private int itemsUsed;
    private int itemsPickedUp;

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

    public void recordEventTriggered() {
        eventsTriggered++;
    }

    public void recordItemUsed() {
        itemsUsed++;
    }

    public void recordItemPickup() {
        itemsPickedUp++;
    }

    // getters for counters

    public int getEventsTriggered() {
        return eventsTriggered;
    }

    public int getItemsUsed() {
        return itemsUsed;
    }

    public int getItemsPickedUp() {
        return itemsPickedUp;
    }

    public GameResult toResult(int elapsedSeconds) {
        return new GameResult(elapsedSeconds, eventsTriggered, itemsUsed, itemsPickedUp);
    }
}
