package com.C11.game.screens;

import java.util.function.IntConsumer;

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
    
    private static final int MIN_GAME_MINUTES = 1;
    private static final int MAX_GAME_MINUTES = 5;
    private static final int GAME_MINUTES_STEP = 1;

    /**
     * Main constructor for the start screen that contains a title and two buttons
     * 
     * @param skin the ui skin owned by main
     * @param onStart runnable for 'Start Game' button
     * @param onExit runnable for 'Exit Game' button
     */
    public StartScreen(Skin skin, IntConsumer onStart, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        Slider timeSlider = addSlider(1, 5, 1, false, "Game Time (minutes)");

        addButton("Start Game", () -> {
            int minutes = (int) timeSlider.getValue();
            onStart.accept(minutes);
        });
        addButton("Exit Game", onExit);
    }
    
}
