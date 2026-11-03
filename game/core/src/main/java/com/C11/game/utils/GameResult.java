package com.C11.game.utils;

public class GameResult {
    private final int eventsTriggered;
    private final int itemsUsed;
    private final int timeElapsed;

    public GameResult(int elapsedSeconds, int events, int items) {
        this.timeElapsed = elapsedSeconds;
        this.eventsTriggered = events;
        this.itemsUsed = items;
    }

    public int getEventsTriggered() {
        return eventsTriggered;
    }

    public int getItemsUsed() {
        return itemsUsed;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

}
