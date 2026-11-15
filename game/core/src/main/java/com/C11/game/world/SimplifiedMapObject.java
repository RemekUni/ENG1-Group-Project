package com.C11.game.world;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public record SimplifiedMapObject(Vector2 position, String name, MapProperties properties) {}
