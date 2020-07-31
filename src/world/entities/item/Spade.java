package world.entities.item;
import utility.Direction;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;
import static utility.Direction.*;

/**
 * Spade objects are used to 'dig' GemStone objects (which in turn end
 * the game).
 */
public class Spade extends Item {

    /**
     * Construct a new Spade item.
     */
    public Spade() {
        super("spade", "a spade");
    }

    /**
     * Used to update in-game button references, based on any given
     * tile. A required method implementation because of the Item
     * super class.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        if(tile != null && tile.getEntity() instanceof GemStone) {
            this.useMessage = "Dig up gemstone";
        }else {
            this.useMessage = "";
        }
        return useMessage;
    }

    /**
     * Allows the player to use the spade item to 'dig and pickup' a GemStone
     * object.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        if(checkStone(player, rowFacing, colFacing)) {
            pickupStone(player, rowFacing, colFacing, itemIndex);
        }
    }

    /**
     * Checks to see if the player occupies a tile directly in front of the GemStone object.
     * @param player
     * @param row
     * @param col
     * @return
     */
    private boolean checkStone(Player player, int row, int col) {
        Room currentRoom = player.getRoom();
        if(row < 0 || row >= currentRoom.getWidth() || col < 0 || col >= currentRoom.getHeight()) { return false; }
        if(currentRoom.getTile(row, col) == null) { return false; }
        if(!(currentRoom.getTile(row, col).getEntity() instanceof GemStone)) { return false; }
        return true;
    }

    /**
     * Allows the stone to be picked up by the user and adds it to the player's inventory.
     * @param player
     * @param row
     * @param col
     * @param itemIndex
     */
    private void pickupStone(Player player, int row, int col, int itemIndex) {
        Tile gemTile = player.getRoom().getTile(row, col);
        GemStone gem = (GemStone) gemTile.getEntity();

        gemTile.setEntity(null);
        player.consumeItem(itemIndex);
        player.addInventory(gem);
    }

}
