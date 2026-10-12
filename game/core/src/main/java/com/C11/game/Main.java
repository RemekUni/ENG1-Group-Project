package com.C11.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.Color;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    Viewport viewport;

    static final float VIEW_WIDTH = 16f;
    static final float VIEW_HEIGHT = 9f;
    float unitScale;
    int worldWidth;
    int worldHeight;

    @Override
    public void create() {
        map = new TmxMapLoader().load("maps/campus.tmx");

        worldWidth = map.getProperties().get("width", Integer.class);
        worldHeight = map.getProperties().get("height", Integer.class);
        
        unitScale = 1f/map.getProperties().get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);

        camera.position.set(worldWidth/2f, worldHeight/2f, 0);

        spriteBatch = new SpriteBatch();
        
    }

    @Override 
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
