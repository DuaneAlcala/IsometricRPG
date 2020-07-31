package world;
import world.entities.mobs.Mob;

/**
 * Room objects are used to create the interact-able space
 * for the player to play the game within.
 */
public class Room {

	private int x;
	private int y;
    private int width = 20;
    private int height = 20;
    //Each room is represented by 20x20 (400) tiles.
    private Tile[][] tiles = new Tile[width][height];

    //Certain rooms can be dark-rooms, void of sunlight.
	//isDark returns this reference.
    private boolean isDark = false;

	/**
	 * Constructor for a new room object, placed at position (x,y) within
	 * the rooms array within the current world.
	 * @param x
	 * @param y
	 */
	public Room(int x, int y) {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                //tiles[i][j] = new Tile("grass", i, j, (Math.random() > 0.8 ? 1 : 0));
				String tileType = (Math.random() > 0.8) ? "grass" : "stone1";
				int height = (Math.random() > 0.99) ? 1 : 0;
				tiles[i][j] = new Tile(tileType, i, j,  height);
            }
        }
        this.x = x;
        this.y = y;
    }

	/**
	 * Constructor used to load the state of the game from the saved parameter
	 * information.
	 * @param tiles
	 * @param isDark
	 * @param roomRow
	 * @param roomCol
	 * @param roomWidth
	 * @param roomHeight
	 */
	public Room(Tile[][] tiles, boolean isDark, int roomRow, int roomCol, int roomWidth, int roomHeight) {
		this.tiles = tiles;
		this.isDark = isDark;
		this.y = roomRow;
		this.x = roomCol;
		this.width = roomWidth;
		this.height = roomHeight;
	}

	/**
	 * Returns whether the room is a dark room or not.
	 * @return
	 */
	public boolean isDark() {
		return isDark;
	}

	/**
	 * Used to set the room to a dark room with the passing
	 * of time.
	 * @param dark
	 */
	public void setDark(boolean dark) {
		isDark = dark;
	}

	/**
	 * Returns the width of the current room.
	 * @return
	 */
	public int getWidth() {
        return width;
    }

	/**
	 * Returns the height of the current room.
	 * @return
	 */
	public int getHeight() {
        return height;
    }

	/**
	 * Returns the tiles which construct the current room.
	 * @return
	 */
	public Tile[][] getTiles() {
        return tiles;
    }

	/**
	 * Returns whether any given tile is within the specified boundary parameters.
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean tileInBounds(int row, int col) {
    	if(row < 0 || col < 0 || col >= width || row >= height) {
    		return false;
    	}
    	
    	return true;
    }

	/**
	 * Gain the tile indexed within the room, indexed by row and col.
	 * @param row
	 * @param col
	 * @return
	 */
	public Tile getTile(int row, int col) {
    	if(!tileInBounds(row, col)) {
    		return null;
    	}
    	
        return tiles[row][col];
    }

	/**
	 * A clock-wise rotation of the room matrix, which is used to render the
	 * screen and output results based on the current viewing perspective of
	 * the player.
	 */
	public void rotateCounterClockwise() {
		Tile[][] temp = new Tile[height][width];
		
		for(int row = 0; row< tiles.length; row++) {
			for(int col = 0; col < tiles[0].length; col++) {
				temp[tiles[0].length - col - 1][row] = tiles[row][col];
				
				Tile iteratedTile = temp[tiles[0].length - col - 1][row];
				
				iteratedTile.setRow(tiles[0].length - col - 1);
				iteratedTile.setCol(row);
			}
		}
		
		this.tiles = temp;
		
		int tempWidth = width;
		this.width = this.height;
		this.height = tempWidth;
	}

	/**
	 * This similarly rotates the viewing perspective of all objects within the
	 * game clockwise, relative to the position of the player.
	 */
	public void rotateClockwise() {
		Tile[][] temp = new Tile[height][width];
		
		for(int row = 0; row< tiles.length; row++) {
			for(int col = 0; col < tiles[0].length; col++) {
				temp[col][tiles.length - row - 1] = tiles[row][col];
				
				Tile iteratedTile = temp[col][tiles.length - row - 1];
				
				iteratedTile.setRow(col);
				iteratedTile.setCol(tiles.length - row - 1);
			}
		}
		
		this.tiles = temp;

		int tempWidth = width;
		this.width = this.height;
		this.height = tempWidth;
	}

	/**
	 * Returns the position of the room within the rooms array (inside world).
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the position of the room within the rooms array (inside world).
	 * @return
	 */
	public int getY() {
		return y;
	}
}
