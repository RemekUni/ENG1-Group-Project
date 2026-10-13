package com.C11.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * An abstract Hitbox class that creates a rectangle around an area on the map and the sprite and checks
 * if the sprite overlaps that as they move.
 * It extends the Rectangle Class as the required "overlaps()" method is already defined.
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 */
public abstract class Hitbox extends Rectangle {

    public Hitbox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void onHit(){
        //Framework to add functionality to different hitboxes
    }
}

