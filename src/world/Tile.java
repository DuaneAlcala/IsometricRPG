package world;

import world.entities.Entity;

/**
 * Tile serves as the container for all entities and objects within
 * the game. Each entity, player, object and visually displayable
 * object within the game must be placed on a tile, which contains
 * references to the its relative position.
 */
public class Tile {

    /**
     * Required fields to hold the relevant entity, tile-type and positional information.
     */
    private String displayType;
    private int row;
    private int col;
    private Entity entity;
    private int height;

    /**
     * Construct a new tile at the specified position, giving it the appropriate
     * height value (raising it off of the ground within the renderer).
     * @param displayType
     * @param row
     * @param col
     * @param height
     */
    public Tile(String displayType, int row, int col, int height) {
        this.displayType = displayType;
        this.row = row;
        this.col = col;
        this.height = height;
    }

    /**
     * Constructor used by the persistence package.
     * @param displayType
     * @param height
     * @param entity
     * @param row
     * @param col
     */
    public Tile(String displayType, int height, Entity entity, int row, int col) {
        this.displayType = displayType;
        this.height = height;
        this.entity = entity;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the type of tile. E.g. Grass tile, Stone Tile, etc.
     * @return
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * Return row position of tile.
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Return the column position of the tile.
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     * Replace the row component of this tile.
     * @param row
     */
    public void setRow(int row) {
    	this.row = row;
    }

    /**
     * Replace the column component of this tile.
     * @param col
     */
    public void setCol(int col) {
    	this.col = col;
    }

    /**
     * Returns the entity stored within the given tile.
     * @return
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Set the entity of the given tile (used when updating mobile-object positions).
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the height of the tile (whether the tile is raised or not).
     * @return
     */
	public int getHeight() {
		return height;
	}

    /**
     * Change the display type of any given tile.
     * @param display
     */
    public void setDisplay(String display) {
        this.displayType = display;
    }

    /**
     * Change the height value of any given tile.
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
