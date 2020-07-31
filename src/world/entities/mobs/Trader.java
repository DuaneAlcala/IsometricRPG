package world.entities.mobs;
import gui.TraderDialog;
import world.Game;
import world.entities.item.Book;
import world.entities.item.Item;
import world.entities.item.Key;
import world.entities.item.Sword;
import java.util.ArrayList;
import java.util.List;

/**
 * Traders never move, however can be interacted with as NPC's
 * within the game, allowing the player to trade for items, based
 * on the coins that they may have,
 */
public class Trader extends Mob {
    //A reference to the items the trader has to offer.
    private List<Item> items = new ArrayList<>();

    /**
     * Constructor for a new trader object.
     */
    public Trader() {
        super("trader", "Use a coin to get items from the trader");
        items.add(new Key());
        items.add(new Book());
        items.add(new Sword());

        this.interactMessage = "Trade";
    }

    /**
     * Constructor for persistence package.
     * @param items
     */
    public Trader(List<Item> items) {
        super("trader", "Use a coin to get items from the trader");
        this.items = items;

        if(items.isEmpty()) {
        	items.add(new Key());
            items.add(new Book());
            items.add(new Sword());
        }
    }

    /**
     * Add a new item for the trader to potentially trade with the player.
     * @param item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Process the trade between the player and the trader, updating
     * all in-game references to the two objects being traded.
     * @param game
     * @param player
     */
    public void trade(Game game, Player player) {
        new TraderDialog(game, player, this);
    }

    /**
     * Returns a list of the item held / in possession of the trader.
     * @return
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Removes an item based on the indexed position within the traders
     * items arraylist.
     * @param index
     */
    public void removeItem(int index) {
        items.remove(index);
    }

    /**
     * Updates in-game button references to allow the player to
     * trade and interact with the trader.
     * @return
     */
    @Override
    public String getInteractMessage() {
        return interactMessage;
    }
}
