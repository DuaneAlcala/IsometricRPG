package editor;

/**
 * A room contains a 2D array of tiles that can have
 * different game entities.
 */
public class EditorRoom {

    private int width = 20;
    private int height = 20;
    private Tile[][] tiles = new Tile[width][height];
    private boolean isDark = false;

    /**
     * Constructor for EditorRoom.
     */
    public EditorRoom() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    public EditorRoom(Tile[][] tiles, boolean isDark, int width, int height){
        this.tiles = tiles;
        this.isDark = isDark;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns whether the room is dark or not.
     *
     * @return Whether the room is dark or not.
     */
    public boolean isDark() {
        return isDark;
    }

    /**
     * Returns the width of the room.
     *
     * @return The width of the room
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the room.
     *
     * @return The height of the room
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns all the tiles of a room.
     *
     * @return All the tiles of a room
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Sets a tile for the room.
     *
     * @param x     Tile x coordinate
     * @param y     Tile y coordinate
     * @param tile  The new tile
     */
    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    /**
     * Gets a tile from the room.
     *
     * @param x Tile x coordinate
     * @param y Tile y coordinate
     *
     * @return  The tile at the x y coordinate of this room
     */
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
}
