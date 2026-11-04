package com.C11.game.utils;

public class GameResult {
    private final int eventsTriggered;
    private final int itemsUsed;
    private final int timeElapsed;
    private final int itemsPickedUp;

    public GameResult(int elapsedSeconds, int events, int itemsUsed, int itemsPickedUp) {
        this.timeElapsed = elapsedSeconds;
        this.eventsTriggered = events;
        this.itemsUsed = itemsUsed;
        this.itemsPickedUp = itemsPickedUp;
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

    public int getItemsPickedUp() {
        return itemsPickedUp;
    }

}
