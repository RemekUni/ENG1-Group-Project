package com.C11.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
    public StartScreen(Skin skin, Runnable onStart, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        addButton("Start Game", onStart);
        addButton("Exit Game", onExit);
    }
    
}
