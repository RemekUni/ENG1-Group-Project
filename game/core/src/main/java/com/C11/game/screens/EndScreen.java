package com.C11.game.screens;

import com.C11.game.utils.GameResult;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EndScreen extends MenuScreen{
    private static final String TITLE = "End Menu";
    private static final Color BG_COLOR = Color.BLACK;

    public EndScreen(Skin skin, GameResult result, Runnable onMainMenu, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        addBodyLabel("GAME OVER!");
        addBodyLabel("Time Elapsed: " + result.getTimeElapsed() / 60 + ":" + String.format("%02d", result.getTimeElapsed() % 60));
        addBodyLabel("Events Triggered: " + result.getEventsTriggered());
        addBodyLabel("Items Used: " + result.getItemsUsed());

        addButton("Back To Main Menu", onMainMenu);
        addButton("Exit Game", onExit);
    }
    
}