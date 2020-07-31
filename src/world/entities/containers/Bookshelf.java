package world.entities.containers;
import gui.ContainerDialog;
import world.Game;
import world.entities.item.Item;
import world.entities.mobs.Player;
import java.util.List;

/**
 * Bookshelf objects extend Container and can be used
 * to store any given number of items.
 */
public class Bookshelf extends Container {

    /**
     * Constructor for an empty bookshelf.
     */
    public Bookshelf() {
        super("bookshelf", "a bookshelf");
    }

    /**
     * Constructor for a bookshelf containing a list of different
     * items
     *
     * @param items
     */
    public Bookshelf(List<Item> items) {
        super("bookshelf", "a bookshelf", items);
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
