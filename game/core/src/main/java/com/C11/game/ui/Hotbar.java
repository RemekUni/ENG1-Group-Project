package com.C11.game.ui;

import com.C11.game.Config;
import com.C11.game.world.Assets;
import com.C11.game.inventory.Inventory;
import com.C11.game.inventory.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * UI representation of the player's inventory in the form of a hotbar.
 *
 * @req FR_INVENTORY
 * @req FR_ITEMS
 *
 * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
 */

public class Hotbar extends Table {

    private static final int NONE = -1;
    private static final float SLOT_SIZE = 1f; // world units, temp

    private final Inventory inventory;
    private final Array<Slot> slots;

    private int selected = NONE;
    private int lastSelected = 0;

    public Hotbar(Inventory inventory) {
        this.inventory = inventory;

        int count = Config.MAX_INVENTORY_SIZE;
        slots = new Array<>(count);

        top().right(); // anchor children to the top-right of the root table

        for (int i = 0; i < count; i++) {
            Slot slot = new Slot(i);
            slots.add(slot);
            add(slot);
        }
    }

    public void select(int i) {
        if (i >= 0 && i < slots.size) {
            if (selected == i) toggleSelect();
            else setSelected(i);
        }
    }

    /** Hide the selection, or restore the last selected slot. */
    public void toggleSelect() {
        if (selected != NONE) {
            lastSelected = selected;
            setSelected(NONE);
        } else {
            setSelected(lastSelected);
        }
    }

    /** Single place selection changes, only the old and new slot get touched. */
    private void setSelected(int newIndex) {
        if (newIndex == selected) return;
        if (selected != NONE) slots.get(selected).setSelected(false);
        selected = newIndex;
        if (selected != NONE) slots.get(selected).setSelected(true);
    }

    public int getSelectedIndex() {
        return selected;
    }

    /** @return the selected item, or null if nothing is selected or the slot is empty. */
    public Item getSelectedItem() {
        Array<Item> items = inventory.getContainer();
        return (selected != NONE && selected < items.size) ? items.get(selected) : null;
    }

    /**
     * Represents a single slot in the hotbar
     *
     * @req FR_ITEMS
     * @req FR_INVENTORY
     *
     * @author Nicholas Glushchak (qgt520@york.ac.uk) | revolutionercik
     */
    private class Slot extends Actor {
        private final int index;
        private Texture background = Assets.SLOT_UNSELECTED_TEXTURE;

        private Slot(int index) {
            this.index = index;
            setSize(SLOT_SIZE, SLOT_SIZE);
        }

        private void setSelected(boolean isSelected) {
            background = isSelected ? Assets.SLOT_SELECTED_TEXTURE : Assets.SLOT_UNSELECTED_TEXTURE;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(background, getX(), getY(), getWidth(), getHeight());

            Array<Item> items = inventory.getContainer();
            if (index < items.size) {
                batch.draw(items.get(index).getTexture(), getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
