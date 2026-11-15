package com.C11.game.world;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

/**
 * Specialised record for map objects for ease of access
 * 
 * @param position Vector2 position of the object
 * @param name name of the object
 * @param properties all properties of the object
 */
public record SimplifiedMapObject(Vector2 position, String name, MapProperties properties) {}
