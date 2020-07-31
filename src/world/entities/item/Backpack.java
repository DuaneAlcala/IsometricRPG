package world.entities.item;
import gui.CarryDialog;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;
import java.util.ArrayList;
import java.util.List;

public class Backpack extends Carry {
    // This cap represents the maximum number of items a backpack can hold.
    int cap = 2;

    /**
     * Constructor for a new backpack object.
     */
    public Backpack() {
        super("backpack", "a backpack");
    }

    /**
     * Allows the player to use the backpack in-game, updating all of the player's
     * attributes within the game to indicate that the item has been used. Using the
     * backpack indicates the items stored within the carry.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        new CarryDialog(game, player, this);
    }
}
