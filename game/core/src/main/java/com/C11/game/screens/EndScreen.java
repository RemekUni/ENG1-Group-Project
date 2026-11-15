package com.C11.game.screens;

import com.C11.game.stats.GameResult;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * End screen which will load once the game has ended in any way.
 * The end screen will take a snapshot of the stats and results from the game and display them,
 * along with back to main menu and exit buttons.
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class EndScreen extends MenuScreen{
    private static final String TITLE = "End Menu";
    private static final Color BG_COLOR = Color.BLACK;

    /**
     * Main constructor that builds the information and buttons that are displayed
     * 
     * @param skin the ui skin owned by main
     * @param result the snapshot stats and results used in the body of the end screen
     * @param onMainMenu runnable for the 'Back To Main Menu' button
     * @param onExit runnable for 'Exit Game' button
     */
    public EndScreen(Skin skin, GameResult result, Runnable onMainMenu, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        addBodyLabel("GAME OVER!");
        addBodyLabel("Time Elapsed: " + result.getTimeElapsed() / 60 + ":" + String.format("%02d", result.getTimeElapsed() % 60));
        addBodyLabel("Events Triggered: " + result.getEventsTriggered());
        addBodyLabel("Items Picked Up: " + result.getItemsPickedUp());
        addBodyLabel("Items Used: " + result.getItemsUsed());

        addButton("Back To Main Menu", onMainMenu);
        addButton("Exit Game", onExit);
    }
    
}