package com.C11.game;

import com.badlogic.gdx.Game;
import com.C11.game.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** 
 * Root game class: It creates resources shared by all screens (spriteBatch) and shows the first screen.
 * Screens borrow the batch using getBatch() and MUST NOT dispose it.
 */
public class Main extends Game {
    private SpriteBatch spriteBatch;

    /**
     * Returns the shared spriteBatch
     * 
     * @return the shared spriteBatch; owned and disposed by Main
     */
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
