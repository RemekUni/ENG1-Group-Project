package com.C11.game.stats;

/**
 * Game results and stats class for creating snapshots after each run of the game
 */
public class GameResult {
    private final int eventsTriggered;
    private final int itemsUsed;
    private final int timeElapsed;
    private final int itemsPickedUp;

    /**
     * Main constructor used for passing in various counters and storing them
     * 
     * @param elapsedSeconds the number of seconds elapsed during the game
     * @param events the number of events triggered throughout the game
     * @param itemsUsed number of items used by the player
     * @param itemsPickedUp number of items picked up by the player
     */
    public GameResult(int elapsedSeconds, int events, int itemsUsed, int itemsPickedUp) {
        this.timeElapsed = elapsedSeconds;
        this.eventsTriggered = events;
        this.itemsUsed = itemsUsed;
        this.itemsPickedUp = itemsPickedUp;
    }

    // getters for all the different counters and stats
    // add more here when more counters/stats are added to the game

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
