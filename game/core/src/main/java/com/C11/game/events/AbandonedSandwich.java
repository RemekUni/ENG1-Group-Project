package com.C11.game.events;

import com.C11.game.Config;
import com.C11.game.entities.EntitiesHandler;
import com.C11.game.entities.AEntity;
import com.C11.game.entities.Goose;
import com.C11.game.entities.Player;
import com.C11.game.world.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An event where the player collects an abandoned sandwich and increases number of geese chasing.
 * The method will new geese to follow the player.
 *
 * @req FR_NEGATIVE_EVENT
 *
 * @author kieranmckenna (gnv523@york.ac.uk)
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class AbandonedSandwich extends EventTrigger {
    private final Player player;
    private final Texture gooseTexture;
    private final GameMap gameMap;
    private final EntitiesHandler entitiesHandler;

    public AbandonedSandwich(TextureRegion sandwichTexture, float startX, float startY, GameMap gameMap, float size, Player player, Texture gooseTexture, EntitiesHandler entitiesHandler) {
        super(sandwichTexture, startX, startY, gameMap, size, true); //You can only do this event once
        this.player = player;
        this.gooseTexture = gooseTexture;
        this.gameMap = gameMap;
        this.entitiesHandler = entitiesHandler;
    }

    /**
     * If a collision between player hitbox and sandwich hitbox, spawn more new geese to follow the player.
     * toDo: add hitboxes to the geese so they dont merge into one
     */
    @Override
    public void action() {

        for (int i = 0; i <= Config.SANDWICH_GEESE_COUNT; i++) {
            //Spawn the geese in a ring around the player
            double angle = (2 * Math.PI / Config.SANDWICH_GEESE_COUNT) * i;
            float spawnX = player.getPosition().x + (float) Math.cos(angle) * Config.SANDWICH_SPAWN_RADIUS;
            float spawnY = player.getPosition().y + (float) Math.sin(angle) * Config.SANDWICH_SPAWN_RADIUS;

            new Goose(gooseTexture, spawnX, spawnY, gameMap, player, entitiesHandler);
        }
    }
}
