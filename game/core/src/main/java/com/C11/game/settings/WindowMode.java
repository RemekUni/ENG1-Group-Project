package com.C11.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

/**
 * Enum for the different window mode options and setting them
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public enum WindowMode {
    FULLSCREEN,
    BORDERLESS,
    WINDOWED;

    // the base window size
    public static final int WINDOWED_WIDTH = 1280;
    public static final int WINDOWED_HEIGHT = 720;

    /**
     * apply method to run the logic for the set window mode
     */
    public void apply() {
        DisplayMode display = Gdx.graphics.getDisplayMode();

        // switch case logic for each window mode
        switch (this) {
            case FULLSCREEN -> Gdx.graphics.setFullscreenMode(display);
            case BORDERLESS -> {
                Gdx.graphics.setUndecorated(false);
                Gdx.graphics.setWindowedMode(display.width, display.height);
            }
            case WINDOWED -> {
                Gdx.graphics.setUndecorated(false);
                if (WINDOWED_WIDTH < display.width && WINDOWED_HEIGHT < display.height) {
                    Gdx.graphics.setWindowedMode(WINDOWED_WIDTH, WINDOWED_HEIGHT);
                } else {
                    Gdx.graphics.setWindowedMode(display.width / 2, display.height / 2);
                }
                
            }
        }

        // do not allow the user to set their own screen size
        Gdx.graphics.setResizable(false);
    }
}
