package com.C11.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Uses the gdx-freetype library to quickly and easily generate Bitmap fonts using .ttf files.
 *
 * @author Paul Turner (sfn544@york.ac.uk)
 */
public abstract class FontGenerator {
    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    /**
     * @param path
     * @param size
     * @param colour
     * @return BitmapFont object
     */
    public static BitmapFont generateBitmapFont(String path, int size, Color colour) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + path));
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        parameter.size = size;
        parameter.color = colour;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
