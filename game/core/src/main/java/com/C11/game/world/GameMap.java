package com.C11.game.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GameMap {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float unitScale;
    private float worldWidth;
    private float worldHeight;

    public GameMap(String mapFilePath) {
        map = new TmxMapLoader().load(mapFilePath);

        worldWidth = map.getProperties().get("width", Integer.class);
        worldHeight = map.getProperties().get("height", Integer.class);

        unitScale = 1f/map.getProperties().get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public float getUnitScale() {
        return unitScale;
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
