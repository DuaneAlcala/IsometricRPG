package world.entities.item;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Swords allow players to inflict greater damage to
 * in-game skeleton objects.
 */
public class Sword extends Item {

    /**
     * Constructs a new sword object.
     */
    public Sword() {
        super("sword", "a sword");
    }

    /**
     * Updates in-game references to reflect that the item
     * can in-fact be used.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return "";
    }

    /**
     * Required implementation by the Item class.
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
