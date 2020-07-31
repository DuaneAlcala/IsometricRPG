package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;
import world.entities.mobs.Trader;

/**
 * An in-game currency which can be used to trade for items
 * by interacting with specified NPC's.
 */
public class Coin extends Item {

    /**
     * Constructor for a new coin object.
     */
    public Coin() {
        super("coin", "a coin");
    }

    /**
     * Updates the corresponding button-values in-game
     * for the coin to be used.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return "";
    }

    /**
     * Coin objects cannot be 'used' and only traded.
     * As Coin extends item, this method must be implemented
     * however is never used throughout the program's execution.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {

    }
}
