package world;

/**
 * The World serves as the fundamental structure for the
 * entire game, essentially building the map for the
 * rooms that the player is able to effectively interact
 * with.
 */
public class World {

    private int width = 3;
    private int height = 3;
    //The rooms array evidently serves as the structure of the game world itself.
    private Room[][] rooms = new Room[width][height];

    /**
     * Constructor for a new game world object.
     */
    public World() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                rooms[i][j] = new Room(i, j);
            }
        }
    }

    public World(Room[][] rooms, int worldWidth, int worldHeight) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.rooms = rooms;
    }

    /**
     * Returns a list of all of the rooms contained within this game.
     * These rooms can vary based on the map-editor and the different
     * rooms which can be loaded into each game.
     * @return
     */
    public Room[][] getRooms() {
        return rooms;
    }

    /**
     * Get width returns the number of rooms (wide) the game can be at max.
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height returns the number of rooms (high) the game can be at max.
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a reference to the room in the position specified by both int x
     * and int y.
     * @param x
     * @param y
     * @return
     */
    public Room getRoom(int x, int y) {
        return rooms[x][y];
    }
}