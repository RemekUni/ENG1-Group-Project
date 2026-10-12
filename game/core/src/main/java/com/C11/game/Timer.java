package com.C11.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {
    private long startTime;
    private int length;
    private boolean playing = false;

    public Timer(int length) {
        startTime = TimeUtils.millis();
        this.length = length;
    }

    public void play() {
        playing = true;
    }

    public void pause() {
        startTime = length - TimeUtils.timeSinceMillis(startTime);
        playing = false;
    }

    public boolean update() {
        if (playing && TimeUtils.timeSinceMillis(startTime) >= length) {
            startTime = TimeUtils.millis();
            return true;
        }
        return false;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isPlaying() {
        return playing;
    }
}
