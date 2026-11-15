package com.C11.game;

import com.C11.game.entities.Player;
import com.C11.game.ui.Hotbar;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameInput extends InputAdapter {
    private final Hotbar hotbar;
    private final int MAX_INVENTORY_SIZE;

    public GameInput(Player player, Hotbar hotbar) { 
        this.hotbar = hotbar;
        this.MAX_INVENTORY_SIZE = player.getInventory().MAX_INVENTORY_SIZE;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.NUM_1 <= keycode && keycode <= Input.Keys.NUM_1 + MAX_INVENTORY_SIZE) {
            hotbar.select(keycode - Input.Keys.NUM_1);
            return true;
        }
        if (keycode == Input.Keys.Q) {
            hotbar.toggleSelect();
            return true;
        }
        
        return false;
    }
}
