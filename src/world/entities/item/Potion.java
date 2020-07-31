package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Potions are in-game items used to increase player health.
 * Similar to the use-case for armour.
 */
public class Potion extends Item {

    /**
     * Construct a new potion object
     */
    public Potion() {
        super("potion", "a potion");
        this.useMessage = "Drink potion";
    }

    /**
     * Method required and implemented by the Item class. Returns
     * use message to update in-game button references based on
     * any given tile.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return useMessage;
    }

    /**
     * Allows players to use potions within the game, and updates all player attributes
     * such as health, accordingly.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        System.out.println("Before: " + player.getHealth());
        player.setHealth(player.getHealth() + 10);
        System.out.println("After: " + player.getHealth());
        player.consumeItem(itemIndex);

    }
}
