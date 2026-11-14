package com.C11.game.settings;

import com.badlogic.gdx.Preferences;

/**
 * Settings class that should hold game settings that persist on game restart
 * such as display settings and key binds.
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class Settings {
    // preferences file name
    public static final String PREFS_NAME = "goose-game-settings";

    private static final String KEY_WINDOW_MODE = "windowMode";
    private static final WindowMode DEFAULT_WINDOW_MODE = WindowMode.BORDERLESS;

    private final Preferences prefs;

    /**
     * sets the preferences from the preferences file, called by main
     * 
     * @param prefs
     */
    public Settings(Preferences prefs) {
        this.prefs = prefs;
    }

    /**
     * getter for the window mode setting
     * 
     * @return the current window mode e.g "FULLSCREEN"
     */
    public WindowMode getWindowMode() {
        String saved = prefs.getString(KEY_WINDOW_MODE, DEFAULT_WINDOW_MODE.name());
        try {
            return WindowMode.valueOf(saved);
        } catch (IllegalArgumentException e) {
            return DEFAULT_WINDOW_MODE;
        }
    }

    /**
     * method for setting the window mode
     * 
     * @param mode what mode to set the game
     */
    public void setWindowMode(WindowMode mode) {
        prefs.putString(KEY_WINDOW_MODE, mode.name());
        prefs.flush();
    }

    public void applySaved() {
        getWindowMode().apply();
    }
    
}
