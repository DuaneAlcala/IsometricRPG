package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

public class Armour extends Item {

    /**
     * Constructor for a new Armour object
     */
    public Armour() {
        super("armour", "an armour");
        this.useMessage = "Put on armour";
    }

    /**
     * Updates the corresponding in-game button when the user selects to use
     * the 'Armour' (provided the player contains armour within their inventory).
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return useMessage;
    }

    /**
     * Allows the player to use the armour in-game, updating all of the player's
     * attributes within the game to indicate that the item has been used.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        int playerHealth = player.getHealth();
        player.setHealth(player.getHealth() + 5);
        player.consumeItem(itemIndex);
        renderer.addMessage("Your health has increased by: " + (player.getHealth() - playerHealth));
    }
}
