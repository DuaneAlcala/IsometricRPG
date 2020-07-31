package editor;

/**
 * A tile can contain a display which is what it will look like
 * and an object which will be the entity that is is on it.
 * The height will determine how tall it will look during
 * rendering.
 */
public class Tile {

    private int x;
    private int y;
    private String type = "grass";
    private String tileObject = "";
    private int height;

    /**
     * Constructor for Tile.
     *
     * @param x Tile x value
     * @param y Tile y value
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tile(String displayType, int height, String entity, int row, int col){
        this.type = displayType;
        this.height = height;
        this.tileObject = entity;
        this.x = row;
        this.y = col;
    }

    /**
     * Returns tile x value.
     *
     * @return X value of tile
     */
    public int getX() {
        return x;
    }

    /**
     * Returns tile y value.
     *
     * @return Y value of tile
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the display of the tile.
     *
     * @return The display of the tile
     */
    public String getType() {
        return type;
    }

    /**
     * Sets a display.
     *
     * @param type A new tile display
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the object on the tile.
     *
     * @return The object on the tile
     */
    public String getTileObject() {
        return tileObject;
    }

    /**
     * Sets an object.
     *
     * @param tileObject A new tile object
     */
    public void setTileObject(String tileObject) {
        this.tileObject = tileObject;
    }

    /**
     * Returns the height of the tile.
     *
     * @return The tile height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets a height for the tile.
     *
     * @param height A new tile height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
