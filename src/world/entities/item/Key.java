package world.entities.item;
import utility.Direction;
import world.Game;
import world.Room;
import world.Tile;
import world.commands.TextCommand;
import world.entities.mobs.Player;
import world.entities.transport.Door;
import static utility.Direction.*;

/**
 * Key items are used to unlock game doors.
 */
public class Key extends Item {

    /**
     * Constructor for a new key object.
     */
    public Key() {
        super("key", "A key to unlock doors");
    }

    /**
     * Updates in-game references to reflect the fact that keys can
     * be used on doors, once the player is oriented into the correct
     * in-game position.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        if(tile != null && tile.getEntity() instanceof Door) {
            Door door = (Door) tile.getEntity();
            if(door.isLocked()) {
                this.useMessage = "Unlock door";
            }
        }else {
            this.useMessage = "";
        }
        return useMessage;
    }

    /**
     * Unlocks the door (should it be locked) for the player.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        if(isDoor(player, rowFacing, colFacing)) {
            unlockDoor(player, rowFacing, colFacing, itemIndex);
            text.execute("You use a key");
        }

    }

    /**
     * Checks to see that the item the key is being used on is in-fact a door.
     * @param player
     * @param row
     * @param col
     * @return
     */
    private boolean isDoor(Player player, int row, int col) {
        Room currentRoom = player.getRoom();
        if(row < 0 || row >= currentRoom.getWidth() || col < 0 || col >= currentRoom.getHeight()) { return false; }
        if(currentRoom.getTile(row, col) == null) { return false; }
        if(currentRoom.getTile(row, col).getEntity() == null) { return false; }
        //if(currentRoom.getTile(row, col).getHeight() != tile.getHeight()) { return false; }
        if(!(currentRoom.getTile(row, col).getEntity() instanceof Door)) return false;
        return true;
    }

    /**
     * Updates the corresponding door-attributes the key item is used on.
     * @param player
     * @param row
     * @param col
     * @param itemIndex
     */
    private void unlockDoor(Player player, int row, int col, int itemIndex) {
        // unlock the door
        Tile doorTile = player.getRoom().getTile(row, col);
        Door door = (Door) doorTile.getEntity();
        door.setLocked(false);

        // consume the item
        player.consumeItem(itemIndex);
    }
}
