package com.C11.game.ui;

import com.C11.game.utils.FontGenerator;
import com.C11.game.utils.Timer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Provides a specific implementation of the Timer class which provides a text display.
 *
 * @req FR_TIMER
 *
 * @author Paul Turner (sfn544@york.ac.uk)
 */
public class TimerDisplay implements Disposable {
    private Timer timer;
    private BitmapFont font;
    private int initialTime;

    /**
     * @param fontFile
     * @param initialTime
     */
    public TimerDisplay(String fontFile, int initialTime) {
        timer = new Timer(1f);
        font = FontGenerator.generateBitmapFont(fontFile, 48, Color.WHITE);
        this.initialTime = initialTime;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getInitialTime() {
        return initialTime;
    }

    /**
     * Draws the remaining time on the timer before the ticks attribute equals the initialTime attribute.
     * @param batch
     * @param x
     * @param y
     */
    public void draw(SpriteBatch batch, float x, float y) {
        String displayTime = Integer.toString(initialTime - timer.getTicks());
        font.draw(batch, displayTime, x, y);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
