package world.entities.item;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;
import world.entities.transport.EndPortal;

/**
 * GemStone objects are used to activate the ending portal for the
 * game which allows the user to win.
 */
public class GemStone extends Item {
    /**
     * Constructor for a new GemStone object
     */
    public GemStone() {
        super("gemstone", "Gemstone for activating end portal");
    }

    /**
     * A method which may later be implemented to allow different GemStones to
     * interact with the player in different ways.
     * @param player
     */
    public void interact(Player player) {
        return;
    }

    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        if(checkTileInfrontEndPortal(player, rowFacing, colFacing)) {
            activatePortal(player, rowFacing, colFacing, itemIndex);
        }
    }

    /**
     * Checks to see if the ending portal can be accessed from a given direction by the player.
     * @param player
     * @param row
     * @param col
     * @return
     */
    private boolean checkTileInfrontEndPortal(Player player, int row, int col) {
        Room currentRoom = player.getRoom();
        if(row < 0 || row >= currentRoom.getWidth() || col < 0 || col >= currentRoom.getHeight()) { return false; }
        if(currentRoom.getTile(row, col) == null) { return false; }
        if(!(currentRoom.getTile(row, col).getEntity() instanceof EndPortal)) { return false; }
        return true;
    }

    /**
     * Sets the appropriate attributes for the game-ending portal.
     * @param player
     * @param row
     * @param col
     * @param itemIndex
     */
    private void activatePortal(Player player, int row, int col, int itemIndex) {
        Tile gemTile = player.getRoom().getTile(row, col);
        EndPortal portal = (EndPortal) gemTile.getEntity();

        player.consumeItem(itemIndex);
        portal.setActivated(true);
    }

    /**
     * Returns the interaction method for this object.
     * @return
     */
    @Override
    public String getInteractMessage() {
        return "";
    }

    /**
     * Updates in-game button references for the GemStone to be used.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        if(tile != null && tile.getEntity() instanceof EndPortal) {
            EndPortal end = (EndPortal) tile.getEntity();
            if(!end.isActivated()) {
                this.useMessage = "Activate end portal";
            }
        }else {
            this.useMessage = "";
        }
        return useMessage;
    }
}
