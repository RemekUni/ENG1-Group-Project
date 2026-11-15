package com.C11.game.screens;

import java.util.function.IntConsumer;

import com.C11.game.Config;
import com.C11.game.settings.Settings;
import com.C11.game.settings.WindowMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Start screen for the game which will load when the game is opened with buttons to start and exit
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class StartScreen extends MenuScreen{
    private static final String TITLE = "Start Menu";
    private static final Color BG_COLOR = Color.BLACK;
    
    /**
     * Main constructor for the start screen that contains a title and two buttons
     * 
     * @param skin the ui skin owned by main
     * @param onStart runnable for 'Start Game' button
     * @param onExit runnable for 'Exit Game' button
     */
    public StartScreen(Skin skin, Settings settings, IntConsumer onStart, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        Slider timeSlider = addSlider(Config.MIN_GAME_MINUTES, Config.MAX_GAME_MINUTES, Config.GAME_MINUTES_STEP, false, "Game Time (minutes)");

        addButton("Start Game", () -> {
            int minutes = (int) timeSlider.getValue();
            onStart.accept(minutes);
        });
        addCycleButton("Display", WindowMode.values(), settings.getWindowMode(), mode -> {
            mode.apply();
            settings.setWindowMode(mode);
        });
        addButton("Exit Game", onExit);
    }
    
}
