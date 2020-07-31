package world.entities.transport;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Doors allow for the player to be transported between
 * multiple rooms within the game.
 */
public class Door extends Transport {
    //Represents whether the door has been locked or unlocked.
    private boolean locked;
    private int roomRow;
    private int roomCol;
    private int tileRow;
    private int tileCol;

    /**
     * Constructs a new room object, connecting both roomX and roomY.
     * These doors are unlocked by default.
     * @param roomX
     * @param roomY
     * @param tileX
     * @param tileY
     */
    public Door(int roomX, int roomY, int tileX, int tileY) {
        super("door", "Unlock a door with a key");
        this.roomX = roomX;
        this.roomY = roomY;
        this.tileX = tileX;
        this.tileY = tileY;
        this.locked = true;
    }

    /**
     * Adds a new door which links together roomX and roomY, and ensures that
     * the door can be locked.
     * @param roomX
     * @param roomY
     * @param tileX
     * @param tileY
     * @param locked
     */
    public Door(int roomX, int roomY, int tileX, int tileY, boolean locked) {
        super("door", "Unlock a door with a key");
        this.roomX = roomX;
        this.roomY = roomY;
        this.tileX = tileX;
        this.tileY = tileY;
        this.locked = locked;
    }

    /**
     * A required method inherited by the transport class.
     * @param game
     * @param player
     */
    @Override
    public void interact(Game game, Player player) { }

    /**
     * Allows interactions between the player and door objects to occur, based
     * on the current direction the player and door are respectively facing.
     * @param game
     * @param player
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void interact(Game game, Player player, int rowFacing, int colFacing) {
        if(locked) {
            return;
        }

        Room nextRoom = game.getWorld().getRoom(roomX, roomY);
        Tile tile = nextRoom.getTile(tileY, tileX);

        System.out.println("Door exit: " + tileX + " Y: " + tileY);

        // Now search for the next free tile
        Tile transportTile = searchTile(nextRoom, tile);
        if(transportTile != null) {
        	game.transportPlayer(player, nextRoom, transportTile);
            text.execute("You go through a door");
        }
    }

    /**
     * Searches through the tiles within the room to check that a new door cannot be
     * placed where all surrounding tiles are occupied by other entities.
     * @param room
     * @param tile
     * @return
     */
    public Tile searchTile(Room room, Tile tile) {
        boolean properSouth = tileOccupied(room, tile.getRow() + 1, tile.getCol());
        if(properSouth) { return room.getTile(tile.getRow() + 1, tile.getCol()); }

        boolean properNorth = tileOccupied(room, tile.getRow() - 1, tile.getCol());
        if(properNorth) { return room.getTile(tile.getRow() - 1, tile.getCol()); }

        boolean properWest = tileOccupied(room, tile.getRow(), tile.getCol() - 1);
        if(properWest) { return room.getTile(tile.getRow(), tile.getCol() - 1); }

        boolean properEast = tileOccupied(room, tile.getRow(), tile.getCol() + 1);
        if(properEast) { return room.getTile(tile.getRow(), tile.getCol() + 1); }

        return null;
    }

    /**
     * Checks to see if a given tile is occupied by an entity or not, and whether
     * there is a direct path for the user to take in order to access the door.
     * @param room
     * @param x
     * @param y
     * @return
     */
    public boolean tileOccupied(Room room, int x, int y) {
        if(x < 0 && x >= room.getWidth() && y < 0 && y >= room.getHeight()) { return false; }
        if(room.getTile(x, y).getEntity() != null) { return false; }
        return true;
    }

    /**
     * Returns whether or not the door is locked.
     * @return
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Changes the locked attribute of the door to unlocked, everytime a
     * door item interacts with a key object.
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        if(this.locked) {
            this.interactMessage = "Go to next room";
            this.description = "The door leads to another room";
        }
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
