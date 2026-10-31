package com.C11.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StartScreen extends MenuScreen{
    private static final String TITLE = "Start Menu";
    private static final Color BG_COLOR = Color.BLACK;

    public StartScreen(Skin skin, Runnable onStart, Runnable onExit) {
        super(skin, TITLE, BG_COLOR);

        addButton("Start Game", onStart);
        addButton("Exit Game", onExit);
    }
    
}
