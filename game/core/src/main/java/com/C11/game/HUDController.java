package com.C11.game;

import com.C11.game.entities.Player;
import com.C11.game.ui.Hotbar;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Controls input for the HUD elements
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */
public class HUDController extends InputAdapter {
    private final Player player;
    private final Hotbar hotbar;

    public HUDController(Player player, Hotbar hotbar) {
        this.player = player;
        this.hotbar = hotbar;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Keys.NUM_1 <= keycode && keycode < Keys.NUM_1 + Config.MAX_INVENTORY_SIZE) {
            hotbar.select(keycode - Keys.NUM_1);
            return true;
        }
        if (keycode == Keys.Q) {
            hotbar.toggleSelect();
            return true;
        }
        // TODO: Fix a bug where whatever item you remove by index from array, visually from Hotbar it will always remove the last item
        if (keycode == Keys.F) {
            player.useItem(hotbar.getSelectedIndex());
            return true;
        }
        if (keycode == Keys.G) {
            player.throwItem(hotbar.getSelectedItem());
            return true;
        }

        return false;
    }
}
