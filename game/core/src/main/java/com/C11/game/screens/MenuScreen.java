package com.C11.game.screens;

import java.util.Arrays;
import java.util.function.Consumer;

import com.C11.game.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Base template for static screens, which owns its own stage.
 * Tables are used for designing the layout where you can add buttons, titles, bodies etc.
 * When shown, input processor is set to its respective screen for button clicks
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public abstract class MenuScreen extends ScreenAdapter {
    private final Stage stage;
    private final Color bgColor;
    private final Skin skin;

    // fraction of the root table's width
    private static final float BUTTON_WIDTH_FRACTION = 0.4f;
    // in UI viewport units, not world units
    private static final float BUTTON_PADDING = 10f;

    private static final float TITLE_PADDING_BOT = 30f;
    private static final float TITLE_PADDING_TOP = 10f;

    private static final float BODY_PADDING_BOT = 10f;

    // matches game view's aspect ratio
    private static final float UI_WIDTH = 1280f;
    private static final float UI_HEIGHT = 720f;

    private final Table root;
    private final Table buttons;
    private final Table body;
    private final Table sliders;
    private final Table content;

    /**
     * Main constructor for the base menu screens. it sets up the global stage and tables that get reused with every menu screen.
     * 
     * @param skin the ui skin owned by main
     * @param title the title of the screen
     * @param color background colour of the screen
     */
    protected MenuScreen(Skin skin, String title, Color color) {
        stage = new Stage(new ExtendViewport(UI_WIDTH, UI_HEIGHT));
        bgColor = color;
        this.skin = skin;

        root = new Table();
        body = new Table();
        sliders = new Table();
        buttons = new Table();
        content = new Table();

        root.setFillParent(true);
        stage.addActor(root);

        Label titleLabel = new Label(title, skin, "title");

        // root is the main table for the entire screen with body and button tables inside it
        // the button table will sit underneath the body table
        root.add(titleLabel).padBottom(TITLE_PADDING_BOT).padTop(TITLE_PADDING_TOP).row();
        content.add(body).padBottom(BODY_PADDING_BOT).row();
        content.add(sliders).row();
        content.add(buttons).row();
        root.add(content).expandY();
    }

    /**
     * helper for adding buttons to the screen
     * 
     * @param buttonText name displayed on the button
     * @param onClick runnable that listens for clicks on the button
     */
    protected void addButton(String buttonText, Runnable onClick) {
        TextButton button = new TextButton(buttonText, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.run();
            }
        });

        buttons.add(button).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
    }

    /**
     * helper for adding a button that cycles through the fixed set of options passed into it on each click
     * 
     * @param currentCycleOption text shown before the current option
     * @param options the options that the button can cycle through
     * @param initial the option selected when the screen opens
     * @param onChange called with the selected option after every click
     */
    protected <Options> void addCycleButton(String currentCycleOption, Options[] options, Options initial, Consumer<Options> onChange) {
        int[] index = {Arrays.asList(options).indexOf(initial)};

        TextButton button = new TextButton(currentCycleOption + ": " + options[index[0]], skin);
        button.addListener(new ChangeListener() {
            @Override 
            public void changed(ChangeEvent event, Actor actor) {
                index[0] = (index[0] + 1) % options.length;
                Options selected = options[index[0]];
                button.setText(currentCycleOption + ": " + selected);
                onChange.accept(selected);
            }
        });

        buttons.add(button).width(Value.percentWidth(BUTTON_WIDTH_FRACTION, root)).pad(BUTTON_PADDING).row();
    }

    /**
     * helper for adding text to the body of the screen
     * 
     * @param text any text to be displayed in the body for that specific row
     */
    protected void addBodyLabel(String text) {
        Label label = new Label(text, skin);
        body.add(label).row();
    }

    protected Slider addSlider(float min, float max, float stepSize, boolean vertical, String sliderName) {
        Slider slider = new Slider(min, max, stepSize, vertical, skin);
        Label name = new Label(sliderName, skin);
        Label value = new Label(String.valueOf((int) min), skin);
        slider.setValue(min);
        slider.addListener(new ChangeListener() {
            @Override 
            public void changed(ChangeEvent event, Actor actor) {
                value.setText(String.valueOf((int) slider.getValue()));
            }
        });

        sliders.add(name).row();
        sliders.add(slider);
        sliders.add(value);
        return slider;
    }

    @Override 
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override 
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override 
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override 
    public void dispose() {
        stage.dispose();
    }
}
