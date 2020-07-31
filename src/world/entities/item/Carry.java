package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class is defined to allow a number of 'carry' objects,
 * which can store other game objects within them.
 */
public abstract class Carry extends Item {
    /**
     * The following fields represent the maximum number of items which
     * can be stored within the carry, as well as a list of the items
     * which the carry actually contains (respectively).
     */
    protected int cap;
    protected List<Item> items = new ArrayList<>();

    /**
     * Constructor for a new carry object.
     * @param name
     * @param description
     */
    public Carry(String name, String description) {
        super(name, description);
        this.useMessage = "Open " + this.getName();
    }

    /**
     * Returns the maximum items that can be held.
     * @return carry_cap.
     */
    public int getCap() {
        return cap;
    }

    /**
     * Allows users to set different cap limits for
     * different carry objects.
     * @param cap
     */
    public void setCap(int cap) {
        this.cap = cap;
    }

    /**
     * Add a item to the carry, storing it within
     * the object.
     * @param item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Returns a list of the items contained
     * within the carry object.
     * @return
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Remove an item from the carry.
     * This method is also called when the
     * item is used by the player.
     * @param index
     */
    public void removeItem(int index) {
        items.remove(index);
    }

    /**
     * Updates the in-game button-message which allows players
     * to view the items held within their carry type object.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return useMessage;
    }
}
