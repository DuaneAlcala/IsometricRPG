package world.entities.containers;
import gui.ContainerDialog;
import world.Game;
import world.entities.item.Item;
import world.entities.mobs.Player;
import java.util.List;

/**
 * Chest objects also extend Container and can be used
 * to store any given number of items.
 */
public class Chest extends Container {

    /**
     * Constructor for an empty chest.
     */
    public Chest() {
        super("chest", "a chest");
    }

    /**
     * Constructor for a chest containing a list of different
     * store-able items.
     *
     * @param items
     */
    public Chest(List<Item> items) {
        super("chest", "a chest", items);
    }

    /**
     * Allows players to open and interact with bookshelf (containers)
     * within the game.
     *
     * @param game
     * @param player
     */
    public void open(Game game, Player player) {
        new ContainerDialog(game, player, this);
    }
}
