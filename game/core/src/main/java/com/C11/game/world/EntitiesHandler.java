package com.C11.game.world;

import com.C11.game.entities.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class EntitiesHandler {
    private Array<Entity> entities = new Array<>();

    public EntitiesHandler() {

    }

    // libGDX Array's default iterator does not support nested iteration over the same array. [lwjgl3:runException in thread "main" com.badlogic.gdx.utils.GdxRuntimeException: #iterator() cannot be used nested.]
    // One case would be looping through each entity again in #ItemEntity inside #getCollisionCollectorEntity().
    // Use indexed loop instead
    // TODO: Ensure removing elements from inside nested loop would not break anything // UPD: it does. Items cannot be removed from the array
    public void updateEntities(float delta) {
        for (int i = 0; i < entities.size; i++) {
            entities.get(i).update(delta);
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < entities.size; i++) {
            entities.get(i).draw(batch);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public Array<Entity> getEntities() {
        return entities;
    }
}
