package com.C11.game;

import com.badlogic.gdx.Game;
import com.C11.game.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch spriteBatch;

    public SpriteBatch getBatch() {
        return spriteBatch;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        setScreen(new GameScreen(this));
        
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        spriteBatch.dispose();
    }
}
