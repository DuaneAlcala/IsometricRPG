package world.entities.transport;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Portals allow for users to be transported to non-adjacent rooms
 * within the game world.
 */
public class Portal extends Transport {

    private int roomRow;
    private int roomCol;
    private int tileRow;
    private int tileCol;

    /**
     * Constructor which can connect two non-adjacent rooms together.
     * @param roomX
     * @param roomY
     * @param tileX
     * @param tileY
     */
    public Portal(int roomX, int roomY, int tileX, int tileY) {
        super("portal", "Portals lead to another room");
        this.roomX = roomX;
        this.roomY = roomY;
        this.tileX = tileX;
        this.tileY = tileY;
        this.interactMessage = "Go through portal";
    }

    /**
     * Method stub required by the Transport-class.
     * @param game
     * @param player
     */
    @Override
    public void interact(Game game, Player player) { }

    /**
     * Manages the interaction between both the player and the portal,
     * transporting the player to the next room, and updating all
     * player references as required.
     * @param game
     * @param player
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void interact(Game game, Player player, int rowFacing, int colFacing) {
        Room nextRoom = game.getWorld().getRoom(roomX, roomY);
        Tile nexTile = nextRoom.getTile(tileY, tileX);

        game.transportPlayer(player, nextRoom, nexTile);
        text.execute("You take the portal to another room");
    }

    public int getRoomRow() {
        return roomRow;
    }

    public int getRoomCol() {
        return roomCol;
    }

    public int getTileRow() {
        return tileRow;
    }

    public int getTileCol() {
        return tileCol;
    }
}
