package com.C11.game.ui;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * PauseMenu is an overlay that is drawn on top of the paused game, not a separate screen.
 * This class dims the game and shows buttons.
 * The GameScreen creates it, calls render/resize/show and then disposes it.
 * 
 * @req FR_PAUSE
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class PauseMenu implements Disposable {
    private final Stage stage;

    // the value for how much the pause screen dims the paused game (0 = no dim, 1 = fully black)
    private static final float DIM_ALPHA = 0.5f;

    // fraction of the root table's width
    private static final float BUTTON_WIDTH_FRACTION = 0.3f;
    // in UI viewport units, not world units
    private static final float BUTTON_PADDING = 10f;

    // matches game view's aspect ratio
    private static final float UI_WIDTH = 1280f;
    private static final float UI_HEIGHT = 720f;

    /**
     * Loads skins (assets for the pause menu) and sets up the stage (holds actors and has a viewport).
     * It creates a table template that the buttons get put on and listeners defined for each button.
     * 
     * @param skin the shared skin created and disposed by Main
     * @param onResume runs when the resume button is pressed
     * @param onExit runs when the exit game button is pressed
     */
    public PauseMenu(Skin skin, Runnable onResume, Runnable onExit) {
        stage = new Stage(new ExtendViewport(UI_WIDTH, UI_HEIGHT));

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", 0f, 0f, 0f, DIM_ALPHA));
        stage.addActor(root);

        Table controlsPanel = new Table();
        controlsPanel.setFillParent(true);
        controlsPanel.setBackground(skin.newDrawable("white", 0f, 0f, 0f, 1));
        controlsPanel.setVisible(false);
        stage.addActor(controlsPanel);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onResume.run();
            }
        });

        TextButton controlsButton = new TextButton("Controls", skin);
        controlsButton.addListener(new ChangeListener() {
            @Override 
            public void changed(ChangeEvent event, Actor actor) {
                controlsPanel.setVisible(true);
            }
        });

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onExit.run();
            }
        });
        root.add(resumeButton).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
        root.add(controlsButton).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
        root.add(exitButton).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();

        TextButton closeControlsButton = new TextButton("Close", skin);
        closeControlsButton.addListener(new ChangeListener() {
            @Override 
            public void changed(ChangeEvent event, Actor actor) {
                controlsPanel.setVisible(false);
            }
        });

        controlsPanel.add(closeControlsButton).expandX().right().row();
        addTableBodyLabel("Player Movement: WASD / Arrow Keys", skin, controlsPanel);
        addTableBodyLabel("Select Inventory Slot: 1-2", skin, controlsPanel);
        addTableBodyLabel("Cycle Inventory Slot: Q", skin, controlsPanel);
        addTableBodyLabel("Use Item: F", skin, controlsPanel);
        addTableBodyLabel("Throw Item: G", skin, controlsPanel);
        addTableBodyLabel("Pause Game: ESCAPE", skin, controlsPanel);
    }

    private void addTableBodyLabel(String text, Skin skin, Table table) {
        Label label = new Label(text, skin);
        table.add(label).row();
    }

    /**
     * renders the pause menu screen
     * call after drawing the game world so that the overlay ends up on top
     * 
     * @param delta seconds since last frame
     */
    public void render(float delta) {
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    /**
     * resizes the UI viewport given the width and height
     * must be forwarded from the owning screen's resize
     * keep the true argument as it centers the camera
     * @param width new window width in pixels
     * @param height new window height in pixels
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * enables inputs on the pause menu screen, taking over from the game. call 'hide()' to resume.
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * disables inputs on the pause menu screen.
     */
    public void hide() {
        // TODO: currently set to null which is ok as we have no input anywhere else, but once we add input for buttons on the main screen, the null should change to give back inputs to the game screen
        Gdx.input.setInputProcessor(null);
    }

    // the menu cannot be used after being disposed
    @Override 
    public void dispose() {
        stage.dispose();
    }
    
}
