package com.C11.game.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GameMap implements Disposable {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float unitScale;
    private float worldWidth;
    private float worldHeight;

    private static final String COLLISION_LAYER = "collision";

    private final Array<Rectangle> collisionRects;

    public GameMap(String mapFilePath) {
        map = new TmxMapLoader().load(mapFilePath);
        collisionRects = new Array<>();

        worldWidth = map.getProperties().get("width", Integer.class);
        worldHeight = map.getProperties().get("height", Integer.class);

        unitScale = 1f/map.getProperties().get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);

        loadCollisionRects();
        addMapBoundary();
    }

    private void loadCollisionRects() {
        MapLayer layer = map.getLayers().get(COLLISION_LAYER);
        for (RectangleMapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle r = object.getRectangle();
            collisionRects.add(new Rectangle(
                r.x * unitScale,
                r.y * unitScale,
                r.width * unitScale,
                r.height * unitScale));
        }
    }

    private void addMapBoundary() {
        collisionRects.add(new Rectangle(-1, 0, 1, worldHeight));
        collisionRects.add(new Rectangle(worldWidth, 0, 1, worldHeight));
        collisionRects.add(new Rectangle(0, -1, worldWidth, 1));
        collisionRects.add(new Rectangle(0, worldHeight, worldWidth, 1));
    }

    public boolean isBlocked(Rectangle bounds) {
        for (Rectangle rect : collisionRects) {
            if (rect.overlaps(bounds)) {
                return true;
            }
        }
        return false;
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

    @Override 
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
