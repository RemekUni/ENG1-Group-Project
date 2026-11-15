package com.C11.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A Timer which returns true on update() when the set time has elapsed. Will repeat
 * until specifically stopped.
 *
 * @req FR_TIMER
 * @req FR_PAUSE
 *
 * @author Paul Turner (sfn544@york.ac.uk)
 */
public class Timer {
    private long startTime;
    private long currentTime;
    private long length;
    private boolean playing = false;
    private int ticks = 0;

    /**
     * @param tickLength the time between ticks in seconds
     */
    public Timer(float tickLength) {
        setLength(tickLength);
    }

    /**
     * Pauses the timer while retaining its elapsed time.
     *
     * @req FR_PAUSE
     */
    public void pause() {
        if (playing) {
            playing = false;
        } else {
            System.out.println("Timer warning: Timer is already paused!");
        }
    }

    /**
     * Unpauses the timer and allows it to continue from when it was paused.
     *
     * @req FR_PAUSE
     */
    public void unpause() {
        if (!playing) {
            playing = true;
        } else {
            System.out.println("Timer warning: Timer is already playing!");
        }
    }

    /**
     * Starts the timer from a new start time.
     */
    public void start() {
        if (!playing) {
            startTime = TimeUtils.millis();
            currentTime = startTime;
            playing = true;
        } else {
            System.out.println("Timer warning: Timer is already started!");
        }
    }

    /**
     * Stops and resets the timer attributes.
     */
    public void stop() {
        if (playing) {
            playing = false;
            ticks = 0;
        } else {
            System.out.println("Timer warning: Timer is already stopped!");
        }
    }

    /**
     * Checks to see if the set time has elapsed. Will only check if the timer is playing.
     *
     * @req FR_TIMER
     *
     * @return true when the timer reaches the set time; false on all other occasions.
     */
    public boolean update() {
        if (playing) {
            long deltaTime = Math.round(Gdx.graphics.getDeltaTime() * 1000);
            currentTime += deltaTime;
            if (currentTime >= length + startTime) {
                startTime = TimeUtils.millis();
                currentTime = startTime;
                ticks++;
                return true;
            }
        }
        return false;
    }

    public void setLength(float tickLength) {
        length = (long) Math.round(tickLength * 1000);  // Convert seconds to ms
    }

    public boolean isPlaying() {
        return playing;
    }

    public long getElapsedTime() {
        return (length + startTime) - currentTime;
    }

    public int getTicks() {
        return ticks;
    }
}
