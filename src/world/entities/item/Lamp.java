package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Used to illuminate a radial area around the user, based on the
 * passing of time (once it is night) in-game. This increases player
 * visibility.
 */
public class Lamp extends Item {
    /**
     * Constructor for a new game object.
     */
    public Lamp() {
        super("lamp", "a lamp");
    }

    /**
     * Required method that must be implemented as part of the item class.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return "";
    }

    /**
     * Allows the player to use the lamp item in-game, illuminating surrounding area.
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
