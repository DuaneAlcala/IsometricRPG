
package renderer;

import gui.Button;
import utility.Sprite;
import utility.SpriteSheet;
import world.Room;
import world.Tile;
import world.entities.Entity;
import world.entities.item.Item;
import world.entities.mobs.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Set;



/**
 * This class is to draw tiles from 'The Curse of David Pearce' game
 * onto a Graphics object supplied by the Application package as well as the entities on those tiles
 * and the games HUD and menu.
 *
 * @author Jericho Jackson (jacksojeri)
 */
public class Renderer {

	private static final int CANVAS_HEIGHT = 720;
	private static final int CANVAS_WIDTH = (CANVAS_HEIGHT*16)/9;

	private static final int CELL_SIZE  = 40;
	private static final int CELL_COUNT = 20;
	private static final int HEIGHT_SCALER = 7; //Decides how high raised tiles will be

	private static final int SIDE_ON_CELL_HEIGHT = 160;
	private static final int SIDE_ON_CELL_SIZE = 80; //Size of cells drawn on screen
	private static final int ROWS_AHEAD = 13;
	private static final float DIMINISH_SCALER = 0.7f; //Decides how high raised tiles will be

	private static final int INVENTORY_LEFT = 30;
	private static final int INVENTORY_IMAGE_SIZE = 30;
	private static final int INVENTORY_MARGIN = 7;
	private static final int INVENTORY_TOP = CANVAS_HEIGHT - INVENTORY_IMAGE_SIZE - INVENTORY_MARGIN - INVENTORY_MARGIN  - 60;
	private static final int INVENTORY_SIZE = 6;
	private static final int INVENTORY_WIDTH = INVENTORY_SIZE*INVENTORY_IMAGE_SIZE + (INVENTORY_SIZE+1)*INVENTORY_MARGIN;
	private static final int INVENTORY_HEIGHT = INVENTORY_IMAGE_SIZE + INVENTORY_MARGIN*2;
	private static final int SELECTED_BORDER_WIDTH = 2;

	private static final int MESSAGES_DISPLAYED = 5;
	private static final int MESSAGE_BOX_MARGIN = 7;
	private static final int MESSAGE_STRING_HEIGHT = 10;
	private static final int MESSAGE_LENGTH = MESSAGE_STRING_HEIGHT * 20;
	private static final int MESSAGE_BOX_HEIGHT = (MESSAGE_STRING_HEIGHT*MESSAGES_DISPLAYED) + (MESSAGES_DISPLAYED+1)*MESSAGE_BOX_MARGIN;
	private static final int MESSAGE_BOX_WIDTH = MESSAGE_LENGTH + MESSAGE_BOX_MARGIN*2;
	private static final int MESSAGE_BOX_LEFT = CANVAS_WIDTH - MESSAGE_BOX_WIDTH - 40;
	private static final int MESSAGE_BOX_TOP = CANVAS_HEIGHT - MESSAGE_BOX_HEIGHT - 60;

	private static final int HEALTH = 10;
	private static final int HEALTH_UNIT_SIZE = 16;
	private static final int HEALTH_MARGIN = 4;
	private static final int HEALTH_BAR_WIDTH = (HEALTH*HEALTH_UNIT_SIZE) + (HEALTH + 1)*HEALTH_MARGIN;
	private static final int HEALTH_BAR_HEIGHT = HEALTH_UNIT_SIZE + HEALTH_MARGIN*2;
	private static final int HEALTH_BAR_LEFT = 30;
	private static final int HEALTH_BAR_TOP = INVENTORY_TOP - 10 - HEALTH_BAR_HEIGHT;

	private ArrayDeque<String> messages; //Messages to be shown in message log on screen

	private SpriteSheet sheet = new SpriteSheet(); //Buffer images in sprite sheet

	boolean sideOn = false; //Viewing perspective

	private Tile[][] tiles;

	private Set<Button> buttons; //In game menu buttons

	//Non sprite images to be drawn in game
	private Image loadGame = loadImage("loadGame.png");
	private Image newGame = loadImage("newGame.png");
	private Image mapEditor = loadImage("mapEditor.png");
	private Image menu = loadImage("menu.png");
	private Image closeButtonImage = loadImage("closeImage.png");
	private Image light = loadImage("light.png");
	private Image smallLight = loadImage("smallLight.png");
	private Image saveGame = loadImage("saveGame.png");
	private Image fButtonImage = loadImage("fButton.png");
	private Image rButtonImage = loadImage("rButton.png");
	private Image spaceButtonImage = loadImage("spaceButton.png");

	//HUD buttons
	private Button menuButton;
	private Button rButton;
	private Button fButton;
	private Button spaceButton;

	//Strings to be shown for each interaction
	private String interactMessage = "";
	private String useMessage = "";
	private String dropMessage = "";

	/**
	 * Creates renderer object that knows of the buttons in the game
	 *
	 * @param buttons      the in game menu buttons
	 * @param menuButton
	 * @param fButton      button that is used in place of 'f' key for dropping items
	 * @param rButton      button that is used in place of 'r' key for use
	 * @param spaceButton  button that is used in place of ' ' for interacting
	 */
	public Renderer(Set<Button> buttons, Button menuButton, Button fButton, Button rButton, Button spaceButton){
		this.menuButton = menuButton;
		this.fButton = fButton;
		this.rButton = rButton;
		this.spaceButton = spaceButton;
		this.buttons = buttons;
		messages = new ArrayDeque<String>();
	}
	
	/**
	 * Draw all the tiles supplied and their entities to the the graphics object
	 *
	 * @param g
	 * @param tiles 2D array of game objects in the room/scene
	 * @param player

	 */
	public void render(Graphics g, Tile[][] tiles, Player player) {

		Graphics2D graphics = (Graphics2D) g; //cast to Graphics2D because its better

		if(sideOn) {
			renderSideOn(graphics, tiles, player);
		}
		else {
			renderTop(graphics, tiles, player);
		}

		renderLight(graphics, player);

		renderInventory(graphics, player);

		//DRAW MENU BUTTON
		graphics.drawImage(getButtonImage(menuButton), menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), null);

		renderMessageBox(graphics);

        renderHealthBar(graphics, player);

		renderButtons(graphics);
	}

	/**
	 * Render the HUD buttons to the graphics object
	 *
	 * @param graphics
	 */
	public void renderButtons(Graphics2D graphics){
		graphics.drawImage(getButtonImage(fButton), fButton.getX(), fButton.getY(), fButton.getWidth(), fButton.getHeight(), null);
		graphics.drawImage(getButtonImage(rButton), rButton.getX(), rButton.getY(), rButton.getWidth(), rButton.getHeight(), null);
		graphics.drawImage(getButtonImage(spaceButton), spaceButton.getX(), spaceButton.getY(), spaceButton.getWidth(), spaceButton.getHeight(), null);

		int buttonX = fButton.getX() + 55;
		graphics.setFont(new Font("Default", Font.BOLD, MESSAGE_STRING_HEIGHT));
		graphics.setColor(Color.WHITE);

		//Draw the three action strings next to the HUD buttons
		graphics.drawString(dropMessage, buttonX, getButtonY(fButton));
		graphics.drawString(useMessage, buttonX, getButtonY(rButton));
		graphics.drawString(interactMessage, buttonX, getButtonY(spaceButton));
	}

	/**
	 * Render the health bar to the graphics objects
	 *
	 * @param graphics
	 * @param player
	 */
	public void renderHealthBar(Graphics2D graphics, Player player){
		//Draw background of health bar
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(HEALTH_BAR_LEFT, HEALTH_BAR_TOP, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

		int left = HEALTH_BAR_LEFT + HEALTH_MARGIN;
		int top = HEALTH_BAR_TOP + HEALTH_MARGIN;

		graphics.setColor(Color.RED);

		//Draw a red bar for each health unit
		for(int i = 0; i < player.getHealth(); i++) {
			graphics.fillRect(left, top, HEALTH_UNIT_SIZE, HEALTH_UNIT_SIZE);
			left += (HEALTH_UNIT_SIZE + HEALTH_MARGIN);
		}
	}

	/**
	 * Render the message box and its messages to the graphics object
	 *
	 * @param graphics
	 */
	public void renderMessageBox(Graphics2D graphics){
		//DRAW MESSAGE BOX
		Color col = Color.DARK_GRAY;
		Color colAlpha = new Color(col.getRed(), col.getGreen(), col.getBlue(), 150);
		graphics.setColor(colAlpha);
		graphics.fillRect(MESSAGE_BOX_LEFT, MESSAGE_BOX_TOP, MESSAGE_BOX_WIDTH, MESSAGE_BOX_HEIGHT);

		int left = MESSAGE_BOX_LEFT + MESSAGE_BOX_MARGIN;
		int top = MESSAGE_BOX_TOP + MESSAGE_BOX_MARGIN + MESSAGE_STRING_HEIGHT/2;

		graphics.setColor(Color.white);

		graphics.setFont(new Font("Default", Font.ITALIC, MESSAGE_STRING_HEIGHT));

		//Render each message to screen
		for(String message : messages) {
			graphics.drawString(message, left, top);
			top += (MESSAGE_BOX_MARGIN + MESSAGE_STRING_HEIGHT);
		}
	}

	/**
	 * Render the light overlay to screen should only be done if room is dark and the image used depends on whether player has lamp or not
	 *
	 * @param graphics
	 */
	public void renderLight(Graphics2D graphics, Player player){
		//LIGHT ROOM IF NECESSARY
		if(player.getRoom().isDark() || player.getGame().getTorchLight() <= 0) {
			if(player.hasItem("lamp")){
				graphics.drawImage(light, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
			}
			else {
				graphics.drawImage(smallLight, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
			}
		}
	}

	/**
	 * Render inventory bar to screen
	 *
	 * @param graphics
	 * @param player    The player whose inventory will be drawn
	 */
	public void renderInventory(Graphics2D graphics, Player player){
		//DRAW INVENTORY BAR BACKGROUND
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(INVENTORY_LEFT, INVENTORY_TOP, INVENTORY_WIDTH, INVENTORY_HEIGHT);

		int left = INVENTORY_LEFT + INVENTORY_MARGIN;
		int top = INVENTORY_TOP + INVENTORY_MARGIN;

		//Draw sprite of each inventory item
		for(int i = 0; i < player.getInventory().size(); i++) {
			Item item = player.getInventory().get(i);

			//If item is selected, draw red outline around it
			if(player.getHeldItem() == item) {
				graphics.setColor(Color.red);
				graphics.fillRect(left-SELECTED_BORDER_WIDTH, top-SELECTED_BORDER_WIDTH, INVENTORY_IMAGE_SIZE+SELECTED_BORDER_WIDTH*2, INVENTORY_IMAGE_SIZE+SELECTED_BORDER_WIDTH*2);
				graphics.setColor(Color.DARK_GRAY);
				graphics.fillRect(left, top, INVENTORY_IMAGE_SIZE, INVENTORY_IMAGE_SIZE);
			}

			graphics.drawImage(this.getEntityImage(item), left, top, INVENTORY_IMAGE_SIZE, INVENTORY_IMAGE_SIZE, null);

			left += INVENTORY_IMAGE_SIZE + INVENTORY_MARGIN;
		}
	}

	/**
	 * Update the interact message with the argument
	 *
	 * @param message
	 */
	public void changeInteractMessage(String message) {
		this.interactMessage = message;
	}

	/**
	 * Update the use message with the argument
	 *
	 * @param message
	 */
	public void changeUseMessage(String message) {
		this.useMessage = message;
	}

	/**
	 * Update the drop message with the argument
	 *
	 * @param message
	 */
	public void changeDropMessage(String message) {
		this.dropMessage = message;
	}

	/**
	 * Returns the Y value of the button
	 *
	 * @param button
	 * @return Y value of button
	 */
	public int getButtonY(Button button){
		return button.getY() + 30;
	}

	/**Updataes boolean to switch viewing style
	 *
	 */
	public void switchView() {
		this.sideOn = !sideOn;
	}

	/**
	 * This method proceeds with rendering the tiles supplied in a top down viewing style
	 *
	 * @param graphics
	 * @param tiles 	to be rendered
	 * @param player
	 */
	public void renderTop(Graphics2D graphics, Tile[][] tiles, Player player) {

		//Draw black background
		graphics.setColor(Color.black);
		graphics.fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);

		//The x and y of the point in array that will be centred at the centre of the screen
		double centreX = player.getCol()+0.5;
		double centreY = player.getRow()+0.5;

		//put tiles into field for use in other methods
		this.tiles = tiles;

		//Draw each quadrant seperately to stop tiles being drawn in wrong order
		renderTopLeft(player, tiles, graphics, centreX, centreY);
		renderTopRight(player, tiles, graphics, centreX, centreY);
		renderBottomLeft(player, tiles, graphics, centreX, centreY);
		renderBottomRight(player, tiles, graphics, centreX, centreY);

		//Calculate bounding indices for tiles being rendered
		int rowMin = (int) Math.max(player.getRow() - CELL_COUNT, 0);
		int rowMax = (int) Math.min(player.getRow() + CELL_COUNT, tiles.length - 1);
		int colMin = (int) Math.max(player.getCol() - CELL_COUNT, 0);
		int colMax = (int) Math.min(player.getCol() + CELL_COUNT, tiles[0].length - 1);

		//For tiles being rendered, draw black outlines on the raised tiles
		for(int row = rowMin; row <= rowMax; row++) {
			for(int col = colMin; col <= colMax; col++) {
				if (tiles[row][col].getHeight() > 0) {
					int left = (int) ((CANVAS_WIDTH/2) + (col -centreX)*CELL_SIZE);
					int top = (int) ((CANVAS_HEIGHT/2) +(row -centreY)*CELL_SIZE);
					drawOutlines(graphics, left, top, tiles[row][col], row, col);
				}
			}
		}
	}

	/**
	 * Render the tiles in the top left quadrant of screen (North-West) of player.
	 *
	 * This must be down by going from top row to bottom and left to right to draw them in correct order
	 *
	 * @param player
	 * @param tiles
	 * @param graphics
	 * @param centreX   X of centre point in array
	 * @param centreY	Y of centre point in array
	 */
	private void renderTopLeft(Player player, Tile[][] tiles, Graphics2D graphics, double centreX, double centreY) {
		for(int row = (int) (player.getRow() - CELL_COUNT > 0 ? player.getRow() - CELL_COUNT : 0); row < player.getRow(); row++) {
			for(int col = (int) (player.getCol() - CELL_COUNT > 0 ? player.getCol() - CELL_COUNT : 0); col < player.getCol(); col++) {
				int left = (int) ((CANVAS_WIDTH/2) + (col -centreX)*CELL_SIZE);
				int top = (int) ((CANVAS_HEIGHT/2) +(row -centreY)*CELL_SIZE);
				renderCellTop(graphics, left, top, tiles[row][col], row, col);
			}
		}
	}

	/**
	 * Render the tiles in the bottom left quadrant of screen (South-West) of player.
	 *
	 * This must be down by going from bottom row to top and left to right to draw them in correct order.
	 *
	 * @param player
	 * @param tiles
	 * @param graphics
	 * @param centreX   X of centre point in array
	 * @param centreY	Y of centre point in array
	 */
	private void renderBottomLeft(Player player, Tile[][] tiles, Graphics2D graphics, double centreX, double centreY) {
		for(int row = (int) (tiles.length - 1 < player.getRow() + CELL_COUNT ? tiles.length - 1 : player.getRow() + CELL_COUNT); row >= player.getRow(); row--) {
			for(int col = (int) (player.getCol() - CELL_COUNT > 0 ? player.getCol() - CELL_COUNT : 0); col < player.getCol(); col++) {
				int left = (int) ((CANVAS_WIDTH/2) + (col -centreX)*CELL_SIZE);
				int top = (int) ((CANVAS_HEIGHT/2) +(row -centreY)*CELL_SIZE);
				renderCellTop(graphics, left, top, tiles[row][col], row, col);

			}
		}
	}

	/**
	 * Render the tiles in the top right quadrant of screen (North-east) of player.
	 *
	 * This must be down by going from top row to bottom and right to left to draw them in correct order
	 *
	 * @param player
	 * @param tiles
	 * @param graphics
	 * @param centreX   X of centre point in array
	 * @param centreY	Y of centre point in array
	 */
	private void renderTopRight(Player player, Tile[][] tiles, Graphics2D graphics, double centreX, double centreY) {
		for(int row = (int) (player.getRow() - CELL_COUNT > 0 ? player.getRow() - CELL_COUNT : 0); row < player.getRow(); row++) {
			for(int col = (int) (tiles[1].length - 1 < player.getCol() + CELL_COUNT ? tiles[1].length - 1 : player.getCol() + CELL_COUNT); col >= player.getCol(); col--) {
				int left = (int) ((CANVAS_WIDTH/2) + (col -centreX)*CELL_SIZE);
				int top = (int) ((CANVAS_HEIGHT/2) +(row -centreY)*CELL_SIZE);
				renderCellTop(graphics, left, top, tiles[row][col], row, col);

			}
		}
	}

	/**
	 * Render the tiles in the bottom right quadrant of screen (South-east) of player.
	 *
	 * This must be down by going from bottom row to top and right to left to draw them in correct order
	 *
	 * @param player
	 * @param tiles
	 * @param graphics
	 * @param centreX   X of centre point in array
	 * @param centreY	Y of centre point in array
	 */
	private void renderBottomRight(Player player, Tile[][] tiles, Graphics2D graphics, double centreX, double centreY) {
		//LOOP THROUGH CELLS TO THE BOTTOM RIGHT OF PLAYER AND DRAW IN PAINTERS ALGORITHM ORDER
		for(int row = (int) (tiles.length - 1 < player.getRow() + CELL_COUNT ? tiles.length - 1 : player.getRow() + CELL_COUNT); row >= player.getRow(); row--) {
			for(int col = (int) (tiles[1].length - 1 < player.getCol() + CELL_COUNT ? tiles[1].length - 1 : player.getCol() + CELL_COUNT); col >= player.getCol(); col--) {
				int left = (int) ((CANVAS_WIDTH/2) + (col -centreX)*CELL_SIZE);
				int top = (int) ((CANVAS_HEIGHT/2) +(row -centreY)*CELL_SIZE);
				renderCellTop(graphics, left, top, tiles[row][col], row, col);

			}
		}
	}

	/**
	 * This method renders the supplied tile to the graphics object at left and top
	 *
	 * @param graphics
	 * @param left			left of the tile on screen
	 * @param top			top of the tile on screen
	 * @param currentTile	Tile to be rendered
	 * @param rowIdx		row idx in tile array of tile
	 * @param colIdx		col idx in array
	 */
	private void renderCellTop(Graphics2D graphics, int left, int top, Tile currentTile, int rowIdx, int colIdx) {

		//TILE IS ON GROUND LEVEL OF MAP
		if(isFlat(currentTile)) {
			graphics.drawImage(getImage(tiles[rowIdx][colIdx]), left, top, CELL_SIZE, CELL_SIZE, null);
			//If has entity then draw entity
			if(tiles[rowIdx][colIdx].getEntity() != null) {
				graphics.drawImage(getEntityImage(tiles[rowIdx][colIdx].getEntity()), left, top, CELL_SIZE, CELL_SIZE, null);
			}
		}
		//ELSE TILE IS RAISED
		else if(isRaised(currentTile)) {
			int height = currentTile.getHeight();
			Color sideColor = getSideColor(currentTile);

			//2D array of vertices of tile by row and then column
			Point[][] tile = new Point[][] {{new Point(left, top), new Point(left + CELL_SIZE, top)}, {new Point(left, top + CELL_SIZE), new Point(left + CELL_SIZE, top + CELL_SIZE)}};
			//2D array of vertices of projected tile by row and then column
			Point[][] raisedTile = new Point[][] {{project(new Point(left, top),height), project(new Point(left + CELL_SIZE, top),height)}, {project(new Point(left, top + CELL_SIZE),height), project(new Point(left + CELL_SIZE, top + CELL_SIZE),height)}};

			//if left of centre screen then draw right side of cube
			if(left < CANVAS_WIDTH/2) {
				renderPolygon(graphics, tile[1][1], tile[0][1], raisedTile[0][1], raisedTile[1][1], sideColor);
			}
			//otherwise draw left side
			else {
				renderPolygon(graphics, tile[0][0], tile[1][0], raisedTile[1][0], raisedTile[0][0], sideColor);
			}

			//if above centre screen draw front side of cube
			if(top < CANVAS_HEIGHT/2) {
				renderPolygon(graphics, tile[1][1], tile[1][0], raisedTile[1][0], raisedTile[1][1], sideColor);
			}
			//otherwise draw back side
			else {
				renderPolygon(graphics, tile[0][0], tile[0][1], raisedTile[0][1], raisedTile[0][0], sideColor);
			}

			//Render sprite to top of cube
			graphics.drawImage(getImage(tiles[rowIdx][colIdx]), raisedTile[0][0].x, raisedTile[0][0].y, (raisedTile[0][1].x-raisedTile[0][0].x), (raisedTile[1][0].y-raisedTile[0][0].y), null);

			//If has entity then draw entity
			if(tiles[rowIdx][colIdx].getEntity() != null) {
				graphics.drawImage(getEntityImage(tiles[rowIdx][colIdx].getEntity()), raisedTile[0][0].x, raisedTile[0][0].y, (raisedTile[0][1].x-raisedTile[0][0].x), (raisedTile[1][0].y-raisedTile[0][0].y), null);
			}
		}
	}

	/**
	 * This method will draw black outlines around raised tiles on their edges that don't have an adjacent raised tile
	 *
	 * @param graphics
	 * @param left			left of tile
	 * @param top			top of tile
	 * @param currentTile	tile
	 * @param rowIdx		row idx in array
	 * @param colIdx		col idx in array
	 */
	private void drawOutlines(Graphics2D graphics, int left, int top, Tile currentTile, int rowIdx, int colIdx) {
		int height = currentTile.getHeight();

		//2D array of vertices of projected tile
		Point[][] raisedTile = new Point[][] {{project(new Point(left, top),height), project(new Point(left + CELL_SIZE, top),height)}, {project(new Point(left, top + CELL_SIZE),height), project(new Point(left + CELL_SIZE, top + CELL_SIZE),height)}};

		//Set black stroke for outlines
		graphics.setColor(Color.black);
		graphics.setStroke(new BasicStroke(3));

		//if no raised tile in row above, draw outline at back side
		if (rowIdx > 0 && height != tiles[rowIdx - 1][colIdx].getHeight()) {
			graphics.drawLine(raisedTile[0][0].x, raisedTile[0][0].y, raisedTile[0][1].x, raisedTile[0][1].y);
		}
		//if no raised tile in row below, draw outline at front side
		if (rowIdx < tiles.length - 1 && height != tiles[rowIdx + 1][colIdx].getHeight()) {
			graphics.drawLine(raisedTile[1][0].x, raisedTile[1][0].y, raisedTile[1][1].x, raisedTile[1][1].y);
		}
		//if no raised tile to left, draw outline on left side
		if (colIdx > 0 && height != tiles[rowIdx][colIdx - 1].getHeight()) {
			graphics.drawLine(raisedTile[0][0].x, raisedTile[0][0].y, raisedTile[1][0].x, raisedTile[1][0].y);
		}
		//if no raised tile to right, draw outline on right side
		if (colIdx < tiles[0].length - 1 && height != tiles[rowIdx][colIdx + 1].getHeight()) {
			graphics.drawLine(raisedTile[0][1].x, raisedTile[0][1].y, raisedTile[1][1].x, raisedTile[1][1].y);
		}
	}

	/**
	 * Project a point to the corresponding point on the raised square above it
	 *
	 * @param point point to be projected
	 * @param height height of tile in units
	 * @return
	 */
	private Point project(Point point, int height) {
		int dx = point.x - CANVAS_WIDTH/2;
		int dy = point.y - CANVAS_HEIGHT/2;

		//dx = (int) Math.min(dx, CELL_SIZE*1.5);
		//dy = (int) Math.min(dy, CELL_SIZE*1.5);

		int newX = point.x + (dx*height/HEIGHT_SCALER);
		int newY = point.y + (dy*height/HEIGHT_SCALER);
		return new Point(newX, newY);
	}

	/**
	 * Render the supplied 4 points as a polygon, assumes the 4 points are in a connected order
	 *
	 * @param graphics
	 * @param point
	 * @param point2
	 * @param point3
	 * @param point4
	 * @param color		The color to fill this polygon
	 */
	private void renderPolygon(Graphics2D graphics, Point point, Point point2, Point point3, Point point4, Color color) {
		Polygon poly = new Polygon();
		poly.addPoint(point.x, point.y);
		poly.addPoint(point2.x, point2.y);
		poly.addPoint(point3.x, point3.y);
		poly.addPoint(point4.x, point4.y);

		graphics.setColor(color);
		graphics.fillPolygon(poly);
	}


	/*
	 * SIDE RENDERING
	 *
	 *
	 *
	 *
	 */

	/**
	 * this method is responsible for rendering the game in a sideon view
	 *
	 * @param g
	 * @param tiles 	tiles to be rendered
	 * @param player
	 */
	public void renderSideOn(Graphics g, Tile[][] tiles, Player player) {

		Graphics2D graphics = (Graphics2D) g;

		//draw black background
		graphics.setColor(Color.black);
		graphics.fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);

		int playerRow = player.getRow();
		int playerCol = player.getCol();

		this.tiles = tiles;

		/*The following loops draw the tiles in a painters algorithm order which is done by drawing the the tiles north-west of the player
		 * then those north-east of the player, then those in his column and north of him, then those south-west, then those south east, then the tile south
		 * of him. Must check that he isn't on the boundary of room and looking to other side as then there would be no tiles south of him
		 */

		for(int row = (0 > playerRow - ROWS_AHEAD ? 0: playerRow - ROWS_AHEAD) ; row <= playerRow; row++) {
			for(int col = 0; col < playerCol; col++) {
				calculatePoints(player, graphics, playerRow, playerCol, row, col);
			}
		}

		for(int row = (0 > playerRow - ROWS_AHEAD ? 0: playerRow - ROWS_AHEAD) ; row <= playerRow; row++) {
			for(int col = tiles[0].length-1; col > playerCol; col--) {
				calculatePoints(player, graphics, playerRow, playerCol, row, col);
			}
		}

		for(int row = (0 > playerRow - ROWS_AHEAD ? 0: playerRow - ROWS_AHEAD); row <= playerRow; row++) {
			calculatePoints(player, graphics, playerRow, playerCol, row, playerCol);
		}


		if(playerRow != tiles.length -1) {
			for(int col = 0; col < playerCol; col++) {
				calculatePoints(player, graphics, playerRow, playerCol, playerRow + 1, col);
			}

			for(int col = tiles[0].length-1; col > playerCol; col--) {
				calculatePoints(player, graphics, playerRow, playerCol, playerRow + 1, col);
			}

			calculatePoints(player, graphics, playerRow, playerCol, playerRow + 1, playerCol);
		}

	}

	/**
	 * This method calculates the 4 onscreen points for the tile at ground level and passes them to the renderer for this
	 *
	 * @param player
	 * @param graphics
	 * @param playerRow
	 * @param playerCol
	 * @param row		row of tile
	 * @param col		col of tile
	 */
	private void calculatePoints(Player player, Graphics2D graphics, int playerRow, int playerCol, int row, int col) {
		//Rows from bottom is how many rows this cell is from the row that is displayed at the bottom of the screen
		int rowsFromBottom = playerRow - row+2;
		//How many columns this tile is from the column that will be at the centre of the screen.
		float colsFromCentre = playerCol - col + 0.5f;

		Point farLeft = new Point((int) (CANVAS_WIDTH/2 - ((Math.pow(DIMINISH_SCALER, rowsFromBottom))*colsFromCentre*SIDE_ON_CELL_SIZE)), (int) (CANVAS_HEIGHT - diminishHeight(rowsFromBottom)));
		Point farRight = new Point((int) (CANVAS_WIDTH/2 - ((Math.pow(DIMINISH_SCALER, rowsFromBottom))*(colsFromCentre-1)*SIDE_ON_CELL_SIZE)), (int) (CANVAS_HEIGHT - diminishHeight(rowsFromBottom)));
		Point nearLeft = new Point((int) (CANVAS_WIDTH/2 - ((Math.pow(DIMINISH_SCALER, rowsFromBottom-1))*colsFromCentre*SIDE_ON_CELL_SIZE)), (int) (CANVAS_HEIGHT - diminishHeight(rowsFromBottom-1)));
		Point nearRight = new Point((int) (CANVAS_WIDTH/2 - ((Math.pow(DIMINISH_SCALER, rowsFromBottom-1))*(colsFromCentre-1)*SIDE_ON_CELL_SIZE)), (int) (CANVAS_HEIGHT - diminishHeight(rowsFromBottom-1)));

		renderCellSideOn(graphics, farLeft, farRight, nearLeft, nearRight, tiles[row][col], row, col, player);
	}

	/**
	 * This method determines the top value of the onscreen tile based on how many rows it is from the bottom of the screen
	 * Each tile from the bottom of the screen gets shorter and shorter by a factor of the DIMINISH_SCALER
	 *
	 * @param rowsFromBottom
	 * @return
	 */
	private float diminishHeight(int rowsFromBottom) {

		float sum = 0;
		float moveBack = 1;

		for(int i = 0; i < rowsFromBottom; i++) {
			moveBack = (moveBack*DIMINISH_SCALER);
			sum += (moveBack);
		}

		return sum*SIDE_ON_CELL_SIZE;
	}

	/**
	 * This method renders the supplied points of a tile to the screen
	 *
	 * @param graphics
	 * @param farLeft   farLeft point for this tile
	 * @param farRight	farRight point for this tile
	 * @param nearLeft	nearLeft point for this tile
	 * @param nearRight	nearRight point for this tile
	 * @param currentTile tile to be drawn
	 * @param rowIdx	row of this tile in array
	 * @param colIdx	col of this tile in array
	 * @param player
	 */
	private void renderCellSideOn(Graphics2D graphics, Point farLeft, Point farRight, Point nearLeft, Point nearRight, Tile currentTile, int rowIdx, int colIdx, Player player) {

		Point[][] tile = new Point[][] {{farLeft, farRight}, {nearLeft, nearRight}};

		//TILE IS ON GROUND LEVEL OF MAP
		if(isFlat(currentTile)) {
			Color color = getColor(currentTile);

			renderPolygon(graphics, tile[0][0], tile[0][1], tile[1][1], tile[1][0], color);

			//if this tile as an entity, render the entity at the top left of this tile
			if(tiles[rowIdx][colIdx].getEntity() != null) {
				graphics.drawImage(getEntityImage(tiles[rowIdx][colIdx].getEntity()), tile[1][0].x, tile[0][0].y, tile[1][1].x- tile[1][0].x, tile[1][0].y- tile[0][0].y, null);
			}
		}
		//ELSE TILE IS RAISED
		else if(isRaised(currentTile)) {
			int height = currentTile.getHeight();

			int rowsFromBottom = player.getRow() - rowIdx+1;

			Color color = getColor(currentTile); //Color of top of tile
			Color sideColor = getSideColor(currentTile); //Color of side of tile

			//2D array of raised tile
			Point[][] raisedTile = new Point[][] {{new Point(farLeft.x, (int) (farLeft.y - (Math.pow(DIMINISH_SCALER, rowsFromBottom)*SIDE_ON_CELL_HEIGHT))), new Point(farRight.x, (int) (farRight.y - (Math.pow(DIMINISH_SCALER, rowsFromBottom)*SIDE_ON_CELL_HEIGHT)))}, {new Point(nearLeft.x,  (int) (nearLeft.y - (Math.pow(DIMINISH_SCALER, rowsFromBottom-1)*SIDE_ON_CELL_HEIGHT))), new Point(nearRight.x,  (int) (nearRight.y - (Math.pow(DIMINISH_SCALER, rowsFromBottom-1)*SIDE_ON_CELL_HEIGHT)))}};

			//Set stroke and colour for outlines
			graphics.setColor(Color.black);
			graphics.setStroke(new BasicStroke(3));

			//Draw side outlines first as tile faces should be drawn over them if they end up that way
			if (colIdx > 0 && currentTile.getHeight() != tiles[rowIdx][colIdx - 1].getHeight()) {
				graphics.drawLine(raisedTile[0][0].x, raisedTile[0][0].y, raisedTile[1][0].x, raisedTile[1][0].y);
			}

			if (colIdx < tiles[0].length - 1 && currentTile.getHeight() != tiles[rowIdx][colIdx + 1].getHeight()) {
				graphics.drawLine(raisedTile[0][1].x, raisedTile[0][1].y, raisedTile[1][1].x, raisedTile[1][1].y);
			}

			//if left of player render right side of cube
			if(colIdx < player.getCol()) {
				renderPolygon(graphics, tile[1][1], tile[0][1], raisedTile[0][1], raisedTile[1][1], sideColor);
			}
			//if right side of player render left side of cube
			else  if (colIdx > player.getCol()){
				renderPolygon(graphics, tile[0][0], tile[1][0], raisedTile[1][0], raisedTile[0][0], sideColor);
			}
			//render front of cube
			renderPolygon(graphics, tile[1][0], tile[1][1], raisedTile[1][1], raisedTile[1][0], sideColor);

			graphics.setColor(Color.black);
			graphics.setStroke(new BasicStroke(3));

			//draw outlines in front face if needed
			if (rowIdx < tiles.length - 1 && currentTile.getHeight() != tiles[rowIdx + 1][colIdx].getHeight()) {
				graphics.drawLine(raisedTile[1][0].x, raisedTile[1][0].y, raisedTile[1][1].x, raisedTile[1][1].y);
			}


		}
	}
	/*
	 * SIDE RENDERING ENDED
	 *
	 *
	 *
	 *
	 */


	/**
	 * Map mouse click location to a tile in room
	 *
	 * @param e			mouse event used to get mouse x and y
	 * @param player	player
	 * @param room		room displayed on screen
	 * @return	tile under mouse or null if not over tile
	 */
	public Tile getTileFromMouse(int x, int y, Player player, Room room) {
		Tile tile = null;

		//Calibrated to get correct row and column
		int row = ((int) ((x - (CANVAS_HEIGHT - (CELL_SIZE/2)))/CELL_SIZE)) + player.getRow() + 8;
		int col = ((int) ((y - (CANVAS_WIDTH - (CELL_SIZE/2)))/CELL_SIZE)) + player.getCol() + 15;

		tile = room.getTile(row, col);

		return tile;
	}

	/**
	 * This method returns the top colour of a tile used by the side on view as sprites cant be drawn in polygons
	 *
	 * @return avg color of the tiles sprite
	 */
	private Color getColor(Tile currentTile) {
		String type = currentTile.getDisplayType();
		Sprite sprite = sheet.getTileSprites().get(type);
		return sprite.getAvgColor();
	}

	/**
	 * this method returns the Image that this tile uses for display
	 *
	 * @param tile	Tile whose image will be returned
	 * @return		image representing tile
	 */
	private Image getImage(Tile tile) {
		String type = tile.getDisplayType();
		Sprite sprite = sheet.getTileSprites().get(type);
		return sprite.getImage();
	}

	/**
	 * this method returns the Image that this entity uses for display
	 *
	 * @param entity	entity whose image will be returned
	 * @return		image representing tile
	 */
	private Image getEntityImage(Entity entity) {
		String type = entity.getName();

		Sprite sprite = sheet.getObjectSprites().get(type);
		return sprite.getImage();
	}

	/**
	 * Returns the image used to draw the button on screen using the buttons name
	 *
	 * @param button	buttons whose image is being requested
	 * @return			image to be drawn for button
	 */
	private Image getButtonImage(Button button) {
		// TODO Auto-generated method stub
		//NEED TO GET BUTTON IMAGES
		if(button.getName().equals("newGame")){
			return newGame;
		}
		if(button.getName().equals("loadGame")) {
			return loadGame;
		}
		if(button.getName().equals("menu")) {
			return menu;
		}
		if(button.getName().equals("mapEditor")) {
			return mapEditor;
		}
		if(button.getName().equals("saveGame")) {
			return saveGame;
		}
		if(button.getName().equals("X")) {
			return closeButtonImage;
		}
		if(button.getName().equals("rButton")){
			return rButtonImage;
		}
		if(button.getName().equals("fButton")){
			return fButtonImage;
		}
		if(button.getName().equals("spaceButton")){
			return spaceButtonImage;
		}
		return spaceButtonImage;
	}

	/**
	 * Return the side color of the raised tile. Currently returns Top Color of tile for top down view otherwise brown for side on
	 *
	 * @param Tilebrown
	 * @return color of sides of raised tile
	 */
	private Color getSideColor(Tile tile) {
		// TODO Auto-generated method stub
		//if(!sideOn) {
			//return getColor(tile);
		//}

		return new Color(139, 69, 19);
	}

	/**
	 * return whether tile is raised or not
	 *
	 * @param Tile
	 * @return true if tile is raised
	 */
	private boolean isRaised(Tile Tile) {
		// TODO Auto-generated method stub
		return (Tile.getHeight() > 0);
	}

	/**
	 * Return whether a tile is flat or not
	 *
	 * @param Tile
	 * @return true if tile is at ground level
	 */
	private boolean isFlat(Tile Tile) {
		// TODO Auto-generated method stub
		return (Tile.getHeight() == 0);
	}

	/**
	 * return the index of the item in the inventory that mosue click is over
	 *
	 * @param x		left of mouse click
	 * @param y		top of mouse click
	 * @return	index of item under mouse in inventory or null if not over an inventory item
	 */
	public int getInventoryIndex(int x, int y) {
		int left = INVENTORY_LEFT + INVENTORY_MARGIN;
		int top = INVENTORY_TOP + INVENTORY_MARGIN;

		for(int i = 0; i < INVENTORY_SIZE; i++) {
			if(x>left && x<left+INVENTORY_IMAGE_SIZE && y>top && y<top+INVENTORY_IMAGE_SIZE) {
				return i;
			}
			left += INVENTORY_IMAGE_SIZE + INVENTORY_MARGIN;
		}

		return -1;
	}

	/**
	 * Load a buffered image from filename
	 *
	 * @param name	name of file of image to be loaded
	 * @return	loaded buffered image
	 */
	private BufferedImage loadImage(String name){
		String dataResourceFolder = "data/";
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(dataResourceFolder + name));
		} catch (IOException e) {
			
		}

		return img;
	}

	/**
	 * This method renders the menu to the supplied graphics button
	 *
	 * @param g
	 */
	public void renderMenu(Graphics g){
		//Render all buttons
		for(Button button : buttons) {
			g.drawImage(getButtonImage(button), button.getX(), button.getY(), button.getWidth(), button.getHeight(), null);
		}
	}

	/**
	 * This method allows a message to be added to the queue of messages that are displayed in the message box and caps the size
	 * by using a last in first out approach.
	 *
	 * @param s	Message to be displayed at next redraw
	 */
	public void addMessage(String s) {
		messages.offer(s);
		if (messages.size() > MESSAGES_DISPLAYED) {
			messages.poll();
		}
	}
}

