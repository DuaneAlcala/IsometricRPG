package editor;

import utility.SpriteSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory for creating tiles when editing the map.
 */
public class TileFactory {

    private EditorController editor;

    private List<String> tileButtonNames;
    private List<String> objectButtonNames;

    private SpriteSheet sheet;

    private List<String> containerTypes = new ArrayList<>(Arrays.asList("chest", "bookshelf"));
    private List<String> containerItems = new ArrayList<>(Arrays.asList("key", "map", "book", "sword", "coins", "lamp", "armour", "potion"));

    /**
     * Constructor for TileFactory.
     *
     * @param editor    The editor controller class
     * @param sheet     The sprite sheet
     */
    public TileFactory(EditorController editor, SpriteSheet sheet) {
        this.editor = editor;
        this.sheet = sheet;

        tileButtonNames = new ArrayList<>();
        objectButtonNames = new ArrayList<>();
        tileButtonNames.addAll(sheet.getTileSprites().keySet());
        objectButtonNames.addAll(sheet.getObjectSprites().keySet());
    }

    /**
     * Creates a tile with the selected display type.
     *
     * @param x             Tile x coordinate
     * @param y             Tile y coordinate
     * @param display       Type of display selected
     * @param tileObject    Type of object on the tile
     *
     * @return              A new tile
     */
    public Tile makeTileDisplay(int x, int y, String display, String tileObject) {
        Tile tile = new Tile(x, y);
        tile.setType(display);
        tile.setTileObject(tileObject);
        return tile;
    }

    /**
     * Creates a tile with a new object type.
     *
     * @param x             Tile x coordinate
     * @param y             Tile y coordinate
     * @param display       Type of tile selected
     * @param tileObject    A new object for the tile
     * @return              A new tile
     */
    public Tile makeTileObject(int x, int y, String display, String tileObject) {
        Tile tile = new Tile(x, y);
        tile.setType(tileObject);
        tile.setTileObject(display);
        return tile;
    }

    /**
     * Changes the tile height.
     *
     * @param tile      The tile
     * @param height    The new height of the tile
     * @return
     */
    public Tile changeTileHeight(Tile tile, int height) {
        tile.setHeight(height);
        return tile;
    }

    /**
     * Checks if a tile display exists in general. Used
     * as an extra check.
     *
     * @param selected The selected tile type
     *
     * @return         Whether a tile display option exists
     */
    public boolean checkDisplayExists(String selected) {
        return tileButtonNames.indexOf(selected) != -1;
    }

    /**
     * Checks if a tile object exists in general. Used
     * as an extra check.
     *
     * @param selected  The selected object type
     *
     * @return          Whether a tile object option exists
     */
    public boolean checkObjectExists(String selected) {
        return objectButtonNames.indexOf(selected) != -1;
    }

    /**
     * Erases a tile that the editor selected. If the thing being
     * erased has a transport entity, it deletes the other end.
     *
     * @param tile          The tile
     * @param tileObject    The entity on the tile
     * @param map           The whole map of rooms
     */
    public void eraseTile(Tile tile, String tileObject, RoomMap map) {
        if(tileObject.contains("=")) {
            String check = tileObject.substring(0, tileObject.indexOf("="));
            if(check.equals("door")) {
                // If the eraser picked a tile that has a door, it deletes the other end too
                String[] doorInfo = tileObject.substring(tileObject.indexOf("=") + 1).split(",");

                // Initialising some values
                int roomEntranceX;
                int roomEntranceY;
                int tileEntranceX;
                int tileEntranceY;

                int roomExitX;
                int roomExitY;
                int tileExitX;
                int tileExitY;
                if(doorInfo.length == 8) {
                    roomEntranceX = Integer.parseInt(doorInfo[0]);
                    roomEntranceY = Integer.parseInt(doorInfo[1]);
                    tileEntranceX = Integer.parseInt(doorInfo[2]);
                    tileEntranceY = Integer.parseInt(doorInfo[3]);

                    roomExitX = Integer.parseInt(doorInfo[4]);
                    roomExitY = Integer.parseInt(doorInfo[5]);
                    tileExitX = Integer.parseInt(doorInfo[6]);
                    tileExitY = Integer.parseInt(doorInfo[7]);

                    // Deleting the other end
                    Tile entranceTile = map.getRoom(roomEntranceX, roomEntranceY).getTile(tileEntranceX, tileEntranceY);
                    Tile exitTile = map.getRoom(roomExitX, roomExitY).getTile(tileExitX, tileExitY);

                    entranceTile.setTileObject("");
                    exitTile.setTileObject("");
                }else if(doorInfo.length == 4) {
                    // Delete the other end and the door on this tile
                    roomExitX = Integer.parseInt(doorInfo[0]);
                    roomExitY = Integer.parseInt(doorInfo[1]);
                    tileExitX = Integer.parseInt(doorInfo[2]);
                    tileExitY = Integer.parseInt(doorInfo[3]);

                    Tile transportTile = map.getRoom(roomExitX, roomExitY).getTile(tileExitX, tileExitY);
                    transportTile.setTileObject("");
                    map.getRoom(editor.getCurrentRoomX(), editor.getCurrentRoomY()).getTile(tile.getX(), tile.getY()).setTileObject("");
                }
            }else if(check.equals("portal") || check.equals("portalExit")) {
                // If the eraser picked a tile that has a portal, it deletes the other end too
                String[] portalInfo = tileObject.substring(tileObject.indexOf("=") + 1).split(",");

                int roomX = Integer.parseInt(portalInfo[0]);
                int roomY = Integer.parseInt(portalInfo[1]);
                int tileX = Integer.parseInt(portalInfo[2]);
                int tileY = Integer.parseInt(portalInfo[3]);

                if(check.equals("portal")) {
                    // If the portal is erased, it deletes the portalExit
                    Tile exitTile = map.getRoom(roomX, roomY).getTile(tileX, tileY);

                    tile.setTileObject("");
                    exitTile.setTileObject("");
                }else if(check.equals("portalExit")) {
                    // If the portalExit is erased, delete the portal
                    Tile entranceTile = map.getRoom(roomX, roomY).getTile(tileX, tileY);

                    tile.setTileObject("");
                    entranceTile.setTileObject("");
                }
            }else if(check.equals("player")) {
                editor.setPlayerLoc(null);
                editor.setPlacedPlayer(false);
                tile.setTileObject("");
            }
        }

        // Delete anything else
        if(!tile.getTileObject().equals("")) {
            tile.setTileObject("");
        }else if(!tile.getType().equals("black")) {
            tile.setType("black");
        }
    }

    /**
     * Handles the logic for when the editor places a door or a portal. Once the editor
     * picks an exit. It creates the transport entity for both the exit and the entrance.
     *
     * @param map           The map of all the rooms
     * @param currentRoom   The current room that the editor is in
     * @param tile          The tile that got selected
     * @param entrance      The entrance of the transport entity
     * @param currentRoomX  The room x index of the editor
     * @param currentRoomY  The room y index of the editor
     * @param x             The x value of the selected tile
     * @param y             The y value of the selected tile
     * @param placingDoor   A boolean that tells whether the editor is placing a door or a portal
     */
    public void addTransport(RoomMap map, EditorRoom currentRoom, Tile tile, EditorController.Entrance entrance, int currentRoomX, int currentRoomY, int x, int y, boolean placingDoor) {
        // Intialising some values
        int fromMapX = entrance.getFromMapX();
        int fromMapY = entrance.getFromMapY();
        int fromTileX = entrance.getFromTileX();
        int fromTileY = entrance.getFromTileY();

        int toMapX = currentRoomX;
        int toMapY = currentRoomY;
        int toTileX = x;
        int toTileY = y;

        if(placingDoor) {
            String entranceString = "door=" + fromMapX + "," + fromMapY + "," + fromTileX + "," + fromTileY + "," + toMapX + "," + toMapY + "," + toTileX + "," + toTileY;
            String exitString = "door=" + currentRoomX + "," + currentRoomY + "," + toTileX + "," + toTileY + "," + fromMapX + "," + fromMapY + "," + fromTileX + "," + fromTileY;

            Tile entranceTile = map.getRoom(fromMapX, fromMapY).getTile(fromTileX, fromTileY);
            entranceTile.setTileObject(entranceString);
            tile.setTileObject(exitString);
        }else {
            // Placing a portal
            String entranceString = "portal=" + toMapX + "," + toMapY + "," + toTileX + "," + toTileY;
            String exitString = "portalExit=" + fromMapX + "," + fromMapY + "," + fromTileX + "," + fromTileY;

            Tile entranceTile = map.getRoom(fromMapX, fromMapY).getTile(fromTileX, fromTileY);
            entranceTile.setTileObject(entranceString);
            tile.setTileObject(exitString);
        }
    }
}
