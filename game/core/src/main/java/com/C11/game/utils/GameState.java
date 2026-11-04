package com.C11.game.utils;

public class GameState {
    private int eventsTriggered;
    private int itemsUsed;
    private int itemsPickedUp;

    public GameState() {
        eventsTriggered = 0;
        itemsUsed = 0;
        itemsPickedUp = 0;
    }

    public void recordEventTriggered() {
        eventsTriggered++;
    }

    public void recordItemUsed() {
        itemsUsed++;
    }

    public void recordItemPickup() {
        itemsPickedUp++;
    }

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
