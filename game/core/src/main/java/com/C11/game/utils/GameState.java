package com.C11.game.utils;

public class GameState {
    private int eventsTriggered;
    private int itemsUsed;

    public GameState() {
        eventsTriggered = 0;
        itemsUsed = 0;
    }

    public void recordEventTriggered() {
        eventsTriggered++;
    }

    public void recordItemUsed() {
        itemsUsed++;
    }

    public int getEventsTriggered() {
        return eventsTriggered;
    }

    public int getItemsUsed() {
        return itemsUsed;
    }

    public GameResult toResult(int elapsedSeconds) {
        return new GameResult(elapsedSeconds, eventsTriggered, itemsUsed);
    }
}
