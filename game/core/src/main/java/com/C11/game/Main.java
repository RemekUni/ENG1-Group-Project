package com.C11.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.C11.game.screens.EndScreen;
import com.C11.game.screens.GameScreen;
import com.C11.game.screens.StartScreen;
import com.C11.game.settings.Settings;
import com.C11.game.utils.FontGenerator;
import com.C11.game.world.Assets;
import com.C11.game.stats.GameResult;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Root game class: It creates resources shared by all screens (spriteBatch) and shows the first screen.
 * Screens borrow the batch using getBatch() and MUST NOT dispose it.
 */
public class Main extends Game {
    private SpriteBatch spriteBatch;
    private Skin skin;

    private Settings settings;

    /**
     * Returns the shared spriteBatch
     *
     * @return the shared spriteBatch; owned and disposed by Main
     */
    public SpriteBatch getBatch() {
        return spriteBatch;
    }

    /**
     * Returns the shared skin
     * 
     * @return the shared skin, owned and disposed by Main
     */
    public Skin getSkin() {
        return skin;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        BitmapFont font = FontGenerator.generateBitmapFont(Assets.TIMER_FONT, Config.REGULAR_FONT_SIZE, Color.WHITE);
        BitmapFont titleFont = FontGenerator.generateBitmapFont(Assets.TIMER_FONT, Config.TITLE_FONT_SIZE, Color.WHITE);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
        skin = new Skin(textureAtlas);
        skin.add("default-font", font, BitmapFont.class);
        skin.add("title-font", titleFont, BitmapFont.class);
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        settings = new Settings(Gdx.app.getPreferences(Settings.PREFS_NAME));
        settings.applySaved();

        showStartScreen();
    }

    // helpers for cycling through the different screens. should grow larger once more screens added

    public void showStartScreen() {
        switchScreen(new StartScreen(skin, settings, minutes -> startGame(minutes), () -> Gdx.app.exit()));
    }

    public void startGame(int minutes) {
        int seconds = minutes * 60;
        switchScreen(new GameScreen(this, seconds));
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
