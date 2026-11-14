package com.C11.game.ui;

import com.C11.game.entities.Player;
import com.C11.game.world.Assets;
import com.C11.game.world.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Inventory extends Table {

    // private final Array<Slot> slots = new Array<>();
    private final Player player;
    private static int selectionIndex = -1; // -1 for no selected slots
    private static boolean isSlotSelected = false;

    public Inventory(Player player) {
        // root table fills the stage, children are anchored to its top-right
        this.top().right();
        this.player = player;

        buildSlots(player.getMaxInventorySize());
    }

    private void buildSlots(int inventorySize) {
        for (int i = 0; i < inventorySize; i++) {
            
            // Add positioned slots relative to an anchor point, drawn to the left of it - one after another
            Slot slot = new Slot(i);

            // slots.add(slot);
            add(slot)/* .size(1, 1) */;
        }
    }

    // public Slot getSlot(int index) {
    //     return slots.get(index);
    // }

    public static void selectSlot(int index) {
        selectionIndex = index;
        isSlotSelected = true;
    }

    public static void unselectSlot() {
        selectionIndex = -1;
        isSlotSelected = false;
    }

    public static int getSelectedSlotIndex() {
        return selectionIndex;
    }

    public static boolean isSlotSelected() {
        return isSlotSelected;
    }

    public class Slot extends Actor {
        private Texture slotTexture;
        // private Item item;
        private final int i;

        /**
         * Add slot widget, positioned relative to the parent stage layer.
         * @param x
         * @param y
         */
        Slot(int i) {
            slotTexture = Assets.SLOT_UNSELECTED_TEXTURE;

            this.i = i;

            setSize(1, 1);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Array<Item> inventory = player.getInventory();

            slotTexture = i == selectionIndex ? Assets.SLOT_SELECTED_TEXTURE : Assets.SLOT_UNSELECTED_TEXTURE;

            // background
            batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight()); // draw slot texture
            
            if (i < inventory.size) {
                batch.draw(inventory.get(i).getTexture(), getX(), getY(), getWidth(), getHeight());
            }
        }

        // public Item getItem() {
        //     return item;
        // }

        // public void setItem(Item item) {
        //     this.item = item;
        // }
    }

    
}