package editor;

/**
 * Contains the array of every room in the game world.
 */
public class RoomMap {

    private int width = 3;
    private int height = 3;
    private EditorRoom[][] rooms = new EditorRoom[width][height];

    /**
     * Constructor for RoomMap.
     */
    public RoomMap() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                rooms[x][y] = new EditorRoom();
            }
        }
    }

    public RoomMap(EditorRoom[][] rooms, int width, int height){
        this.rooms = rooms;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width of the whole map.
     *
     * @return The width of the map
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the whole map.
     *
     * @return The height of the map
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a room from the map.
     *
     * @param x A room x coordinate
     * @param y A room y coordinate
     *
     * @return A room from the map
     */
    public EditorRoom getRoom(int x, int y) {
        return rooms[x][y];
    }

    /**
     * Returns all the rooms that the map has
     *
     * @return The rooms that the map has
     */
    public EditorRoom[][] getRooms() {
        return rooms;
    }

    public void setRoom(int x, int y, EditorRoom room) {
    	rooms[x][y] = room;
    }
}
