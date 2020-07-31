package editor;

import editor.views.EditorGui;
import persistence.LoadMapFile;
import persistence.SaveMapFile;
import utility.SpriteSheet;
import world.Room;
import world.entities.mobs.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;

/**
 * The main controller class for map editing.
 */
public class EditorController {

    private EditorGui gui;
    private TileFactory tileFactory;
    private SpriteSheet sheet;

    private RoomMap map;
    private EditorRoom currentRoom;
    private int currentRoomX = 0;
    private int currentRoomY = 0;
    private boolean viewingMap = false;
    private boolean viewingRoom = false;

    private Entrance entrance;
    private boolean pickingExit = false;
    private boolean placingDoor = false;
    private boolean placingPortal = false;

    public boolean testing = false;

    private boolean placedPlayer = false;

    private PlayerLoc playerLoc;

    /**
     * Constructor for EditorController.
     *
     * @param testing Boolean whether the editor was invoked for testing
     */
    public EditorController(boolean testing) {
        this.testing = testing;

        map = new RoomMap();
        sheet = new SpriteSheet();
        tileFactory = new TileFactory(this, sheet);

        currentRoom = map.getRoom(currentRoomX, currentRoomY);
        viewingMap = true;
        viewingRoom = false;

        gui = new EditorGui(this, sheet);

        if(testing) {
            initiate();
        }
    }

    /**
     * Sets up the listeners for different parts of the gui.
     */
    public void setupListeners() {
        gui.getDrawingFragment().setMouseListener(new EditListener());
        gui.getDrawingFragment().setMouseMotionListener(new DragListener());
    }

    /**
     * Switches from map to room view and vice versa.
     */
    public void viewSwitch() {
        viewingMap = !viewingMap;
        viewingRoom = !viewingRoom;
        update();
    }

    /**
     * Moves the editor from one room to another.
     *
     * @param direction Direction to move to
     */
    public void moveRoom(String direction) {
        // Move to a room through direction
        if(direction.equals("North")) {
            currentRoomY -= 1;
            currentRoom = map.getRoom(currentRoomX, currentRoomY);
        }else if(direction.equals("South")) {
            currentRoomY += 1;
            currentRoom = map.getRoom(currentRoomX, currentRoomY);
        }else if(direction.equals("East")) {
            currentRoomX += 1;
            currentRoom = map.getRoom(currentRoomX, currentRoomY);
        }else if(direction.equals("West")) {
            currentRoomX -= 1;
            currentRoom = map.getRoom(currentRoomX, currentRoomY);
        }
    }

    /**
     * Updates and redraws the gui.
     */
    public void update() {
        gui.getDrawingFragment().repaint();
        gui.getControlsFragment().configureControls(viewingMap, viewingRoom);
        gui.getControlsFragment().updateNavOptions(map, currentRoomX, currentRoomY);
        gui.getControlsFragment().repaint();
    }

    /**
     * Called when the editor clicks on the drawing area
     * with their mouse. If they click on it while in room, it
     * edits the tile. If they click the drawing area while
     * in map view, you in inside a room.
     *
     * @param px Click x coordinate
     * @param py Click y coordinate
     */
    private void handleClick(int px, int py) {
        // Get the mouse point
        if(viewingMap) {
            int x = px / (gui.getDrawingFragment().getWidth() / map.getWidth());
            int y = py / (gui.getDrawingFragment().getHeight() / map.getHeight());

            // If you're in map view, go inside the room that's clicked
            if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                currentRoom = map.getRoom(x, y);
                currentRoomX = x;
                currentRoomY = y;

                viewSwitch();
            }
        }else if(viewingRoom) {
            int x = px / (gui.getDrawingFragment().getWidth() / currentRoom.getWidth());
            int y = py / (gui.getDrawingFragment().getHeight() / currentRoom.getHeight());

            // If you're in room view, place a tile
            if(x >= 0 && x < currentRoom.getWidth() && y >=0 && y < currentRoom.getHeight()) {
                String selected = gui.getControlsFragment().getSelectedTileButton();
                placeTile(x, y, selected, currentRoom, tileFactory);
                update();
            }
        }
    }

    /**
     * Handles the logic for when the editor places a tile in a room.
     * The editor can place tile displays, entities, and remove tiles.
     *
     * @param x             Tile x coordinate
     * @param y             Tile y coordinate
     * @param selected      Type of selected tile
     * @param currentRoom   The room the editor is in
     * @param tileFactory   Tile factory
     *
     * @return              The room with the new edited tile
     */
    private EditorRoom placeTile(int x, int y, String selected, EditorRoom currentRoom, TileFactory tileFactory) {
        if(selected == null) { return currentRoom; }
        Tile tile = currentRoom.getTile(x, y);

        if(tileFactory.checkDisplayExists(selected)) {
            // Place a tile display
            tile = tileFactory.makeTileDisplay(x, y, selected, tile.getTileObject());
            currentRoom.setTile(x, y, tile);
        }else if(tileFactory.checkObjectExists(selected)){
            if(tile.getType().equals("black")) { return currentRoom; }

            // Place an entity
            if(selected.equals("door") || selected.equals("portal")) {
                createTransportTile(x, y, selected);
            }else if(selected.equals("eraser")) {
                String tileObject = tile.getTileObject();
                tileFactory.eraseTile(tile, tileObject, map);
            }else if(selected.equals("player")) {
                // Checking if a player has already been placed
                if(placedPlayer) {
                    map.getRoom(playerLoc.roomX, playerLoc.roomY).getTile(playerLoc.x, playerLoc.y).setTileObject("");
                    playerLoc = null;
                    placedPlayer = false;
                }

                placedPlayer = true;
                playerLoc = new PlayerLoc(currentRoomX, currentRoomY, x, y);
                tile = tileFactory.makeTileObject(x, y, selected, tile.getType());
                currentRoom.setTile(x, y, tile);
            }else {
                tile = tileFactory.makeTileObject(x, y, selected, tile.getType());
                currentRoom.setTile(x, y, tile);
            }
        }else if(selected.equals("0") || selected.equals("1")) {
            // Changing tile heights
            currentRoom.setTile(x, y, tileFactory.changeTileHeight(tile, Integer.parseInt(selected)));
        }
        return currentRoom;
    }

    /**
     * Creates a tile that can move a player from one room to another.
     *
     * @param x         Tile x coordinate
     * @param y         Tile y coordinate
     * @param selected  Type of transport selected
     */
    private void createTransportTile(int x, int y, String selected) {
        gui.getControlsFragment().untoggle();
        pickingExit = true;

        // Place a door or a portal
        if(selected.equals("door")) {
            System.out.println("PICKING DOOR");
            placingDoor = true;
        }else if(selected.equals("portal")) {
            System.out.println("PICKING A PORTAL");
            placingPortal = true;
        }

        // Creating an entrance reference for the transport
        entrance = new Entrance(currentRoomX, currentRoomY, x, y);
        try {
            Thread.sleep(500);
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        viewSwitch();
    }

    /**
     * Finds the tile that the player is on when the game is loaded and then returns
     * their location.
     *
     * @return The player location
     */
    private PlayerLoc findPlayer() {
        PlayerLoc loc = null;
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {

                EditorRoom room = map.getRoom(x, y);
                for(int i = 0; i < room.getWidth(); i++) {
                    for(int j = 0; j < room.getHeight(); j++) {
                        if(room.getTile(i, j).getTileObject().equals("player")) {
                            loc = new PlayerLoc(x, y, i, j);
                            placedPlayer = true;
                            break;
                        }
                    }
                }
            }
        }
        if(loc == null) {
            placedPlayer = false;
        }
        return loc;
    }

    /**
     * Creates the other end of the transport entity after the editor
     * creates the entrance.
     *
     * @param px Mouse x coordinate
     * @param py Mouse y coordinate
     */
    private void pickExit(int px, int py) {
        gui.getControlsFragment().disableControls();
        if(viewingMap) {
            // Must disable all the control fragment buttons so the editor can't change tiles
            int x = px / (gui.getDrawingFragment().getWidth() / map.getWidth());
            int y = py / (gui.getDrawingFragment().getHeight() / map.getHeight());

            if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                if(x == currentRoomX && y == currentRoomY) { return; }

                // Can only place doors to adjacent rooms
                if(placingDoor && ((Math.abs(x - currentRoomX) == 1) && (Math.abs(y - currentRoomY) == 0)) || ((Math.abs(x - currentRoomX) == 0) && (Math.abs(y - currentRoomY) == 1))) {
                    currentRoom = map.getRoom(x, y);
                    currentRoomX = x;
                    currentRoomY = y;

                    gui.getControlsFragment().updateNavOptions(map, currentRoomX, currentRoomY);
                    viewSwitch();
                }
                // Can pick any room to place a portal in
                else if(placingPortal) {
                    currentRoom = map.getRoom(x, y);
                    currentRoomX = x;
                    currentRoomY = y;

                    gui.getControlsFragment().updateNavOptions(map, currentRoomX, currentRoomY);
                    viewSwitch();
                }
            }
        }else if(viewingRoom) {
            // If you're finally in room view, pick a tile to place the exit of the portal
            int x = px / (gui.getDrawingFragment().getWidth() / currentRoom.getWidth());
            int y = py / (gui.getDrawingFragment().getHeight() / currentRoom.getHeight());

            if(x >= 0 && x < currentRoom.getWidth() && y >=0 && y < currentRoom.getHeight()) {
                Tile tile = currentRoom.getTile(x, y);
                // Place a transport on a non-empty tile
                if(!tile.getType().equals("black")) {
                    tileFactory.addTransport(map, currentRoom, tile, entrance, currentRoomX, currentRoomY, x, y, placingDoor);
                }
                offExit();
            }
        }
    }

    /**
     * Resets everything back to a state where the editor isn't forced to
     * place the exit of a transport tile anymore.
     */
    private void offExit() {
        pickingExit = false;
        placingDoor = false;
        placingPortal = false;

        entrance = null;

        // Re-enable everything again
        gui.getControlsFragment().enableControls();
        gui.getControlsFragment().repaint();
        gui.getDrawingFragment().repaint();
    }

    /**
     * Saves the layout of the map.
     */
    public void save() {
        try {
            if(!placedPlayer) {
                System.out.println("Needs to place a player");
                return;
            }
            SaveMapFile.save(flipMap(), "mapFile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a map layout from a file.
     */
    public void load() {
        try{
            this.map = LoadMapFile.load(new File("mapFile.xml"));
            playerLoc = findPlayer();
            update();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public RoomMap flipMap() {
    	RoomMap tempMap = new RoomMap();
    	for(int x = 0; x < map.getWidth(); x++) {
    		for(int y = 0; y < map.getHeight(); y++) {
    			EditorRoom room = new EditorRoom();

    			for(int i = 0; i < room.getWidth(); i++) {
    				for(int j = 0; j < room.getHeight(); j++) {
    					room.setTile(j, i, map.getRoom(x, y).getTile(i, j));
    				}
    			}
    			tempMap.setRoom(x, y, room);
    		}
    	}
    	return tempMap;
    }

    /**
     * Returns whether the editor is viewing the map.
     *
     * @return Whether the editor is viewing the map
     */
    public boolean isViewingMap() {
        return viewingMap;
    }

    /**
     * Returns whether the editor is viewing the room.
     *
     * @return Whether the editor is viewing the room
     */
    public boolean isViewingRoom() {
        return viewingRoom;
    }

    /**
     * Returns the room x index of where the editor is at.
     *
     * @return The room x index of where the editor is at
     */
    public int getCurrentRoomX() {
        return currentRoomX;
    }

    /**
     * Gets the room y index of where the editor is at.
     *
     * @return The room y index of where the editor is at
     */
    public int getCurrentRoomY() {
        return currentRoomY;
    }

    /**
     * Returns all the rooms.
     *
     * @return All the rooms
     */
    public RoomMap getMap() {
        return map;
    }

    /**
     * Returns the current room where the editor is at.
     *
     * @return The current room.
     */
    public EditorRoom getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the gui.
     *
     * @return The gui.
     */
    public EditorGui getGui() {
        return gui;
    }

    /**
     * Returns the tile factory.
     *
     * @return The tile factory.
     */
    public TileFactory getTileFactory() {
        return tileFactory;
    }

    /**
     * Sets the boolean for whether the editor is placing a door.
     *
     * @param placingDoor Whether the editor is placing a door
     */
    public void setPlacingDoor(boolean placingDoor) {
        this.placingDoor = placingDoor;
    }

    /**
     * Sets the boolean for whether the editor is placing a portal.
     *
     * @param placingPortal Whether the editor is placing a portal
     */
    public void setPlacingPortal(boolean placingPortal) {
        this.placingPortal = placingPortal;
    }

    /**
     * Sets a boolean for whether the player has been placed.
     *
     * @param placedPlayer A new boolean for whether the player is placed.
     */
    public void setPlacedPlayer(boolean placedPlayer) {
        this.placedPlayer = placedPlayer;
    }

    /**
     * Sets a new location for the player.
     *
     * @param playerLoc A new location for the player
     */
    public void setPlayerLoc(PlayerLoc playerLoc) {
        this.playerLoc = playerLoc;
    }

    /**
     * Automatically sets up the editing during testing.
     */
    private void initiate() {
        gui.setupEditing();
    }

    /**
     * Class that listens for mouse clicks for when the editor clicks
     * in the drawing area.
     */
    private class EditListener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            //
            if(!pickingExit) {
                handleClick((int)p.getX(), (int) p.getY());
                update();
            }
            else { pickExit((int)p.getX(), (int)p.getY()); }
        }

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }

    /**
     * Class the listens for mouse dragging for smoother tile placing.
     */
    private class DragListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            // Sets a tile when the editor isn't being forced to place an door exit or portal
            if(!pickingExit) {
                handleClick((int) e.getPoint().getX(), (int) e.getPoint().getY());
                update();
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) { }
    }

    /**
     * Class used to keep track of transport entities such
     * as doors and portals.
     */
    class Entrance {
        private int fromMapX;
        private int fromMapY;
        private int fromTileX;
        private int fromTileY;

        /**
         * Constructor for Entrance.
         *
         * @param mapX  The room x index of where the entrance is
         * @param mapY  The room y index of where the entrance is
         * @param tileX The tile x index of where the entrance is
         * @param tileY The tile y index of where the entrance is
         */
        private Entrance(int mapX, int mapY, int tileX, int tileY) {
            this.fromMapX = mapX;
            this.fromMapY = mapY;
            this.fromTileX = tileX;
            this.fromTileY = tileY;
        }

        /**
         * Returns the room x index of the entrance.
         *
         * @return The room x index of the entrance
         */
        public int getFromMapX() {
            return fromMapX;
        }

        /**
         * Returns the room y index of the entrance.
         *
         * @return The room y index of the entrance
         */
        public int getFromMapY() {
            return fromMapY;
        }

        /**
         * Returns the tile x index.
         *
         * @return The tile x index
         */
        public int getFromTileX() {
            return fromTileX;
        }

        /**
         * Returns the tile y index.
         *
         * @return The tile y index
         */
        public int getFromTileY() {
            return fromTileY;
        }
    }

    /**
     * Class to keep track of the location of the player when editing.
     */
    class PlayerLoc {

        private int roomX;
        private int roomY;
        private int x;
        private int y;

        /**
         * Constructor for PlayerLoc.
         *
         * @param roomX The room x index of the player
         * @param roomY The room y index of the player
         * @param x     The tile x of the player
         * @param y     The tile y of the player
         */
        PlayerLoc(int roomX, int roomY, int x, int y) {
            this.roomX = roomX;
            this.roomY = roomY;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Main method.
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        new EditorController(false);
    }
}
