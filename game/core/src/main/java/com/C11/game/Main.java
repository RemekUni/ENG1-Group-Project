package com.C11.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private ShapeRenderer shapeRenderer;

    //Declare hitboxes
    private Hitbox player;
    private Hitbox hitbox;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        //Create new hitboxes around an item and the player
        shapeRenderer = new ShapeRenderer();

        player = new Player(100,100,150,200);
        hitbox = new HiddenItem(100,50,100,100);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();

        //Draw the hitboxes so visible to test
        //toDo: remove these boxes later, so not visible to player
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(player.x, player.y, player.width, player.height);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();

        //Checks if player and hitbox overlap
        if (player.overlaps(hitbox)){
            hitbox.onHit();
            System.out.println("Player in the hitbox");
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
