package world.entities.containers;
import world.Game;
import world.entities.Entity;
import world.entities.item.Item;
import world.entities.mobs.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Containers are the underlying data type structures
 * used to hold other item types throughout the game.
 */
public abstract class Container extends Entity {
    // A list pertaining all of the currently held items within the container
    protected List<Item> items = new ArrayList<>();

    /**
     * Constructor for empty container type.
     *
     * @param name
     * @param description
     */
    public Container(String name, String description) {
        super(name, description);

        this.interactMessage = "Open " + this.name;
    }

    /**
     * Constructor for container containing a list of item objects.
     *
     * @param name
     * @param description
     */
    public Container(String name, String description, List<Item> items) {
        super(name, description);
        this.items.addAll(items);
    }

    /**
     * Method used to add items to this specified container.
     *
     * @param item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Allows for players to freely interact (add items and take them
     * out of) the current container.
     *
     * @param game
     * @param player
     */
    public void open(Game game, Player player) {
        // Actually opens
        if(items.isEmpty()) { return; }
    }

    /**
     * Returns the interaction's message, updating the GUI based on the
     * events carried out by the player.
     *
     * @return
     */
    @Override
    public String getInteractMessage() {
        return interactMessage;
    }

    /**
     * Retrieves a list of items contained within the container.
     *
     * @return
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Removes items from the container for the users.
     *
     * @param index
     */
    public void removeItem(int index) {
        items.remove(index);
    }
}

