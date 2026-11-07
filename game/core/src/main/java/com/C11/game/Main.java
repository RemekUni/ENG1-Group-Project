package com.C11.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.C11.game.screens.EndScreen;
import com.C11.game.screens.GameScreen;
import com.C11.game.screens.StartScreen;
import com.C11.game.utils.GameResult;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Root game class: It creates resources shared by all screens (spriteBatch) and shows the first screen.
 * Screens borrow the batch using getBatch() and MUST NOT dispose it.
 */
public class Main extends Game {
    private SpriteBatch spriteBatch;
    private Skin skin;

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
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        showStartScreen();
        
    }

    // helpers for cycling through the different screens. should grow larger once more screens added

    public void showStartScreen() {
        switchScreen(new StartScreen(skin, () -> startGame(), () -> Gdx.app.exit()));
    }

    public void startGame() {
        switchScreen(new GameScreen(this));
    }

    public void showEndScreen(GameResult result) {
        switchScreen(new EndScreen(skin, result, () -> showStartScreen(), () -> Gdx.app.exit()));
    }

    private void switchScreen(Screen next) {
        Gdx.app.postRunnable(() -> {
            final Screen previous = getScreen();
            setScreen(next);
            if (previous != null) previous.dispose();
        });
    }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        spriteBatch.dispose();
        skin.dispose();
    }
}
