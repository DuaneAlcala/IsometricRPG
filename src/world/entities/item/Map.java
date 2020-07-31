package world.entities.item;
import gui.MapDialog;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Used to provide players with a true reference to the game world,
 * and allows players to effectively navigate the game.
 */
public class Map extends Item {

    /**
     * Construct a new map object
     */
    public Map() {
        super("map", "a map");
        this.useMessage = "Open map";
    }

    /**
     * Method is required by Item class, and returns the use message,
     * based on the provided tile.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return useMessage;
    }

    /**
     * Allows the users to view the map in-game (should they hold one
     * within their inventory).
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        new MapDialog(game, player);
    }
}
