package com.C11.game.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Loads a Tiled (.tmx) map and gets what the game needs from it: size, unit scale, rendering and collisions.
 * Everything outside this class works in 'world units', where 1 unit = 1 tile.
 * Other classes should not access the TiledMap or layer names directly
 * 
 * @req UR_MAP UR_BOUNDARIES FR_MAP_TEXTURE FR_MAP_BOUNDARY
 * 
 * @author Dorian Kerzan (rvl546@york.ac.uk)
 */
public class GameMap implements Disposable {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float unitScale;
    private float worldWidth;
    private float worldHeight;

    /**
     * Name of the Tiled object layer that contains all collision rectangles.
     * Must match the object layer's name in Tiled
     */
    private static final String COLLISION_LAYER = "collision";

    private final Array<Rectangle> collisionRects;

    /** 
     * Loads the map file using the mapFilePath param.
     * Gets the collision rectangles, world width/height, and unit scale from the map file.
     * Creates the map renderer and adds map boundaries.
     * 
     * @req FR_MAP_TEXTURE
     * 
     * @param mapFilePath path relative to the assets folder, e.g. "maps/campus.tmx"
     */
    public GameMap(String mapFilePath) {
        map = new TmxMapLoader().load(mapFilePath);

        // -- Fix tiling edges caused by float imprecisions by using Nearest Neighbour Filtering on the texture during the runtime (in case of edge boundaries shifted it will use that neighbouring pixels)
        // UPD: Was not tested yet - may not work if tile bounds match the texture dimensions rather than extending by 1
        // UPD2: TODO: Tile bounds do indeed need to be increased
        TiledMapTileSet tileset = map.getTileSets().getTileSet(0);

        for (TiledMapTile tile : tileset) {
            if (tile instanceof StaticTiledMapTile) {
                TextureRegion region = tile.getTextureRegion();
                region.getTexture().setFilter(
                    Texture.TextureFilter.Nearest,
                    Texture.TextureFilter.Nearest
                );
            }
        }

        collisionRects = new Array<>();

        worldWidth = map.getProperties().get("width", Integer.class);
        worldHeight = map.getProperties().get("height", Integer.class);

        unitScale = 1f/map.getProperties().get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);

        loadCollisionRects();
        addMapBoundary();
    }

    // Tiled stores rectangles in pixels; scale to world units. The y-axis flip is already done by TmxMapLoader
    /** @req FR_MAP_COLLISION */
    private void loadCollisionRects() {
        // TODO: add in a null check for layers
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

    // Four 1 tile thick walls around the map, creating a boundary at the map edges so nothing can leave it
    /** @req FR_MAP_BOUNDARY */
    private void addMapBoundary() {
        collisionRects.add(new Rectangle(-1, 0, 1, worldHeight));
        collisionRects.add(new Rectangle(worldWidth, 0, 1, worldHeight));
        collisionRects.add(new Rectangle(0, -1, worldWidth, 1));
        collisionRects.add(new Rectangle(0, worldHeight, worldWidth, 1));
    }

    /**
     * Returns whether the given area overlaps with a collision rectangle or map boundary.
     * 
     * @param bounds area in world units.
     * @return true if blocked
     */
    public boolean isBlocked(Rectangle bounds) {
        for (Rectangle rect : collisionRects) {
            if (rect.overlaps(bounds)) {
                return true;
            }
        }
        return false;
    }

    /** 
     * @return world width in tiles (e.g. 60 tiles wide)
     */
    public float getWorldWidth() {
        return worldWidth;
    }

    /**
     * @return world height in tiles (e.g. 40 tiles high)
     */
    public float getWorldHeight() {
        return worldHeight;
    }

    /**
     * @return world units per pixel (1 / tile width)
     */
    public float getUnitScale() {
        return unitScale;
    }

    /** 
     * Draws the visible part of the map using the camera's view 
     * 
     * @req FR_MAP_TEXTURE
     * 
     * @param camera the camera whose view decides what part of the map is drawn
     */
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
